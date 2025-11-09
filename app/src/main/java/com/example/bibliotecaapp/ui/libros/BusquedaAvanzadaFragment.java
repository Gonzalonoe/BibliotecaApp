package com.example.bibliotecaapp.ui.libros;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bibliotecaapp.R;

import java.util.ArrayList;

public class BusquedaAvanzadaFragment extends Fragment {

    private BusquedaAvanzadaViewModel vm;
    private EditText etTitulo, etAutor, etAnio, etStock, etDescripcion;
    private Button btnBuscar, btnAnterior, btnSiguiente;
    private TextView tvPagina;
    private RecyclerView rvResultados;
    private LibrosAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_busqueda_avanzada, container, false);

        etTitulo = root.findViewById(R.id.etTituloFiltro);
        etAutor = root.findViewById(R.id.etAutorFiltro);
        etAnio = root.findViewById(R.id.etAnioFiltro);
        etStock = root.findViewById(R.id.etStockFiltro);
        etDescripcion = root.findViewById(R.id.etDescripcionFiltro);
        btnBuscar = root.findViewById(R.id.btnBuscarAvanzado);
        btnAnterior = root.findViewById(R.id.btnAnterior);
        btnSiguiente = root.findViewById(R.id.btnSiguiente);
        tvPagina = root.findViewById(R.id.tvPagina);
        rvResultados = root.findViewById(R.id.rvResultadosBusqueda);

        vm = new ViewModelProvider(this).get(BusquedaAvanzadaViewModel.class);

        rvResultados.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new LibrosAdapter(new ArrayList<>(), this);
        rvResultados.setAdapter(adapter);

        btnBuscar.setOnClickListener(v -> vm.buscar(
                etTitulo.getText().toString().trim(),
                etAutor.getText().toString().trim(),
                etAnio.getText().toString().trim(),
                etStock.getText().toString().trim(),
                etDescripcion.getText().toString().trim()
        ));

        btnAnterior.setOnClickListener(v -> vm.paginaAnterior());
        btnSiguiente.setOnClickListener(v -> vm.paginaSiguiente());

        vm.getResultados().observe(getViewLifecycleOwner(), adapter::actualizarLista);

        vm.getPaginaActual().observe(getViewLifecycleOwner(), actual -> {
            Integer total = vm.getTotalPaginas().getValue();
            if (total != null)
                tvPagina.setText("Página " + actual + " / " + total);

            btnAnterior.setEnabled(actual != null && actual > 1);
            btnSiguiente.setEnabled(total != null && actual < total);
        });

        vm.getTotalPaginas().observe(getViewLifecycleOwner(), total -> {
            Integer actual = vm.getPaginaActual().getValue();
            if (actual != null)
                tvPagina.setText("Página " + actual + " / " + total);

            btnAnterior.setEnabled(actual != null && actual > 1);
            btnSiguiente.setEnabled(total != null && actual < total);
        });

        return root;
    }
}

