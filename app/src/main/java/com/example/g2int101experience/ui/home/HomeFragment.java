package com.example.g2int101experience.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.g2int101experience.R;
import com.example.g2int101experience.databinding.FragmentHomeBinding;
import com.example.g2int101experience.models.Desafio;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private ListDesafiosAdapter adapter;
    private HomeViewModel model;
    private List<Desafio> userList;


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
            RecyclerView recyclerView = binding.homeRvMisRetos;
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
            adapter = new ListDesafiosAdapter(new ArrayList<>(), (position, mode) -> {
                Desafio desafio = model.getDesafioLiveData().getValue().get(position);
                Bundle bundle = new Bundle();
                bundle.putString("id", String.valueOf(desafio.getId())); // TODO no entiendo del todo para que sirve esto
                // TODO victor esto  te puede servir este Bundle manda info entre ventanas
                // TODO victor aqui es donde se navega a la lista de experiencias ;) por si lo necesitas
                Navigation.findNavController(view).navigate(R.id.listadoDeExperiencias, bundle); // Navegar al frgmento de la lista de experiencias
            }, model);
            recyclerView.setAdapter(adapter);

            model.getDesafioLiveData().observe(getViewLifecycleOwner(), this::cargarDesafios);

            model.cargarDesafios();
        }

    }

    private void cargarDesafios(ArrayList<Desafio> desafios) {
        adapter.setDesafioList(desafios);
    }

    private void setContentView(ConstraintLayout root) {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}