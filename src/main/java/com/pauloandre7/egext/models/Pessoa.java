package com.pauloandre7.egext.models;

/**
 * Interface Pessoa para padronizar os contratos dos cadastros
 * de pessoas (Voluntarios e Inscritos) no sistema Egext.
 * 
 * @author pauloandre7
 */
public interface Pessoa {
    int id();
    String nome();
    String email();
    String celular();
}
