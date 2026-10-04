package com.pauloandre7.egext.repositories;

import com.pauloandre7.egext.models.VoluntarioRecord;
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
public class VoluntarioRepositoryText implements IVoluntarioRepository{

    public final String VOLUNTARIOS_FILENAME = "voluntarios_dados.txt";
    
    public String voluntariosPath;
    public File voluntarios_arq;
    
    private List<VoluntarioRecord> voluntarios;
    
    public VoluntarioRepositoryText(String caminho){
        this.voluntarios = new ArrayList<>();
        
        if(caminho.isBlank()){
            this.voluntariosPath = "C:/users/paulo/Desktop/egext-files/";
        } else {
            this.voluntariosPath = caminho;
        }
        
        try {
            carregarDoArquivo();
        } catch (IOException ex) {
            System.err.println("Erro ao ler o arquivo: " + ex.getMessage());
        }
    }
    
    private void carregarDoArquivo() throws IOException{
        this.voluntarios_arq = new File(this.voluntariosPath, this.VOLUNTARIOS_FILENAME);
        
        
        try (BufferedReader reader = new BufferedReader(new FileReader(voluntarios_arq))){ 
            String linha; 
        while ((linha = reader.readLine()) != null) { 
            if (!linha.isBlank()) { 
                String[] campos = linha.split(";"); 
                int id = Integer.parseInt(campos[0]); 
                String nome = campos[1]; 
                String email = campos[2];
                String celular = campos[3];
                String ra = campos[4];
                voluntarios.add(new VoluntarioRecord(id, nome, email, celular, ra)); 
            } 
        } 
        } catch (IOException e) { 
            System.err.println("Erro ao ler o arquivo: " + e.getMessage()); 
        }
        
    }
    
    private void salvarNoArquivo(){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(voluntarios_arq, false))){
            
            // Passa por cada voluntário da lista e concatena a string com o ';'
            for (VoluntarioRecord volunt : this.voluntarios){
                String linha = String.join(";",
                        String.valueOf(volunt.id()),
                        volunt.nome(),
                        volunt.email(),
                        volunt.celular(),
                        volunt.ra()
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
    public VoluntarioRecord findById(int id) {
        
        // Aplica o método filter na lista e pega o primeiro que tiver o id igual.
        // Se não achar, lança uma exceção
        return voluntarios.stream().filter(v -> v.id() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Registro com ID " + id + " não encontrado."));
    }

    
    @Override
    public VoluntarioRecord findByNome(String nome) {
        return voluntarios.stream().filter(v -> v.nome().equals(nome))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Registro com nome " + nome + " não encontrado."));
    }

    @Override
    public List<VoluntarioRecord> findAll() {
        // retorna uma nova array list
        return new ArrayList<>(voluntarios);
    }

    @Override
    public void create(VoluntarioRecord voluntario) {
        int idAux = voluntario.id();

        // Pega o maior id da lista e adiciona +1 ou defini 0, se não houver ids
        int proximoId = voluntarios.stream()
                .mapToInt(VoluntarioRecord::id)
                .max()
                .orElse(0) + 1;
        
    // Cria nova instancia, adiciona na lista e salva a nova lista
        VoluntarioRecord voluntAux = new VoluntarioRecord(proximoId, voluntario.ra(), 
                voluntario.nome(), voluntario.email(), voluntario.celular());
        
        voluntarios.add(voluntAux);
        salvarNoArquivo();        
    }

    @Override
    public void delete(int id) {
        // O remove if usa um lambda, que é parecido com a arrow function do javascript.
        // Percorre a lista e remove aquele objeto 'v' que tiver a mesma id.
        if(voluntarios.removeIf(v -> v.id() == id)){
            salvarNoArquivo();
        }
    }

    @Override
    public void update(int id, VoluntarioRecord newVoluntario) {
        for (int i = 0; i < voluntarios.size(); i++) { 
            if (voluntarios.get(i).id() == id) { 
                voluntarios.set(i, newVoluntario);
                salvarNoArquivo();
                return;
            }
        } 
    }
    
}
