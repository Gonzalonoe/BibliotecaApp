package com.example.bibliotecaapp.ui.reportes;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.bibliotecaapp.R;
import com.example.bibliotecaapp.models.Reporte;

public class DetalleReporteFragment extends Fragment {

    private DetalleReporteViewModel vm;
    private ImageView ivImagen;
    private TextView tvTitulo, tvSinopsis, tvUsuario, tvEmail, tvFecha, tvEstado;
    private Button btnAceptar, btnCancelar, btnEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_detalle_reporte, container, false);
        inicializar(root);

        vm = new ViewModelProvider(this).get(DetalleReporteViewModel.class);

        int reporteId = getArguments() != null
                ? getArguments().getInt("reporteId", -1)
                : -1;


        if (reporteId == -1) {
            Toast.makeText(getContext(), "Error al recibir el reporte", Toast.LENGTH_SHORT).show();
            return root;
        }


        String rol = requireContext()
                .getSharedPreferences("datos_usuario", Context.MODE_PRIVATE)
                .getString("rol", "");

        boolean esAdmin = rol.equals("1") || rol.equalsIgnoreCase("admin");

        if (!esAdmin) {
            btnAceptar.setVisibility(View.GONE);
            btnCancelar.setVisibility(View.GONE);
            btnEmail.setVisibility(View.GONE);
        }

        vm.cargarReporte(reporteId);

        vm.getReporte().observe(getViewLifecycleOwner(), this::mostrarDatos);

        vm.getMensaje().observe(getViewLifecycleOwner(),
                msg -> Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show()
        );

        btnAceptar.setOnClickListener(v -> vm.aceptarReporte(reporteId));
        btnCancelar.setOnClickListener(v -> vm.cancelarReporte(reporteId));


        btnEmail.setOnClickListener(v -> enviarEmail());

        return root;
    }

    private void inicializar(View root) {
        ivImagen = root.findViewById(R.id.ivPortadaReporte);
        tvTitulo = root.findViewById(R.id.tvTituloReporte);
        tvSinopsis = root.findViewById(R.id.tvSinopsisReporte);
        tvUsuario = root.findViewById(R.id.tvUsuarioReporte);
        tvEmail = root.findViewById(R.id.tvEmailReporte);
        tvFecha = root.findViewById(R.id.tvFechaReporte);
        tvEstado = root.findViewById(R.id.tvEstadoReporte);

        btnAceptar = root.findViewById(R.id.btnAceptar);
        btnCancelar = root.findViewById(R.id.btnCancelar);
        btnEmail = root.findViewById(R.id.btnEnviarEmail);
    }

    private void mostrarDatos(Reporte r) {
        if (r == null) return;

        Glide.with(this)
                .load(r.getImagenPortada())
                .placeholder(R.drawable.ic_menu_book)
                .into(ivImagen);

        tvTitulo.setText(r.getTituloLibro());
        tvSinopsis.setText(r.getSinopsis());
        tvUsuario.setText("Usuario: " + r.getUsuarioNombre());
        tvEmail.setText("Email: " + r.getUsuarioEmail());
        tvFecha.setText("Fecha: " + r.getFecha());

        String estadoStr =
                r.getEstado() == 0 ? "Pendiente" :
                        r.getEstado() == 1 ? "Aceptado" :
                                "Cancelado";

        tvEstado.setText("Estado: " + estadoStr);
    }

    private void enviarEmail() {
        Reporte r = vm.getReporte().getValue();
        if (r == null) return;

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + r.getUsuarioEmail()));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Respuesta a tu reporte");
        intent.putExtra(Intent.EXTRA_TEXT,
                "Hola " + r.getUsuarioNombre() + ",\n\nTe escribo sobre tu reporte enviado.\n");

        startActivity(intent);
    }
}
