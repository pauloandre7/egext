/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pauloandre7.egext.presenters;

import com.pauloandre7.egext.models.EventoRecord;
import com.pauloandre7.egext.models.InscritoRecord;
import com.pauloandre7.egext.models.StatusEvento;
import com.pauloandre7.egext.models.VoluntarioRecord;
import com.pauloandre7.egext.repositories.IEventoRepository;
import com.pauloandre7.egext.repositories.IInscritoRepository;
import com.pauloandre7.egext.repositories.IVoluntarioRepository;
import com.pauloandre7.egext.views.IEventosView;
import com.pauloandre7.egext.views.IInscritoView;
import com.pauloandre7.egext.views.IVoluntariosView;
import com.pauloandre7.egext.views.InscritosView;
import com.pauloandre7.egext.views.swingui.VoluntariosView;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 *
 * @author paulo
 */
public class EventoPresenter implements IEventoPresenter{
    
    private final IEventosView view;
    private final IEventoRepository eventRepo;
    private final IInscritoRepository inscRepo;
    private final IVoluntarioRepository volRepo;
    private final IVoluntarioPresenter volPresenter;
    private final IVoluntariosView volView;
    private final IInscritoPresenter insPresenter;
    private final IInscritoView insView;
    
    private List<String> inscritosNaoEncontrados;
    private List<String> voluntariosNaoEncontrados;
    
    public EventoPresenter(IEventosView view, IEventoRepository eventRepo,
            IInscritoRepository inscRepo, IVoluntarioRepository volRepo, 
            IVoluntarioPresenter volPresenter, IVoluntariosView volView, 
            IInscritoPresenter insPresenter, IInscritoView insView){
        this.view = view;
        this.eventRepo = eventRepo;
        this.inscRepo = inscRepo;
        this.volRepo = volRepo;
        this.volPresenter = volPresenter;
        this.volView = volView;
        this.insPresenter = insPresenter;
        this.insView = insView;
    }
    
    private EventoRecord coletarDadosDaView() throws Exception{
              
        int id = view.getIdSelecionado();
        String titulo = view.getTituloInput();
        String data = view.getDataInput();
        String local = view.getLocalInput();
        
        String statusBruto = view.getStatusInput();

        // Se a string vier com o pacote da classe (tiver pontos), corta e pega só o final
        if (statusBruto != null && statusBruto.contains(".")) {
            statusBruto = statusBruto.substring(statusBruto.lastIndexOf('.') + 1);
        }
        StatusEvento status = StatusEvento.valueOf(statusBruto);
        
        // pesquisa o Voluntario Responsável no repo
        String responsavelNome = view.getResponsavelInput();
        
        // Joga exception se der errado
        VoluntarioRecord responsavel = volRepo.findByNome(responsavelNome);
        
        // Pesquisa e monta o array de Voluntarios Internos
        String[] voluntariosNomes = view.getVoluntariosInput();
        List<VoluntarioRecord> voluntarios = new ArrayList<>();
        
        if(voluntariosNomes != null){
            voluntariosNaoEncontrados = new ArrayList<>();
            for(String nome : voluntariosNomes){
                if(!nome.trim().isBlank()){
                    try{
                        // Uso try catch para permitir que o fluxo continue caso haja um nome errado.
                        voluntarios.add(volRepo.findByNome(nome.trim()));
                    } catch(NoSuchElementException e){
                        voluntariosNaoEncontrados.add(nome.trim());
                    }
                    
                }
            }
        }
        
        
        // Pesquisa e monta o array de Inscritos
        String[] inscritosNomes = view.getInscritosInput();
        List<InscritoRecord> inscritos = new ArrayList<>();
        
        if(inscritosNomes != null){
            inscritosNaoEncontrados = new ArrayList<>();
            
            for(String nome : inscritosNomes){
                if(!nome.trim().isBlank()){
                    try{
                        inscritos.add(inscRepo.findByNome(nome.trim()));
                    }catch(NoSuchElementException e){
                        inscritosNaoEncontrados.add(nome.trim());
                    }
                }
            }
        }
        boolean algumVazio = titulo.isBlank() || data.isBlank() || local.isBlank();
        if( algumVazio || responsavel == null){
            throw new Exception("Faltam atributos para montar o inscrito");
        }
        
        return new EventoRecord(id, titulo, data, local, responsavel, 
                voluntarios, inscritos, status);
    }

    @Override
    public void criarEvento() {
        try{
            EventoRecord evento = coletarDadosDaView();
            
            eventRepo.create(evento);
            view.listarEventos(eventRepo.findAll());
            
            StringBuilder avisos = new StringBuilder(); 
            if (!voluntariosNaoEncontrados.isEmpty()) { 
                avisos.append("\\nVoluntários não encontrados: ")
                        .append(String.join(", ", voluntariosNaoEncontrados)); 
            } 
            if (!inscritosNaoEncontrados.isEmpty()) { 
                avisos.append("\\nInscritos não encontrados: ")
                        .append(String.join(", ", inscritosNaoEncontrados)); 
            }
            
            String msg = "Evento criado com sucesso";
            msg += avisos.toString();
            
            view.exibirMensagem(msg);
            
        } catch( Exception e){
            view.exibirMensagemErro(e.getMessage());
            System.err.print(e.getMessage());
        }
    }

    @Override
    public void excluirEvento(int id) {
        try{
            eventRepo.delete(id);
            view.listarEventos(eventRepo.findAll());
            
            view.exibirMensagem("Evento excluído com sucesso");
            
        } catch( Exception e){
            view.exibirMensagemErro(e.getMessage());
            System.err.print(e.getMessage());
        }
    }

    @Override
    public void modificarEvento(int id) {
        try{
            EventoRecord evento = coletarDadosDaView();
            
            if(evento.idEv() == -1){
                view.exibirMensagemErro("É preciso selecionar um inscrito na tabela!");
                System.err.print("É preciso selecionar um inscrito na tabela!");
                return;
            }
            
            eventRepo.update(evento.idEv(), evento);
            view.listarEventos(eventRepo.findAll());
            
            StringBuilder avisos = new StringBuilder(); 
            if (!voluntariosNaoEncontrados.isEmpty()) { 
                avisos.append("\\nVoluntários não encontrados: ")
                        .append(String.join(", ", voluntariosNaoEncontrados)); 
            } 
            if (!inscritosNaoEncontrados.isEmpty()) { 
                avisos.append("\\nInscritos não encontrados: ")
                        .append(String.join(", ", inscritosNaoEncontrados)); 
            }
            
            String msg = "Evento modificado com sucesso";
            msg += avisos.toString();
            view.exibirMensagem(msg);
            
        } catch( Exception e){
            view.exibirMensagemErro(e.getMessage());
            System.err.print(e.getMessage());
        }
    }

    @Override
    public List<EventoRecord> buscarTodosEventos() {
        return eventRepo.findAll();
    }
    
    public void abrirTelaVoluntarios() { 
        volView.setPresenter(volPresenter);        
        volView.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
        volView.setVisible(true);
        volView.listarVoluntarios(volPresenter.listarVoluntarios());
    }
    
    public void abrirTelaInscritos() { 
        insView.setPresenter(insPresenter); 
        insView.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE); 
        insView.setVisible(true); 

        insView.listarInscritos(insPresenter.listarInscritos()); }
}
