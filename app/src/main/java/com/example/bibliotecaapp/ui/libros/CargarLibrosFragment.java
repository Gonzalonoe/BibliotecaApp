package com.example.bibliotecaapp.ui.libros;

import android.net.Uri;
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

import com.example.bibliotecaapp.R;
import com.example.bibliotecaapp.databinding.FragmentCargarLibrosBinding;
import com.example.bibliotecaapp.utils.PathUtil;

import java.io.File;

public class CargarLibrosFragment extends Fragment {

    private FragmentCargarLibrosBinding binding;
    private CargarLibrosViewModel viewModel;
    private Uri portadaUri;

    private final ActivityResultLauncher<String> seleccionarImagenLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    portadaUri = uri;
                    binding.ivPortadaPreview.setImageURI(uri);
                    viewModel.onPortadaSeleccionada(uri);
                }
            });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCargarLibrosBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(CargarLibrosViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.onPantallaCargarLibrosMostrada();

        getViewLifecycleOwner().getLifecycle().addObserver(new androidx.lifecycle.DefaultLifecycleObserver() {
            @Override
            public void onDestroy(@NonNull androidx.lifecycle.LifecycleOwner owner) {
                viewModel.onPantallaCargarLibrosCerrada();
            }
        });

        viewModel.getMensaje().observe(getViewLifecycleOwner(), msg ->
                android.widget.Toast.makeText(requireContext(), msg, android.widget.Toast.LENGTH_SHORT).show()
        );

        binding.btnSeleccionarPortada.setOnClickListener(v -> seleccionarImagenLauncher.launch("image/*"));
        binding.btnGuardarLibro.setOnClickListener(v -> {
            File portadaFile = portadaUri != null ? new File(PathUtil.getPath(requireContext(), portadaUri)) : null;
            viewModel.onGuardarClick(
                    binding.etTitulo.getText().toString(),
                    binding.etAutor.getText().toString(),
                    binding.etAnio.getText().toString(),
                    binding.etStock.getText().toString(),
                    binding.etDescripcion.getText().toString(),
                    portadaFile
            );
        });
    }
}

