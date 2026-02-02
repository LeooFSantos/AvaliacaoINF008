package br.ifba.edu.inf011.command;

import br.ifba.edu.inf011.decorator.SeloUrgenciaDecorator;
import br.ifba.edu.inf011.model.documentos.Documento;


public class TornarUrgenteCommand implements Command, DocumentoAwareCommand {

    private Documento documentoOriginal;
    private Documento documentoUrgente;
    private boolean executado = false;

    public TornarUrgenteCommand(Documento documento) {
        this.documentoOriginal = documento;
    }

    @Override
    public void setDocumento(Documento documento) {
        this.documentoOriginal = documento;
        this.documentoUrgente = null;
        this.executado = false;
    }

    @Override
    public Documento execute() {
        if (!executado) {
            documentoUrgente = new SeloUrgenciaDecorator(documentoOriginal);
            executado = true;
        }
        return documentoUrgente;
    }

    @Override
    public Documento undo() {
        executado = false;
        return documentoOriginal;
    }

    @Override
    public Documento redo() {
        if (executado) return documentoUrgente;
        if (documentoUrgente == null) return execute();
        executado = true;
        return documentoUrgente;
    }

    @Override
    public String getDescricao() {
        return "Marcação de urgência do documento";
    }
}
