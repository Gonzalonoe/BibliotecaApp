package com.example.bibliotecaapp.ui.libros;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.bibliotecaapp.R;

public class DetalleLibroFragment extends Fragment {

    private DetalleLibroViewModel vm;
    private ImageView ivPortada;
    private EditText etTitulo, etAutor, etAnio, etStock;
    private TextView etDescripcion;
    private Button btnReservar, btnEditar, btnEliminar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_detalle_libro, container, false);
        inicializarVista(root);

        vm = new ViewModelProvider(this).get(DetalleLibroViewModel.class);

        vm.cargarLibroDesdeArgs(getArguments());

        vm.libro.observe(getViewLifecycleOwner(), libro -> {
            etTitulo.setText(libro.getTitulo());
            etAutor.setText(libro.getAutor());
            etAnio.setText(String.valueOf(libro.getAnio()));
            etStock.setText(String.valueOf(libro.getStock()));
            etDescripcion.setText(libro.getDescripcion());

            Glide.with(requireContext())
                    .load(libro.getPortada())
                    .placeholder(R.drawable.ic_menu_book)
                    .error(R.drawable.ic_menu_book)
                    .into(ivPortada);
        });

        vm.visibleEditar.observe(getViewLifecycleOwner(), v -> btnEditar.setVisibility(v));
        vm.visibleEliminar.observe(getViewLifecycleOwner(), v -> btnEliminar.setVisibility(v));

        vm.editable.observe(getViewLifecycleOwner(), enabled -> {
            etTitulo.setEnabled(enabled);
            etAutor.setEnabled(enabled);
            etAnio.setEnabled(enabled);
            etStock.setEnabled(enabled);
        });

        vm.textoBotonEditar.observe(getViewLifecycleOwner(),
                txt -> btnEditar.setText(txt));

        vm.toast.observe(getViewLifecycleOwner(),
                msg -> android.widget.Toast.makeText(requireContext(), msg, android.widget.Toast.LENGTH_SHORT).show());

        vm.volverAtras.observe(getViewLifecycleOwner(),
                v -> Navigation.findNavController(requireView()).popBackStack());

        vm.mostrarDialogoEliminar.observe(getViewLifecycleOwner(), unused -> {
            new android.app.AlertDialog.Builder(requireContext())
                    .setTitle("Dar de baja el libro")
                    .setMessage("El libro será ocultado, pero se mantendrá en la base de datos.\n¿Deseas continuar?")
                    .setPositiveButton("Sí", (dialog, which) -> vm.confirmarEliminar())
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        btnEditar.setOnClickListener(v ->
                vm.onEditarClick(
                        etTitulo.getText().toString(),
                        etAutor.getText().toString(),
                        etAnio.getText().toString(),
                        etStock.getText().toString(),
                        etDescripcion.getText().toString()
                ));

        btnReservar.setOnClickListener(v -> vm.reservarLibro());

        btnEliminar.setOnClickListener(v -> vm.solicitarEliminar());

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
