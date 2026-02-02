package br.ifba.edu.inf011.command;

import br.ifba.edu.inf011.model.documentos.Documento;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class CompositeCommand implements Command {

    private final List<Command> comandos = new ArrayList<>();
    private final String descricao;
    private final Documento documentoInicial;

    private boolean executado = false;
    private Documento documentoResultado;

    public CompositeCommand(String descricao, Documento documentoInicial) {
        this.descricao = descricao;
        this.documentoInicial = documentoInicial;
    }

    public void adicionar(Command comando) {
        comandos.add(comando);
    }

    @Override
    public Documento execute() {
        if (!executado) {
            Documento atual = documentoInicial;

            for (Command comando : comandos) {
                if (comando instanceof DocumentoAwareCommand) {
                    ((DocumentoAwareCommand) comando).setDocumento(atual);
                }
                atual = comando.execute();
            }

            documentoResultado = atual;
            executado = true;
        }
        return documentoResultado;
    }

    @Override
    public Documento undo() {
        List<Command> inverso = new ArrayList<>(comandos);
        Collections.reverse(inverso);

        for (Command comando : inverso) {
            comando.undo();
        }

        executado = false;
        return documentoInicial;
    }

    @Override
    public Documento redo() {
        Documento atual = documentoInicial;

        for (Command comando : comandos) {
            if (comando instanceof DocumentoAwareCommand) {
                ((DocumentoAwareCommand) comando).setDocumento(atual);
            }
            atual = comando.redo();
        }

        documentoResultado = atual;
        executado = true;
        return documentoResultado;
    }

    @Override
    public String getDescricao() {
        return String.format("Operação Composta: %s (%d etapas)", descricao, comandos.size());
    }
}
