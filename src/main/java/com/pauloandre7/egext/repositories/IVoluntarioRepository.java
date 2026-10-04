package com.pauloandre7.egext.repositories;
import com.pauloandre7.egext.models.VoluntarioRecord;
import java.util.List;
/**
 *
 * @author paulo
 */
public interface IVoluntarioRepository {
    
    VoluntarioRecord findById(int id);
    VoluntarioRecord findByNome(String nome);
    List<VoluntarioRecord> findAll();
    void create(VoluntarioRecord voluntario);
    void delete(int id);
    void update(int id, VoluntarioRecord newVoluntario);
}
