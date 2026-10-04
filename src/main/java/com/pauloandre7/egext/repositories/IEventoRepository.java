/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.pauloandre7.egext.repositories;

import com.pauloandre7.egext.models.EventoRecord;
import java.util.List;

/**
 *
 * @author paulo
 */
public interface IEventoRepository {
    EventoRecord findById(int id);
    EventoRecord findByTitulo(String nome);
    List<EventoRecord> findAll();
    void create(EventoRecord voluntario);
    void delete(int id);
    void update(int id, EventoRecord newVoluntario);
}
