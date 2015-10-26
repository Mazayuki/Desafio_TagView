package desafio_Bonus2;

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
public class Desafio_Bonus2 {

    public static void main(String[] args) throws Exception {
        List filmes = new ArrayList();
        Scanner entrada = new Scanner(System.in);
        Scanner nome = new Scanner(System.in);
        boolean jaEntrou = false, contador = false;
        int ID = 0;
        int q = 0;
        String nomeAtor[] = null;
        System.out.println("Digite a quantidado de atores que deseja ver os filmes: ");
        int qtd = entrada.nextInt();
        nomeAtor = new String[qtd];
        while (q != qtd) {
            System.out.println("Digite o nome do " + q + " ator: ");
            nomeAtor[q] = nome.nextLine();
            nomeAtor[q] = nomeAtor[q].replaceAll("\\s+", " ");
            nomeAtor[q] = nomeAtor[q].replaceAll(" ", "-");
            q++;
        }
        q = 0;
        try {
            while (q != qtd) {
                URL buscaId = new URL("https://api.themoviedb.org/3/search/person?api_key=ae9920c6307a74abdd5b44b603426542&query=" + nomeAtor[q]);
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
                } else {
                    final JSONObject pegaID = results.getJSONObject(0);
                    ID = pegaID.getInt("id");
                    System.out.println("O ID do ator" + nomeAtor[q] + " é: " + ID);
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
                    if (jaEntrou == false) {
                        filmes.add(filme.getString("original_title"));
                    } else if (jaEntrou == true) {
                        for (int j = 0; j < filmes.size(); j++) {
                            if (filmes.get(j).equals(filme.getString("original_title"))) {
                                contador = true; //se em algum momento for igual, não insere
                            }
                        }
                    } else if (!contador) {
                        filmes.add(filme.getString("original_title"));
                    }
                }
                jaEntrou = true;
                q++;
            }

            System.out.println("Lista completa dos filmes realizados:\n" + filmes);

        } catch (java.io.IOException e) {
            System.out.println("Não há resultados para sua pesquisa!");
        }
    }
}
