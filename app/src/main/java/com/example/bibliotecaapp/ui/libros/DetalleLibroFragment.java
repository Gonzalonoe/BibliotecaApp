package com.example.bibliotecaapp.ui.libros;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.bibliotecaapp.R;
import com.example.bibliotecaapp.models.Libro;

public class DetalleLibroFragment extends Fragment {

    private DetalleLibroViewModel vm;
    private ImageView ivPortada;
    private EditText etTitulo, etAutor, etAnio, etStock, etDescripcion;
    private Button btnReservar, btnEditar, btnEliminar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detalle_libro, container, false);
        inicializarVista(root);

        vm = new ViewModelProvider(this).get(DetalleLibroViewModel.class);
        if (getArguments() != null) {
            Libro libro = (Libro) getArguments().getSerializable("libro");
            if (libro != null) {
                vm.setLibro(libro);
            }
        }

        vm.getLibro().observe(getViewLifecycleOwner(), libro -> {
            etTitulo.setText(libro.getTitulo());
            etAutor.setText(libro.getAutor());
            etAnio.setText(String.valueOf(libro.getAnio()));
            etStock.setText(String.valueOf(libro.getStock()));
            etDescripcion.setText(libro.getDescripcion());

            if (libro.getPortada() != null && !libro.getPortada().isEmpty()) {
                Glide.with(requireContext())
                        .load(libro.getPortada())
                        .placeholder(R.drawable.ic_menu_book)
                        .error(R.drawable.ic_menu_book)
                        .into(ivPortada);
            } else {
                ivPortada.setImageResource(R.drawable.ic_menu_book);
            }
        });

        vm.getTextoBotonEditar().observe(getViewLifecycleOwner(), texto -> btnEditar.setText(texto));

        vm.getModoEdicion().observe(getViewLifecycleOwner(), editando -> {
            etTitulo.setEnabled(editando);
            etAutor.setEnabled(editando);
            etAnio.setEnabled(editando);
            etStock.setEnabled(editando);
            etDescripcion.setEnabled(editando);
        });

        btnEditar.setOnClickListener(v -> {
            Boolean editando = vm.getModoEdicion().getValue();
            if (editando != null && editando) {
                vm.guardarCambios(
                        etTitulo.getText().toString(),
                        etAutor.getText().toString(),
                        etAnio.getText().toString(),
                        etStock.getText().toString(),
                        etDescripcion.getText().toString()
                );
            } else {
                vm.alternarModoEdicion();
            }
        });

        btnReservar.setOnClickListener(v -> vm.reservarLibro());

        btnEliminar.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Confirmar eliminación")
                    .setMessage("¿Seguro que deseas eliminar este libro?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        vm.eliminarLibro();
                        // 🔙 Volver automáticamente al listado de libros
                        Navigation.findNavController(v).popBackStack(R.id.nav_libros, false);
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        return root;
    }

    private void inicializarVista(View root) {
        ivPortada = root.findViewById(R.id.ivPortadaDetalle);
        etTitulo = root.findViewById(R.id.tvTituloDetalle);
        etAutor = root.findViewById(R.id.tvAutorDetalle);
        etAnio = root.findViewById(R.id.tvAnioDetalle);
        etStock = root.findViewById(R.id.tvStockDetalle);
        etDescripcion = root.findViewById(R.id.tvDescripcionDetalle);
        btnReservar = root.findViewById(R.id.btnReservar);
        btnEditar = root.findViewById(R.id.btnEditar);
        btnEliminar = root.findViewById(R.id.btnEliminar);
    }
}
