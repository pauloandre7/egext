/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pauloandre7.egext.repositories.text;

import com.pauloandre7.egext.models.EventoRecord;
import com.pauloandre7.egext.models.InscritoRecord;
import com.pauloandre7.egext.models.StatusEvento;
import com.pauloandre7.egext.models.VoluntarioRecord;
import com.pauloandre7.egext.repositories.IEventoRepository;
import com.pauloandre7.egext.repositories.IInscritoRepository;
import com.pauloandre7.egext.repositories.IVoluntarioRepository;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author paulo
 */
public class EventoRepositoryText implements IEventoRepository{

    public final String EVENTOS_FILENAME = "eventos_dados.txt";
    
    public String eventosPath;
    public File eventos_arq;
    
    private final IInscritoRepository inscritosRepo;
    private final IVoluntarioRepository voluntariosRepo;
    
    List<EventoRecord> eventos;
    
    public EventoRepositoryText(String caminho, IInscritoRepository inscritosRepo, 
            IVoluntarioRepository voluntariosRepo){
        this.eventos = new ArrayList<>();
        
        this.inscritosRepo = inscritosRepo;
        this.voluntariosRepo = voluntariosRepo;
        
        if(caminho.isBlank()){
            this.eventosPath = "C:/users/paulo/Desktop/egext/";
        } else {
            this.eventosPath = caminho;
        }
        
        try {
            carregarDoArquivo();
        } catch (IOException ex) {
            System.err.println("Erro ao ler o arquivo: " + ex.getMessage());
        }
    }
    
    private void carregarDoArquivo() throws IOException{
        this.eventos_arq = new File(this.eventosPath, this.EVENTOS_FILENAME);
        
        
        try (BufferedReader reader = new BufferedReader(new FileReader(eventos_arq))){ 
            String linha; 
        while ((linha = reader.readLine()) != null) { 
            if (!linha.isBlank()) { 
                String[] campos = linha.split(";"); 
                int idEv = Integer.parseInt(campos[0]); 
                String titulo = campos[1]; 
                String data = campos[2];
                String local = campos[3];
                
                int idResponsavel = Integer.parseInt(campos[4]);
                VoluntarioRecord responsavel = voluntariosRepo.findById(idResponsavel);
                
                
                String[] idsVoluntarios = campos[5].split(",");
                // percorre os Ids em idsVoluntarios e busca o voluntario de cada id
                List<VoluntarioRecord> voluntarios = new ArrayList<>();
                
                for(String idVol : idsVoluntarios){
                    if(!idVol.isBlank()){
                        voluntarios.add(voluntariosRepo.findById(Integer.parseInt(idVol)));
                    }
                }
                
                String[] idsInscritos = campos[6].split(",");
                List<InscritoRecord> inscritos = new ArrayList<>();
                
                for(String idInsc : idsInscritos){
                    if(!idInsc.isBlank()){
                        inscritos.add(inscritosRepo.findById(Integer.parseInt(idInsc)));
                    }
                }
                
                StatusEvento status = StatusEvento.valueOf(campos[7]);
                
                eventos.add(new EventoRecord(idEv, titulo, data, local, 
                        responsavel, voluntarios, inscritos, status)); 
            } 
        } 
        } catch (IOException e) { 
            System.err.println("Erro ao ler o arquivo: " + e.getMessage()); 
        }
        
    }
    
    private void salvarNoArquivo(){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(eventos_arq, false))){
            
            
            
            // Passa por cada voluntário da lista e concatena a string com o ';'
            for (EventoRecord evento : this.eventos){
                // TODO
                // Vou guardar sempre os Ids nos lugares que houver objetos.
                // Antes do Join principal com ';', fazer um join com ',' para 
                // as listas de voluntarios e inscritos
                
                String idsVoluntarios = evento.voluntariosInternos().stream()
                        .map(vol -> String.valueOf(vol.id()))
                        .collect(Collectors.joining(","));
                
                String idsInscritos = evento.inscritos().stream()
                        .map(insc -> String.valueOf(insc.id()))
                        .collect(Collectors.joining(","));
                
                String linha = String.join(";",
                        String.valueOf(evento.idEv()),
                        evento.titulo(),
                        evento.data(),
                        evento.local(),
                        String.valueOf(evento.responsavel().id()),
                        idsVoluntarios,
                        idsInscritos,
                        evento.statusEvento().toString()
                );
                
                // Adiciona a linha e pula para a próxima
                writer.append(linha);
                writer.newLine();
            }
            
        } catch(IOException e){
            System.err.println("Erro ao salvar no arquivo: " + e.getMessage());
        }
        
    }
    
    @Override
    public EventoRecord findById(int id) {
        return eventos.stream().filter( ev -> ev.idEv() == id).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Não foi possível achar nenhum inscrito com esse Id."));
    }

    @Override
    public EventoRecord findByTitulo(String nome) {
        return eventos.stream().filter( i -> i.titulo().equals(nome)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Não foi possível achar nenhum inscrito com esse Nome."));
    }

    @Override
    public List<EventoRecord> findAll() {
        return new ArrayList<>(eventos);
    }

    @Override
    public void create(EventoRecord evento) {
        int idAux = evento.idEv();

        // Pega o maior id da lista e adiciona +1 ou defini 0, se não houver ids
        int proximoId = eventos.stream()
                .mapToInt(EventoRecord::idEv)
                .max()
                .orElse(0) + 1;
        
        // Cria nova instancia, adiciona na lista e salva a nova lista
        EventoRecord eventoAux = new EventoRecord(proximoId, evento.titulo(), 
                evento.data(), evento.local(), evento.responsavel(), 
                evento.voluntariosInternos(), evento.inscritos(), evento.statusEvento());
        
        eventos.add(eventoAux);
        salvarNoArquivo();
    }

    @Override
    public void delete(int id) {
        if(eventos.removeIf(v -> v.idEv() == id)){
            salvarNoArquivo();
        }
    }

    @Override
    public void update(int id, EventoRecord newEvento) {
        for (int i = 0; i < eventos.size(); i++) { 
            if (eventos.get(i).idEv() == id) { 
                eventos.set(i, newEvento);
                salvarNoArquivo();
                return;
            }
        } 
    }
    
}
