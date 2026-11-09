package com.pauloandre7.egext.models;

import java.io.Serializable;

/**
 * Interface Pessoa para padronizar os contratos dos cadastros
 * de pessoas (Voluntarios e Inscritos) no sistema Egext.
 * 
 * @author pauloandre7
 */
public interface Pessoa extends Serializable {
    int id();
    String nome();
    String email();
    String celular();
}
