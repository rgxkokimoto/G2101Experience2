package com.example.g2int101experience.ui.listadoexperiencias;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

// ExperienciaManager.java
public class ExperienciaManager {

    // Método para marcar una experiencia como completada para un usuario
    public void completarExperiencia(String userId, String experienciaId) {
        // Obtén la referencia a la base de datos de Firebase
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        // Actualiza el estado de la experiencia como completada para este usuario
        database.child("Users").child(userId).child("completadas").child(experienciaId).setValue(true);

        // También puedes actualizar la experiencia en el nodo 'Experiencias' si lo necesitas
        database.child("Experiencias").child(experienciaId).child("completada").setValue(true);
    }
}

