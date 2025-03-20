package com.example.g2int101experience.ui.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.g2int101experience.models.Desafio;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<Desafio>> desafioLiveData = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Desafio>> desafiosPopularesLiveData = new MutableLiveData<>();

    // Referencias a firebase
    private DatabaseReference refDesafios;

    /*
        Aqui vamos a inicializar las referencias a firebase para poder acceder a los datos en los nodos
     */
    public HomeViewModel() {
        refDesafios = FirebaseDatabase.getInstance().getReference("Desafios");
    }

    public MutableLiveData<ArrayList<Desafio>> getDesafioLiveData() {
        return desafioLiveData;
    }

    public MutableLiveData<ArrayList<Desafio>> getDesafiosPopularesLiveData() {
        return desafiosPopularesLiveData;
    }

    public void cargarDesafios() {
        refDesafios.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Desafio> desafioList = new ArrayList<>();
                String nombre;
                String imagen;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    nombre = dataSnapshot.child("nombre").getValue(String.class);
                    imagen = dataSnapshot.child("imagen").getValue(String.class);
                    desafioList.add(new Desafio(nombre, imagen));
                }

                desafioLiveData.postValue(desafioList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejo de errores
            }
        });
    }

    public void cargarDesafiosPopulares() {

        Query query = refDesafios.orderByChild("popularidad").startAt(1).limitToLast(10);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Desafio> desafiosPopulares = new ArrayList<>();
                String nombre;
                String imagen;
                Long popularidad;

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.hasChild("popularidad")) {
                        nombre = dataSnapshot.child("nombre").getValue(String.class);
                        imagen = dataSnapshot.child("imagen").getValue(String.class);
                        popularidad = dataSnapshot.child("popularidad").getValue(Long.class);

                        if (nombre != null && imagen != null && popularidad != null) {
                            Desafio desafio = new Desafio(nombre, imagen);
                            desafiosPopulares.add(0, desafio);
                        }
                    }
                }

                desafiosPopularesLiveData.postValue(desafiosPopulares);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejo de errores
            }
        });
    }
}