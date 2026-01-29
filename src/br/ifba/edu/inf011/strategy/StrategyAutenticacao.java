package br.ifba.edu.inf011.strategy;

import br.ifba.edu.inf011.model.documentos.Documento;

public interface StrategyAutenticacao {
	String gerarNumeroProtocolo(Documento documento);
	String descricao();
}
