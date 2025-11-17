package com.example.bibliotecaapp.ui.libros;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.bibliotecaapp.databinding.FragmentCargarLibrosBinding;

public class CargarLibrosFragment extends Fragment {

    private FragmentCargarLibrosBinding binding;
    private CargarLibrosViewModel vm;

    private final ActivityResultLauncher<String> seleccionarImagenLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                vm.onPortadaSeleccionada(uri);
            });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCargarLibrosBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(this).get(CargarLibrosViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnSeleccionarPortada.setOnClickListener(v ->
                seleccionarImagenLauncher.launch("image/*")
        );

        binding.btnGuardarLibro.setOnClickListener(v ->
                vm.onGuardarClick(
                        binding.etTitulo.getText().toString(),
                        binding.etAutor.getText().toString(),
                        binding.etAnio.getText().toString(),
                        binding.etStock.getText().toString(),
                        binding.etDescripcion.getText().toString()
                )
        );

        vm.getPortadaPreviewUri().observe(getViewLifecycleOwner(),
                uri -> binding.ivPortadaPreview.setImageURI(uri)
        );

        vm.getMensaje().observe(getViewLifecycleOwner(),
                msg -> android.widget.Toast.makeText(requireContext(), msg, android.widget.Toast.LENGTH_SHORT).show()
        );

        vm.getLimpiarCampos().observe(getViewLifecycleOwner(), limpiar -> {
            if (limpiar != null && limpiar) {

                binding.etTitulo.setText("");
                binding.etAutor.setText("");
                binding.etAnio.setText("");
                binding.etStock.setText("");
                binding.etDescripcion.setText("");

                binding.ivPortadaPreview.setImageURI(null);

            }
        });
    }
}
