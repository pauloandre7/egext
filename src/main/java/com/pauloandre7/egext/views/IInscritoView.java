/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.pauloandre7.egext.views;

import com.pauloandre7.egext.models.InscritoRecord;
import java.util.List;

/**
 * Interface para estabelecer o contrato a ser seguido pela view e pelo Presenter,
 * desaclopando a comunicação entre esses dois módulos.
 * 
 * @author paulo
 */
public interface IInscritoView {
    
    String getNomeInput(); 
    String getEmailInput(); 
    String getCelularInput();
    
    void limparCampos();
    void listarInscritos(List<InscritoRecord> voluntarios);
    void exibirMensagem(String msg);
    void exibirMensagemErro(String erro);
}
