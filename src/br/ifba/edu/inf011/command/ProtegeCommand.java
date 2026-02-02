package br.ifba.edu.inf011.command;

import br.ifba.edu.inf011.model.documentos.Documento;
import br.ifba.edu.inf011.proxy.DocumentoConfidencial;


public class ProtegeCommand implements Command, DocumentoAwareCommand {

    private Documento documentoOriginal;
    private Documento documentoProtegido;
    private boolean executado = false;

    public ProtegeCommand(Documento documento) {
        this.documentoOriginal = documento;
    }

    @Override
    public void setDocumento(Documento documento) {
        this.documentoOriginal = documento;
        this.documentoProtegido = null;
        this.executado = false;
    }

    @Override
    public Documento execute() {
        if (!executado) {
            documentoProtegido = new DocumentoConfidencial(documentoOriginal);
            executado = true;
        }
        return documentoProtegido;
    }

    @Override
    public Documento undo() {
        executado = false;
        return documentoOriginal;
    }

    @Override
    public Documento redo() {
        if (executado) return documentoProtegido;
        if (documentoProtegido == null) return execute();
        executado = true;
        return documentoProtegido;
    }

    @Override
    public String getDescricao() {
        return "Proteção do documento (Confidencial)";
    }
}
