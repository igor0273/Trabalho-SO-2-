package br.edu.ifsul;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Classe representa o cliente que era 
 * receber o jogador e conectalo ao servidor
 * @author Igor Rocha
 *
 */

public class Client extends Thread {

    private static boolean done = false;
    private Socket connection;
    
    // Construtor
    public Client(Socket connection) {
        this.connection = connection;
    }

    public void run() {

		try {

            BufferedReader startingPoint = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            while (true) {
			    line = startingPoint.readLine();

                if (line == null || line.equals("Adeus!") || line.equals("Erro: Token inválido!")) {
                    System.out.println(line);
                    System.out.println("Fim da conexão!");
                    break;
                }

                System.out.println(line);
            }

		} catch (IOException e) {
            System.out.println("IOException: " + e);
		}

		done =true;

	}
    
    public static void main(String[] args) throws InterruptedException {
        try {

            // Define o socket
            Socket connection = new Socket("192.168.56.1", 2222);
            PrintStream endPoint = new PrintStream(connection.getOutputStream());
            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

  
            // Lê o nome do cliente
            System.out.println("Seu nome: ");
            String myAccount = keyboard.readLine();
            endPoint.println(myAccount);

            // Cria a conexão
            Thread t = new Client(connection);
            t.start();
            String line = "";
            Thread.sleep(1000);
                
            while (true) {
                if (done) {
                    break;
                } else {
                    line = keyboard.readLine();
                    endPoint.println(line);
                }
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }

    }

}
