package com.example.bibliotecaapp.ui.reportes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bibliotecaapp.R;
import com.example.bibliotecaapp.models.Reporte;

import java.util.List;

public class ReportesAdapter extends RecyclerView.Adapter<ReportesAdapter.ViewHolder> {

    private final List<Reporte> lista;

    public ReportesAdapter(List<Reporte> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reporte, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reporte reporte = lista.get(position);
        holder.tvTitulo.setText(reporte.getTituloLibro());
        holder.tvSinopsis.setText(reporte.getSinopsis());
        holder.tvUsuario.setText("Por: " + reporte.getUsuarioNombre());
        holder.tvFecha.setText("Fecha: " + reporte.getFecha());

        if (reporte.getImagenPortada() != null && !reporte.getImagenPortada().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(reporte.getImagenPortada())
                    .placeholder(R.drawable.ic_menu_book)
                    .into(holder.ivPortada);
        } else {
            holder.ivPortada.setImageResource(R.drawable.ic_menu_book);
        }
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void actualizarLista(List<Reporte> nuevos) {
        lista.clear();
        lista.addAll(nuevos);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPortada;
        TextView tvTitulo, tvSinopsis, tvUsuario, tvFecha;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPortada = itemView.findViewById(R.id.ivPortadaReporte);
            tvTitulo = itemView.findViewById(R.id.tvTituloReporte);
            tvSinopsis = itemView.findViewById(R.id.tvSinopsisReporte);
            tvUsuario = itemView.findViewById(R.id.tvUsuarioReporte);
            tvFecha = itemView.findViewById(R.id.tvFechaReporte);
        }
    }
}
