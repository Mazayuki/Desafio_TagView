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
        int valorVet = 0, numeroAtor = 1;

        System.out.println("digite a sua API KEY!");
        String API_KEY = entrada.nextLine();

        String nomeAtor[] = null;
        System.out.println("Digite a quantidado de atores que deseja ver os filmes: ");
        
        try {
            int qtd = entrada.nextInt();
            nomeAtor = new String[qtd];
            while (valorVet != qtd) {
                System.out.println("Digite o nome do " + numeroAtor + " ator: ");
                nomeAtor[valorVet] = nome.nextLine();
                nomeAtor[valorVet] = nomeAtor[valorVet].replaceAll("\\s+", " ");
                nomeAtor[valorVet] = nomeAtor[valorVet].replaceAll(" ", "-");
                valorVet++;
                numeroAtor++;
            }
            valorVet = 0; //o contador do vetor é zarado para que
                          //a query leia todos nomes

            while (valorVet != qtd) {
                URL buscaId = new URL("https://api.themoviedb.org/3/search/person?api_key="+API_KEY+"&query=" + nomeAtor[valorVet]);
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
                    System.out.println("O ID do ator " + nomeAtor[valorVet] + " é: " + ID);
                }

                //Inicio da listagem de filmes
                URL buscaFilmes = new URL("https://api.themoviedb.org/3/person/" + ID + "/credits?api_key="+API_KEY);
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
                                break;
                            }
                        }
                    }

                    if (!contador && jaEntrou) {
                        filmes.add(filme.getString("original_title"));
                    }
                    jaEntrou = true;
                    contador = false;
                }
                valorVet++;
            }

            System.out.println("Lista completa dos filmes realizados:\n" + filmes);

        } catch (java.io.IOException e) {
            System.out.println("Não há resultados para sua pesquisa!");
        } catch (java.util.InputMismatchException e) {
            System.out.println("Insira um número válido!");
        }
    }
}
