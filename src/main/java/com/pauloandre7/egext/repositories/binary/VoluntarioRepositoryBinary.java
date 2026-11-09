/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pauloandre7.egext.repositories.binary;

import com.pauloandre7.egext.models.VoluntarioRecord;
import com.pauloandre7.egext.repositories.IVoluntarioRepository;
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
 * @author pauloandre7
 */
public class VoluntarioRepositoryBinary implements IVoluntarioRepository{

    private final String VOLUNTARIOS_FILENAME = "voluntarios_dados.dat";
    private String voluntariosPath;
    
    private File voluntariosArq;
    private ObjectInputStream leitor = null;
    private ObjectOutputStream escritor = null;
    
    private List<VoluntarioRecord> voluntarios;
    
    public VoluntarioRepositoryBinary(String path){
        this.voluntarios = new ArrayList<>();
        
        if(path.isBlank()){
            this.voluntariosPath = "./";
        } else {
            this.voluntariosPath = path;
        }
        
        carregarArquivo();
        
    }
    
    
    
    private void carregarArquivo(){
        
        if(voluntariosArq == null || !voluntariosArq.exists()){
            voluntariosArq = new File(voluntariosPath, VOLUNTARIOS_FILENAME);
        }
        
        try{ 
            leitor = new ObjectInputStream(new FileInputStream(voluntariosArq));
            voluntarios = (ArrayList<VoluntarioRecord>) leitor.readObject();
            leitor.close();
        } catch(ClassNotFoundException classErro){
            System.err.println("ERRO - Classe não encontrada: "+ classErro.getMessage());
        } catch(IOException ioErro){
            System.err.println("ERRO - Não foi possível ler o arquivo: "+ ioErro);
        }
    };
    
    private void salvarNoArquivo(){
        try{
            // false no param append para manter apenas uma lista sempre
            escritor = new ObjectOutputStream( new FileOutputStream(voluntariosArq, false));
            escritor.writeObject(voluntarios);
            escritor.close();
        } catch(FileNotFoundException fileError){
            System.err.println(String.format("Erro ao criar arquivo: %s", fileError.getMessage()));
        } catch(IOException ioError){
            System.err.println(String.format("Erro ao escrever no arquivo: %s", ioError.getMessage()));
        }
    };
    
    @Override
    public VoluntarioRecord findById(int id) {
        
        /* Me inspirei no javascript para fazer o retorno abaixo. 
        / Abrir uma stream() na list permite percorrer com o método de filtragem, que
        / utiliza uma função lambda (semelhante à arrowfunction do JS) para determinar
        / a lógica de comparação. Nesse caso utiliza "vol" como o objeto atual do ciclo
        / e pega o seu id para comparar com o id do param. */ 
        return voluntarios.stream().filter(vol -> vol.id() == id)
                .findFirst()
                .orElseThrow( () -> new NoSuchElementException(
                        String.format("Não foi encontrado nenhum registro com id %d", id))
                );
    }

    @Override
    public VoluntarioRecord findByNome(String nome) {
        return voluntarios.stream().filter(vol -> vol.nome().equalsIgnoreCase(nome))
                .findFirst()
                .orElseThrow( () -> new NoSuchElementException(
                        String.format("Não foi encontrado nenhum registro com o nome '%s'", nome)
                ));
    }

    @Override
    public List<VoluntarioRecord> findAll() {
        // Ao invés de passar a instância da lista do repository, envia uma cópia;
        return new ArrayList<>(voluntarios);
    }

    @Override
    public void create(VoluntarioRecord voluntario) {
        
        /* Abre uma stream pra percorrer e pegar o maior id da lista. Mapear pra Int
        /  permite usar o método max(), que retorna o maior valor. caso esteja vazia,
        /  entra em orElse, que retorna 0. No fim de qualquer um dos dois caminhos, 
        /  soma +1 e atribui à variável;
        */
        int proximoId = voluntarios.stream()
                .mapToInt(VoluntarioRecord::id)
                .max()
                .orElse(0) + 1;
        
        VoluntarioRecord voluntarioAux = new VoluntarioRecord(proximoId, 
                voluntario.ra(), voluntario.nome(), voluntario.email(), 
                voluntario.celular()
        );
        
        voluntarios.add(voluntarioAux);
        
        salvarNoArquivo();
    }

    @Override
    public void delete(int id) {
        
        /* removeIf é utiliza lambda pra excluir um objeto da lista. Percorre a 
        /  lista e remove os elementos que satisfazerem o lambda. 
        /  Resolvi deixar essa técnica aqui apenas para questão de aprendizado, pois
        /  imagino que essa lista não tende a crescer muito. Porém, pode ser que 
        /  eu remova em futuras refatorações. Para listas grandes, o for tende a ser melhor!
        */
        if(voluntarios.removeIf(v -> v.id() == id)){
            salvarNoArquivo();
        }
    }

    @Override
    public void update(int id, VoluntarioRecord newVoluntario) {
        
        // optei pelo for normal para poder parar assim que encontrar o id correto.
        for(int i = 0; i < voluntarios.size(); i++){
            if(voluntarios.get(i).id() == id){
                voluntarios.set(i, newVoluntario);
                salvarNoArquivo();
                return;
            }
        }
    }
    
}
