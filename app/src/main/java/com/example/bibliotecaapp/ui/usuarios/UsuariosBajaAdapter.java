package com.example.bibliotecaapp.ui.usuarios;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bibliotecaapp.R;
import com.example.bibliotecaapp.models.Usuario;

import java.util.List;

public class UsuariosBajaAdapter extends RecyclerView.Adapter<UsuariosBajaAdapter.ViewHolder> {

    private List<Usuario> lista;
    private UsuariosBajaViewModel vm;

    public UsuariosBajaAdapter(List<Usuario> lista, UsuariosBajaViewModel vm) {
        this.lista = lista;
        this.vm = vm;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_usuario_baja, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Usuario u = lista.get(position);

        holder.nombre.setText(u.getNombre());
        holder.email.setText(u.getEmail());
        holder.rol.setText("Rol: " + u.getRol());

        holder.btnReactivar.setOnClickListener(v ->
                vm.activar(Integer.parseInt(u.getId()), () -> {})
        );
    }

    @Override
    public int getItemCount() { return lista.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, email, rol;
        Button btnReactivar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.tvNombreUsuarioBaja);
            email = itemView.findViewById(R.id.tvEmailUsuarioBaja);
            rol = itemView.findViewById(R.id.tvRolUsuarioBaja);
            btnReactivar = itemView.findViewById(R.id.btnReactivarUsuario);
        }
    }
}

