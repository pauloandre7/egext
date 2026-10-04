/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pauloandre7.egext.presenters;

import com.pauloandre7.egext.models.InscritoRecord;
import com.pauloandre7.egext.models.VoluntarioRecord;
import com.pauloandre7.egext.repositories.IInscritoRepository;
import com.pauloandre7.egext.views.IInscritoView;
import java.util.List;

/**
 *
 * @author paulo
 */
public class InscritoPresenter implements IInscritoPresenter{

    private final IInscritoView view;
    private final IInscritoRepository repository;
    
    public InscritoPresenter(IInscritoView inscritoView,
            IInscritoRepository inscritoRepo){
        
        this.view = inscritoView;
        this.repository = inscritoRepo;
    }

    private InscritoRecord coletarDadosDaView() throws Exception{
        InscritoRecord inscrito;
        
        String nome = view.getNomeInput();
        String email = view.getEmailInput();
        String celular = view.getCelularInput();
        
        boolean algumVazio = nome.isBlank() || email.isBlank() || celular.isBlank();
        if( algumVazio){
            throw new Exception("Faltam atributos para montar o inscrito");
        }
        
        return new InscritoRecord(0, nome, email, celular);
    }
    
    @Override
    public void registrarInscrito() {
        try{
            InscritoRecord inscrito = coletarDadosDaView();
            
            repository.create(inscrito);
            view.listarInscritos(repository.findAll());
            view.exibirMensagem("Voluntário criado com sucesso");
        } catch( Exception e){
            view.exibirMensagemErro(e.getMessage());
            System.err.print(e.getMessage());
        }
    }

    @Override
    public void excluirInscrito(int id) {
        try{
            repository.delete(id);
            view.listarInscritos(repository.findAll());
            view.exibirMensagem("Voluntário excluído com sucesso");
        } catch( Exception e){
            view.exibirMensagemErro(e.getMessage());
            System.err.print(e.getMessage());
        }
    }

    @Override
    public void modificarInscrito(int id) {
        try{
            InscritoRecord inscrito = coletarDadosDaView();
            
            repository.create(inscrito);
            view.listarInscritos(repository.findAll());
            view.exibirMensagem("Voluntário criado com sucesso");
        } catch( Exception e){
            view.exibirMensagemErro(e.getMessage());
            System.err.print(e.getMessage());
        }
    }

    @Override
    public List<InscritoRecord> listarInscritos() {
        
        return repository.findAll();
    }
    
}
