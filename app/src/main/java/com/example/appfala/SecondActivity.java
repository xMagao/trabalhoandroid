package com.example.appfala;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {

    ImageView fotoPerfil;
    TextView nomeUsuario, email, id;
    Button signOut, novoItem;
    GoogleSignInClient mGoogleSignInClient;
    public RecyclerView recyclerView;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

            recyclerView = findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);

            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
            recyclerView.setLayoutManager(gridLayoutManager);

            fotoPerfil = findViewById(R.id.iv_fotoPerfil);

            //email = findViewById(R.id.tv_email);
            nomeUsuario = findViewById(R.id.tv_nomeUsuario);
            //id = findViewById(R.id.tv_id);

            novoItem = findViewById(R.id.btn_novoItem);

            //signOut = findViewById(R.id.btn_logout);

        /*signOut.setOnClickListener(new View.OnClickListener() {

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
        });*/


        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users").child(acct.getId());

        if (acct != null) {

            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            nomeUsuario.setText(personName);
            Picasso.get().load(acct.getPhotoUrl()).placeholder(R.mipmap.ic_launcher).into(fotoPerfil);

            //email.setText(personEmail);
            //id.setText(personId);

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

    @Override
    protected void onStart() {

        super.onStart();
        FirebaseRecyclerOptions<Item> options =
                new FirebaseRecyclerOptions.Builder<Item>()
                .setQuery(databaseReference, Item.class)
                .build();

        FirebaseRecyclerAdapter<Item, ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Item, ViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Item model) {
                        holder.setdetails(getApplication(), model.getNome(), model.getImagem());
                    }

                    @NonNull
                    @Override
                    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.visualizar_imagem, parent, false);

                        return new ViewHolder(view);
                    }
                };

            firebaseRecyclerAdapter.startListening();
            recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(SecondActivity.this, "Deslogado com sucesso.", Toast.LENGTH_LONG).show();
                        finish();
                    }});
    }
}