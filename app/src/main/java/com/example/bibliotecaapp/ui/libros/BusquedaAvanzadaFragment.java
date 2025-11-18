package com.example.bibliotecaapp.ui.libros;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bibliotecaapp.R;

import java.util.ArrayList;

public class BusquedaAvanzadaFragment extends Fragment {

    private BusquedaAvanzadaViewModel vm;
    private LibrosAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_busqueda_avanzada, container, false);

        vm = new ViewModelProvider(this).get(BusquedaAvanzadaViewModel.class);

        EditText etTitulo = root.findViewById(R.id.etTituloFiltro);
        EditText etAutor = root.findViewById(R.id.etAutorFiltro);
        EditText etAnio = root.findViewById(R.id.etAnioFiltro);
        EditText etStock = root.findViewById(R.id.etStockFiltro);
        EditText etDescripcion = root.findViewById(R.id.etDescripcionFiltro);
        Button btnBuscar = root.findViewById(R.id.btnBuscarAvanzado);
        Button btnAnterior = root.findViewById(R.id.btnAnterior);
        Button btnSiguiente = root.findViewById(R.id.btnSiguiente);

        TextView tvPagina = root.findViewById(R.id.tvPagina);

        RecyclerView rv = root.findViewById(R.id.rvResultadosBusqueda);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new LibrosAdapter(new ArrayList<>(), this);
        rv.setAdapter(adapter);

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

        vm.getTextoPagina().observe(getViewLifecycleOwner(), tvPagina::setText);

        vm.getAnteriorEnabled().observe(getViewLifecycleOwner(), btnAnterior::setEnabled);
        vm.getSiguienteEnabled().observe(getViewLifecycleOwner(), btnSiguiente::setEnabled);

        vm.getMensaje().observe(getViewLifecycleOwner(),
                msg -> Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        );

        return root;
    }
}
