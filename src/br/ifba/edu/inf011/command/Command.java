package br.ifba.edu.inf011.command;

import br.ifba.edu.inf011.model.documentos.Documento;


public interface Command {

    
    Documento execute();

    
    Documento undo();

    
    Documento redo();

    
    String getDescricao();
}
