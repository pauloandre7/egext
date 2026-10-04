package com.pauloandre7.egext.models;

import java.util.List;

/**
 * Record representando um Evento no Egext.
 * 
 * @author pauloandre7
 */
public record EventoRecord(
    int idEv,
    String titulo,
    String data,
    String local,
    VoluntarioRecord responsavel,
    List<VoluntarioRecord> voluntariosInternos,
    List<InscritoRecord> inscritos,
    StatusEvento statusEvento
) {
}
// Professor, optei por records para diminuir a verbosidade do Java, sem a 
// necessidade dependências externas, como Lombok.
