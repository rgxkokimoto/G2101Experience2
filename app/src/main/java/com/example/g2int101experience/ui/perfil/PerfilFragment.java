package com.example.g2int101experience.ui.perfil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.g2int101experience.databinding.FragmentPerfilBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PerfilFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private FragmentPerfilBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inicializamos el binding
        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Inicialización de Firebase
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        // Obtener los datos del usuario logueado
        String userId = mAuth.getCurrentUser().getUid();
        DatabaseReference userRef = database.getReference("Users").child(userId);

        // Cargar los datos del usuario (nombre, foto de perfil)
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String userName = dataSnapshot.child("name").getValue(String.class);
                    String userImageUrl = dataSnapshot.child("image").getValue(String.class);

                    // Actualizar el nombre y la imagen
                    binding.tvNombreUsuario.setText(userName);

                    // Usamos Glide para cargar la imagen de perfil
                    Glide.with(getContext())
                            .load(userImageUrl)
                            .into(binding.ivPerfil);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error al cargar los datos", Toast.LENGTH_SHORT).show();
            }
        });

        // Acción del botón de cerrar sesión
        binding.btnCerrarSesion.setOnClickListener(v -> {
            mAuth.signOut();
            // Redirige a la pantalla de login
            getActivity().finish();
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Limpiar el binding para evitar fugas de memoria
        binding = null;
    }
}
