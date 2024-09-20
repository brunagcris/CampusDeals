package com.example.campusdeals.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusdeals.R;
import com.example.campusdeals.adapter.AdapterAnuncios;
import com.example.campusdeals.helper.ConfiguracaoFirebase;
import com.example.campusdeals.model.Anuncio;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AnunciosActivity extends AppCompatActivity {
    private FirebaseAuth autenticacao;
    private RecyclerView recyclerAnunciosPublicos;
    private Button buttonRegiao, buttonCategoria;
    private AdapterAnuncios adapterAnuncios;
    private List<Anuncio> listaAnuncios = new ArrayList<>();
    private DatabaseReference anunciosPublicosRef;
    private AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_anuncios);

        inicializarComponentes();

        autenticacao = ConfiguracaoFirebase.getFirebaseAuth();
        anunciosPublicosRef = ConfiguracaoFirebase.getFirebase()
                .child("anuncios");

        //config recyclerView
        recyclerAnunciosPublicos.setLayoutManager(new LinearLayoutManager(this));
        recyclerAnunciosPublicos.setHasFixedSize(true);
        adapterAnuncios = new AdapterAnuncios(listaAnuncios, this);

        recyclerAnunciosPublicos.setAdapter(adapterAnuncios);

        recuperarAnunciosPublicos();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // SE DER ERRO SOBE ISSO AQUI

//        autenticacao.signOut();
    }

    public void recuperarAnunciosPublicos(){

        // Criar um ProgressBar
        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setIndeterminate(true); // Estilo de barra indeterminada

        // Configurar o AlertDialog
        androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Carregando Anúncios")
                .setView(progressBar) // Adicionar o ProgressBar no diálogo
                .setCancelable(false) // Não permite cancelar
                .create();

        dialog.show(); // Exibe o diálogo

        listaAnuncios.clear();
        anunciosPublicosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot estados: dataSnapshot.getChildren()){
                    for (DataSnapshot categorias: estados.getChildren()){
                        for (DataSnapshot anuncios: categorias.getChildren()){

                            Anuncio anuncio = anuncios.getValue(Anuncio.class);
                            listaAnuncios.add(anuncio);

                        }
                    }
                }
                Collections.reverse(listaAnuncios);
                adapterAnuncios.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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

    public void inicializarComponentes(){

        recyclerAnunciosPublicos = findViewById(R.id.recyclerAnunciosPublicos);

    }

}