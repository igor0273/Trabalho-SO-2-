package br.edu.ifsul.model;

import br.edu.ifsul.enums.CardCor;
import br.edu.ifsul.enums.CardValue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.collections4.ListUtils;

/**
 * Classe controla e gera o baralho do jogo
 * @author Igor Rocha
 *
 */
public class Deck { 
    
    private final CardCor[] SUITS = {CardCor.VERDE,CardCor.VERMELHO,CardCor.AMARELO,CardCor.AZUL};
    private final CardValue[] RANKS = {CardValue.UM, CardValue.DOIS, CardValue.TRES, CardValue.QUATRO, CardValue.CINCO, CardValue.SEIS,CardValue.SETE, CardValue.OITO,CardValue.NOVE,CardValue.ADD2,CardValue.ADD4,CardValue.REVERSO,CardValue.BLOQUEIO};
    private List<Card> deck;

    // Inicializa o jogo com apenas um baralho
    public void initializeOneDeck() {
        deck = new ArrayList<>();
        
        for (CardValue RANKS1 : RANKS) {
            for (CardCor SUITS1 : SUITS) {
                Card c = new Card();
                c.setValue(RANKS1);
                c.setType(SUITS1);
                deck.add(c);
            }
        }
    }
    
    // Inicializa o jogo com 2 baralhos
    public void initializeTwoDeck() {
        initializeOneDeck();
        deck = ListUtils.union(deck,deck);
    }

    // Embaralha 
    public void shuffle() {
       Collections.shuffle(deck);
    }
    
    // Mostra todas as cartas do baralho (usado apenas para testes)
    public void printDeck() {
        System.out.println(deck.size());

        for(Card c:deck){
            System.out.println(c.getValue()+" of "+c.getType());
        }
    }
    
    // Retornar as 5 primeiras cartas do baralho retirando do mesmo
    public List<Card> giveCards() {
        List<Card> aux = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Card c = deck.remove(0);
            aux.add(c);
        }
 
        return aux;
    }
    
    // Retorna a primeira carta do baralho retirando do mesmo
    public Card giveFirstCard() {
       return deck.remove(0); 
    }

    // Retorna a ultima carta(trashCard) retirando do baralho
    public Card giveLastCard() {
       int index = deck.size() - 1;
       return deck.remove(index);
    }

    // Adiciona ao fim do baralho(trashCard)
    public void pushBack(Card c) {
        deck.add(c);
    }
}
