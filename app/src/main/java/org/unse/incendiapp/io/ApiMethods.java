package org.unse.incendiapp.io;

import org.unse.incendiapp.Entidades.Usuario;
import org.unse.incendiapp.io.entidadesApi.DTOEventoResponse;
import org.unse.incendiapp.io.entidadesApi.DTOListadoGeneral;
import org.unse.incendiapp.Entidades.Evento;
import org.unse.incendiapp.io.entidadesApi.UsuarioApi;
import org.unse.incendiapp.io.entidadesApi.loginDatosResponde;
import org.unse.incendiapp.io.entidadesApi.loginDatosEnvia;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiMethods {


    @GET("/eventos/listado/general")
    Call<List<Evento>> getListaEventosGeneral();

    @POST("/usuarios/alta/")
    Call<UsuarioApi> createUser(@Body UsuarioApi user);

    @POST("/usuarios/login/")
    Call<loginDatosResponde> logIn(@Body loginDatosEnvia user);

    @PUT("/usuarios/actualizar/contrasenia/")
    Call<UsuarioApi> updatePass(@Body loginDatosEnvia user);

    @GET("/eventos/listado/general/")
    Call<DTOListadoGeneral> listaGeneralEventos();

    @GET("/eventos/{id}/")
    Call<DTOEventoResponse> obtenerEvento(@Path("id") Long id);

    @POST("/eventos/alta/")
    Call<DTOEventoResponse> altaEvento(@Header("Authorization") String token, @Body DTOEventoResponse evento);

    @PUT("/eventos/actualizacion/")
    Call<DTOEventoResponse> modificarEvento(@Header("Authorization") String token, @Body DTOEventoResponse evento);

    @DELETE("/eventos/baja/{id}/")
    Call<DTOEventoResponse> borrarEvento(@Header("Authorization") String token, @Path("id") Long id);

    @GET("/eventos/listado/generalencurso/")
    Call<DTOListadoGeneral> listaEventosEnCurso();

    @GET("/eventos/listado/generalencursosinverificar/")
    Call<DTOListadoGeneral> listaEventosEnCursoSinVerificar(@Header("Authorization") String token);

    @GET("/eventos/listado/verificados/")
    Call<DTOListadoGeneral> listaEventosVerificados(@Header("Authorization") String token);

    @POST("/usuarios/tipousuario/")
    Call<UsuarioApi> obtenerTipoUsuario(@Header("Authorization") String token, @Body UsuarioApi user);

    @POST("/eventos/alta/botonantipanico/")
    Call<DTOEventoResponse> altaEventoBotonAntipanico(@Body DTOEventoResponse evento);

    @GET("/eventos/listado/finalizados/")
    Call<DTOListadoGeneral> obtenerListadoFinalizados(@Header("Authorization") String token);

    @GET("/eventos/listado/ubicacion/{ubicacion}/")
    Call<DTOListadoGeneral> obtenerListadoPorUbicacion(@Header("Authorization") String token, @Path("ubicacion") String ubicacion);
}