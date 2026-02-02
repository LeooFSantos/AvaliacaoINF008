package br.ifba.edu.inf011.ui;

import br.ifba.edu.inf011.command.CommandInvoker;
import br.ifba.edu.inf011.model.GestorDocumento;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;


public class CommandPatternUIHelper {

    private GestorDocumento gestor;
    private CommandInvoker invoker;

    
    private JButton btnUndo;
    private JButton btnRedo;
    private JButton btnConsolidar;
    private JLabel lblHistorico;

    public CommandPatternUIHelper(GestorDocumento gestor) {
        this.gestor = gestor;
        this.invoker = gestor.obterInvoker();
    }

    
    public JButton criarBotaoUndo() {
        btnUndo = new JButton("↶ Desfazer");
        btnUndo.addActionListener(this::executarUndo);
        atualizarEstadoBotaoUndo();
        return btnUndo;
    }

    
    public JButton criarBotaoRedo() {
        btnRedo = new JButton("↷ Refazer");
        btnRedo.addActionListener(this::executarRedo);
        atualizarEstadoBotaoRedo();
        return btnRedo;
    }

    
    public JButton criarBotaoConsolidar() {
        btnConsolidar = new JButton("✓ Consolidar Alterações");
        btnConsolidar.addActionListener(this::executarConsolidar);
        atualizarEstadoBotaoConsolidar();
        return btnConsolidar;
    }

    
    public JLabel criarLabelHistorico() {
        lblHistorico = new JLabel();
        atualizarLabelHistorico();
        return lblHistorico;
    }

    
    private void executarUndo(ActionEvent e) {
        if (gestor.temUndo()) {
            gestor.undo();
            atualizarInterfaceCompleta();
            JOptionPane.showMessageDialog(null, "Operação desfeita!");
        } else {
            JOptionPane.showMessageDialog(null, "Nenhuma operação para desfazer.",
                    "Undo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    
    private void executarRedo(ActionEvent e) {
        if (gestor.temRedo()) {
            gestor.redo();
            atualizarInterfaceCompleta();
            JOptionPane.showMessageDialog(null, "Operação refeita!");
        } else {
            JOptionPane.showMessageDialog(null, "Nenhuma operação para refazer.",
                    "Redo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    
    private void executarConsolidar(ActionEvent e) {
        int resposta = JOptionPane.showConfirmDialog(null,
                "Consolidar alterações? Isto não poderá ser desfeito.",
                "Consolidação", JOptionPane.YES_NO_OPTION);

        if (resposta == JOptionPane.YES_OPTION) {
            gestor.consolidar();
            atualizarInterfaceCompleta();
            JOptionPane.showMessageDialog(null,
                    "Alterações consolidadas permanentemente!");
        }
    }

    
    public void atualizarEstadoBotaoUndo() {
        if (btnUndo != null) {
            btnUndo.setEnabled(gestor.temUndo());
        }
    }

    
    public void atualizarEstadoBotaoRedo() {
        if (btnRedo != null) {
            btnRedo.setEnabled(gestor.temRedo());
        }
    }

    
    public void atualizarEstadoBotaoConsolidar() {
        if (btnConsolidar != null) {
            btnConsolidar.setEnabled(gestor.temUndo() || gestor.temRedo());
        }
    }

    
    public void atualizarLabelHistorico() {
        if (lblHistorico != null) {
            int undo = invoker.obterTamanhoPilhaUndo();
            int redo = invoker.obterTamanhoPilhaRedo();
            lblHistorico.setText(String.format("Histórico: %d undo | %d redo", undo, redo));
        }
    }

    
    public void atualizarInterfaceCompleta() {
        atualizarEstadoBotaoUndo();
        atualizarEstadoBotaoRedo();
        atualizarEstadoBotaoConsolidar();
        atualizarLabelHistorico();
    }

    
    public static class ExemploUsoEmPainel extends JPanel {

        private GestorDocumento gestor;
        private CommandPatternUIHelper uiHelper;
        private JTextArea textArea;
        private JButton btnAlterar;

        public ExemploUsoEmPainel(GestorDocumento gestor) {
            this.gestor = gestor;
            this.uiHelper = new CommandPatternUIHelper(gestor);

            setLayout(new BorderLayout());

            
            JPanel painelControle = new JPanel();
            painelControle.add(uiHelper.criarBotaoUndo());
            painelControle.add(uiHelper.criarBotaoRedo());
            painelControle.add(uiHelper.criarBotaoConsolidar());
            painelControle.add(uiHelper.criarLabelHistorico());
            add(painelControle, BorderLayout.NORTH);

            
            textArea = new JTextArea(10, 40);
            add(new JScrollPane(textArea), BorderLayout.CENTER);

            
            JPanel painelAcoes = new JPanel();
            btnAlterar = new JButton("Alterar Conteúdo");
            btnAlterar.addActionListener(e -> {
                gestor.alterarConteudoComComando(textArea.getText());
                uiHelper.atualizarInterfaceCompleta();
            });
            painelAcoes.add(btnAlterar);
            add(painelAcoes, BorderLayout.SOUTH);
        }
    }
}
