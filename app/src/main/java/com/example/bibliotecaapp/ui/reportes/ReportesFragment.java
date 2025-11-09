package com.example.bibliotecaapp.ui.reportes;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bibliotecaapp.R;
import com.example.bibliotecaapp.models.Reporte;

import java.util.ArrayList;

public class ReportesFragment extends Fragment {

    // 🔹 ViewModel
    private ReportesViewModel vm;

    // 🔹 Vistas
    private EditText etTitulo;
    private EditText etSinopsis;
    private ImageView ivPreview;
    private RecyclerView rvReportes;
    private Uri imagenUri;
    private ReportesAdapter adapter;

    // 🔹 Lanzador para seleccionar imagen desde galería
    private final ActivityResultLauncher<String> seleccionarImagen =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    imagenUri = uri;
                    ivPreview.setImageURI(uri);
                }
            });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflar layout
        View root = inflater.inflate(R.layout.fragment_reportes, container, false);

        // ================================
        // 🧩 Inicialización de vistas
        // ================================
        etTitulo = root.findViewById(R.id.etTituloReporte);
        etSinopsis = root.findViewById(R.id.etSinopsisReporte);
        ivPreview = root.findViewById(R.id.ivPreviewPortada);
        Button btnSeleccionar = root.findViewById(R.id.btnSeleccionarImagen);
        Button btnEnviar = root.findViewById(R.id.btnEnviarReporte);
        rvReportes = root.findViewById(R.id.rvReportes);

        // ================================
        // 🧠 ViewModel
        // ================================
        vm = new ViewModelProvider(this).get(ReportesViewModel.class);

        // ================================
        // 🗂️ Configurar RecyclerView
        // ================================
        rvReportes.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ReportesAdapter(new ArrayList<Reporte>());
        rvReportes.setAdapter(adapter);

        // ================================
        // 👀 Observadores
        // ================================
        vm.getReportes().observe(getViewLifecycleOwner(), reportes -> {
            if (reportes != null) {
                adapter.actualizarLista(reportes);
            }
        });

        // ================================
        // 📸 Seleccionar imagen
        // ================================
        btnSeleccionar.setOnClickListener(v -> seleccionarImagen.launch("image/*"));

        // ================================
        // 🚀 Enviar reporte
        // ================================
        btnEnviar.setOnClickListener(v -> {
            String titulo = etTitulo.getText().toString().trim();
            String sinopsis = etSinopsis.getText().toString().trim();

            if (titulo.isEmpty() || sinopsis.isEmpty()) {
                Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            vm.enviarReporte(titulo, sinopsis, imagenUri);

            // ✅ Limpiar formulario tras envío
            etTitulo.setText("");
            etSinopsis.setText("");
            ivPreview.setImageResource(R.drawable.ic_menu_book);
            imagenUri = null;
        });

        // ================================
        // 📋 Cargar reportes existentes
        // ================================
        vm.cargarReportes();

        return root;
    }
}
