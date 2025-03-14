package com.example.g2int101experience.ui.detalle;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.g2int101experience.R;
import com.example.g2int101experience.models.Experiencia;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DetalleExperienciaFragment extends Fragment {
    private static final String ARG_EXPERIENCIA_ID = "idExperiencia";
    private String id;

    public static DetalleExperienciaFragment newInstance(String id) {
        DetalleExperienciaFragment fragment = new DetalleExperienciaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EXPERIENCIA_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString(ARG_EXPERIENCIA_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle_experiencia, container, false);

        // Referencia a tus vistas aquí
        TextView titulo = view.findViewById(R.id.tvTitulo);
        ImageView imagen = view.findViewById(R.id.ivImagenExperiencia);
        TextView descripcion = view.findViewById(R.id.tvDescripcion);

        /*// Verifica que el ID no sea null
        if (id != null && !id.isEmpty()) {
            // Cargar datos de Firebase Realtime Database
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Experiencias").child(id);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Experiencia experiencia = dataSnapshot.getValue(Experiencia.class);
                    if (experiencia != null) {
                        titulo.setText(experiencia.getTitulo());
                        descripcion.setText(experiencia.getDescripcion());
                        Picasso.get()
                                .load(experiencia.getImgUlr())
                                .fit()
                                .into(imagen);
                    } else {
                        // Manejar el caso donde la experiencia es null
                        titulo.setText("Datos no disponibles");
                        descripcion.setText("");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Manejar error de Firebase
                    titulo.setText("Error al cargar datos");
                    descripcion.setText("");
                }
            });
        } else {
            // Manejar el caso donde el ID es null o vacío
            titulo.setText("ID no válido");
            descripcion.setText("");
        } */

        return view;
    }
}
