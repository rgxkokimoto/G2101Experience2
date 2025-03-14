package com.example.g2int101experience.ui.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.g2int101experience.R;
import com.example.g2int101experience.databinding.HomeCardviewItemBinding;
import com.example.g2int101experience.models.Desafio;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListDesafiosAdapter extends RecyclerView.Adapter<ListDesafiosAdapter.HomeViewHolder> {

    private ArrayList<Desafio> desafioList;
    private final OnItemClickListener listener;
    private HomeViewModel homeViewModel;


    public ListDesafiosAdapter(ArrayList<Desafio> desafioList, OnItemClickListener listener, HomeViewModel homeViewModel) {
        this.desafioList = desafioList;
        this.listener = listener;
        this.homeViewModel = homeViewModel;

    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent , int viewType) {
        HomeCardviewItemBinding binding = HomeCardviewItemBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new HomeViewHolder(binding);
    }

    /**
     *
     * @param holder el ViewHolder que se está vinculando a los datos.
     * @param position la posición del elemento en la lista
     *
     * Este método se llama cuando se necesita vincular datos a un ViewHolder.
     *
     */
    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        Desafio desafio = desafioList.get(position);

        if (desafio != null) {

            holder.binding.tvDesafioButton.setText(desafio.getTitulo());

            Picasso.get()
                    .load(desafio.getImgUlr())
                    .fit()
                    .error(R.drawable.ic_launcher_background)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(holder.binding.ivDesafioButton);

        }

        holder.binding.ivDesafioButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position, 0);
            }
        });

    }

    @Override
    public int getItemCount() {
        return desafioList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position, int mode);
    }

    public void setDesafioList(ArrayList<Desafio> desafioList) {
        this.desafioList = desafioList;
        notifyDataSetChanged();
    }

    static class HomeViewHolder extends RecyclerView.ViewHolder {
        private final HomeCardviewItemBinding binding;
        public HomeViewHolder(HomeCardviewItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
