package com.example.appfala;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SecondActivity extends AppCompatActivity {

    ImageView fotoPerfil;
    TextView nomeUsuario, email, id;
    Button signOut, novoItem;
    GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

            fotoPerfil = findViewById(R.id.iv_fotoPerfil);

            email = findViewById(R.id.tv_email);
            nomeUsuario = findViewById(R.id.tv_nomeUsuario);
            id = findViewById(R.id.tv_id);
            novoItem = findViewById(R.id.btn_novoItem);

            signOut = findViewById(R.id.btn_logout);
            signOut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    // ...
                    case R.id.btn_logout:
                        signOut();
                        break;
                    // ...
                }
            }
        });


        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);

        if (acct != null) {

            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            nomeUsuario.setText(personName);
            email.setText(personEmail);
            id.setText(personId);

            Glide.with(this).load(String.valueOf(personPhoto)).into(fotoPerfil);
        }

        novoItem.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                Intent intent = new Intent(SecondActivity.this, NovoItem.class);
                intent.putExtra("ID", acct.getId());

                startActivity(intent);
            }
        });
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(SecondActivity.this, "Deslogado com sucesso.", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
    }
}