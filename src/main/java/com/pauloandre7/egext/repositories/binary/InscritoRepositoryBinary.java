/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pauloandre7.egext.repositories.binary;

import com.pauloandre7.egext.models.InscritoRecord;
import com.pauloandre7.egext.repositories.IInscritoRepository;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
public class InscritoRepositoryBinary implements IInscritoRepository{

    private final String INSCRITO_FILENAME = "inscritos_dados.dat";
    private String inscritosPath;
    
    private File inscritosArq;
    private ObjectInputStream leitor = null;
    private ObjectOutputStream escritor = null;
    
    private List<InscritoRecord> inscritos;    
    
    public InscritoRepositoryBinary(String path){
        this.inscritos = new ArrayList<>();
        
        if(path.isBlank()){
            this.inscritosPath = "./";
        } else {
            this.inscritosPath = path;
        }
        
        carregarArquivo();
    }
    
    private void carregarArquivo(){
        
        if(inscritosArq == null || !inscritosArq.exists()){
            inscritosArq = new File(inscritosPath, INSCRITO_FILENAME);
        }
        
        try{
            leitor = new ObjectInputStream(new FileInputStream(inscritosArq));
            inscritos = (ArrayList<InscritoRecord>) leitor.readObject();
            leitor.close();
        }catch(ClassNotFoundException classError){
            System.err.println("Error - Classe não encontrada: " + classError.getMessage());
        }catch(FileNotFoundException fileError){
            System.err.println("Erro - Arquivo não encontrado: "+ fileError.getMessage());
        }catch(IOException ioError){
            System.err.println("Erro de Gravação: "+ ioError.getMessage());
        }
    }
    
    private void salvarNoArquivo(){
        
        try{
            escritor = new ObjectOutputStream(new FileOutputStream(inscritosArq, false));
            escritor.writeObject(inscritos);
            escritor.close();
        }catch (IOException ioErr){
            System.err.println("Erro de Gravação: "+ ioErr.getMessage());
        }
    }
    
    @Override
    public InscritoRecord findById(int id) {
        return inscritos.stream().filter(ins -> ins.id() == id)
                .findFirst()
                .orElseThrow( () -> new NoSuchElementException(
                        String.format("Não foi encontrado nenhum registro com id %d.", id)
        ));
    }

    @Override
    public InscritoRecord findByNome(String nome) {
        return inscritos.stream().filter(ins -> ins.nome().contains(nome))
                .findFirst()
                .orElseThrow( () -> new NoSuchElementException(
                String.format("Não foi encontrado nenhum registro com nome %s.", nome)
        ));
    }

    @Override
    public List<InscritoRecord> findAll() {
        return new ArrayList<>(inscritos);
    }

    @Override
    public void create(InscritoRecord inscrito) {
        
        int proximoId = inscritos.stream()
                .mapToInt(InscritoRecord::id)
                .max()
                .orElse(0) + 1;
        
        InscritoRecord inscritoAux = new InscritoRecord(proximoId, inscrito.nome(), 
                inscrito.email(), inscrito.celular());
        
        inscritos.add(inscritoAux);
        salvarNoArquivo();  
    }

    @Override
    public void delete(int id) {
        
        // Preferi usar o for para poder parar a busca quando encontrar o primeiro
        for(int i = 0; i < inscritos.size(); i++){
            if(inscritos.get(i).id() == id){
                inscritos.remove(i);
                salvarNoArquivo();
                return;
            }
        }
    }

    @Override
    public void update(int id, InscritoRecord newVoluntario) {
        for(int i = 0; i < inscritos.size(); i++){
            if(inscritos.get(i).id() == id){
                inscritos.set(i, newVoluntario);
                salvarNoArquivo();
                return;
            }
        }
    }
    
}
