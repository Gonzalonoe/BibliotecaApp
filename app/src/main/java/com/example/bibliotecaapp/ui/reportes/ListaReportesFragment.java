package com.example.bibliotecaapp.ui.reportes;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bibliotecaapp.R;

import java.util.ArrayList;

public class ListaReportesFragment extends Fragment {

    private ListaReportesViewModel vm;
    private ReportesAdapter adapter;
    private RecyclerView rvTodos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_lista_reportes, container, false);

        rvTodos = root.findViewById(R.id.rvTodosReportes);
        rvTodos.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ReportesAdapter(new ArrayList<>());
        rvTodos.setAdapter(adapter);

        adapter.setOnReporteClickListener(id -> {
            Bundle bundle = new Bundle();
            bundle.putInt("reporteId", id);

            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
                    .navigate(R.id.action_listaReportesFragment_to_detalleReporteFragment, bundle);
        });


        vm = new ViewModelProvider(this).get(ListaReportesViewModel.class);

        vm.getReportes().observe(getViewLifecycleOwner(), reportes -> {
            if (reportes != null) {
                adapter.actualizarLista(reportes);
            }
        });

        return root;
    }
}
