package br.edu.ifsul;

import br.edu.ifsul.enums.CardValue;
import br.edu.ifsul.model.Card;
import br.edu.ifsul.model.Deck;
import br.edu.ifsul.model.Player;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Classe executa o servidor 
 * que ira executar o jogo e mostrar as mensagens para
 * os jogadores
 * @author Igor Rocha
 *
 */
public class Server extends Thread {
	private static List<Player> players;
	private Player player;
	private Socket connection;
	private static Deck deck;
	private static int i;
	private static volatile boolean end;
	private static String winner;
	private static boolean start;
	private static Card trashCard;
	private static int position;
	private static boolean trocarTurno;

	// Construtor
	public Server(Player player) {
		this.player = player;
	}

	// Função responsável por printar todos os jogadores conectados
	public String listPlayers() {

		int i = 0;
		String out = "";

		for (Player p : this.getPlayers()) {

			out = out + p.getName() + "\n";
		}
		return out;

	}

	// Verifica se todos os jogadores estão prontos
	public boolean checkStart() {

		for (Player p : players) {
			if (!p.isTurn()) {
				return false;
			}
		}
		return true;

	}

	// Instancia Thread para cada cliente
	public void run() {
		try {
			BufferedReader startingPoint = new BufferedReader(
					new InputStreamReader(player.getSocket().getInputStream()));
			PrintStream endPoint = new PrintStream(player.getSocket().getOutputStream());
			player.setEndPoint(endPoint);

			String name = startingPoint.readLine();
			
			this.player.getEndPoint().flush();
			this.player.setName(name);
			this.player.setPosition(position);
			position += 1;
			String line = "n";

			while ((line != null && !(line.trim().equals(""))) && !this.isStart()) {

				endPoint.println("1. Lista Jogadores\n2. Mudar o status para pronto\n3. Iniciar Jogo: ");
				line = startingPoint.readLine();

				switch (line) {

				case "1": // Listar jogadores
					endPoint.println(this.listPlayers());
					break;
				case "2": // Mudar o status do jogador
					this.player.setTurn(true);
					this.player.reciveCards(deck.giveCards());
					this.player.sortHand();
					endPoint.println("Aguardando os outros jogadores");
					break;
				case "3": // Inicia o jogo
					if (this.players.size() == 2) {
						if(checkStart()) {
							this.setStart(true);
						}else {
							 endPoint.println("Erro: Só é possivel iniciar a partida se todos os jogadores estiverem prontos!");
						}
					} else {
						endPoint.println("Nao a 2 jogadores");
					}
					break;
				default: // Opção inválida
					endPoint.println("Erro: Opção inválida!");
					break;

				}

			}

			// Altera o status do turno para falso
			this.player.setTurn(false);
			line = "n";
			endPoint.println("Atenção: O jogo irá começar!");

			// Ativa o turno do primeiro jogador
			players.get(0).setTurn(true);

			while ((line != null && !(line.trim().equals(""))) && !end) {

				if (this.player.isTurn()) {
					this.player.getEndPoint().flush();
					endPoint.flush(); 
					endPoint.println(
							"É seu turno e a carta do topo é " + trashCard.getValue() + " de " + trashCard.getType());
					endPoint.println("Suas cartas são: " + this.player.showCards());
					endPoint.println("1. Pega uma nova carta");
					endPoint.println("2. Jogar carta da mao");
					if (this.player.getHand().size() == 1) {
						endPoint.println("3. Declarar Uno");
					}

					line = startingPoint.readLine();
					this.player.getEndPoint().flush();
					switch (line) {

					// Pega uma nova carta
					case "1":
						trashCard = deck.giveFirstCard();
						this.player.takeNewCard(trashCard);
						endPoint.println(this.player.showCards());
						this.player.sortHand();
						this.trocarTurno = true;
						break;

					// Jogar a carta disponível (trashCard)
					case "2":
						endPoint.println(this.player.showCards());
						endPoint.println("qual carta deseja retirar(1 a " + this.player.getHand().size() + ")");
						line = startingPoint.readLine();
						this.player.getEndPoint().flush();
						Card aux = this.player.getHand().get(Integer.parseInt(line) - 1);
						if (this.player.getHand().get(Integer.parseInt(line) - 1).getType().equals(deck.giveLastCard().getType())) {
							trashCard = this.player.discart(Integer.parseInt(line) - 1);
							deck.pushBack(trashCard);
							if (trashCard.getValue() == CardValue.ADD2 || trashCard.getValue() == CardValue.ADD4) {
								if (this.player.getPosition() == position - 1) {
									this.getPlayers().get(0).efeitoCardAdd(trashCard, deck);
								} else {
									this.getPlayers().get(this.player.getPosition() + 1).efeitoCardAdd(trashCard, deck);
								}
							}if(trashCard.getValue() == CardValue.REVERSO || trashCard.getValue() == CardValue.BLOQUEIO) {
								if (this.player.getPosition() == position - 1) {
									trocarTurno = false;
									this.getPlayers().get(0).getEndPoint().println("Jogador "+this.player.getName()+" jogou uma carta "+trashCard.getValue());
									break;
								} else {
									trocarTurno = false;
									this.getPlayers().get(this.player.getPosition() + 1).getEndPoint().println("Jogador "+this.player.getName()+" jogou uma carta "+trashCard.getValue());
									break;
								}
							}
							this.trocarTurno = true;
							break;

						} else {
							endPoint.println(
									"Carta Invalida jogue um carta com o mesmo numero ou cor da carta do topo");
							this.trocarTurno = false;
							break;
						}

					case "3":
						Iterator<Player> x = this.players.iterator();
						while (x.hasNext()) {
							Player p = x.next();
							if (p.getName() != this.player.getName())
								p.getEndPoint().println("Jogador " + this.player.getName() + " declarou Uno");
						}
						trocarTurno = false;
						break;
					default:
						endPoint.println("opção invalida");
						break;
					}

					// Checa se o player ganhou com sua jogada
					if (this.player.checkWin()) {
						this.setWinner(this.player.getName());
						this.setEnd(true);
					}

					// Ativa o turno do proximo jogador
					if (this.trocarTurno) {
						if (this.player.getPosition() == position - 1) {

							this.player.setTurn(false);
							this.getPlayers().get(0).setTurn(true);
							PrintStream next = (PrintStream) this.getPlayers().get(0).getEndPoint();

						} else {
							this.player.setTurn(false);
							this.getPlayers().get(this.player.getPosition() + 1).setTurn(true);
							PrintStream next = (PrintStream) this.getPlayers().get(this.player.getPosition() + 1)
									.getEndPoint();
						}
						this.setPlayers(this.getPlayers());
					}

				}
			}

			endPoint.println("[FIM] A partida acabou, o ganhador foi " + winner);
			endPoint.println("Adeus!");
			players.remove(endPoint);
			players.remove(this.player);
			connection.close();

		} catch (NullPointerException e) {
			System.out.println("IOException: " + e);
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Função Main
	public static void main(String[] args) {

		// Inicializa variaveis statics
		// players = new ArrayList<>();
		players = Collections.synchronizedList(new ArrayList<>());
		deck = new Deck();
		deck.initializeTwoDeck();
		deck.shuffle();
		end = false;
		start = false;
		i = 0;
		trocarTurno = false;
		// Disponibiliza a primeira trashCard
		trashCard = deck.giveFirstCard();
		deck.pushBack(trashCard);
		position = 0;

		try {
			// Inicialzia o socket na porta desejada
			ServerSocket s = new ServerSocket(2222);
			while (true) {

				// Mostra se o token esta definido com um operador ternario
				System.out.println("Esperando conexão...\nIP: 127.0.0.1\nPorta: 2222");
				Socket connection = s.accept();

				// Novo player do client
				Player player = new Player();
				player.setSocket(connection);

				players.add(player);
				i++;
				System.out.println("Conectou!: " + connection.getRemoteSocketAddress());

				// Cria a thread do client
				Thread t = new Server(player);
				t.start();

			}

		} catch (Exception e) {
			System.out.println(e);
		}

	}

	// Getters e Setter normais
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Socket getConnection() {
		return connection;
	}

	public void setConnection(Socket connection) {
		this.connection = connection;
	}

	// Getters e Setters com funções de sincronização entre todos os clientes
	public synchronized static List<Player> getPlayers() {
		return players;
	}

	public synchronized static void setPlayers(List<Player> players) {
		Server.players = players;
	}

	public synchronized static Deck getDeck() {
		return deck;
	}

	public synchronized static void setDeck(Deck deck) {
		Server.deck = deck;
	}

	public synchronized static boolean isEnd() {
		return end;
	}

	public synchronized static void setEnd(boolean end) {
		Server.end = end;
	}

	public synchronized static String getWinner() {
		return winner;
	}

	public synchronized static void setWinner(String winner) {
		Server.winner = winner;
	}

	public synchronized static boolean isStart() {
		return start;
	}

	public synchronized static void setStart(boolean start) {
		Server.start = start;
	}

	public synchronized static Card getTrashCard() {
		return trashCard;
	}

	public synchronized static void setTrashCard(Card trashCard) {
		Server.trashCard = trashCard;
	}

	public synchronized static int getPosition() {
		return position;
	}

	public synchronized static void setPosition(int position) {
		Server.position = position;
	}

}
