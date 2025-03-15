package com.example.g2int101experience.ui.detalle;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.g2int101experience.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DetalleExperienciaFragment extends Fragment {

    private static final String ARG_EXPERIENCIA_ID = "id";
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

        cargarDatosExperiencia(tvTitulo, ivExpr, tVdescripcion);
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
                    mostrarErrorDatos(tvTitulo, tVdescripcion, "Error al cargar datos: " + error.getMessage());
                }
            });
        } else {
            mostrarErrorDatos(tvTitulo, tVdescripcion, "ID no válido");
        }
    }

    private void mostrarErrorDatos(TextView titulo, TextView descripcion, String mensaje) {
        titulo.setText(mensaje);
        descripcion.setText("");
    }
}