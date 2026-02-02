package br.ifba.edu.inf011.model;

import br.ifba.edu.inf011.af.DocumentOperatorFactory;
import br.ifba.edu.inf011.model.documentos.Documento;
import br.ifba.edu.inf011.model.documentos.Privacidade;
import br.ifba.edu.inf011.model.operador.Operador;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GerenciadorDocumentoModel {

    private List<Documento> repositorio;
    private DocumentOperatorFactory factory;
    private Autenticador autenticador;
    private Map<Documento, GestorDocumento> gestoraParaDocumento;
    private Documento atual;

    public GerenciadorDocumentoModel(DocumentOperatorFactory factory) throws FWDocumentException {
        this.repositorio = new ArrayList<>();
        this.factory = factory;
        this.autenticador = new Autenticador();
        this.gestoraParaDocumento = new HashMap<>();
        this.atual = null;
    }

    public Documento criarDocumento(int tipoAutenticadorIndex, Privacidade privacidade) throws FWDocumentException {
        Operador operador = factory.getOperador();
        Documento documento = factory.getDocumento();

        operador.inicializar("jdc", "João das Couves");
        documento.inicializar(operador, privacidade);

        this.autenticador.autenticar(tipoAutenticadorIndex, documento);
        this.repositorio.add(documento);
        this.atual = documento;

        GestorDocumento gestor = new GestorDocumento(documento);
        this.gestoraParaDocumento.put(documento, gestor);

        return documento;
    }

    public void salvarDocumento(Documento doc, String conteudo) throws Exception {
        if (doc != null) {
            GestorDocumento gestor = obterGestorDocumento(doc);

            Documento atualizado = gestor.alterarConteudoComComando(conteudo);
            this.atualizarRepositorio(doc, atualizado);
            this.atual = atualizado;
        }
    }

    public List<Documento> getRepositorio() {
        return repositorio;
    }

    public void selecionarDocumento(Documento doc) throws FWDocumentException {
        if (doc == null) {
            this.atual = null;
            return;
        }

        this.atual = doc;
        this.gestoraParaDocumento.computeIfAbsent(doc, d -> new GestorDocumento(d));
    }

    public Documento assinarDocumento(Documento doc) throws FWDocumentException {
        if (doc == null) return null;

        Operador operador = factory.getOperador();
        operador.inicializar("jdc", "João das Couves");

        GestorDocumento gestor = obterGestorDocumento(doc);
        Documento assinado = gestor.assinarComComando(operador);
        this.atualizarRepositorio(doc, assinado);
        this.atual = assinado;
        return assinado;
    }

    public Documento protegerDocumento(Documento doc) throws FWDocumentException {
        if (doc == null) return null;

        GestorDocumento gestor = obterGestorDocumento(doc);
        Documento protegido = gestor.protegerComComando();
        this.atualizarRepositorio(doc, protegido);
        this.atual = protegido;
        return protegido;
    }

    public Documento tornarUrgente(Documento doc) throws FWDocumentException {
        if (doc == null) return null;

        GestorDocumento gestor = obterGestorDocumento(doc);
        Documento urgente = gestor.tornarUrgenteComComando();
        this.atualizarRepositorio(doc, urgente);
        this.atual = urgente;
        return urgente;
    }

    
    
    public Documento alterarEAssinar(Documento doc, String novoConteudo) throws FWDocumentException {
        if (doc == null) return null;

        Operador operador = factory.getOperador();
        operador.inicializar("jdc", "João das Couves");

        GestorDocumento gestor = obterGestorDocumento(doc);
        Documento resultado = gestor.alterarEAssinar(novoConteudo, operador);
        this.atualizarRepositorio(doc, resultado);
        this.atual = resultado;
        return resultado;
    }

    
    public Documento priorizar(Documento doc) throws FWDocumentException {
        if (doc == null) return null;

        Operador operador = factory.getOperador();
        operador.inicializar("jdc", "João das Couves");

        GestorDocumento gestor = obterGestorDocumento(doc);
        Documento resultado = gestor.priorizar(operador);
        this.atualizarRepositorio(doc, resultado);
        this.atual = resultado;
        return resultado;
    }

    
    public Documento undo() throws FWDocumentException {
        if (atual == null) return null;

        Documento antigo = this.atual;
        GestorDocumento gestor = obterGestorDocumento(antigo);
        Documento d = gestor.undo();

        this.atualizarRepositorio(antigo, d);
        this.atual = d;
        return d;
    }

    public Documento redo() throws FWDocumentException {
        if (atual == null) return null;

        Documento antigo = this.atual;
        GestorDocumento gestor = obterGestorDocumento(antigo);
        Documento d = gestor.redo();

        this.atualizarRepositorio(antigo, d);
        this.atual = d;
        return d;
    }

    public void consolidar() throws FWDocumentException {
        GestorDocumento gestor = obterGestorDocumento(atual);
        gestor.consolidar();
    }

    public int obterTamanhoPilhaUndo() throws FWDocumentException {
        GestorDocumento gestor = obterGestorDocumento(atual);
        return gestor.obterInvoker().obterTamanhoPilhaUndo();
    }

    public int obterTamanhoPilhaRedo() throws FWDocumentException {
        GestorDocumento gestor = obterGestorDocumento(atual);
        return gestor.obterInvoker().obterTamanhoPilhaRedo();
    }

    public void atualizarRepositorio(Documento antigo, Documento novo) {
        int index = repositorio.indexOf(antigo);
        if (index != -1) {
            repositorio.set(index, novo);

            if (gestoraParaDocumento.containsKey(antigo)) {
                GestorDocumento gestor = gestoraParaDocumento.remove(antigo);
                gestoraParaDocumento.put(novo, gestor);
            }
        }
    }

    public Documento getDocumentoAtual() {
        return this.atual;
    }

    public void setDocumentoAtual(Documento doc) {
        this.atual = doc;
    }

    private GestorDocumento obterGestorDocumento(Documento doc) throws FWDocumentException {
        if (doc == null) {
            return new GestorDocumento(factory.getDocumento());
        }
        return gestoraParaDocumento.computeIfAbsent(doc, d -> new GestorDocumento(d));
    }

    public GestorDocumento obterGestor() throws FWDocumentException {
        return obterGestorDocumento(atual);
    }
}
