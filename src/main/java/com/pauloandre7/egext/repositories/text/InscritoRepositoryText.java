/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pauloandre7.egext.repositories.text;

import com.pauloandre7.egext.models.InscritoRecord;
import com.pauloandre7.egext.repositories.IInscritoRepository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author paulo
 */
public class InscritoRepositoryText implements IInscritoRepository{

    public final String INSCRITOS_FILENAME = "inscritos_dados.txt";
    
    public String inscritosPath;
    public File inscritos_arq;
    
    private List<InscritoRecord> inscritos;
    
    public InscritoRepositoryText(String caminho){
        this.inscritos = new ArrayList<>();
        
        if(caminho.isBlank()){
            this.inscritosPath = "C:/users/paulo/Desktop/egext-files/";
        } else {
            this.inscritosPath = caminho;
        }
        
        try {
            carregarDoArquivo();
        } catch (IOException ex) {
            System.err.println("Erro ao ler o arquivo: " + ex.getMessage());
        }
    }
    
    private void carregarDoArquivo() throws IOException{
        this.inscritos_arq = new File(this.inscritosPath, this.INSCRITOS_FILENAME);
        
        
        try (BufferedReader reader = new BufferedReader(new FileReader(inscritos_arq))){ 
            String linha; 
        while ((linha = reader.readLine()) != null) { 
            if (!linha.isBlank()) { 
                String[] campos = linha.split(";"); 
                int id = Integer.parseInt(campos[0]); 
                String nome = campos[1]; 
                String email = campos[2];
                String celular = campos[3];
                inscritos.add(new InscritoRecord(id, nome, email, celular)); 
            } 
        } 
        } catch (IOException e) { 
            System.err.println("Erro ao ler o arquivo: " + e.getMessage()); 
        }
        
    }
    
    private void salvarNoArquivo(){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(inscritos_arq, false))){
            
            // Passa por cada voluntário da lista e concatena a string com o ';'
            for (InscritoRecord inscrito : this.inscritos){
                String linha = String.join(";",
                        String.valueOf(inscrito.id()),
                        inscrito.nome(),
                        inscrito.email(),
                        inscrito.celular()
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
    public InscritoRecord findById(int id) {
        
        return inscritos.stream().filter( i -> i.id() == id).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Não foi possível achar nenhum inscrito com esse Id."));
    }

    @Override
    public InscritoRecord findByNome(String nome) {
        return inscritos.stream().filter( i -> i.nome().equals(nome)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Não foi possível achar nenhum inscrito com esse Nome."));
    }

    @Override
    public List<InscritoRecord> findAll() {
        
        return new ArrayList<>(inscritos);
    }

    @Override
    public void create(InscritoRecord voluntario) {
        int idAux = voluntario.id();

        // Pega o maior id da lista e adiciona +1 ou defini 0, se não houver ids
        int proximoId = inscritos.stream()
                .mapToInt(InscritoRecord::id)
                .max()
                .orElse(0) + 1;
        
        // Cria nova instancia, adiciona na lista e salva a nova lista
        InscritoRecord inscritoAux = new InscritoRecord(proximoId, voluntario.nome(), 
                voluntario.email(), voluntario.celular());
        
        inscritos.add(inscritoAux);
        salvarNoArquivo();  
    }

    @Override
    public void delete(int id) {
        if(inscritos.removeIf(v -> v.id() == id)){
            salvarNoArquivo();
        }
    }

    @Override
    public void update(int id, InscritoRecord newInscrito) {
        for (int i = 0; i < inscritos.size(); i++) { 
            if (inscritos.get(i).id() == id) { 
                inscritos.set(i, newInscrito);
                salvarNoArquivo();
                return;
            }
        } 
    }
    
}
