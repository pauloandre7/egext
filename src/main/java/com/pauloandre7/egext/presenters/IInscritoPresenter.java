/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.pauloandre7.egext.presenters;

import com.pauloandre7.egext.models.InscritoRecord;
import java.util.List;

/**
 *
 * @author paulo
 */
public interface IInscritoPresenter {
    void registrarInscrito();
    void excluirInscrito(int id);
    void modificarInscrito(int id);
    List<InscritoRecord> listarInscritos();
}
