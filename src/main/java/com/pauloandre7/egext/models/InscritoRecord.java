package com.pauloandre7.egext.models;

/**
 * Record representando um Inscrito do evento.
 * Implementa a interface Pessoa, que ajuda na hora de criar métodos genéricos para
 * os métodos definidos na interface.
 * 
 * @author pauloandre7
 */
public record InscritoRecord(
    int id,
    String nome,
    String email,
    String celular
) implements Pessoa {
}
