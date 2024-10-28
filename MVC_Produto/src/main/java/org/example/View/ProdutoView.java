package org.example.View;

import org.example.Model.Produto;

import java.util.List;
import java.util.Scanner;

public class ProdutoView {

    Scanner scanner = new Scanner(System.in);

    // Coleta dos dados

    public int getIdProduto() {
        System.out.println("Digite o código do produto: ");
        return scanner.nextInt();
    }

    public String getNomeProduto() {
        System.out.println("Digite o nome do produto: ");
        return scanner.next();
    }

    public double getPrecoProduto() {
        System.out.println("Informe o preço do produto: ");
        return scanner.nextDouble();
    }

    public void transmitirMensagem(String msg) {
        System.out.println(msg);
    }

    public void imprimirTodos(List<Produto> produtos) {
        for (Produto produto : produtos) {
            System.out.println(produto.toString());
        }
    }
}
