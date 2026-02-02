package br.ifba.edu.inf011.command;

import br.ifba.edu.inf011.decorator.AssinaturaDecorator;
import br.ifba.edu.inf011.model.Assinatura;
import br.ifba.edu.inf011.model.documentos.Documento;
import br.ifba.edu.inf011.model.operador.Operador;
import java.time.LocalDateTime;


public class AssinarCommand implements Command, DocumentoAwareCommand {

    private Documento documentoOriginal;
    private Documento documentoAssinado;
    private Operador operador;
    private boolean executado = false;

    public AssinarCommand(Documento documento, Operador operador) {
        this.documentoOriginal = documento;
        this.operador = operador;
    }

    @Override
    public void setDocumento(Documento documento) {
        this.documentoOriginal = documento;
        this.documentoAssinado = null;
        this.executado = false;
    }

    @Override
    public Documento execute() {
        if (!executado) {
            Assinatura assinatura = new Assinatura(operador, LocalDateTime.now());
            documentoAssinado = new AssinaturaDecorator(documentoOriginal, assinatura);
            executado = true;
        }
        return documentoAssinado;
    }

    @Override
    public Documento undo() {
        executado = false;
        return documentoOriginal;
    }

    @Override
    public Documento redo() {
        if (executado) return documentoAssinado;
        if (documentoAssinado == null) return execute();
        executado = true;
        return documentoAssinado;
    }

    @Override
    public String getDescricao() {
        return String.format("Assinatura do documento por %s", operador.getNome());
    }
}
