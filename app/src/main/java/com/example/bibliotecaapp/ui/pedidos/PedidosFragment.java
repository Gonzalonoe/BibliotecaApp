package com.example.bibliotecaapp.ui.pedidos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bibliotecaapp.R;
import com.example.bibliotecaapp.models.Pedido;


public class PedidosFragment extends Fragment {

    private PedidosViewModel vm;
    private RecyclerView recyclerView;
    private PedidosAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_pedidos, container, false);
        recyclerView = root.findViewById(R.id.rvPedidos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new PedidosAdapter();
        recyclerView.setAdapter(adapter);

        vm = new ViewModelProvider(this).get(PedidosViewModel.class);

        vm.getPedidosLiveData().observe(getViewLifecycleOwner(), pedidos -> {
            if (pedidos != null && !pedidos.isEmpty()) {
                adapter.setPedidos(pedidos);
            } else {
            }
        });

        adapter.setOnPedidoAccionListener(new PedidosAdapter.OnPedidoAccionListener() {
            @Override
            public void onCancelarPedido(Pedido pedido) {
                vm.cancelarPedido(pedido.getId());
            }

            @Override
            public void onVerDetalles(Pedido pedido) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("pedidoSeleccionado", pedido);
                NavController navController = Navigation.findNavController(requireView());
                navController.navigate(R.id.detallePedidoFragment, bundle);
            }
        });

        vm.cargarPedidos();

        return root;
    }
}
