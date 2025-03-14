package com.example.g2int101experience.ui.listadoexperiencias;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.g2int101experience.models.Desafio;
import com.example.g2int101experience.models.Experiencia;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListadoDeExperienciasViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<Experiencia>> experienciaLiveData = new MutableLiveData<>();

    // Referencias a firebase
    private DatabaseReference refExperiencias;

    /*
        Aqui vamos a inicializar las referencias a firebase para poder acceder a los datos en los nodos
     */
    public ListadoDeExperienciasViewModel() {

        refExperiencias = FirebaseDatabase.getInstance().getReference("Experiencias");

    }

    public MutableLiveData<ArrayList<Experiencia>> getExperienciaLiveData() {
        return experienciaLiveData;
    }

    public void cargarExperiencias() {

        refExperiencias.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Experiencia> experienciaList = new ArrayList<>();
                String id;
                String nombre;
                String imagen;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    //id = dataSnapshot.child("id").getValue(String.class);
                    nombre = dataSnapshot.child("nombre").getValue(String.class);
                    imagen = dataSnapshot.child("img").getValue(String.class);
                    experienciaList.add(new Experiencia(nombre, imagen));
                }

                experienciaLiveData.postValue(experienciaList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}
