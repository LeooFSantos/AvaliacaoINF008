package br.ifba.edu.inf011.strategy.singletonfactory;

import br.ifba.edu.inf011.strategy.StrategyAutenticacao;
import br.ifba.edu.inf011.strategy.StrategyAutenticacaoPorOrgao;
import br.ifba.edu.inf011.strategy.StrategyAutenticacaoPorUnidade;
import br.ifba.edu.inf011.strategy.StrategyAutenticacaoSimples;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class SingletonFactoryStrategyAutenticacao {

	private static final SingletonFactoryStrategyAutenticacao instance = new SingletonFactoryStrategyAutenticacao();
	private final Map<Integer, Supplier<StrategyAutenticacao>> registry = new ConcurrentHashMap<>();
	private final Supplier<StrategyAutenticacao> defaultSupplier = StrategyAutenticacaoSimples::new;

	private SingletonFactoryStrategyAutenticacao() {
		registrar(0, StrategyAutenticacaoSimples::new);
		registrar(1, StrategyAutenticacaoPorOrgao::new);
		registrar(2, StrategyAutenticacaoPorUnidade::new);
	}

	public static SingletonFactoryStrategyAutenticacao getInstance() {
		return instance;
	}

	public void registrar(Integer tipo, Supplier<StrategyAutenticacao> supplier) {
		Objects.requireNonNull(tipo, "tipo");
		Objects.requireNonNull(supplier, "supplier");
		registry.put(tipo, supplier);
	}

	public void remover(Integer tipo) {
		if (tipo == null) return;
		registry.remove(tipo);
	}

	public StrategyAutenticacao criar(Integer tipo) {
		Supplier<StrategyAutenticacao> supplier = registry.getOrDefault(tipo, defaultSupplier);
		return supplier.get();
	}
}
