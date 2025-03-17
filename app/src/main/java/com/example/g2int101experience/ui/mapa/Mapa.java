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
import android.widget.Toast;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class Mapa extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private BottomSheetBehavior<View> bottomSheetBehavior;
    private TextView textViewExperienceName, textViewDescription;
    private ImageView imageView;
    private Button btnWebsite, btnClose, btnShare;
    private View bottomSheet;
    private DatabaseReference dbRef;
    private final Map<Marker, DataSnapshot> marcadorExperienciaMap = new HashMap<>();

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

        // Inicializar elementos del Bottom Sheet
        bottomSheet = view.findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN); // Ocultar al inicio

        textViewExperienceName = view.findViewById(R.id.textViewExperienceName);
        textViewDescription = view.findViewById(R.id.textViewDescription);
        imageView = view.findViewById(R.id.imageView);
        btnWebsite = view.findViewById(R.id.btnWebsite);
        btnClose = view.findViewById(R.id.btnClose);
        btnShare = view.findViewById(R.id.btnShare);

        // Configurar el mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fgMap);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Botón para cerrar la pestaña
        btnClose.setOnClickListener(v -> bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN));

        // Botón de compartir
        btnShare.setOnClickListener(v -> compartir());
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Obtener datos de Firebase
        dbRef = FirebaseDatabase.getInstance().getReference("Experiencias");
        dbRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                LatLng firstLocation = null;

                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    String titulo = snapshot.child("nombre").getValue(String.class); // nombre
                    Double latitud = snapshot.child("latitud").getValue(Double.class); // latitud
                    Double longitud = snapshot.child("longitud").getValue(Double.class); // longitud

                    // Verificar si los datos son válidos
                    if (titulo != null && latitud != null && longitud != null) {
                        LatLng ubicacion = new LatLng(latitud, longitud);
                        Marker marker = mMap.addMarker(new MarkerOptions().position(ubicacion).title(titulo));

                        // Guardar el DataSnapshot asociado al marcador
                        marcadorExperienciaMap.put(marker, snapshot);

                        // Guardar la primera ubicación del marcador para mover la cámara
                        if (firstLocation == null) {
                            firstLocation = ubicacion;
                        }
                    }
                }

                // Si encontramos la primera ubicación, centramos la cámara pero NO mostramos el Bottom Sheet
                if (firstLocation != null) {
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(firstLocation, 15f);
                    mMap.moveCamera(cameraUpdate);
                }
            }
        });

        // Manejar clic en marcadores
        mMap.setOnMarkerClickListener(marker -> {
            DataSnapshot snapshot = marcadorExperienciaMap.get(marker);
            if (snapshot != null) {
                mostrarDetalles(snapshot);
                return true;
            }
            return false;
        });
    }

    private void mostrarDetalles(DataSnapshot snapshot) {
        String nombre = snapshot.child("nombre").getValue(String.class);
        String descripcion = snapshot.child("descripcion").getValue(String.class);
        String imgUrl = snapshot.child("img").getValue(String.class);
        String web = snapshot.child("web").exists() ? snapshot.child("web").getValue(String.class) : null;

        // Establecer el nombre de la experiencia
        textViewExperienceName.setText(nombre != null ? nombre : "Experiencia sin nombre");

        // Establecer la descripción
        textViewDescription.setText(descripcion != null ? descripcion : "Descripción no disponible");

        // Cargar imagen con Picasso
        if (imgUrl != null && !imgUrl.isEmpty()) {
            Picasso.get().load(imgUrl).into(imageView);
        } else {
            imageView.setImageResource(R.drawable.ic_launcher_background); // Imagen por defecto
        }

        // Configurar botón de la web
        btnWebsite.setOnClickListener(v -> {
            if (web != null && !web.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(web));
                startActivity(intent);
            } else {
                Toast.makeText(getContext(), "Este lugar no tiene sitio web", Toast.LENGTH_SHORT).show();
            }
        });

        // Mostrar el Bottom Sheet solo si no está visible
        if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    // Función para compartir el contenido del Bottom Sheet
    private void compartir() {
        // Obtener el contenido a compartir
        String nombre = textViewExperienceName.getText().toString();
        String descripcion = textViewDescription.getText().toString();

        // Crear el Intent para compartir
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");

        // Configurar el mensaje que se va a compartir
        String mensaje = nombre + "\n\n" + descripcion + "\n\nMira este lugar en el mapa.";
        shareIntent.putExtra(Intent.EXTRA_TEXT, mensaje);

        // Verificar si hay alguna aplicación que pueda manejar la acción de compartir
        if (shareIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(Intent.createChooser(shareIntent, "Compartir con"));
        } else {
            Toast.makeText(getContext(), "No hay aplicaciones disponibles para compartir", Toast.LENGTH_SHORT).show();
        }
    }
}
