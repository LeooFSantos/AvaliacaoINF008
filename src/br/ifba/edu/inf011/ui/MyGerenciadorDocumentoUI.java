package br.ifba.edu.inf011.ui;

import javax.swing.JOptionPane;

import br.ifba.edu.inf011.af.DocumentOperatorFactory;
import br.ifba.edu.inf011.model.FWDocumentException;
import br.ifba.edu.inf011.model.documentos.Documento;
import br.ifba.edu.inf011.model.documentos.Privacidade;

public class MyGerenciadorDocumentoUI extends AbstractGerenciadorDocumentosUI {

    public MyGerenciadorDocumentoUI(DocumentOperatorFactory factory) throws FWDocumentException {
        super(factory);
    }

    @Override
    protected JPanelOperacoes montarMenuOperacoes() throws FWDocumentException {
        JPanelOperacoes painel = new JPanelOperacoes();

        painel.addOperacao("➕ Criar Publico", e -> this.criarDocumentoPublico());
        painel.addOperacao("➕ Criar Privado", e -> this.criarDocumentoPrivado());
        painel.addOperacao("💾 Salvar", e -> this.salvarConteudo());
        painel.addOperacao("🔑 Proteger", e -> this.protegerDocumento());
        painel.addOperacao("✍️ Assinar", e -> this.assinarDocumento());
        painel.addOperacao("⏰ Urgente", e -> this.tornarUrgente());

        
        painel.addOperacao("⚡ Alterar e Assinar", e -> this.macroAlterarEAssinar());
        painel.addOperacao("⚡ Priorizar", e -> this.macroPriorizar());

        
        painel.integrarCommandPattern(this.controller, this);

        return painel;
    }

    protected void criarDocumentoPublico() {
        this.criarDocumento(Privacidade.PUBLICO);
    }

    protected void criarDocumentoPrivado() {
        this.criarDocumento(Privacidade.SIGILOSO);
    }

    protected void salvarConteudo() {
        try {
            this.controller.salvarDocumento(this.atual, this.areaEdicao.getConteudo());
            this.atual = this.controller.getDocumentoAtual();
            this.refreshUI();

            if (this.pnlOperacoes != null) {
                this.pnlOperacoes.atualizarEstadoBotoes();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao Salvar: " + e.getMessage());
        }
    }

    protected void protegerDocumento() {
        try {
            this.controller.protegerDocumento(this.atual);
            this.atual = this.controller.getDocumentoAtual();
            this.refreshUI();

            if (this.pnlOperacoes != null) {
                this.pnlOperacoes.atualizarEstadoBotoes();
            }
        } catch (FWDocumentException e) {
            JOptionPane.showMessageDialog(this, "Erro ao proteger: " + e.getMessage());
        }
    }

    protected void assinarDocumento() {
        try {
            this.controller.assinarDocumento(this.atual);
            this.atual = this.controller.getDocumentoAtual();
            this.refreshUI();

            if (this.pnlOperacoes != null) {
                this.pnlOperacoes.atualizarEstadoBotoes();
            }
        } catch (FWDocumentException e) {
            JOptionPane.showMessageDialog(this, "Erro ao assinar: " + e.getMessage());
        }
    }

    protected void tornarUrgente() {
        try {
            this.controller.tornarUrgente(this.atual);
            this.atual = this.controller.getDocumentoAtual();
            this.refreshUI();

            if (this.pnlOperacoes != null) {
                this.pnlOperacoes.atualizarEstadoBotoes();
            }
        } catch (FWDocumentException e) {
            JOptionPane.showMessageDialog(this, "Erro ao tornar urgente: " + e.getMessage());
        }
    }

    
    
    protected void macroAlterarEAssinar() {
        try {
            if (this.atual == null) {
                JOptionPane.showMessageDialog(this, "Selecione um documento primeiro.");
                return;
            }

            String textoAtualEditor = this.areaEdicao.getConteudo();
            this.controller.alterarEAssinar(this.atual, textoAtualEditor);
            this.atual = this.controller.getDocumentoAtual();
            this.refreshUI();

            if (this.pnlOperacoes != null) {
                this.pnlOperacoes.atualizarEstadoBotoes();
            }
        } catch (FWDocumentException e) {
            JOptionPane.showMessageDialog(this, "Erro na macro Alterar e Assinar: " + e.getMessage());
        }
    }

    
    protected void macroPriorizar() {
        try {
            if (this.atual == null) {
                JOptionPane.showMessageDialog(this, "Selecione um documento primeiro.");
                return;
            }

            this.controller.priorizar(this.atual);
            this.atual = this.controller.getDocumentoAtual();
            this.refreshUI();

            if (this.pnlOperacoes != null) {
                this.pnlOperacoes.atualizarEstadoBotoes();
            }
        } catch (FWDocumentException e) {
            JOptionPane.showMessageDialog(this, "Erro na macro Priorizar: " + e.getMessage());
        }
    }

    @Override
    public void atualizarDocumentoAposUndo(Documento doc) {
        super.atualizarDocumentoAposUndo(doc);
    }

    private void criarDocumento(Privacidade privacidade) {
        try {
            int tipoIndex = this.barraSuperior.getTipoSelecionadoIndice();
            this.atual = this.controller.criarDocumento(tipoIndex, privacidade);
            this.barraDocs.addDoc("[" + atual.getNumero() + "]");

            this.controller.selecionarDocumento(this.atual);

            this.refreshUI();
            if (this.pnlOperacoes != null) {
                this.pnlOperacoes.atualizarEstadoBotoes();
            }
        } catch (FWDocumentException e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
        }
    }
}
