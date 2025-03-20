package com.example.g2int101experience.ui.detalle;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.g2int101experience.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DetalleExperienciaFragment extends Fragment {

    private static final String ARG_EXPERIENCIA_ID = "id";
    private DatabaseReference databaseReference;
    private String id;

    // En el contexto en el que estamos y la extrcutura que seguimos este método es inecesario.
    /*
    public static DetalleExperienciaFragment newInstance(String id) {
        DetalleExperienciaFragment fragment = new DetalleExperienciaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EXPERIENCIA_ID, id);
        fragment.setArguments(args);
        return fragment;
    }
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString(ARG_EXPERIENCIA_ID);
            databaseReference = FirebaseDatabase.getInstance().getReference("Experiencias");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detalle_experiencia, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvTitulo = view.findViewById(R.id.tvTitulo);
        ImageView ivExpr = view.findViewById(R.id.ivImagenExperiencia);
        TextView tVdescripcion = view.findViewById(R.id.tvDescripcion);
        Button btnEstadoExp = view.findViewById(R.id.btnEstadoDesafio);
        ImageButton btnVolver = view.findViewById(R.id.btnVolver);

        btnVolver.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        comprobarEstadoExp(btnEstadoExp);

        cargarDatosExperiencia(tvTitulo, ivExpr, tVdescripcion);
    }

    private void comprobarEstadoExp(Button btnEstadoExp) {
        if (id != null && !id.isEmpty()) {
            // Obtener el UID del usuario actual
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            // Referencia al nodo 'experienciasCompletadas' del usuario
            DatabaseReference userExpRef = FirebaseDatabase.getInstance()
                    .getReference("Users")
                    .child(uid)
                    .child("experienciasCompletadas")
                    .child(id);

            // Verificar si la experiencia está marcada como completada para el usuario
            userExpRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Boolean completada = dataSnapshot.getValue(Boolean.class);

                    // Si la experiencia está completada para este usuario
                    if (completada != null && completada) {
                        btnEstadoExp.setEnabled(false);
                        btnEstadoExp.setText("Completada");
                        btnEstadoExp.setAlpha(0.5f);
                    } else {
                        // Si no está completada, habilitar el botón para marcarla como completada
                        btnEstadoExp.setOnClickListener(v -> marcarComoCompletada());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // En caso de error, mostrar un mensaje
                    Toast.makeText(getContext(), "Error al verificar estado: " + error.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            btnEstadoExp.setVisibility(View.GONE);
        }
    }


    private void marcarComoCompletada() {
        // Obtener el ID del usuario autenticado
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user != null ? user.getUid() : null; // El ID del usuario

        if (userId != null) {
            // Referencia a la base de datos para "Users"
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");

            // Añadir la experiencia al nodo "experienciasCompletadas" del usuario
            usersRef.child(userId).child("experienciasCompletadas").child(id).setValue(true)
                    .addOnSuccessListener(aVoid -> {
                        // Éxito al añadir la experiencia a "experienciasCompletadas"
                        Toast.makeText(getContext(), "¡Experiencia completada!", Toast.LENGTH_SHORT).show();

                        // Actualizar el estado del botón para indicar que la experiencia fue completada
                        Button btnEstadoExp = requireView().findViewById(R.id.btnEstadoDesafio);
                        btnEstadoExp.setEnabled(false);
                        btnEstadoExp.setText("Completada");
                        btnEstadoExp.setAlpha(0.5f);
                    })
                    .addOnFailureListener(e -> {
                        // Manejo de error si no se pudo añadir a "experienciasCompletadas"
                        Toast.makeText(getContext(), "Error al completar la experiencia: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            // Si no hay usuario autenticado
            Toast.makeText(getContext(), "No hay usuario autenticado", Toast.LENGTH_SHORT).show();
        }
    }



    private void cargarDatosExperiencia(TextView tvTitulo, ImageView ivExpr, TextView tVdescripcion) {
        if (id != null && !id.isEmpty()) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Experiencias").child(id);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        tvTitulo.setText(dataSnapshot.child("nombre").getValue(String.class));
                        tVdescripcion.setText(dataSnapshot.child("descripcion").getValue(String.class));
                        Picasso.get()
                                .load(dataSnapshot.child("img").getValue(String.class))
                                .fit()
                                .error(R.drawable.ic_launcher_background)
                                .placeholder(R.drawable.ic_launcher_background)
                                .into(ivExpr);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Error al cargar datos: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "ID de experiencia no válido", Toast.LENGTH_SHORT).show();
        }
    }


}