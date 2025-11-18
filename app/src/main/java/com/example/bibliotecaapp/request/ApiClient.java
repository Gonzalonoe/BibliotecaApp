package com.example.bibliotecaapp.request;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.bibliotecaapp.models.CambiarPasswordRequest;
import com.example.bibliotecaapp.models.Libro;
import com.example.bibliotecaapp.models.LibroUpdateRequest;
import com.example.bibliotecaapp.models.LoginRequest;
import com.example.bibliotecaapp.models.LoginResponse;
import com.example.bibliotecaapp.models.Pedido;
import com.example.bibliotecaapp.models.PedidoRequest;
import com.example.bibliotecaapp.models.Reporte;
import com.example.bibliotecaapp.models.Usuario;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class ApiClient {

    public static String BASE_URL = "http://127.0.0.1:5000/api/";

    public static InmoServicio getInmoServicio() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(InmoServicio.class);
    }

    public interface InmoServicio {

        @POST("auth/login")
        Call<LoginResponse> login(@Body LoginRequest request);

        @GET("usuarios/perfil")
        Call<Usuario> obtenerPerfil(@Header("Authorization") String token);

        @POST("usuarios/crear-lector")
        Call<Usuario> registrarUsuario(@Body Usuario usuario);

        @GET("libros")
        Call<List<Libro>> obtenerLibros(@Header("Authorization") String token);

        @Multipart
        @POST("libros/crear")
        Call<Libro> crearLibro(
                @Header("Authorization") String token,
                @Part("titulo") RequestBody titulo,
                @Part("autor") RequestBody autor,
                @Part("anio") RequestBody anio,
                @Part("stock") RequestBody stock,
                @Part("descripcion") RequestBody descripcion,
                @Part MultipartBody.Part portada
        );

        @PUT("libros/{id}")
        Call<Libro> actualizarLibro(
                @Header("Authorization") String token,
                @Path("id") int id,
                @Body LibroUpdateRequest libroRequest
        );

        @DELETE("libros/{id}")
        Call<Void> eliminarLibro(@Header("Authorization") String token, @Path("id") int id);

        @GET("libros/buscar")
        Call<Map<String, Object>> buscarLibrosPaginados(
                @Header("Authorization") String token,
                @Query("titulo") String titulo,
                @Query("autor") String autor,
                @Query("anio") Integer anio,
                @Query("stock") Integer stock,
                @Query("descripcion") String descripcion,
                @Query("page") int page,
                @Query("pageSize") int pageSize
        );


        @Multipart
        @POST("reportes/crear")
        Call<Reporte> crearReporte(
                @Header("Authorization") String token,
                @Part("TituloLibro") RequestBody tituloLibro,
                @Part("Sinopsis") RequestBody sinopsis,
                @Part MultipartBody.Part ImagenPortada
        );

        @GET("reportes")
        Call<List<Reporte>> getReportes(@Header("Authorization") String token);


        @POST("pedidos/crear")
        Call<Pedido> crearPedido(
                @Header("Authorization") String token,
                @Body PedidoRequest pedido
        );

        @GET("pedidos/mios")
        Call<List<Pedido>> obtenerMisPedidos(@Header("Authorization") String token);

        @PUT("pedidos/{id}/cancelar")
        Call<Void> cancelarPedido(
                @Header("Authorization") String token,
                @Path("id") int id
        );

        @GET("pedidos/{id}")
        Call<Pedido> obtenerPedidoPorId(
                @Header("Authorization") String token,
                @Path("id") int id
        );

        @PUT("pedidos/{id}/devolver")
        Call<Pedido> devolverPedido(
                @Header("Authorization") String token,
                @Path("id") int id
        );

        @PUT("pedidos/{id}/estado")
        Call<Pedido> cambiarEstadoPedido(
                @Header("Authorization") String token,
                @Path("id") int id,
                @Body int nuevoEstado
        );

        @GET("pedidos")
        Call<List<Pedido>> obtenerTodosLosPedidos(@Header("Authorization") String token);

        @PUT("reportes/{id}/aceptar")
        Call<Reporte> aceptarReporte(@Header("Authorization") String token, @Path("id") int id);

        @PUT("reportes/{id}/cancelar")
        Call<Reporte> cancelarReporte(@Header("Authorization") String token, @Path("id") int id);

        @GET("reportes/{id}")
        Call<Reporte> obtenerReportePorId(@Header("Authorization") String token, @Path("id") int id);

        @POST("auth/cambiar-password")
        Call<Void> cambiarPassword(@Body CambiarPasswordRequest req);


    }
    public static void guardarToken(Context context, String token) {
        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        sp.edit().putString("token", token).apply();
    }

    public static String leerToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        return sp.getString("token", null);
    }

    public static Usuario leerUsuario(Context context) {
        SharedPreferences sp = context.getSharedPreferences("Usuario.xml", Context.MODE_PRIVATE);
        String usuarioJson = sp.getString("Usuario", null);

        if (usuarioJson != null) {
            return new Gson().fromJson(usuarioJson, Usuario.class);
        } else {
            return null;
        }
    }
}
