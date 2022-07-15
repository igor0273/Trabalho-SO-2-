package br.edu.ifsul.model;

import br.edu.ifsul.enums.CardValue;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Collections;
import java.util.List;

/**
 * Classe representa o jogador
 * para que seja possivel controlar 
 * o turno e a mao do jogador
 * @author Igor Rocha
 *
 */
public class Player {
    
    private List<Card> hand;
    private PrintStream endPoint;
    private Socket socket;
    private String name;
    private boolean turn;
    private int position;

    // Construtor
    public Player() {
       
    }
    
    // Adiciona uma lista de cartas a mão
    public void reciveCards(List<Card> cards) {
       hand = cards;
    }
    
    // Discarta uma carta com o indice informado
    public Card discart(int i) {
        return hand.remove(i);
    }

    // Recebe uma nova carta
    public void takeNewCard(Card c) {
        hand.add(c);
    }

    // Ordena a mão do menor ao maior valor
    public void sortHand() {
        Collections.sort(hand);
    }
    
    public void efeitoCardAdd(Card c,Deck deck) {
    	if(c.getValue() == CardValue.ADD2) {
    		for(int i = 1;i<2;i++) {
    			takeNewCard(deck.giveFirstCard());
    		}
    		this.endPoint.println("Jogador "+this.getName()+" jogou uma carta "+CardValue.ADD2);
    	} 
    	
    	if(c.getValue() == CardValue.ADD4) {
    		for(int i = 1;i<4;i++) {
    			takeNewCard(deck.giveFirstCard());
    		}
    		this.endPoint.println("Jogador "+this.getName()+" jogou uma carta "+CardValue.ADD4);
    	}
    }
    
    public boolean checaCard(Card card,Deck deck) {
		if(card.getValue() == deck.giveLastCard().getValue()) {
			return true;
		}else if(card.getType() == deck.giveLastCard().getType()) {
			return true;
		}else if(card.getType() == deck.giveLastCard().getType() && card.getValue()!= deck.giveLastCard().getValue()) {
			return true;
		}else if(card.getType() != deck.giveLastCard().getType() && card.getValue() == deck.giveLastCard().getValue()) {
			return true;
		}else if(card.getType() == deck.giveLastCard().getType() && card.getValue() == deck.giveLastCard().getValue()) {
			return true;
		}else {
			return false;
		}
    	    	
    }
  

    // Checa se o player ganhou 
    // o player ganha quando não tiver mais cartas na mao
    public boolean checkWin() {
       if(hand.size() == 0) {
    	   return true;
       }
        return false;
    }

    // Retorna as cartas da mão do jogador
    public String showCards() {
        String s = "";
        
        for(Card c : hand){
            s = s + c.getValue() + " de " + c.getType() + "    ";
        }

        return s;
    }
    
    public boolean checkCard(Card card,Deck deck) {
    	
    	if(card.getType() != deck.giveLastCard().getType() || card.getValue() != deck.giveLastCard().getValue()) {
    		return true;
    	}
    	
    	return false;
    }
    
    // Getters e Setters
    public List<Card> getHand() {
        return hand;
    }
    
    public PrintStream getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(PrintStream endPoint) {
        this.endPoint = endPoint;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

  

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isTurn() {
        return turn;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
    
}

