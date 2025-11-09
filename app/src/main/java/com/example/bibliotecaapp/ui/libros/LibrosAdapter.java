package com.example.bibliotecaapp.ui.libros;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bibliotecaapp.R;
import com.example.bibliotecaapp.models.Libro;

import java.util.List;

public class LibrosAdapter extends RecyclerView.Adapter<LibrosAdapter.ViewHolder> {

    private List<Libro> lista;
    private Fragment fragment;

    public LibrosAdapter(List<Libro> lista, Fragment fragment) {
        this.lista = lista;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_libro, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Libro libro = lista.get(position);

        holder.titulo.setText(libro.getTitulo());
        holder.autor.setText("Autor: " + libro.getAutor());
        holder.anio.setText("Año: " + (libro.getAnio() != null ? libro.getAnio() : "-"));
        holder.stock.setText("Stock: " + libro.getStock());

        if (libro.getPortada() != null && !libro.getPortada().isEmpty()) {
            Glide.with(fragment.requireContext())
                    .load(libro.getPortada())
                    .placeholder(R.drawable.ic_menu_book)
                    .error(R.drawable.ic_menu_book)
                    .into(holder.portada);
        } else {
            holder.portada.setImageResource(R.drawable.ic_menu_book);
        }

        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("libro", libro);

            NavOptions navOptions = new NavOptions.Builder()
                    .setEnterAnim(R.anim.slide_in_right)
                    .setExitAnim(R.anim.slide_out_left)
                    .setPopEnterAnim(R.anim.slide_in_left)
                    .setPopExitAnim(R.anim.slide_out_right)
                    .build();

            NavHostFragment.findNavController(fragment)
                    .navigate(R.id.action_nav_libros_to_detallesLibroFragment, bundle, navOptions);
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void actualizarLista(List<Libro> nuevosLibros) {
        this.lista.clear();
        this.lista.addAll(nuevosLibros);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titulo, autor, anio, stock;
        ImageView portada;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.tvTitulo);
            autor = itemView.findViewById(R.id.tvAutor);
            anio = itemView.findViewById(R.id.tvAnio);
            stock = itemView.findViewById(R.id.tvStock);
            portada = itemView.findViewById(R.id.ivPortada);
        }
    }
}
