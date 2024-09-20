package com.example.campusdeals.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.campusdeals.R;
import com.example.campusdeals.adapter.ViewPagerAdapter;
import com.example.campusdeals.model.Anuncio;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DetalhesProdutoActivity extends AppCompatActivity {

    private ViewPager2 carouselView;
    private TextView titulo;
    private TextView descricao;
    private TextView estado;
    private TextView preco;
    private Anuncio anuncioSelecionado;
    private Handler handler;
    private Runnable runnable;
    private int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_produto);

        getSupportActionBar().setTitle("Detalhes do produto");

        // Inicializa componentes da interface
        inicializarComponentes();

        // Recupera o anúncio selecionado
        anuncioSelecionado = (Anuncio) getIntent().getSerializableExtra("anuncioSelecionado");

        if (anuncioSelecionado != null) {
            titulo.setText(anuncioSelecionado.getTitulo());
            descricao.setText(anuncioSelecionado.getDescricao());
            estado.setText(anuncioSelecionado.getEstado());
            preco.setText(anuncioSelecionado.getValor());

            // Configura o ViewPager2 com as imagens do anúncio
            List<String> imageUrls = anuncioSelecionado.getFotos();
            ViewPagerAdapter adapter = new ViewPagerAdapter(imageUrls);
            carouselView.setAdapter(adapter);

            // Configura o carrossel para mudar automaticamente
            iniciarCarrosselAutomatico(imageUrls.size());
        }
    }

    private void inicializarComponentes() {
        carouselView = findViewById(R.id.carouselView);
        titulo = findViewById(R.id.textTituloDetalhe);
        descricao = findViewById(R.id.textDescricaoDetalhe);
        estado = findViewById(R.id.textEstadoDetalhe);
        preco = findViewById(R.id.textPrecoDetalhe);
    }

    public void visualizarTelefone(View view){
        Intent i = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", anuncioSelecionado.getTelefone(), null));
        startActivity(i);
    }

    private void iniciarCarrosselAutomatico(int numPages) {
        handler = new Handler(Looper.getMainLooper());

        runnable = new Runnable() {
            @Override
            public void run() {
                if (currentPage == numPages) {
                    currentPage = 0;
                }
                carouselView.setCurrentItem(currentPage++, true);
                handler.postDelayed(this, 3000); // Tempo de troca entre imagens (3000ms = 3s)
            }
        };

        handler.postDelayed(runnable, 3000); // Inicia após 3 segundos
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove os callbacks para evitar vazamento de memória
        handler.removeCallbacks(runnable);
    }
}
