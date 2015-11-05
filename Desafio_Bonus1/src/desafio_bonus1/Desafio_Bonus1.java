package desafio_Bonus1;

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
public class Desafio_Bonus1 {

    public static void main(String[] args) throws Exception {
        List filmesAtores = new ArrayList();
        Scanner entrada = new Scanner(System.in);
        int ID[] = new int[2];
        int contador = 0, tamanhoVet = 0;
        String filmes1Ator[] = new String[tamanhoVet];
        String filmes2Ator[] = new String[tamanhoVet];
        String nomeAtor[] = new String[2];

        System.out.println("digite a sua API KEY!");
        String API_KEY = entrada.nextLine();

        System.out.println("Digite o nome do primeiro ator: ");
        nomeAtor[0] = entrada.nextLine();

        System.out.println("Digite o nome do segundo ator: ");
        nomeAtor[1] = entrada.nextLine();

        //organiza as Strings de nomes
        nomeAtor[0] = nomeAtor[0].replaceAll("\\s+", " ");
        nomeAtor[0] = nomeAtor[0].replaceAll(" ", "-");
        nomeAtor[1] = nomeAtor[1].replaceAll("\\s+", " ");
        nomeAtor[1] = nomeAtor[1].replaceAll(" ", "-");

        try {
            while (contador != 2) {
                URL buscaId = new URL("https://api.themoviedb.org/3/search/person?api_key=" + API_KEY + "&query=" + nomeAtor[contador]);
                URLConnection conexao = buscaId.openConnection();
                BufferedReader leitor = new BufferedReader(
                        new InputStreamReader(
                                conexao.getInputStream()));

                String resposta = "";
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
                    ID[contador] = pegaID.getInt("id");
                    System.out.println("O ID do(a) ator(a) " + nomeAtor[contador] + " é: " + ID[contador]);
                }

                //Inicio da listagem de filmes
                URL buscaFilmes = new URL("https://api.themoviedb.org/3/person/" + ID[contador] + "/credits?api_key="+API_KEY);
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

                //inserir os filmes nas listas corretas
                if (contador == 0) {
                    for (int i = 0; i < cast.length(); i++) {
                        tamanhoVet++;
                    }
                    //aumenta o tamanho do vetor para o tamanho ideal
                    filmes1Ator = new String[tamanhoVet];

                    for (int i = 0; i < cast.length(); i++) {
                        final JSONObject filme = cast.getJSONObject(i);
                        filmes1Ator[i] = filme.getString("original_title");
                    }
                } else {
                    tamanhoVet = 0;
                    for (int i = 0; i < cast.length(); i++) {
                        tamanhoVet++;
                    }
                    //aumenta o tamanho do vetor para o tamanho ideal
                    filmes2Ator = new String[tamanhoVet];

                    for (int i = 0; i < cast.length(); i++) {
                        final JSONObject filme = cast.getJSONObject(i);
                        filmes2Ator[i] = filme.getString("original_title");
                    }
                }
                contador++;

            }
            for (int i = 0; i < filmes1Ator.length; i++) {
                for (int j = 0; j < filmes2Ator.length; j++) {
                    if (filmes1Ator[i].equals(filmes2Ator[j])) {
                        filmesAtores.add(filmes1Ator[i]);
                    }
                }
            }

            System.out.println("Lista completa dos filmes realizados:\n" + filmesAtores);

        } catch (java.io.IOException e) {
            System.out.println("Não há resultados para sua pesquisa!");
        }

    }
}
