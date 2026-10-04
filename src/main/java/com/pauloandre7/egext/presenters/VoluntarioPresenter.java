package com.pauloandre7.egext.presenters;

import com.pauloandre7.egext.models.VoluntarioRecord;
import com.pauloandre7.egext.repositories.IVoluntarioRepository;
import com.pauloandre7.egext.views.IVoluntariosView;
import java.util.List;

/**
 * Presenter para manipular a view e a model de Voluntários, utilizando interface
 * para uma comunicação de baixo acoplamento com a view.
 * 
 * @author paulo
 */
public class VoluntarioPresenter implements IVoluntarioPresenter{
    
    private final IVoluntariosView view;
    private final IVoluntarioRepository repository;
    
    public VoluntarioPresenter(IVoluntariosView voluntariosView,
            IVoluntarioRepository voluntariosRepo){
        
        this.view = voluntariosView;
        this.repository = voluntariosRepo;
    }

    private VoluntarioRecord coletarDadosDaView() throws Exception{
        
        String ra = view.getRaInput();
        String nome = view.getNomeInput();
        String email = view.getEmailInput();
        String celular = view.getCelularInput();
        
        boolean algumVazio = ra.isBlank() || nome.isBlank() || email.isBlank() || celular.isBlank();
        if( algumVazio){
            throw new Exception("Faltam atributos para montar o voluntario");
        }
        
        return new VoluntarioRecord(0, ra, nome, email, celular);
    }
    
    @Override
    public void registrarVoluntario() {
        
        try{
            VoluntarioRecord voluntario = coletarDadosDaView();
            
            repository.create(voluntario);
            view.listarVoluntarios(repository.findAll());
            view.exibirMensagem("Voluntário criado com sucesso");
        } catch( Exception e){
            view.exibirMensagemErro(e.getMessage());
            System.err.print(e.getMessage());
        }
                
    }

    @Override
    public void excluirVoluntario(int id) {
        try{
            
            repository.delete(id);
            view.listarVoluntarios(repository.findAll());
            view.exibirMensagem("Voluntário excluído com sucesso");
        } catch( Exception e){
            view.exibirMensagemErro(e.getMessage());
            System.err.print(e.getMessage());
        }
    }

    @Override
    public void modificarVoluntario(int id) {
        try{
            VoluntarioRecord voluntario = coletarDadosDaView();
            
            repository.update(voluntario.id(), voluntario);
            view.listarVoluntarios(repository.findAll());
            view.exibirMensagem("Voluntário atualizado com sucesso");
        } catch( Exception e){
            view.exibirMensagemErro(e.getMessage());
            System.err.print(e.getMessage());
        }
    }

    @Override
    public List<VoluntarioRecord> buscarVoluntarioPorNome(String nome) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<VoluntarioRecord> listarVoluntarios() {
        
        return repository.findAll();
    }
}
