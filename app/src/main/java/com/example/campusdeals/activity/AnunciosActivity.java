package com.example.campusdeals.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.campusdeals.R;
import com.example.campusdeals.helper.ConfiguracaoFirebase;
import com.google.firebase.auth.FirebaseAuth;

public class AnunciosActivity extends AppCompatActivity {
    private FirebaseAuth autenticacao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_anuncios);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        autenticacao = ConfiguracaoFirebase.getFirebaseAuth();
//        autenticacao.signOut();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (autenticacao.getCurrentUser() == null){
            menu.setGroupVisible(R.id.group_deslogado,true);
        }else{
            menu.setGroupVisible(R.id.group_logado,true);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.menu_cadastrar){
            startActivity( new Intent(getApplicationContext(), LoginActivity.class));
        } else if (item.getItemId() == R.id.menu_sair) {
            autenticacao.signOut();
            invalidateOptionsMenu();
            Toast.makeText(getApplicationContext(),
                    "Deslogado com sucesso!",
                    Toast.LENGTH_LONG).show();

        }else if (item.getItemId() ==R.id.menu_anuncios)
        {
            startActivity( new Intent(getApplicationContext(), MeusAnunciosActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}