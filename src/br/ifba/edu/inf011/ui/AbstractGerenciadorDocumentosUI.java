package br.ifba.edu.inf011.ui;

import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import br.ifba.edu.inf011.af.DocumentOperatorFactory;
import br.ifba.edu.inf011.model.FWDocumentException;
import br.ifba.edu.inf011.model.GerenciadorDocumentoModel;
import br.ifba.edu.inf011.model.documentos.Documento;

public abstract class AbstractGerenciadorDocumentosUI extends JFrame implements ListSelectionListener {

    protected GerenciadorDocumentoModel controller;
    protected JPanelBarraSuperior<String> barraSuperior;
    protected JPanelAreaEdicao areaEdicao;
    protected JPanelListaDocumentos<String> barraDocs;

    
    protected JPanelOperacoes pnlOperacoes;

    protected String[] tipos = {"Criminal", "Pessoal", "Exportação", "Confidencial"};

    protected Documento atual;
    protected DefaultListModel<String> listDocs;

    public AbstractGerenciadorDocumentosUI(DocumentOperatorFactory factory) throws FWDocumentException {
        this.controller = new GerenciadorDocumentoModel(factory);
        this.listDocs = new DefaultListModel<>();
        this.barraSuperior = new JPanelBarraSuperior<>(tipos);
        this.areaEdicao = new JPanelAreaEdicao();
        this.barraDocs = new JPanelListaDocumentos<>(this.listDocs, this);
        this.montarAparencia();
    }

    protected abstract JPanelOperacoes montarMenuOperacoes() throws FWDocumentException;

    public void montarAparencia() throws FWDocumentException {
        
        this.setTitle("Sistema de Gestão de Documentos - INF011");
        this.setSize(800, 500);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        
        
        
        this.pnlOperacoes = this.montarMenuOperacoes();
        this.add(this.pnlOperacoes, BorderLayout.EAST);

        
        this.add(this.barraSuperior, BorderLayout.NORTH);
        this.add(this.areaEdicao, BorderLayout.CENTER);
        this.add(this.barraDocs, BorderLayout.WEST);
    }

    protected void refreshUI() {
        try {
            this.areaEdicao.atualizar(this.atual.getConteudo());
        } catch (Exception e) {
            this.areaEdicao.atualizar("");
            JOptionPane.showMessageDialog(this, "Erro ao Carregar : " + e.getMessage());
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            int index = this.barraDocs.getIndiceDocSelecionado();
            if (index != -1) {
                this.atual = controller.getRepositorio().get(index);

                try {
                    
                    controller.selecionarDocumento(this.atual);
                } catch (FWDocumentException ex) {
                    throw new RuntimeException(ex);
                }

                
                this.refreshUI();

                
                if (this.pnlOperacoes != null) {
                    this.pnlOperacoes.atualizarEstadoBotoes();
                }
            }
        }
    }

    
    public void atualizarDocumentoAposUndo(Documento doc) {
        this.atual = doc;
        this.refreshUI();

        if (this.pnlOperacoes != null) {
            this.pnlOperacoes.atualizarEstadoBotoes();
        }
    }
}
