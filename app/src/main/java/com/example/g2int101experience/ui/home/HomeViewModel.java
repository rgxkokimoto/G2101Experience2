package com.example.g2int101experience.ui.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.g2int101experience.models.Desafio;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<Desafio>> desafioLiveData = new MutableLiveData<>();

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

    public void cargarDesafios() {

        refDesafios.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Desafio> desafioList = new ArrayList<>();
                String id;
                String nombre;
                String imagen;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    //id = dataSnapshot.child("id").getValue(String.class);
                    nombre = dataSnapshot.child("nombre").getValue(String.class);
                    imagen = dataSnapshot.child("imagen").getValue(String.class);
                    desafioList.add(new Desafio(nombre, imagen));
                }

                desafioLiveData.postValue(desafioList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}
