package com.pauloandre7.egext.views;

import com.pauloandre7.egext.models.EventoRecord;
import com.pauloandre7.egext.models.InscritoRecord;
import com.pauloandre7.egext.models.VoluntarioRecord;
import java.util.List;

/**
 * Mock View para testes do módulo de Eventos (IEventosView).
 * Atualizada conforme o contrato de IEventosView em interfaces.txt,
 * utilizando entradas String e String[] para nomes de responsável, voluntários e inscritos.
 * 
 * @author paulo
 */
public class MockEventosView implements IEventosView {

    private String tituloInput = "";
    private String dataInput = "";
    private String localInput = "";
    private String responsavelInput = "";
    private String[] voluntariosInput = new String[0];
    private String[] inscritosInput = new String[0];
    private String statusInput = "PLANEJAMENTO";

    /**
     * Define os dados do formulário simulado no Mock para os testes.
     */
    public void setDadosFormulario(String titulo, String data, String local, 
                                  String responsavelNome, String[] voluntariosNomes, 
                                  String[] inscritosNomes, String status) {
        this.tituloInput = titulo != null ? titulo : "";
        this.dataInput = data != null ? data : "";
        this.localInput = local != null ? local : "";
        this.responsavelInput = responsavelNome != null ? responsavelNome : "";
        this.voluntariosInput = voluntariosNomes != null ? voluntariosNomes : new String[0];
        this.inscritosInput = inscritosNomes != null ? inscritosNomes : new String[0];
        this.statusInput = status != null ? status : "PLANEJAMENTO";
    }

    @Override
    public String getTituloInput() {
        return tituloInput;
    }

    @Override
    public String getDataInput() {
        return dataInput;
    }

    @Override
    public String getLocalInput() {
        return localInput;
    }

    @Override
    public String getResponsavelInput() {
        return responsavelInput;
    }

    @Override
    public String[] getVoluntariosInput() {
        return voluntariosInput;
    }

    @Override
    public String[] getInscritosInput() {
        return inscritosInput;
    }

    @Override
    public String getStatusInput() {
        return statusInput;
    }

    @Override
    public void listarEventos(List<EventoRecord> eventos) {
        System.out.println("\n--- [VIEW EVENTOS] LISTA ATUALIZADA ---");
        if (eventos == null || eventos.isEmpty()) {
            System.out.println("(Nenhum evento cadastrado)");
        } else {
            for (EventoRecord e : eventos) {
                System.out.printf("ID: %d | Título: %s | Data: %s | Local: %s | Responsável: %s | Voluntários: %d | Inscritos: %d | Status: %s%n",
                        e.idEv(), e.titulo(), e.data(), e.local(),
                        e.responsavel() != null ? e.responsavel().nome() : "Sem responsável",
                        e.voluntariosInternos() != null ? e.voluntariosInternos().size() : 0,
                        e.inscritos() != null ? e.inscritos().size() : 0,
                        e.statusEvento());
            }
        }
        System.out.println("---------------------------------------\n");
    }

    @Override
    public void detalharEvento(EventoRecord evento) {
        System.out.println("\n=== [DETALHES DO EVENTO] ===");
        if (evento == null) {
            System.out.println("Evento nulo!");
            return;
        }
        System.out.println("ID: " + evento.idEv());
        System.out.println("Título: " + evento.titulo());
        System.out.println("Data: " + evento.data());
        System.out.println("Local: " + evento.local());
        System.out.println("Responsável: " + (evento.responsavel() != null ? evento.responsavel().nome() : "Nenhum"));
        System.out.println("Status: " + evento.statusEvento());
        
        System.out.println("Voluntários Internos:");
        if (evento.voluntariosInternos() != null && !evento.voluntariosInternos().isEmpty()) {
            for (VoluntarioRecord v : evento.voluntariosInternos()) {
                System.out.println("  - " + v.nome() + " (RA: " + v.ra() + ")");
            }
        } else {
            System.out.println("  (Nenhum voluntário vinculado)");
        }

        System.out.println("Inscritos:");
        if (evento.inscritos() != null && !evento.inscritos().isEmpty()) {
            for (InscritoRecord i : evento.inscritos()) {
                System.out.println("  - " + i.nome() + " (" + i.email() + ")");
            }
        } else {
            System.out.println("  (Nenhum inscrito vinculado)");
        }
        System.out.println("============================\n");
    }

    @Override
    public void exibirMensagem(String msg) {
        System.out.println("[VIEW EVENTOS - SUCESSO]: " + msg);
    }

    @Override
    public void exibirMensagemErro(String erro) {
        System.out.println("[VIEW EVENTOS - ERRO]: " + erro);
    }
}
