package org.example.Controller;

import org.example.Model.Produto;
import org.example.View.ProdutoView;

import java.util.ArrayList;
import java.util.List;

public class ProdutoController {

    private ProdutoView produtoView;
    private List<Produto> produtoList;

    public ProdutoController(ProdutoView produtoView) {
        this.produtoView = produtoView;
        this.produtoList = new ArrayList<>();
    }

    public void criarProduto() {
        insertProduto(produtoView.getIdProduto(), produtoView.getNomeProduto(), produtoView.getPrecoProduto());
    }

    public void insertProduto(int idProduto, String nomeProduto, double precoProduto) {
        Produto produto = new Produto(idProduto, nomeProduto, precoProduto);
        produtoList.add(produto);
    }

    public Produto findProductById(int id) {
        for (Produto produto : produtoList) {
            if (produto.getId() == id) {
                return produto;
            }
        }

        return null;
    }

    public void deleteProduto() {
        int id = produtoView.getIdProduto();
        Produto produtoDeletar = findProductById(id);
        if(produtoDeletar != null) {
            produtoList.remove(produtoDeletar);
            produtoView.transmitirMensagem("Produto removido com sucesso!");
            return;
        }

        produtoView.transmitirMensagem("Produto inexistente");
    }

    public void updateProduto() {
        int id = produtoView.getIdProduto();
        Produto produtoAtual = findProductById(id);
        if (produtoAtual != null) {
            String novoNome = produtoView.getNomeProduto();
            double novoPreco = produtoView.getPrecoProduto();
            produtoAtual.setNome(novoNome);
            produtoAtual.setPreco(novoPreco);

            produtoView.transmitirMensagem("Produto atualizado com sucesso!");
        } else {
            produtoView.transmitirMensagem("Produto inexistente");
        }
    }

    public void selectAll() {
        produtoView.imprimirTodos(produtoList);
    }

}
