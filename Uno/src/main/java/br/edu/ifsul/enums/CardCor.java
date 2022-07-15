package br.edu.ifsul.enums;

/**
 * Enum que define a cor das cartas
 * @author Igor Rocha
 *
 */
public enum CardCor {

	VERMELHO("Red"),
	AMARELO("Yellow"),
	VERDE("Green"),
	AZUL("Blue");
	
	private String descricao;
	 
	private CardCor(String desc) {
		this.descricao = desc;
	}

	public String getDescricao() {
		return descricao;
	}
	
	
}
