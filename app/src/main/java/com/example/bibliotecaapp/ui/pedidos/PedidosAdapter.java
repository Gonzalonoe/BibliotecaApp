package com.example.bibliotecaapp.ui.pedidos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bibliotecaapp.R;
import com.example.bibliotecaapp.models.Pedido;

import java.util.ArrayList;
import java.util.List;

public class PedidosAdapter extends RecyclerView.Adapter<PedidosAdapter.PedidoViewHolder> {

    private List<Pedido> pedidos = new ArrayList<>();
    private OnPedidoAccionListener listener;

    // 🔹 Interfaz para eventos del adapter
    public interface OnPedidoAccionListener {
        void onCancelarPedido(Pedido pedido);
        void onVerDetalles(Pedido pedido);
    }

    public void setOnPedidoAccionListener(OnPedidoAccionListener listener) {
        this.listener = listener;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pedido, parent, false);
        return new PedidoViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoViewHolder holder, int position) {
        Pedido pedido = pedidos.get(position);
        holder.bind(pedido, listener);
    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }

    static class PedidoViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvEstado, tvFecha, tvVencimiento;
        Button btnCancelar;

        public PedidoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTituloPedido);
            tvEstado = itemView.findViewById(R.id.tvEstadoPedido);
            tvFecha = itemView.findViewById(R.id.tvFechaPedido);
            tvVencimiento = itemView.findViewById(R.id.tvFechaVencimiento);
            btnCancelar = itemView.findViewById(R.id.btnCancelarPedido);
        }

        public void bind(Pedido pedido, OnPedidoAccionListener listener) {
            String titulo = (pedido.getLibro() != null)
                    ? pedido.getLibro().getTitulo()
                    : pedido.getTituloSolicitado();

            tvTitulo.setText(titulo);
            tvEstado.setText("Estado: " + getNombreEstado(pedido.getEstado()));

            String fechaPedido = pedido.getFechaPedido();
            String fechaVenc = pedido.getFechaVencimiento();

            if (fechaPedido != null && fechaPedido.length() >= 10) {
                fechaPedido = fechaPedido.substring(0, 10);
            }
            if (fechaVenc != null && fechaVenc.length() >= 10) {
                fechaVenc = fechaVenc.substring(0, 10);
            }

            tvFecha.setText("Pedido: " + (fechaPedido != null ? fechaPedido : "-"));
            tvVencimiento.setText("Vence: " + (fechaVenc != null ? fechaVenc : "-"));

            if (pedido.getEstado() == 0) {
                btnCancelar.setVisibility(View.VISIBLE);
                btnCancelar.setOnClickListener(v -> {
                    if (listener != null) listener.onCancelarPedido(pedido);
                });
            } else {
                btnCancelar.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onVerDetalles(pedido);
            });
        }

        private String getNombreEstado(int estado) {
            switch (estado) {
                case 0:
                    return "Pendiente";
                case 1:
                    return "Aprobado";
                case 2:
                    return "Prestado";
                case 3:
                    return "Devuelto";
                case 4:
                    return "Vencido";
                case 5:
                    return "Cancelado";
                default:
                    return "Desconocido";
            }
        }
    }
}
