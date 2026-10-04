package com.pauloandre7.egext.views;

import com.pauloandre7.egext.models.VoluntarioRecord;
import java.util.List;

/**
 * Interface para estabelecer o contrato a ser seguido pela view e pelo Presenter,
 * desaclopando a comunicação entre esses dois módulos.
 * 
 * @author paulo
 */
public interface IVoluntariosView {
    
    String getNomeInput(); 
    String getEmailInput(); 
    String getCelularInput();
    String getRaInput(); 
    
    void limparCampos();
    void listarVoluntarios(List<VoluntarioRecord> voluntarios);
    void exibirMensagem(String msg);
    void exibirMensagemErro(String erro);
}
