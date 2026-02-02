package br.ifba.edu.inf011.ui;

import br.ifba.edu.inf011.model.FWDocumentException;
import br.ifba.edu.inf011.model.GerenciadorDocumentoModel;
import br.ifba.edu.inf011.model.GestorDocumento;
import br.ifba.edu.inf011.model.documentos.Documento;

import java.awt.Component;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class JPanelOperacoes extends JPanel {

    private GerenciadorDocumentoModel controller;
    private JButton btnUndo;
    private JButton btnRedo;
    private JButton btnConsolidar;
    private JLabel lblHistorico;
    private AbstractGerenciadorDocumentosUI ui;

    public JPanelOperacoes() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createTitledBorder("Ações"));
    }

    
    public void integrarCommandPattern(GerenciadorDocumentoModel controller, AbstractGerenciadorDocumentosUI ui) throws FWDocumentException {
        
        
        
        this.controller = controller;
        this.ui = ui;

        
        this.add(Box.createVerticalStrut(40));
        lblHistorico = new JLabel("─── Histórico ───");
        lblHistorico.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(lblHistorico);

        
        btnUndo = new JButton("↶ Desfazer");
        btnUndo.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnUndo.addActionListener(e -> {
            try {
                executarUndo();
            } catch (FWDocumentException ex) {
                throw new RuntimeException(ex);
            }
        });
        this.add(Box.createVerticalStrut(10));
        this.add(btnUndo);

        
        btnRedo = new JButton("↷ Refazer");
        btnRedo.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRedo.addActionListener(e -> {
            try {
                executarRedo();
            } catch (FWDocumentException ex) {
                throw new RuntimeException(ex);
            }
        });
        this.add(Box.createVerticalStrut(10));
        this.add(btnRedo);

        
        lblHistorico = new JLabel("Histórico: 0 undo | 0 redo");
        lblHistorico.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(Box.createVerticalStrut(10));
        this.add(lblHistorico);

        
        btnConsolidar = new JButton("✓ Consolidar Alterações");
        btnConsolidar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnConsolidar.addActionListener(e -> {
            try {
                executarConsolidar();
            } catch (FWDocumentException ex) {
                throw new RuntimeException(ex);
            }
        });
        this.add(Box.createVerticalStrut(10));
        this.add(btnConsolidar);

        atualizarEstadoBotoes();
        this.updateUI();
    }

    
    private void executarUndo() throws FWDocumentException {
        GestorDocumento gestorAtual = obterGestorAtual();
        if (gestorAtual != null && gestorAtual.temUndo()) {
            Documento d = controller.undo();
            atualizarEstadoBotoes();
            if (ui != null) ui.atualizarDocumentoAposUndo(d);
            JOptionPane.showMessageDialog(this, "✓ Desfeito!");
        }
    }

    
    private void executarRedo() throws FWDocumentException {
        GestorDocumento gestorAtual = obterGestorAtual();
        if (gestorAtual != null && gestorAtual.temRedo()) {
            Documento d = controller.redo();
            atualizarEstadoBotoes();
            if (ui != null) ui.atualizarDocumentoAposUndo(d);
            JOptionPane.showMessageDialog(this, "✓ Refeito!");
        }
    }

    
    private void executarConsolidar() throws FWDocumentException {
        int resposta = JOptionPane.showConfirmDialog(this,
                "Consolidar? Não poderá desfazer mais.",
                "Consolidação", JOptionPane.YES_NO_OPTION);
        if (resposta == JOptionPane.YES_OPTION) {
            controller.consolidar();
            atualizarEstadoBotoes();
            JOptionPane.showMessageDialog(this, "✓ Consolidado!");
        }
    }

    
    public void atualizarEstadoBotoes() {
        if (btnUndo == null) return;

        GestorDocumento gestorAtual = obterGestorAtual();
        if (gestorAtual == null) {
            btnUndo.setEnabled(false);
            btnRedo.setEnabled(false);
            btnConsolidar.setEnabled(false);
            if (lblHistorico != null) lblHistorico.setText("Histórico: 0 undo | 0 redo");
            return;
        }

        btnUndo.setEnabled(gestorAtual.temUndo());
        btnRedo.setEnabled(gestorAtual.temRedo());
        btnConsolidar.setEnabled(gestorAtual.temUndo() || gestorAtual.temRedo());

        int undo = gestorAtual.obterInvoker().obterTamanhoPilhaUndo();
        int redo = gestorAtual.obterInvoker().obterTamanhoPilhaRedo();
        if (lblHistorico != null) {
            lblHistorico.setText(String.format("Histórico: %d undo | %d redo", undo, redo));
        }
    }

    private GestorDocumento obterGestorAtual() {
        if (controller == null) return null;
        try {
            return controller.obterGestor();
        } catch (FWDocumentException e) {
            return null;
        }
    }

    public void addOperacao(String operacao, ActionListener acao) {
        JButton btn = new JButton(operacao);
        btn.addActionListener(acao);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(Box.createVerticalStrut(10));
        this.add(btn);
        this.updateUI();
    }
}
