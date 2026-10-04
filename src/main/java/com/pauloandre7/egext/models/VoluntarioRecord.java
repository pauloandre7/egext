package com.pauloandre7.egext.models;

/**
 * Record representando um Voluntario no Egext.
 * 
 * @author pauloandre7
 */
public record VoluntarioRecord(
    int id,
    String ra,
    String nome,
    String email,
    String celular
) implements Pessoa {
}
