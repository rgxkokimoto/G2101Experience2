package com.example.g2int101experience.ui.perfil;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.g2int101experience.databinding.FragmentPerfilBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.UUID;

public class PerfilFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private FragmentPerfilBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inicializamos el binding
        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Inicializamos Firebase
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        // Cargar los datos del usuario
        loadUserData();

        // Configurar el ImageButton para seleccionar una nueva imagen
        binding.ivPerfil.setOnClickListener(v -> openImageChooser());

        // Acción del botón de cerrar sesión
        binding.btnCerrarSesion.setOnClickListener(v -> {
            mAuth.signOut();
            getActivity().finish();
        });

        // Acción del botón de guardar nombre de usuario
        binding.btnGuardarNombre.setOnClickListener(v -> saveUserName());

        return root;
    }

    // Método para guardar el nombre de usuario en Firebase Realtime Database
    private void saveUserName() {
        String userId = mAuth.getCurrentUser().getUid();
        String userName = binding.etNombreUsuario.getText().toString().trim();  // Obtener el texto del EditText

        // Verificar que el nombre no esté vacío
        if (!userName.isEmpty()) {
            DatabaseReference userRef = database.getReference("Users").child(userId);
            userRef.child("nombreUsuario").setValue(userName)  // Guardamos el nombre de usuario
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "Nombre de usuario actualizado", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Error al guardar el nombre de usuario", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(getContext(), "Por favor ingresa un nombre de usuario", Toast.LENGTH_SHORT).show();
        }
    }

    // Cargar los datos del usuario (correo, imagen y desafíos completados)
    private void loadUserData() {
        String userId = mAuth.getCurrentUser().getUid();
        DatabaseReference userRef = database.getReference("Users").child(userId);

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Obtener el correo desde FirebaseAuth
                String userEmail = mAuth.getCurrentUser().getEmail();
                String userImageUrl = task.getResult().child("imagen").getValue(String.class);
                String userName = task.getResult().child("nombreUsuario").getValue(String.class);

                // Establecer el correo en el TextView
                binding.tvCorreo.setText(userEmail);

                // Establecer el nombre de usuario en el EditText
                if (userName != null) {
                    binding.etNombreUsuario.setText(userName);
                }

                // Cargar la imagen de perfil con Glide
                if (userImageUrl != null && !userImageUrl.isEmpty()) {
                    Glide.with(getContext()).load(userImageUrl).into(binding.ivPerfil);
                }

                // Cargar los desafíos completados
                loadChallenges(userId);
            } else {
                Toast.makeText(getContext(), "Error al cargar los datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para cargar los desafíos completados
    private void loadChallenges(String userId) {
        DatabaseReference challengesRef = database.getReference("Users").child(userId).child("experienciasCompletadas");

        challengesRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().exists()) {
                    // Crear una lista con los desafíos completados
                    ArrayList<String> challengesList = new ArrayList<>();
                    for (DataSnapshot snapshot : task.getResult().getChildren()) {
                        String experienciaKey = snapshot.getKey();  // Obtener el nombre del desafío
                        // Formatear el nombre antes de agregarlo a la lista
                        String experienciaFormateada = formatNombreExperiencia(experienciaKey);
                        challengesList.add(experienciaFormateada); // Agregar el nombre formateado
                    }

                    // Usar un ArrayAdapter para mostrar los desafíos en el ListView
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, challengesList);
                    binding.lvExperienciasCompletadas.setAdapter(adapter);
                } else {
                    // Si no hay desafíos, mostrar el mensaje "Ningún desafío completado"
                    ArrayList<String> emptyList = new ArrayList<>();
                    emptyList.add("Ninguna experiencia completada");
                    ArrayAdapter<String> emptyAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, emptyList);
                    binding.lvExperienciasCompletadas.setAdapter(emptyAdapter);
                }
            } else {
                Toast.makeText(getContext(), "Error al cargar las experiencias", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Abrir la galería para elegir una nueva imagen de perfil
    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();

            // Subir la nueva imagen
            uploadImageToFirebase(imageUri);
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        // Obtener una referencia única para la imagen
        String userId = mAuth.getCurrentUser().getUid();
        StorageReference storageRef = storage.getReference("profile_images/" + userId + "/" + UUID.randomUUID().toString());

        // Subir la imagen al storage
        storageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            // La URL de la imagen subida
                            String imageUrl = uri.toString();

                            // Actualizar la imagen en Firebase Realtime Database
                            updateUserProfileImage(imageUrl);
                        })
                        .addOnFailureListener(e -> Toast.makeText(getContext(), "Error al obtener la URL", Toast.LENGTH_SHORT).show()))
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error al subir la imagen", Toast.LENGTH_SHORT).show());
    }

    private void updateUserProfileImage(String imageUrl) {
        String userId = mAuth.getCurrentUser().getUid();
        DatabaseReference userRef = database.getReference("Users").child(userId);

        // Actualizamos la URL de la imagen en la base de datos
        userRef.child("imagen").setValue(imageUrl)
                .addOnSuccessListener(aVoid -> {
                    // Actualizamos la imagen en el ImageView
                    Glide.with(getContext()).load(imageUrl).into(binding.ivPerfil);
                    Toast.makeText(getContext(), "Imagen actualizada", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error al actualizar la imagen", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Método para agregar espacios entre mayúsculas
    private String formatNombreExperiencia(String nombre) {
        // Añadir un espacio antes de cada letra mayúscula, excepto al principio
        return nombre.replaceAll("([a-z])([A-Z])", "$1 $2");
    }

}
