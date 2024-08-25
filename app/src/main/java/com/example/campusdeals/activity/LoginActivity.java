package com.example.campusdeals.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.campusdeals.R;
import com.example.campusdeals.helper.ConfiguracaoFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private Button loginBtn;
    private TextView txt_sign_up;
    private EditText emailField, passwordField;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initWidgets();
        auth = ConfiguracaoFirebase.getFirebaseAuth();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();

                if (!email.isEmpty()){
                    if (!password.isEmpty()){
                        auth.signInWithEmailAndPassword(
                                email,password
                        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(LoginActivity.this,
                                            "Logado com sucesso",
                                            Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(LoginActivity.this, AnunciosActivity.class);
                                    startActivity(intent);
                                }else{

                                    String exception = "";

                                    try {
                                        throw Objects.requireNonNull(task.getException());
                                    } catch (FirebaseAuthInvalidCredentialsException e) {
                                        exception = "E-mail ou senha incorretos!";
                                    }catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }

                                    Toast.makeText(LoginActivity.this,
                                             exception,
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{
                        Toast.makeText(LoginActivity.this,
                                "Preencha a senha!",
                                Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(LoginActivity.this,
                            "Preencha o E-mail!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        txt_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initWidgets(){
        loginBtn=findViewById(R.id.login_btn);
        txt_sign_up=findViewById(R.id.sign_up_txt);
        emailField=findViewById(R.id.loginEmailAddress);
        passwordField=findViewById(R.id.loginPassword);
    }
}

