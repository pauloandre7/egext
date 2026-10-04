package com.pauloandre7.egext.views;

import com.pauloandre7.egext.models.VoluntarioRecord;
import java.util.List;

/**
 * Mock View para testes do módulo de Voluntários sem necessidade de interface Swing.
 * Implementa IVoluntariosView.
 * 
 * @author paulo
 */
public class MockVoluntariosView implements IVoluntariosView {

    private String raInput = "";
    private String nomeInput = "";
    private String emailInput = "";
    private String celularInput = "";

    public void setDadosFormulario(int par, String ra, String nome, String email, String celular) {
        this.raInput = ra;
        this.nomeInput = nome;
        this.emailInput = email;
        this.celularInput = celular;
    }

    @Override
    public String getRaInput() {
        return raInput;
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
        this.raInput = "";
        this.nomeInput = "";
        this.emailInput = "";
        this.celularInput = "";
    }

    @Override
    public void listarVoluntarios(List<VoluntarioRecord> voluntarios) {
        System.out.println("\n--- [VIEW VOLUNTÁRIOS] LISTA ATUALIZADA ---");
        if (voluntarios == null || voluntarios.isEmpty()) {
            System.out.println("(Nenhum voluntário cadastrado)");
        } else {
            for (VoluntarioRecord v : voluntarios) {
                System.out.printf("ID: %d | RA: %s | Nome: %s | Email: %s | Celular: %s%n",
                        v.id(), v.ra(), v.nome(), v.email(), v.celular());
            }
        }
        System.out.println("-------------------------------------------\n");
    }

    @Override
    public void exibirMensagem(String msg) {
        System.out.println("[VIEW VOLUNTÁRIOS - SUCESSO]: " + msg);
    }

    @Override
    public void exibirMensagemErro(String erro) {
        System.out.println("[VIEW VOLUNTÁRIOS - ERRO]: " + erro);
    }
}
