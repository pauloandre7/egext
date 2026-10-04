/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.pauloandre7.egext.presenters;

import com.pauloandre7.egext.models.VoluntarioRecord;
import java.util.List;

/**
 * Interface Presenter para definir o contrato entre a view e o presenter que
 * irá fornecer os dados para a exposição na tela.
 * 
 * Com a injeção de dependências, a view terá um baixo acoplamento com o Presenter
 * e ficará limitada a apenas os métodos que forem declarados aqui.
 * 
 * @author paulo
 */
public interface IVoluntarioPresenter {
   void registrarVoluntario();
   void excluirVoluntario(int id);
   void modificarVoluntario(int id);
   List<VoluntarioRecord> buscarVoluntarioPorNome(String nome);
   List<VoluntarioRecord> listarVoluntarios();
}
