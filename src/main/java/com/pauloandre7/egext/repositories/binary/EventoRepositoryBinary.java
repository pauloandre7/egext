/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pauloandre7.egext.repositories.binary;

import com.pauloandre7.egext.models.EventoRecord;
import com.pauloandre7.egext.repositories.IEventoRepository;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 *
 * @author paulo
 */
public class EventoRepositoryBinary implements IEventoRepository{

    private final String EVENTOS_FILENAME = "eventos_dados.dat";
    private String eventosPath;
    
    private File eventosArq;
    private ObjectInputStream leitor;
    private ObjectOutputStream escritor;
    
    private List<EventoRecord> eventos;
    
    public EventoRepositoryBinary(String path){
        eventos = new ArrayList<>();
        if(path.isBlank()){
            this.eventosPath = "./";
        } else {
            this.eventosPath = path;
        }
        
        carregarArquivo();
    }
    
    private void carregarArquivo(){
        if(eventosArq == null || !eventosArq.exists()){
            this.eventosArq = new File(eventosPath, EVENTOS_FILENAME);
        }
        
        try{
            leitor = new ObjectInputStream(new FileInputStream(eventosArq));
            eventos = (ArrayList<EventoRecord>) leitor.readObject();
            leitor.close();
        }catch(IOException ioError){
            System.err.println("Erro de leitura: "+ ioError.getMessage());
        }catch(ClassNotFoundException classError){
            System.err.println("Erro - Classe não encontrada: "+ classError.getMessage());
        }
    }
    
    private void salvarNoArquivo(){
        
        try{
            escritor = new ObjectOutputStream(new FileOutputStream(eventosArq));
            escritor.writeObject(eventos);
            escritor.close();
        }catch(IOException err){
            System.err.println("Erro de gravação: "+ err.getMessage());
        }
    }
    
    @Override
    public EventoRecord findById(int id) {
        return eventos.stream().filter(ev -> ev.idEv() == id)
                .findFirst()
                .orElseThrow( () -> new NoSuchElementException(
                        String.format("Não há eventos com id %d.", id)
        ));
    }

    @Override
    public EventoRecord findByTitulo(String titulo) {
        return eventos.stream().filter(ev -> ev.titulo().equals(titulo))
                .findFirst()
                .orElseThrow( () -> new NoSuchElementException(
                        String.format("Não há eventos com o título %s.", titulo) 
                ));
    }

    @Override
    public List<EventoRecord> findAll() {
        return new ArrayList<>(eventos);
    }

    @Override
    public void create(EventoRecord evento) {
        int proximoId = eventos.stream().mapToInt(ev -> ev.idEv())
                .max()
                .orElse(0) 
                +1 ;
        
        EventoRecord eventoAux = new EventoRecord(proximoId, evento.titulo(), 
                evento.data(), evento.local(), evento.responsavel(), 
                evento.voluntariosInternos(), evento.inscritos(), evento.statusEvento());
        
        eventos.add(eventoAux);
        salvarNoArquivo();
    }

    @Override
    public void delete(int id) {
        for( int i =0; i < eventos.size(); i++){
            if(eventos.get(i).idEv() == id){
                eventos.remove(i);
            }
        }
    }

    @Override
    public void update(int id, EventoRecord newEvento) {
        for( int i =0; i < eventos.size(); i++){
            if(eventos.get(i).idEv() == id){
                eventos.set(i, newEvento);
            }
        }
    }
    
}
