package com.example.bibliotecaapp.ui.usuarios;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bibliotecaapp.R;

public class UsuariosActivosFragment extends Fragment {

    private UsuariosActivosViewModel vm;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_usuarios_activos, container, false);

        RecyclerView recycler = root.findViewById(R.id.recyclerUsuariosActivos);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));

        vm = new ViewModelProvider(this).get(UsuariosActivosViewModel.class);

        vm.usuarios.observe(getViewLifecycleOwner(), lista -> {
            recycler.setAdapter(new UsuariosActivosAdapter(lista, vm));
        });

        vm.cargarUsuarios();

        return root;
    }
}
