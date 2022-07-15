package br.edu.ifsul.model;

import br.edu.ifsul.enums.CardCor;
import br.edu.ifsul.enums.CardValue;

/**
 * Classe representa as cartas
 * do baralho que sera Usado no jogo
 * @author Igor Rocha
 *
 */
public class Card implements Comparable<Card>{
    private CardValue value;
    private CardCor cor;

    // Getters e Setters
    public CardValue getValue() {
        return value;
    }

    public void setValue(CardValue value) {
        this.value = value;
    }

    public CardCor getType() {
        return cor;
    }

    public void setType(CardCor type) {
        this.cor = type;
    }
    
    // Compara as cartas para a realização dos sorts
    @Override
    public int compareTo(Card c) {
        if (this.value.getCardValue() < c.value.getCardValue()) {
            return -1;
        }
        
        if (this.value.getCardValue() > c.value.getCardValue()) {
            return 1;
        }

        return 0;
    }
}

