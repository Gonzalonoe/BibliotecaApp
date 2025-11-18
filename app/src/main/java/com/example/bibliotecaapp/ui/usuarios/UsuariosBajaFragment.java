package com.example.bibliotecaapp.ui.usuarios;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bibliotecaapp.R;

public class UsuariosBajaFragment extends Fragment {

    private UsuariosBajaViewModel vm;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_usuarios_baja, container, false);

        RecyclerView recycler = root.findViewById(R.id.recyclerUsuariosBaja);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));

        vm = new ViewModelProvider(this).get(UsuariosBajaViewModel.class);

        vm.usuarios.observe(getViewLifecycleOwner(), lista -> {
            recycler.setAdapter(new UsuariosBajaAdapter(lista, vm));
        });

        vm.cargar();

        return root;
    }
}
