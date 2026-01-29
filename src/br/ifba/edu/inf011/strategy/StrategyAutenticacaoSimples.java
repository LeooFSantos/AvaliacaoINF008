package br.ifba.edu.inf011.strategy;

import java.time.LocalDate;

import br.ifba.edu.inf011.model.documentos.Documento;

public class StrategyAutenticacaoSimples implements StrategyAutenticacao {
	
	private static int contador = 0;
	
	@Override
	public String gerarNumeroProtocolo(Documento documento) {
		int ano = LocalDate.now().getYear();
		int hashDoc = Math.abs(documento.hashCode());
		contador++;
		
		return String.format("CRI-%d-%d-%d", ano, hashDoc, contador);
	}
	
	@Override
	public String descricao() {
		return "Strategy Simples - Geração sequencial com ano, hash e contador";
	}
}
