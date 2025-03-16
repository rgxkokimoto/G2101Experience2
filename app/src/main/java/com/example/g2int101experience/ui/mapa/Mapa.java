package com.example.g2int101experience.ui.mapa;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.g2int101experience.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class Mapa extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private BottomSheetBehavior<View> bottomSheetBehavior;
    private TextView textViewDescription;
    private ImageView imageView;
    private Button btnWebsite, btnShare;

    public Mapa() {
        // Constructor vacío requerido
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mapa, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Referencias a los elementos de la Bottom Sheet
        View bottomSheet = view.findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN); // Ocultar al inicio

        textViewDescription = view.findViewById(R.id.textViewDescription);
        imageView = view.findViewById(R.id.imageView);
        btnWebsite = view.findViewById(R.id.btnWebsite);
        btnShare = view.findViewById(R.id.btnShare);

        // Configurar el mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fgMap);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Configuración del botón de la página web
        btnWebsite.setOnClickListener(v -> {
            // URL de la página web que deseas abrir
            String url = "https://www.madrid.es/portales/munimadrid/es/Inicio/El-Ayuntamiento/Parques-y-jardines/Patrimonio-Verde/Parques-en-Madrid/Parque-del-Museo-del-Prado/?vgnextfmt=default&vgnextoid=3047ee4002e2e210VgnVCM2000000c205a0aRCRD&vgnextchannel=38bb1914e7d4e210VgnVCM1000000b205a0aRCRD";

            // Crear el Intent para abrir la URL
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);  // Abrir la URL en el navegador predeterminado
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Referencia a Firebase
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Experiencias/MapaPrueba1NoBorrar");

        databaseReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                DataSnapshot dataSnapshot = task.getResult();
                String nombre = dataSnapshot.child("nombre").getValue(String.class);
                String descripcion = dataSnapshot.child("descripcion").getValue(String.class);
                String imgUrl = dataSnapshot.child("img").getValue(String.class);
                double latitud = dataSnapshot.child("Latitud").getValue(Double.class);
                double longitud = dataSnapshot.child("Longitud").getValue(Double.class);

                LatLng ubicacion = new LatLng(latitud, longitud);
                Marker marker = mMap.addMarker(new MarkerOptions().position(ubicacion).title(nombre));
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(ubicacion, 15f);
                mMap.moveCamera(cameraUpdate);

                // Evento al tocar el marcador
                mMap.setOnMarkerClickListener(marker1 -> {
                    if (marker1.getTitle().equals(nombre)) {
                        mostrarDetalles(descripcion, imgUrl);
                    }
                    return false;
                });
            }
        });
    }

    private void mostrarDetalles(String descripcion, String imgUrl) {
        textViewDescription.setText(descripcion);

        // Cargar imagen de Firebase Storage usando Picasso
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imgUrl);
        storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.get().load(uri).into(imageView);
        });

        // Mostrar la Bottom Sheet
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }
}
