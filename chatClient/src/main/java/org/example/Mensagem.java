package org.example;
import java.io.Serializable;

public class Mensagem implements Serializable {
    private static final long serialVersionUID = 1L; // Versão de serialização

    private String mensagem;

    public Mensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
