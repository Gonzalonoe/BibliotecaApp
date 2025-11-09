package com.example.bibliotecaapp.ui.pedidos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.bibliotecaapp.R;
import com.example.bibliotecaapp.models.Pedido;

public class DetallePedidoFragment extends Fragment {

    private DetallePedidoViewModel vm;
    private TextView tvTitulo, tvAutor, tvAnio, tvUsuario, tvEmail, tvEstado, tvFechaPedido, tvFechaVencimiento, tvObservaciones;
    private Button btnPendiente, btnPrestado, btnDevuelto, btnCancelado;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_detalle_pedido, container, false);
        inicializar(root);

        vm = new ViewModelProvider(this).get(DetallePedidoViewModel.class);

        // ✅ Recibir el pedido seleccionado
        if (getArguments() != null && getArguments().containsKey("pedidoSeleccionado")) {
            Pedido pedido = (Pedido) getArguments().getSerializable("pedidoSeleccionado");
            vm.setPedido(pedido);
        }

        // 🔹 Observadores
        vm.getPedido().observe(getViewLifecycleOwner(), this::mostrarDetalles);
        vm.getMensaje().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null) Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        });

        // 🟢 Acciones de los botones
        btnPendiente.setOnClickListener(v -> {
            Pedido pedido = vm.getPedido().getValue();
            if (pedido != null) vm.cambiarEstado(pedido.getId(), 0);
        });

        btnPrestado.setOnClickListener(v -> {
            Pedido pedido = vm.getPedido().getValue();
            if (pedido != null) vm.cambiarEstado(pedido.getId(), 2);
        });

        btnDevuelto.setOnClickListener(v -> {
            Pedido pedido = vm.getPedido().getValue();
            if (pedido != null) vm.devolverPedido(pedido.getId());
        });

        btnCancelado.setOnClickListener(v -> {
            Pedido pedido = vm.getPedido().getValue();
            if (pedido != null) vm.cambiarEstado(pedido.getId(), 5);
        });

        return root;
    }

    private void inicializar(View root) {
        tvTitulo = root.findViewById(R.id.tvTituloLibro);
        tvAutor = root.findViewById(R.id.tvAutorLibro);
        tvAnio = root.findViewById(R.id.tvAnioLibro);
        tvUsuario = root.findViewById(R.id.tvNombreUsuario);
        tvEmail = root.findViewById(R.id.tvEmailUsuario);
        tvEstado = root.findViewById(R.id.tvEstadoPedido);
        tvFechaPedido = root.findViewById(R.id.tvFechaPedido);
        tvFechaVencimiento = root.findViewById(R.id.tvFechaVencimiento);
        tvObservaciones = root.findViewById(R.id.tvObservaciones);

        // Botones de estado
        btnPendiente = root.findViewById(R.id.btnPendiente);
        btnPrestado = root.findViewById(R.id.btnPrestado);
        btnDevuelto = root.findViewById(R.id.btnDevuelto);
        btnCancelado = root.findViewById(R.id.btnCancelado);
    }

    private void mostrarDetalles(Pedido pedido) {
        if (pedido == null) return;

        // 📖 Datos del libro
        tvTitulo.setText("📖 " + (pedido.getLibro() != null ? pedido.getLibro().getTitulo() : pedido.getTituloSolicitado()));
        tvAutor.setText("✍️ " + (pedido.getLibro() != null ? pedido.getLibro().getAutor() : "-"));
        tvAnio.setText("📅 " + (pedido.getLibro() != null && pedido.getLibro().getAnio() != null ? pedido.getLibro().getAnio() : "-"));

        // 👤 Datos del usuario
        if (pedido.getUsuario() != null) {
            tvUsuario.setText("👤 " + pedido.getUsuario().getNombre());
            tvEmail.setText("📧 " + pedido.getUsuario().getEmail());
        } else {
            tvUsuario.setText("👤 -");
            tvEmail.setText("📧 -");
        }

        // 📦 Estado
        tvEstado.setText("📦 Estado: " + getNombreEstado(pedido.getEstado()));

        // 📆 Fechas
        String fechaPedido = pedido.getFechaPedido();
        String fechaVenc = pedido.getFechaVencimiento();

        if (fechaPedido != null && fechaPedido.length() >= 10)
            fechaPedido = fechaPedido.substring(0, 10);
        if (fechaVenc != null && fechaVenc.length() >= 10)
            fechaVenc = fechaVenc.substring(0, 10);

        tvFechaPedido.setText("📆 Pedido: " + (fechaPedido != null ? fechaPedido : "-"));
        tvFechaVencimiento.setText("⏳ Vence: " + (fechaVenc != null ? fechaVenc : "-"));

        // 📝 Observaciones
        tvObservaciones.setText("📝 " + (pedido.getObservaciones() != null ? pedido.getObservaciones() : "Sin observaciones"));
    }

    // 🔹 Traducción de estado
    private String getNombreEstado(int estado) {
        switch (estado) {
            case 0: return "Pendiente";
            case 1: return "Aprobado";
            case 2: return "Prestado";
            case 3: return "Devuelto";
            case 4: return "Vencido";
            case 5: return "Cancelado";
            default: return "Desconocido";
        }
    }
}
