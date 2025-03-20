package com.example.g2int101experience.ui.home;

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
import com.example.g2int101experience.databinding.FragmentHomeBinding;
import com.example.g2int101experience.models.Desafio;
import java.util.ArrayList;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private ListDesafiosAdapter misDesafiosAdapter;
    private ListDesafiosAdapter desafiosPopularesAdapter;
    private HomeViewModel model;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        model = new ViewModelProvider(this).get(HomeViewModel.class);

        if(binding != null){
            configurarMisDesafios(view);

            configurarDesafiosPopulares(view);

            model.getDesafioLiveData().observe(getViewLifecycleOwner(), this::cargarDesafios);
            model.getDesafiosPopularesLiveData().observe(getViewLifecycleOwner(), this::cargarDesafiosPopulares);

            model.cargarDesafios();
            model.cargarDesafiosPopulares();
        }
    }

    private void configurarMisDesafios(View view) {
        RecyclerView recyclerViewMisDesafios = binding.homeRvMisRetos;
        recyclerViewMisDesafios.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        misDesafiosAdapter = new ListDesafiosAdapter(new ArrayList<>(), (position, mode) -> {
            Desafio desafio = Objects.requireNonNull(model.getDesafioLiveData().getValue()).get(position);

            Bundle bundle = new Bundle();
            bundle.putString("nombreDesafio", desafio.getTitulo());

            Navigation.findNavController(view).navigate(R.id.listadoDeExperiencias, bundle);
        }, model);

        recyclerViewMisDesafios.setAdapter(misDesafiosAdapter);
    }

    private void configurarDesafiosPopulares(View view) {
        RecyclerView recyclerViewPopulares = binding.homeRvDesafiosPopulares;
        recyclerViewPopulares.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        desafiosPopularesAdapter = new ListDesafiosAdapter(new ArrayList<>(), (position, mode) -> {
            Desafio desafio = Objects.requireNonNull(model.getDesafiosPopularesLiveData().getValue()).get(position);

            Bundle bundle = new Bundle();
            bundle.putString("nombreDesafio", desafio.getTitulo());

            Navigation.findNavController(view).navigate(R.id.listadoDeExperiencias, bundle);
        }, model);

        recyclerViewPopulares.setAdapter(desafiosPopularesAdapter);
    }

    private void cargarDesafios(ArrayList<Desafio> desafios) {
        misDesafiosAdapter.setDesafioList(desafios);
    }

    private void cargarDesafiosPopulares(ArrayList<Desafio> desafiosPopulares) {
        desafiosPopularesAdapter.setDesafioList(desafiosPopulares);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}