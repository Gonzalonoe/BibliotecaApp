package com.example.bibliotecaapp.ui.libros;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bibliotecaapp.R;

public class LibrosFragment extends Fragment {

    private LibrosViewModel vm;
    private RecyclerView recyclerLibros;
    private Button btnBusquedaAvanzada, btnCargarLibro;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_libros, container, false);

        vm = new ViewModelProvider(this).get(LibrosViewModel.class);

        recyclerLibros = root.findViewById(R.id.recyclerLibros);
        btnBusquedaAvanzada = root.findViewById(R.id.btnBusquedaAvanzada);
        btnCargarLibro = root.findViewById(R.id.btnCargarLibro);

        recyclerLibros.setLayoutManager(new LinearLayoutManager(getContext()));

        vm.getLibros().observe(getViewLifecycleOwner(), libros -> {
            LibrosAdapter adapter = new LibrosAdapter(libros, LibrosFragment.this);
            recyclerLibros.setAdapter(adapter);
        });

        vm.getEsAdmin().observe(getViewLifecycleOwner(), esAdmin -> {
            btnCargarLibro.setVisibility(esAdmin ? View.VISIBLE : View.GONE);
        });

        btnBusquedaAvanzada.setOnClickListener(v -> {
            Navigation.findNavController(v)
                    .navigate(R.id.action_nav_libros_to_busquedaAvanzadaFragment);
        });


        btnCargarLibro.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_nav_libros_to_nav_cargar_libro);
        });

        vm.cargarLibros();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        vm.cargarLibros();
    }
}
