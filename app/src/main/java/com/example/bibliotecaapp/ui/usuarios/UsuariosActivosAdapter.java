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

public class UsuariosActivosAdapter extends RecyclerView.Adapter<UsuariosActivosAdapter.ViewHolder> {

    private List<Usuario> lista;
    private UsuariosActivosViewModel vm;

    public UsuariosActivosAdapter(List<Usuario> lista, UsuariosActivosViewModel vm) {
        this.lista = lista;
        this.vm = vm;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_usuario_activo, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Usuario u = lista.get(position);

        holder.nombre.setText(u.getNombre());
        holder.email.setText(u.getEmail());
        holder.rol.setText("Rol: " + u.getRol());

        holder.btnBaja.setOnClickListener(v ->
                vm.darDeBaja(Integer.parseInt(u.getId()), () -> {})
        );
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, email, rol;
        Button btnBaja;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.tvNombreUsuarioActivo);
            email = itemView.findViewById(R.id.tvEmailUsuarioActivo);
            rol = itemView.findViewById(R.id.tvRolUsuarioActivo);
            btnBaja = itemView.findViewById(R.id.btnDarDeBaja);
        }
    }
}
