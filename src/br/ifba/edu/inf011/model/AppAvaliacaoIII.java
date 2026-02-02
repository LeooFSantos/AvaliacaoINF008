package br.ifba.edu.inf011.model;

import javax.swing.SwingUtilities;

import br.ifba.edu.inf011.af.CalculoPericialPeritoFactory;
import br.ifba.edu.inf011.af.DocumentOperatorFactory;
import br.ifba.edu.inf011.ui.MyGerenciadorDocumentoUI;

public class AppAvaliacaoIII {
	
	public AppAvaliacaoIII() {
	}
	

	public void run(DocumentOperatorFactory factory) throws FWDocumentException {
	    SwingUtilities.invokeLater(() -> {
            try {
                new MyGerenciadorDocumentoUI(new CalculoPericialPeritoFactory()).setVisible(true);
            } catch (FWDocumentException e) {
                throw new RuntimeException(e);
            }
        });
	}

	public static void main(String[] args) throws FWDocumentException {
		new AppAvaliacaoIII().run(null);
	}
	
}
