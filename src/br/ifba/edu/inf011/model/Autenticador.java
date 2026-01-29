package br.ifba.edu.inf011.model;

import br.ifba.edu.inf011.model.documentos.Documento;
import br.ifba.edu.inf011.strategy.StrategyAutenticacao;
import br.ifba.edu.inf011.strategy.StrategyAutenticacaoSimples;
import br.ifba.edu.inf011.strategy.singletonfactory.SingletonFactoryStrategyAutenticacao;

public class Autenticador {
	
	private StrategyAutenticacao strategy;
	private SingletonFactoryStrategyAutenticacao factory;
	
	public Autenticador() {
		this.strategy = new StrategyAutenticacaoSimples();
		this.factory = SingletonFactoryStrategyAutenticacao.getInstance();
	}
	
	public Autenticador(StrategyAutenticacao strategy) {
		this.strategy = strategy;
		this.factory = SingletonFactoryStrategyAutenticacao.getInstance();
	}
	
	public void autenticar(Documento documento) {
		String numero = strategy.gerarNumeroProtocolo(documento);
		documento.setNumero(numero);
	}
	
	public void autenticar(Integer tipo, Documento documento) {
		this.strategy = factory.criar(tipo);
		String numero = strategy.gerarNumeroProtocolo(documento);
		documento.setNumero(numero);
	}
	
	public void setStrategy(StrategyAutenticacao strategy) {
		this.strategy = strategy;
	}
	
	public StrategyAutenticacao getStrategy() {
		return strategy;
	}
}
