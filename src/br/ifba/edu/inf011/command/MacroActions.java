package br.ifba.edu.inf011.command;

import br.ifba.edu.inf011.model.documentos.Documento;
import br.ifba.edu.inf011.model.operador.Operador;


public class MacroActions {

    
    public static CompositeCommand alterarEAssinar(Documento documento,
                                                   String novoConteudo,
                                                   Operador operador) {
        CompositeCommand macro = new CompositeCommand("Alterar e Assinar", documento);

        macro.adicionar(new AlterarConteudoCommand(documento, novoConteudo));
        macro.adicionar(new AssinarCommand(documento, operador));

        return macro;
    }

    
    public static CompositeCommand priorizar(Documento documento, Operador operador) {
        CompositeCommand macro = new CompositeCommand("Priorizar", documento);

        macro.adicionar(new TornarUrgenteCommand(documento));
        macro.adicionar(new AssinarCommand(documento, operador));

        return macro;
    }

    
    public static CompositeCommand protegerEAssinar(Documento documento, Operador operador) {
        CompositeCommand macro = new CompositeCommand("Proteger e Assinar", documento);

        macro.adicionar(new ProtegeCommand(documento));
        macro.adicionar(new AssinarCommand(documento, operador));

        return macro;
    }
}
