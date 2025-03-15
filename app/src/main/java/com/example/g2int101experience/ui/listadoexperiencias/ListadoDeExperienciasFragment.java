package com.example.g2int101experience.ui.listadoexperiencias;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.g2int101experience.R;
import com.example.g2int101experience.databinding.FragmentListadoDeExperienciasBinding;
import com.example.g2int101experience.models.Experiencia;

import java.util.ArrayList;
import java.util.Objects;

public class ListadoDeExperienciasFragment extends Fragment implements ListadoDeExperienciasAdapter.OnItemClickListener {

    private FragmentListadoDeExperienciasBinding binding;
    private ListadoDeExperienciasAdapter adapter;
    private ListadoDeExperienciasViewModel model;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentListadoDeExperienciasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        model = new ViewModelProvider(this).get(ListadoDeExperienciasViewModel.class);

        /*
         *  Recogemos el Bundle con el nombre del desafio del anterior fragmento
         */
        if (getArguments() != null && getArguments().containsKey("nombreDesafio")) {
            String nombreDesafioSeleccionado = getArguments().getString("nombreDesafio", "");

            binding.tvTituloListadoExperiencias.setText(nombreDesafioSeleccionado);

            model.cargarExperienciasPorDesafio(nombreDesafioSeleccionado);
        }

        if(binding != null) {
            RecyclerView recyclerView = binding.rvListaExperiencias;
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
            adapter = new ListadoDeExperienciasAdapter(new ArrayList<>(), this, model); // Pasar `this` como listener
            recyclerView.setAdapter(adapter);

            model.getExperienciaLiveData().observe(getViewLifecycleOwner(), this::cargarExperiencias);
        }

        // Otra forma de recibir datos del anterior fragmento
        /*getParentFragmentManager().setFragmentResultListener("datosDesafioParaExperiencias", this,
                new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        nombreDesafioSeleccionado = result.getString("nombreDesafio", "");

                        if (binding.tvTituloListadoExperiencias != null) {
                            binding.tvTituloListadoExperiencias.setText(nombreDesafioSeleccionado);
                        }

                        model.cargarExperienciasPorDesafio(nombreDesafioSeleccionado);
                    }
                }); */

    }

    @Override
    public void onItemClick(int position, int mode) {
        Experiencia experiencia = Objects.requireNonNull(model.getExperienciaLiveData().getValue()).get(position);
        Bundle bundle = new Bundle();
        bundle.putString("id", experiencia.getId());
        Navigation.findNavController(requireView()).navigate(R.id.action_listadoDeExperiencias_to_experienciaDetalleFragment, bundle);
    }

    private void cargarExperiencias(ArrayList<Experiencia> experiencias) {
        adapter.setExperienciaList(experiencias);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
