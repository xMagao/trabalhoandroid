package com.example.appfala;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Item{

    private String imagemURL;

    public Item(){ }

    public Item(String imagemURL) {
        this.imagemURL = imagemURL;
    }

    public String getImagem() {
        return imagemURL;
    }

    public void setImagem(String imagem) {
        this.imagemURL = imagem;
    }

    public void salvar(String id, String nomeItem){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("users").child(id).child(nomeItem).setValue(this);
    }
}
