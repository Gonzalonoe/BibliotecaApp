package com.example.bibliotecaapp.request;

import android.content.Context;
import android.content.SharedPreferences;

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
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class ApiClient {
    public static String BASE_URL="http://127.0.0.1:5000/";


    public static InmoServicio getInmoServicio(){
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss") // formato exacto del backend
                .setLenient()
                .create();
        Retrofit retrofit =new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit.create(InmoServicio.class);
    }


    public interface InmoServicio{

        @POST("api/auth/login")
        Call<LoginResponse> login(@Body LoginRequest request);

        @GET("api/usuarios/perfil")
        Call<Usuario> obtenerPerfil(@Header("Authorization") String token);

        @GET("api/libros")
        Call<List<Libro>> obtenerLibros(@Header("Authorization") String token);

        @POST("api/usuarios/crear-lector")
        Call<Usuario> registrarUsuario(@Body Usuario usuario);

        @Multipart
        @POST("api/libros/crear")
        Call<Libro> crearLibro(
                @Header("Authorization") String token,
                @Part("titulo") RequestBody titulo,
                @Part("autor") RequestBody autor,
                @Part("anio") RequestBody anio,
                @Part("stock") RequestBody stock,
                @Part("descripcion") RequestBody descripcion,
                @Part MultipartBody.Part portada
        );

        @PUT("api/libros/{id}")
        Call<Libro> actualizarLibro(
                @Header("Authorization") String token,
                @Path("id") int id,
                @Body LibroUpdateRequest libroRequest
        );

        @DELETE("api/libros/{id}")
        Call<Void> eliminarLibro(@Header("Authorization") String token, @Path("id") int id);

        @GET("api/libros/buscar")
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
        @POST("api/reportes/crear")
        Call<Reporte> crearReporte(
                @Header("Authorization") String token,
                @Part("TituloLibro") RequestBody tituloLibro,
                @Part("Sinopsis") RequestBody sinopsis,
                @Part MultipartBody.Part ImagenPortada
        );

        @GET("api/reportes")
        Call<List<Reporte>> getReportes(@Header("Authorization") String token);

        @POST("api/pedidos/crear")
        Call<Pedido> crearPedido(
                @Header("Authorization") String token,
                @Body PedidoRequest pedido
        );

        @GET("api/pedidos/mios")
        Call<List<Pedido>> obtenerMisPedidos(
                @Header("Authorization") String token
        );

        @PUT("api/pedidos/{id}/cancelar")
        Call<Void> cancelarPedido(
                @Header("Authorization") String token,
                @Path("id") int id
        );

        @GET("api/pedidos/{id}")
        Call<Pedido> obtenerPedidoPorId(
                @Header("Authorization") String token,
                @Path("id") int id
        );

        @PUT("api/pedidos/{id}/devolver")
        Call<Pedido> devolverPedido(
                @Header("Authorization") String token,
                @Path("id") int id
        );

        @PUT("api/pedidos/{id}/estado")
        Call<Pedido> cambiarEstadoPedido(
                @Header("Authorization") String token,
                @Path("id") int id,
                @Body int nuevoEstado
        );


    }
    public static void guardarToken(Context context, String token) {
        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("token", token);
        editor.apply();
    }
    public static String leerToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        return sp.getString("token", null);
    }


    public static void guardarUsuario(Context context, Usuario usuario) {
        SharedPreferences sp = context.getSharedPreferences("Usuario.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String UsuarioJson = gson.toJson(usuario);
        editor.putString("Usuario", UsuarioJson);
        editor.apply();
    }

    public static Usuario leerUsuario(Context context) {
        SharedPreferences sp = context.getSharedPreferences("usuario.xml", Context.MODE_PRIVATE);
        String usuarioJson = sp.getString("Usuario", null);

        if (usuarioJson != null) {
            Gson gson = new Gson();
            return gson.fromJson(usuarioJson, Usuario.class);
        } else {
            return null;
        }
    }
}
