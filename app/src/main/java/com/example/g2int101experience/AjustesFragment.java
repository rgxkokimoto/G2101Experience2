package com.example.g2int101experience;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AjustesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AjustesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AjustesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AjustesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AjustesFragment newInstance(String param1, String param2) {
        AjustesFragment fragment = new AjustesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflamos el layout para este fragment
        View rootView = inflater.inflate(R.layout.fragment_ajustes, container, false);

        // Aquí asignamos el MaterialButton y le colocamos el OnClick
        MaterialButton btnCambiarIdioma = rootView.findViewById(R.id.btnCambiarIdioma);

        // Obtenemos el idioma guardado
        String languageCode = GuardarIdioma.getSavedLanguage(getContext());
        ManejarIdioma.setAppLocale(getContext(), languageCode);

        // Seteamos el OnClick del botón
        btnCambiarIdioma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtenemos el idioma actual
                String currentLanguage = GuardarIdioma.getSavedLanguage(getContext());

                // Definimos el nuevo idioma
                String newLanguage = currentLanguage.equals("es") ? "en" : "es";

                // Guardamos el nuevo idioma
                GuardarIdioma.saveLanguage(getContext(), newLanguage);

                // Cambiamos el idioma en la aplicación
                ManejarIdioma.setAppLocale(getContext(), newLanguage);

                // Recargamos la actividad para aplicar el cambio
                requireActivity().recreate();

                // Mostramos un mensaje de éxito
                Toast.makeText(getContext(), "Idioma cambiado a " + (newLanguage.equals("es") ? "Español" : "Inglés"), Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }
}
