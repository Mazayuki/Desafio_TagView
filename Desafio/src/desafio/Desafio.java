package desafio;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Lucas
 */
public class Desafio {

    public static void main(String[] args) throws Exception {
        List filmes = new ArrayList();
        Scanner entrada = new Scanner(System.in);
        int ID = 0;
        
        System.out.println("digite a sua API KEY!");
        String API_KEY = entrada.nextLine();
        
        System.out.println("Digite o nome do ator: ");
        String nomeAtor = entrada.nextLine();
        nomeAtor = nomeAtor.replaceAll("\\s+", " ");
        nomeAtor = nomeAtor.replaceAll(" ", "-");

        try {
            URL buscaId = new URL("https://api.themoviedb.org/3/search/person?api_key=" + API_KEY +"&query=" + nomeAtor);
            Query_URL url = new Query_URL();
            String JSON_DATA = url.execQuery(buscaId);

            JSONObject obj = new JSONObject(JSON_DATA);
            final JSONArray results = obj.getJSONArray("results");
            
            if (obj.getInt("total_results") == 0) {
                System.out.println("Não há resultados para sua pesquisa! (talvez a sua API esteja incorreta)");
                System.exit(0);
            } else {
                final JSONObject pegaID = results.getJSONObject(0);
                ID = pegaID.getInt("id");
                System.out.println("O ID desse ator é: " + ID);
            }

            //Inicio da listagem de filmes
            URL buscaFilmes = new URL("https://api.themoviedb.org/3/person/" + ID + "/credits?api_key=" + API_KEY);
            JSON_DATA = url.execQuery(buscaFilmes);
            
            obj = new JSONObject(JSON_DATA);
            final JSONArray cast = obj.getJSONArray("cast");
            
            for (int i = 0; i < cast.length(); i++) {
                final JSONObject filme = cast.getJSONObject(i);
                filmes.add(filme.getString("original_title"));
            }
            
            System.out.println("Lista completa dos filmes realizados:\n"+filmes);
            
        } catch (java.io.IOException e) {
            System.out.println("Não há resultados para sua pesquisa!");
        }
    }
}
