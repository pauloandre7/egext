package com.pauloandre7.egext.views;

import com.pauloandre7.egext.models.InscritoRecord;
import java.util.List;

/**
 * Mock View para testes do módulo de Inscritos sem necessidade de interface Swing.
 * Implementa IInscritoView.
 * 
 * @author paulo
 */
public class MockInscritoView implements IInscritoView {

    private String nomeInput = "";
    private String emailInput = "";
    private String celularInput = "";

    public void setDadosFormulario(String nome, String email, String celular) {
        this.nomeInput = nome;
        this.emailInput = email;
        this.celularInput = celular;
    }

    @Override
    public String getNomeInput() {
        return nomeInput;
    }

    @Override
    public String getEmailInput() {
        return emailInput;
    }

    @Override
    public String getCelularInput() {
        return celularInput;
    }

    @Override
    public void limparCampos() {
        this.nomeInput = "";
        this.emailInput = "";
        this.celularInput = "";
    }

    @Override
    public void listarInscritos(List<InscritoRecord> inscritos) {
        System.out.println("\n--- [VIEW INSCRITOS] LISTA ATUALIZADA ---");
        if (inscritos == null || inscritos.isEmpty()) {
            System.out.println("(Nenhum inscrito cadastrado)");
        } else {
            for (InscritoRecord i : inscritos) {
                System.out.printf("ID: %d | Nome: %s | Email: %s | Celular: %s%n",
                        i.id(), i.nome(), i.email(), i.celular());
            }
        }
        System.out.println("-----------------------------------------\n");
    }

    @Override
    public void exibirMensagem(String msg) {
        System.out.println("[VIEW INSCRITOS - SUCESSO]: " + msg);
    }

    @Override
    public void exibirMensagemErro(String erro) {
        System.out.println("[VIEW INSCRITOS - ERRO]: " + erro);
    }
}
