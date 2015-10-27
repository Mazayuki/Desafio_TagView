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

        System.out.println("Digite o nome do ator: ");
        String nomeAtor = entrada.nextLine();
        nomeAtor = nomeAtor.replaceAll("\\s+", " ");
        nomeAtor = nomeAtor.replaceAll(" ", "-");

        try {
            URL buscaId = new URL("https://api.themoviedb.org/3/search/person?api_key=ae9920c6307a74abdd5b44b603426542&query=" + nomeAtor);
            URLConnection conexao = buscaId.openConnection();
            BufferedReader leitor = new BufferedReader(
                    new InputStreamReader(
                            conexao.getInputStream()));

            String resposta;
            String JSON_DATA = "";
            while ((resposta = leitor.readLine()) != null) {
                JSON_DATA += resposta;
            }
            leitor.close();

            JSONObject obj = new JSONObject(JSON_DATA);
            final JSONArray results = obj.getJSONArray("results");
            if (obj.getInt("total_results") == 0) {
                System.out.println("Não há resultados para sua pesquisa!");
                System.exit(0);
            } else {
                final JSONObject pegaID = results.getJSONObject(0);
                ID = pegaID.getInt("id");
                System.out.println("O ID desse ator é: " + ID);
            }

            //Inicio da listagem de filmes
            URL buscaFilmes = new URL("https://api.themoviedb.org/3/person/" + ID + "/credits?api_key=ae9920c6307a74abdd5b44b603426542");
            conexao = buscaFilmes.openConnection();
            leitor = new BufferedReader(
                    new InputStreamReader(
                            conexao.getInputStream()));

            JSON_DATA = "";
            while ((resposta = leitor.readLine()) != null) {
                JSON_DATA += resposta;
            }

            leitor.close();
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
