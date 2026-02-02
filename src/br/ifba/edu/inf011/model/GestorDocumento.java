package br.ifba.edu.inf011.model;

import br.ifba.edu.inf011.command.AlterarConteudoCommand;
import br.ifba.edu.inf011.command.AssinarCommand;
import br.ifba.edu.inf011.command.CommandInvoker;
import br.ifba.edu.inf011.command.MacroActions;
import br.ifba.edu.inf011.command.ProtegeCommand;
import br.ifba.edu.inf011.command.TornarUrgenteCommand;
import br.ifba.edu.inf011.decorator.AssinaturaDecorator;
import br.ifba.edu.inf011.decorator.SeloUrgenciaDecorator;
import br.ifba.edu.inf011.model.documentos.Documento;
import br.ifba.edu.inf011.model.operador.Operador;
import br.ifba.edu.inf011.proxy.DocumentoConfidencial;
import java.time.LocalDateTime;


public class GestorDocumento {

    private CommandInvoker invoker;

    public GestorDocumento() {
        this.invoker = null;
    }

    public GestorDocumento(Documento documento) {
        this.invoker = new CommandInvoker(documento);
    }
    
    public Documento assinar(Documento documento, Operador operador) {
        Assinatura assinatura = new Assinatura(operador, LocalDateTime.now());
        Documento assinado = new AssinaturaDecorator(documento, assinatura);
        return assinado;
    }

    public Documento proteger(Documento documento) {
        return new DocumentoConfidencial(documento);
    }

    public Documento tornarUrgente(Documento documento) {
        return new SeloUrgenciaDecorator(documento);
    }

    
    
    public Documento assinarComComando(Operador operador) {
        AssinarCommand comando = new AssinarCommand(invoker.obterDocumentoAtual(), operador);
        return invoker.executar(comando);
    }

    
    public Documento alterarConteudoComComando(String novoConteudo) {
        AlterarConteudoCommand comando = new AlterarConteudoCommand(
                invoker.obterDocumentoAtual(), novoConteudo);
        return invoker.executar(comando);
    }

    
    public Documento protegerComComando() {
        ProtegeCommand comando = new ProtegeCommand(invoker.obterDocumentoAtual());
        return invoker.executar(comando);
    }

    
    public Documento tornarUrgenteComComando() {
        TornarUrgenteCommand comando = new TornarUrgenteCommand(invoker.obterDocumentoAtual());
        return invoker.executar(comando);
    }

    
    
    public Documento alterarEAssinar(String novoConteudo, Operador operador) {
        var macro = MacroActions.alterarEAssinar(invoker.obterDocumentoAtual(), novoConteudo, operador);
        return invoker.executar(macro);
    }

    
    public Documento priorizar(Operador operador) {
        var macro = MacroActions.priorizar(invoker.obterDocumentoAtual(), operador);
        return invoker.executar(macro);
    }

    
    public Documento protegerEAssinar(Operador operador) {
        var macro = MacroActions.protegerEAssinar(invoker.obterDocumentoAtual(), operador);
        return invoker.executar(macro);
    }

    
    
    public Documento undo() {
        return invoker.undo();
    }

    
    public Documento redo() {
        return invoker.redo();
    }

    
    public boolean temUndo() {
        return invoker.temUndo();
    }

    
    public boolean temRedo() {
        return invoker.temRedo();
    }

    
    
    public void consolidar() {
        invoker.consolidar();
    }

    
    
    public Documento obterDocumentoAtual() {
        return invoker.obterDocumentoAtual();
    }

    
    public CommandInvoker obterInvoker() {
        return invoker;
    }

    
    public void setDocumentoAtual(Documento documento) {
        this.invoker.setDocumento(documento);
    }

    
    public void limparHistoricoEAtualizar(Documento documento) {
        this.invoker.limparHistorico();
        this.invoker.setDocumento(documento);
    }
}
