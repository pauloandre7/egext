package com.pauloandre7.egext.repositories;

import com.pauloandre7.egext.models.InscritoRecord;
import java.util.List;

/**
 *
 * @author paulo
 */
public interface IInscritoRepository {
    InscritoRecord findById(int id);
    InscritoRecord findByNome(String nome);
    List<InscritoRecord> findAll();
    void create(InscritoRecord voluntario);
    void delete(int id);
    void update(int id, InscritoRecord newVoluntario);
}
