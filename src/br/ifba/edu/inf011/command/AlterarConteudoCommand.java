package br.ifba.edu.inf011.command;

import br.ifba.edu.inf011.model.documentos.Documento;


public class AlterarConteudoCommand implements Command, DocumentoAwareCommand {

    private Documento documento;
    private String conteudoAnterior;
    private String conteudoNovo;
    private boolean executado = false;

    public AlterarConteudoCommand(Documento documento, String conteudoNovo) {
        this.documento = documento;
        this.conteudoNovo = conteudoNovo;
        this.conteudoAnterior = lerConteudoSeguro(documento);
    }

    @Override
    public void setDocumento(Documento documento) {
        this.documento = documento;
        this.conteudoAnterior = lerConteudoSeguro(documento);
        this.executado = false;
    }

    private String lerConteudoSeguro(Documento doc) {
        try {
            String c = (doc != null) ? doc.getConteudo() : "";
            return (c == null) ? "" : c;
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public Documento execute() {
        if (!executado) {
            documento.setConteudo(conteudoNovo);
            executado = true;
        }
        return documento;
    }

    @Override
    public Documento undo() {
        documento.setConteudo(conteudoAnterior);
        executado = false;
        return documento;
    }

    @Override
    public Documento redo() {
        if (executado) return documento;
        documento.setConteudo(conteudoNovo);
        executado = true;
        return documento;
    }

    @Override
    public String getDescricao() {
        int lenAnterior = conteudoAnterior != null ? conteudoAnterior.length() : 0;
        int lenNovo = conteudoNovo != null ? conteudoNovo.length() : 0;
        return String.format("Alteração de conteúdo do documento (de %d para %d caracteres)",
                lenAnterior, lenNovo);
    }
}
