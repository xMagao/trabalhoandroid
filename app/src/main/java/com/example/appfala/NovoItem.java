package com.example.appfala;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.load.model.Model;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class NovoItem extends AppCompatActivity {

    ImageView imageView;
    EditText editText;
    public Uri imageUri;
    private Button uploadBtn;
    private ProgressBar progressBar;

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("Users");
    private StorageReference reference = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_item);

        Item item = new Item();

        imageView = findViewById(R.id.iv_novoItem);
        uploadBtn = findViewById(R.id.btn_upload);
        progressBar = findViewById(R.id.progressBar);
        editText = findViewById(R.id.et_nomeItem);

        progressBar.setVisibility(View.INVISIBLE);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null){
                    uploadToFirebase(imageUri);
                }else{
                    Toast.makeText(NovoItem.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void choosePicture(){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 2);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);

        }
    }

    private void uploadToFirebase(Uri uri){

        Intent intent = getIntent();
        String id = intent.getStringExtra("ID");

        final StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));

        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Item item = new Item(uri.toString(), editText.getText().toString());
                        String modelId = root.push().getKey();

                        root.child(id).child(modelId).setValue(item);
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(NovoItem.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        imageView.setImageResource(R.drawable.add_image);
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(NovoItem.this, "Uploading Failed !!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri mUri){

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));

    }
}