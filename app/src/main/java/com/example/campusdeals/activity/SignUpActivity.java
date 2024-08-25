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
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private TextView txtSignIn;
    private EditText emailField, passwordField;
    private Button signUpbtn;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.singUp), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initWidgets();
        auth = ConfiguracaoFirebase.getFirebaseAuth();

        signUpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();

                if (!email.isEmpty() ){
                    if(!password.isEmpty()){
                        auth.createUserWithEmailAndPassword(
                                email,password
                        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(SignUpActivity.this,
                                            "Cadastro realizado com sucesso!",
                                            Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                                    startActivity(intent);
                                }else {
                                    String exeption = "";

                                    try {
                                        throw Objects.requireNonNull(task.getException());
                                    }catch (FirebaseAuthWeakPasswordException e){
                                        exeption = "Use uma senha mais forte!";
                                    }catch (FirebaseAuthInvalidCredentialsException e){
                                        exeption = "Por favor, digite um e-mail válido";
                                    }catch (FirebaseAuthUserCollisionException e){
                                        exeption = "Esta conta já foi cadastrada";
                                    }catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }

                                    Toast.makeText(SignUpActivity.this,
                                            exeption,
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else {
                        Toast.makeText(SignUpActivity.this,
                                "Preencha a senha!",
                                Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(SignUpActivity.this,
                            "Preencha o E-mail!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                startActivity(intent);


            }
        });
    }

    private void initWidgets(){
        txtSignIn=findViewById(R.id.signInTxt);
        emailField=findViewById(R.id.signUpTextEmailAddress);
        passwordField=findViewById(R.id.signUpTextPassword);
        signUpbtn=findViewById(R.id.signUpbtn);
    }
}
