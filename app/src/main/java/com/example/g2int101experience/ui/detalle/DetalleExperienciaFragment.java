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
            // Referencia directa a la experiencia
            DatabaseReference expRef = databaseReference.child(id);

            expRef.child("completada").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Boolean completada = dataSnapshot.getValue(Boolean.class);

                    if (completada != null && completada) {
                        btnEstadoExp.setEnabled(false);
                        btnEstadoExp.setText("Completada");
                        btnEstadoExp.setAlpha(0.5f);

                    } else {
                        btnEstadoExp.setOnClickListener(v -> marcarComoCompletada());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // En caso de error, configurar botón por defecto
                    Toast.makeText(getContext(), "Error al verificar estado: " + error.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            btnEstadoExp.setVisibility(View.GONE);
        }
    }

    private void marcarComoCompletada() {

        DatabaseReference expRef = databaseReference.child(id);

        expRef.child("completada").setValue(true)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "¡Experiencia completada!", Toast.LENGTH_SHORT).show();

                    Button btnEstadoExp = requireView().findViewById(R.id.btnEstadoDesafio);
                    btnEstadoExp.setEnabled(false);
                    btnEstadoExp.setText("Completada");
                    btnEstadoExp.setAlpha(0.5f);

                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error al completar: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show());


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