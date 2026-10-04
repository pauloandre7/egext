/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.pauloandre7.egext.views;

import com.pauloandre7.egext.models.EventoRecord;
import com.pauloandre7.egext.models.InscritoRecord;
import com.pauloandre7.egext.models.VoluntarioRecord;
import java.util.List;

/**
 * Interface para estabelecer o contrato a ser seguido pela view e pelo Presenter,
 * desaclopando a comunicação entre esses dois módulos.
 * 
 * Optei por usar o MVP para poder reaproveitar o projeto em outros modos de front.
 * 
 * @author paulo
 */
public interface IEventosView {
    
    String getTituloInput();
    String getDataInput();
    String getLocalInput();
    // Recebo nome para pesquisar no repo
    String getResponsavelInput();
    // Recebo array de nomes para pesquisar no Repo
    String[] getVoluntariosInput();
    String[] getInscritosInput();
    String getStatusInput();
        
    void listarEventos(List<EventoRecord> eventos);
    void detalharEvento(EventoRecord evento);
    void exibirMensagem(String msg);
    void exibirMensagemErro(String erro);
}
