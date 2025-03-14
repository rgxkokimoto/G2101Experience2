package com.example.g2int101experience.ui.listadoexperiencias;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.g2int101experience.R;
import com.example.g2int101experience.databinding.RecyclerItemLayoutBinding;
import com.example.g2int101experience.models.Experiencia;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListadoDeExperienciasAdapter extends RecyclerView.Adapter<ListadoDeExperienciasAdapter.ListadoDeExperienciasViewHolder> {

    private ArrayList<Experiencia> experienciaList;
    private final OnItemClickListener listener;
    private ListadoDeExperienciasViewModel listadoDeExperienciasViewModel;

    public ListadoDeExperienciasAdapter(ArrayList<Experiencia> experienciaList, OnItemClickListener listener, ListadoDeExperienciasViewModel listadoDeExperienciasViewModel) {
        this.experienciaList = experienciaList;
        this.listener = listener;
        this.listadoDeExperienciasViewModel = listadoDeExperienciasViewModel;
    }

    @NonNull
    @Override
    public ListadoDeExperienciasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerItemLayoutBinding binding = RecyclerItemLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ListadoDeExperienciasViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ListadoDeExperienciasViewHolder holder, int position) {
        Experiencia experiencia = experienciaList.get(position);

        if (experiencia != null) {
            holder.binding.tvTituloExperiencia.setText(experiencia.getTitulo());

            Picasso.get()
                    .load(experiencia.getImgUlr())
                    .fit()
                    .error(R.drawable.ic_launcher_background)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(holder.binding.imgImagenExperiencia);

            // Controlador del botón "Ver más"
            holder.binding.btnVerMas.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(position, 0);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return experienciaList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position, int mode);
    }

    public void setExperienciaList(ArrayList<Experiencia> experienciaList) {
        this.experienciaList = experienciaList;
        notifyDataSetChanged();
    }

    static class ListadoDeExperienciasViewHolder extends RecyclerView.ViewHolder {
        private final RecyclerItemLayoutBinding binding;
        public ListadoDeExperienciasViewHolder(RecyclerItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
