package com.example.g2int101experience;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.g2int101experience.databinding.ActivityLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
public class Login extends AppCompatActivity {

     ActivityLoginBinding binding;
     FirebaseAuth mAuth;
     FirebaseDatabase database;
     GoogleSignInOptions gso;
     GoogleSignInClient gsc;
     int RC_SIGN_IN = 20;
     // ./gradlew signingReport usa esto en la terminal para encontra el SHA1 es algo necesario hay que implementarlo en la consola de firbase


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            startActivity(new Intent(getApplicationContext(), Home.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail().build();

        gsc = GoogleSignIn.getClient(this, gso);

        View view = binding.getRoot();
        setContentView(view);
        
        binding.btnGoogle.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 signIn();
             }
        });

        binding.tvLinkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Registro.class));
            }
        });

        binding.editTextText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus) {
                    if (binding.editTextText.getText().toString().isEmpty()) {
                        binding.tvMensajeErrorCorreo.setText(R.string.el_correo_electr_nico_no_puede_estar_vacio);
                        binding.tvMensajeErrorCorreo.setVisibility(View.VISIBLE);
                    }
                } else {
                    binding.editTextText.setText("");
                    binding.tvMensajeErrorCorreo.setVisibility(View.GONE);
                }

            }
        });

        binding.editTextTextPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus) {

                    if (binding.editTextTextPassword.getText().toString().isEmpty()) {
                        binding.tvMensajeErrorPassword.setText(R.string.la_contrase_a_no_puede_estar_vacia);
                        binding.tvMensajeErrorPassword.setVisibility(View.VISIBLE);
                    }

                } else {
                    binding.editTextTextPassword.setText("");
                    binding.tvMensajeErrorPassword.setVisibility(View.GONE);
                }

            }

        });

        // TODO Implementar botton recordarme.

        binding.btnInicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email, pasword;
                email = binding.editTextText.getText().toString().trim();
                pasword = binding.editTextTextPassword.getText().toString().trim();

                // TODO Poner una progressbar

                if (email.isEmpty()) {
                    binding.tvMensajeErrorCorreo.setText(R.string.el_correo_electr_nico_no_puede_estar_vacio);
                    binding.tvMensajeErrorCorreo.setVisibility(View.VISIBLE);
                    return;
                }

                if (pasword.isEmpty()) {
                    binding.tvMensajeErrorPassword.setText(R.string.la_contrase_a_no_puede_estar_vacia);
                    binding.tvMensajeErrorPassword.setVisibility(View.VISIBLE);
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, pasword)
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    binding.tvMensajeErrorCorreo.setVisibility(View.GONE);
                                    binding.tvMensajeErrorPassword.setVisibility(View.GONE);
                                    binding.editTextText.setText("");
                                    binding.editTextTextPassword.setText("");
                                    startActivity(new Intent(getApplicationContext(), Home.class));


                                } else {
                                    binding.tvMensajeErrorCorreo.setText(R.string.correo_no_encontrado_revise_los_datos);
                                    binding.tvMensajeErrorPassword.setText(R.string.contrsse_a_no_encontrada_revise_los_datos);
                                    binding.tvMensajeErrorCorreo.setVisibility(View.VISIBLE);
                                    binding.tvMensajeErrorPassword.setVisibility(View.VISIBLE);
                                }
                            }
                        });

            }
        });

    }

    private void signIn() {

        Intent singInIntent = gsc.getSignInIntent();
        startActivityForResult(singInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuth(account.getIdToken());
            } catch (Exception e) {
                Toast.makeText(this, "Hubo un problema al iniciar sesión", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void firebaseAuth(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            binding.editTextTextPassword.setText("");
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("email", user.getUid());
                            map.put("name", user.getDisplayName());
                            map.put("image", user.getPhotoUrl().toString());
                            database.getReference().child("Users").child(user.getUid()).setValue(map);
                            startActivity(new Intent(getApplicationContext(), Home.class));
                        } else {
                            Toast.makeText(Login.this, "Hubo un problema al iniciar sesión", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }


}