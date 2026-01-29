package br.ifba.edu.inf011.strategy;

import java.time.LocalDate;

import br.ifba.edu.inf011.model.documentos.Documento;
import br.ifba.edu.inf011.model.operador.Operador;

public class StrategyAutenticacaoPorOrgao implements StrategyAutenticacao {
	
	private static int contador = 0;
	
	@Override
	public String gerarNumeroProtocolo(Documento documento) {
		Operador proprietario = documento.getProprietario();
		int ano = LocalDate.now().getYear();
		int hashDoc = Math.abs(documento.hashCode());
		contador++;
		
		String orgao = proprietario.getClass().getSimpleName().toUpperCase();
		
		return String.format("%s-%d-%d-%d", orgao, ano, hashDoc, contador);
	}
	
	@Override
	public String descricao() {
		return "Strategy por Órgão - Prefixo baseado na classe do operador proprietário";
	}
}
