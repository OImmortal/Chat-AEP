package org.example;

import org.example.Controller.ProdutoController;
import org.example.View.ProdutoView;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        ProdutoView view = new ProdutoView();
        ProdutoController controller = new ProdutoController(view);

        controller.criarProduto();
        controller.selectAll();
        controller.deleteProduto();
        controller.selectAll();
    }
}