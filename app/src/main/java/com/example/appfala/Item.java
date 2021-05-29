package com.example.appfala;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Item{

    private String imagemURL, nome;

    public Item(){ }

    public Item(String imagemURL, String nome) {
        this.imagemURL = imagemURL;
        this.nome = nome;
    }

    public String getImagem() {
        return imagemURL;
    }

    public void setImagem(String imagem) {
        this.imagemURL = imagem;
    }

    public String getNome() { return nome; }

    public void setNome(String nome) { this.nome = nome; }

}
