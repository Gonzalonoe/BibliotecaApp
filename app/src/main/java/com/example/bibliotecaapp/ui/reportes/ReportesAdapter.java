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

import java.util.ArrayList;
import java.util.List;

public class ReportesAdapter extends RecyclerView.Adapter<ReportesAdapter.ViewHolder> {

    private final List<Reporte> lista;
    private OnReporteClickListener listener;

    public interface OnReporteClickListener {
        void onReporteClick(int reporteId);
    }

    public void setOnReporteClickListener(OnReporteClickListener listener) {
        this.listener = listener;
    }

    public ReportesAdapter(List<Reporte> lista) {
        this.lista = lista != null ? lista : new ArrayList<>();
    }

    @NonNull
    @Override
    public ReportesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reporte, parent, false);
        return new ReportesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportesAdapter.ViewHolder holder, int position) {
        Reporte r = lista.get(position);

        holder.tvTitulo.setText(r.getTituloLibro());
        holder.tvSinopsis.setText(r.getSinopsis());
        holder.tvUsuario.setText("Por: " + r.getUsuarioNombre());
        holder.tvFecha.setText("Fecha: " + r.getFecha());

        if (r.getImagenPortada() != null && !r.getImagenPortada().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(r.getImagenPortada())
                    .placeholder(R.drawable.ic_menu_book)
                    .into(holder.ivPortada);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onReporteClick(r.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void actualizarLista(List<Reporte> nuevos) {
        lista.clear();
        if (nuevos != null) {
            lista.addAll(nuevos);
        }
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivPortada;
        TextView tvTitulo, tvSinopsis, tvUsuario, tvFecha;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPortada = itemView.findViewById(R.id.ivPortadaReporte);
            tvTitulo = itemView.findViewById(R.id.tvTituloReporte);
            tvSinopsis = itemView.findViewById(R.id.tvSinopsisReporte);
            tvUsuario = itemView.findViewById(R.id.tvUsuarioReporte);
            tvFecha = itemView.findViewById(R.id.tvFechaReporte);
        }
    }
}
