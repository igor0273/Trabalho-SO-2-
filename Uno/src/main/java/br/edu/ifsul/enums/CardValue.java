package br.edu.ifsul.enums;


/**
 * Enum que define os numeros das cardas
 * @author Igor Rocha
 *
 */
public enum CardValue
{
  UM(1),
  DOIS(2),
  TRES(3),
  QUATRO(4),
  CINCO(5),
  SEIS(6),
  SETE(7),
  OITO(8),
  NOVE(9),
  ADD2(20),
  ADD4(50),
  REVERSO(20), 
  BLOQUEIO(20);

  private int valor;

  // Inicializa a carta com valor numérico
  private CardValue (int value) {
    this.valor = value;
  }

  // Retorna o valor numérico da carta
  public int getCardValue() {
    return valor;
  }
  
}
