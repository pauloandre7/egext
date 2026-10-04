/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.pauloandre7.egext.presenters;

import com.pauloandre7.egext.models.EventoRecord;
import java.util.List;

/**
 *
 * @author paulo
 */
public interface IEventoPresenter {
    
    void criarEvento();
    void excluirEvento(int id);
    void modificarEvento(int id);
    void abrirTelaVoluntarios();
    void abrirTelaInscritos();
    List<EventoRecord> buscarTodosEventos();
}
