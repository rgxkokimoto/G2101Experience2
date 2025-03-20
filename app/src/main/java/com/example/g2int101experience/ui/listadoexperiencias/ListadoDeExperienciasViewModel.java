package com.example.g2int101experience.ui.listadoexperiencias;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.g2int101experience.models.Experiencia;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListadoDeExperienciasViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<Experiencia>> experienciaLiveData = new MutableLiveData<>();

    // Referencias a firebase
    private final  DatabaseReference refExperiencias;

    /*
        Aqui vamos a inicializar las referencias a firebase para poder acceder a los datos en los nodos
     */
    public ListadoDeExperienciasViewModel() {

        refExperiencias = FirebaseDatabase.getInstance().getReference("Experiencias");

    }

    public MutableLiveData<ArrayList<Experiencia>> getExperienciaLiveData() {
        return experienciaLiveData;
    }

    // Método para cargar experiencias por con todos los desafios
    /*public void cargarExperiencias() {

        refExperiencias.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Experiencia> experienciaList = new ArrayList<>();
                //String id;
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

    } */


    public void cargarExperienciasPorDesafio(String nombreDesafioSeleccionado) {
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Referencia a las experiencias
        refExperiencias.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Experiencia> experienciaList = new ArrayList<>();

                // Obtener las experiencias completadas del usuario
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("experienciasCompletadas");

                userRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            // Lista de experiencias completadas del usuario
                            DataSnapshot completedExperiencesSnapshot = task.getResult();

                            // Iterar sobre todas las experiencias en Firebase
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                String desafioExperiencia = dataSnapshot.child("desafio").getValue(String.class);

                                if (desafioExperiencia != null && desafioExperiencia.equals(nombreDesafioSeleccionado)) {
                                    String id = dataSnapshot.child("id").getValue(String.class);
                                    String titulo = dataSnapshot.child("nombre").getValue(String.class);
                                    String imgURL = dataSnapshot.child("img").getValue(String.class);

                                    // Comprobar si la experiencia está completada para este usuario
                                    Boolean completada = completedExperiencesSnapshot.child(id).getValue(Boolean.class);

                                    // Crear la experiencia y agregarla a la lista
                                    Experiencia exp = new Experiencia(titulo, imgURL, id);
                                    if (completada != null && completada) {
                                        exp.setCompletada(true);
                                    }
                                    experienciaList.add(exp);
                                }
                            }

                            // Usar el ViewModel o LiveData para actualizar la interfaz de usuario
                            experienciaLiveData.postValue(experienciaList);
                        } else {
                            Log.e("Firebase", "Error al obtener las experiencias completadas del usuario");
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error en la carga de experiencias desde Firebase", error.toException());
            }
        });
    }


    public void completarExperiencia(String idExp) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Actualiza la experiencia completada en el nodo del usuario
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        // Añadimos la experiencia al nodo de experienciasCompletadas
        userRef.child("experienciasCompletadas").child(idExp).setValue(true)
                .addOnSuccessListener(aVoid -> {
                    // Si se completó con éxito, podemos hacer algo (por ejemplo, mostrar un mensaje)
                    Log.d("Firebase", "Experiencia completada correctamente");
                })
                .addOnFailureListener(e -> {
                    // Si ocurrió algún error, mostramos un mensaje de error
                    Log.e("Firebase", "Error al completar la experiencia", e);
                });
    }


}
