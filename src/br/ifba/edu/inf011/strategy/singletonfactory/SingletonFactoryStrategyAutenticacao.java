package br.ifba.edu.inf011.strategy.singletonfactory;

import br.ifba.edu.inf011.strategy.StrategyAutenticacao;
import br.ifba.edu.inf011.strategy.StrategyAutenticacaoSimples;
import br.ifba.edu.inf011.strategy.StrategyAutenticacaoPorOrgao;
import br.ifba.edu.inf011.strategy.StrategyAutenticacaoPorUnidade;

public class SingletonFactoryStrategyAutenticacao {
	
	private static final SingletonFactoryStrategyAutenticacao instance = new SingletonFactoryStrategyAutenticacao();
	
	private SingletonFactoryStrategyAutenticacao() {
	}
	
	public static SingletonFactoryStrategyAutenticacao getInstance() {
		return instance;
	}
	
	public StrategyAutenticacao criar(Integer tipo) {
		switch(tipo) {
			case 0:
				return new StrategyAutenticacaoSimples();
			case 1:
				return new StrategyAutenticacaoPorOrgao();
			case 2:
				return new StrategyAutenticacaoPorUnidade();
			default:
				return new StrategyAutenticacaoSimples();
		}
	}
}
