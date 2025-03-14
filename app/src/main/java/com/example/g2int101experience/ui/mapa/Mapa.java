package com.example.g2int101experience.ui.mapa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.g2int101experience.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Mapa extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;

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
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fgMap);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        LatLng plazaMayor = new LatLng(40.4611107, -3.7580916);
        LatLng museoDeLasIlusiones = new LatLng(40.41325, -3.70392);
        LatLng museoDelPadro = new LatLng(40.41354, -3.69143);
        LatLng temploDeDebod = new LatLng(40.425, -3.713);
        LatLng barrioMalanesa = new LatLng(40.4270, -3.7050);

        mMap.addMarker(new MarkerOptions().position(plazaMayor).title("Plaza Mayor")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))).showInfoWindow();

        mMap.addMarker(new MarkerOptions().position(museoDeLasIlusiones).title("Museo De Las Ilusiones")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))).showInfoWindow();

        mMap.addMarker(new MarkerOptions().position(museoDelPadro).title("Museo del Prado")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))).showInfoWindow();

        mMap.addMarker(new MarkerOptions().position(temploDeDebod).title("Templo de Debod")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))).showInfoWindow();

        mMap.addMarker(new MarkerOptions().position(barrioMalanesa).title("Barrio de Malasaña")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))).showInfoWindow();

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(plazaMayor, 10f);
        mMap.moveCamera(cameraUpdate);
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        // Implementación futura si es necesario
    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        // Implementación futura si es necesario
    }
}
