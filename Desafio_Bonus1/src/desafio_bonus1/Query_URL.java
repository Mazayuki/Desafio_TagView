/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package desafio_bonus1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 *
 * @author Lucas
 */
public class Query_URL {

    public String execQuery(URL url) throws IOException {
        URLConnection conexao = url.openConnection();
        BufferedReader leitor = new BufferedReader(
                new InputStreamReader(
                        conexao.getInputStream()));

        String resposta;
        String JSON_DATA = "";
        while ((resposta = leitor.readLine()) != null) {
            JSON_DATA += resposta;
        }
        leitor.close();

        return JSON_DATA;
    }
}
