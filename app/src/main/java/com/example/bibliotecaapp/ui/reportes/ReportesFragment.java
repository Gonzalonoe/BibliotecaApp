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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bibliotecaapp.R;
import com.example.bibliotecaapp.models.Reporte;

import java.util.ArrayList;

public class ReportesFragment extends Fragment {

    private ReportesViewModel vm;
    private EditText etTitulo;
    private EditText etSinopsis;
    private ImageView ivPreview;
    private RecyclerView rvReportes;
    private Uri imagenUri;
    private ReportesAdapter adapter;
    private Button btnVerTodos;

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

        View root = inflater.inflate(R.layout.fragment_reportes, container, false);

        etTitulo = root.findViewById(R.id.etTituloReporte);
        etSinopsis = root.findViewById(R.id.etSinopsisReporte);
        ivPreview = root.findViewById(R.id.ivPreviewPortada);
        Button btnSeleccionar = root.findViewById(R.id.btnSeleccionarImagen);
        Button btnEnviar = root.findViewById(R.id.btnEnviarReporte);
        btnVerTodos = root.findViewById(R.id.btnVerTodosReportes);
        rvReportes = root.findViewById(R.id.rvReportes);

        vm = new ViewModelProvider(this).get(ReportesViewModel.class);

        rvReportes.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ReportesAdapter(new ArrayList<>());
        rvReportes.setAdapter(adapter);


        adapter.setOnReporteClickListener(id -> {
            Bundle bundle = new Bundle();
            bundle.putInt("reporteId", id);

            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
                    .navigate(R.id.detalleReporteFragment, bundle);
        });

        vm.getReportes().observe(getViewLifecycleOwner(), reportes -> {
            if (reportes != null) {
                adapter.actualizarLista(reportes);
            }
        });

        vm.getEsAdmin().observe(getViewLifecycleOwner(), esAdmin -> {
            btnVerTodos.setVisibility(esAdmin ? View.VISIBLE : View.GONE);
        });

        vm.getNavegarVerTodos().observe(getViewLifecycleOwner(), navegar -> {
            if (navegar) {
                Navigation.findNavController(root)
                        .navigate(R.id.action_reportesFragment_to_listaReportesFragment);
                vm.resetNavegacion();
            }
        });

        btnSeleccionar.setOnClickListener(v -> seleccionarImagen.launch("image/*"));

        btnEnviar.setOnClickListener(v -> {
            String titulo = etTitulo.getText().toString().trim();
            String sinopsis = etSinopsis.getText().toString().trim();

            if (titulo.isEmpty() || sinopsis.isEmpty()) {
                Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            vm.enviarReporte(titulo, sinopsis, imagenUri);

            etTitulo.setText("");
            etSinopsis.setText("");
            ivPreview.setImageResource(R.drawable.ic_menu_book);
            imagenUri = null;
        });

        btnVerTodos.setOnClickListener(v -> vm.onVerTodosClicked());

        vm.cargarReportes();

        return root;
    }
}
