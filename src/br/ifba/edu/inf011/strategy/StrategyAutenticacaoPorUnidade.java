package br.ifba.edu.inf011.strategy;

import java.time.LocalDate;

import br.ifba.edu.inf011.model.documentos.Documento;

public class StrategyAutenticacaoPorUnidade implements StrategyAutenticacao {
	
	private static int contador = 0;
	
	@Override
	public String gerarNumeroProtocolo(Documento documento) {
		int ano = LocalDate.now().getYear();
		int diaDoAno = LocalDate.now().getDayOfYear();
		int hashDoc = Math.abs(documento.hashCode());
		contador++;
		
		return String.format("UNI-%d-%d-%d-%d", ano, diaDoAno, hashDoc, contador);
	}
	
	@Override
	public String descricao() {
		return "Strategy por Unidade - Prefixo baseado em unidades administrativas com dia do ano";
	}
}
