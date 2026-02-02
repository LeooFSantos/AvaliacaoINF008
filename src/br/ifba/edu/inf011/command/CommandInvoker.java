package br.ifba.edu.inf011.command;

import br.ifba.edu.inf011.model.documentos.Documento;
import java.util.Stack;


public class CommandInvoker {

    private Stack<Command> pilhaUndo;
    private Stack<Command> pilhaRedo;
    private OperationLogger logger;
    private Documento documentoAtual;

    public CommandInvoker(Documento documentoInicial) {
        this.pilhaUndo = new Stack<>();
        this.pilhaRedo = new Stack<>();
        
        this.logger = new OperationLogger(documentoInicial != null ? documentoInicial.getNumero() : null);
        this.documentoAtual = documentoInicial;
    }

    
    public Documento executar(Command comando) {
        documentoAtual = comando.execute();
        pilhaUndo.push(comando);
        pilhaRedo.clear(); 
        logger.registrarExecucao(comando);
        return documentoAtual;
    }

    
    public Documento undo() {
        if (pilhaUndo.isEmpty()) {
            System.out.println("Nenhuma operação para desfazer.");
            return documentoAtual;
        }

        Command comando = pilhaUndo.pop();
        documentoAtual = comando.undo();
        pilhaRedo.push(comando);
        logger.registrarUndo(comando);
        return documentoAtual;
    }

    
    public Documento redo() {
        if (pilhaRedo.isEmpty()) {
            System.out.println("Nenhuma operação para refazer.");
            return documentoAtual;
        }

        Command comando = pilhaRedo.pop();
        documentoAtual = comando.redo();
        pilhaUndo.push(comando);
        logger.registrarRedo(comando);
        return documentoAtual;
    }

    
    public boolean temUndo() {
        return !pilhaUndo.isEmpty();
    }

    
    public boolean temRedo() {
        return !pilhaRedo.isEmpty();
    }

    
    public void consolidar() {
        int operacoesDescartadas = pilhaUndo.size() + pilhaRedo.size();
        pilhaUndo.clear();
        pilhaRedo.clear();
        logger.registrarConsolidacao(operacoesDescartadas);
        System.out.println("Alterações consolidadas. " + operacoesDescartadas
                + " operações de undo/redo foram descartadas.");
    }

    
    public Documento obterDocumentoAtual() {
        return documentoAtual;
    }

    
    public OperationLogger obterLogger() {
        return logger;
    }

    
    public int obterTamanhoPilhaUndo() {
        return pilhaUndo.size();
    }

    
    public int obterTamanhoPilhaRedo() {
        return pilhaRedo.size();
    }

    
    public void setDocumento(Documento documento) {
        this.documentoAtual = documento;
    }

    
    public void limparHistorico() {
        this.pilhaUndo.clear();
        this.pilhaRedo.clear();
    }
}
