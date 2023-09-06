package org.unse.incendiapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.unse.incendiapp.DB.AdminSQLiteOpenHelper;
import org.unse.incendiapp.Entidades.Usuario;
import org.unse.incendiapp.io.entidadesApi.DTOEventoResponse;
import org.unse.incendiapp.io.entidadesApi.DTOListadoGeneral;
import org.unse.incendiapp.databinding.ActivityMainBinding;
import org.unse.incendiapp.io.ApiMethods;
import org.unse.incendiapp.io.entidadesApi.UsuarioApi;
import org.unse.incendiapp.io.entidadesApi.loginDatosResponde;
import org.unse.incendiapp.io.entidadesApi.loginDatosEnvia;
import org.unse.incendiapp.reciclerViewAssets.AdaptadorDatos;
import org.unse.incendiapp.reciclerViewAssets.item_evento;


import java.security.MessageDigest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;


    /*
    CAMBIAR IP Y PUERTO DEPENDIENDO LA IP LOCAL DE LA PC, EL PUERTO EN LA API ES EL 8080
     */
    private final String ipApi = "192.168.1.19:8080";

    private Long idEventoTemp, idEventoModifica;

    private DTOEventoResponse evento, eventoModifica;

    private LocalDateTime fechaInicioIngreso, fechaFinIngreso;

    private Double latitudIngreso, longitudIngreso, latitudIngresoModifica, longitudIngresoModifica;

    private Integer tipoUsu = 0;

    private Usuario usuarioIngreso;

    private String nombreIngreso, contraseniaIngreso;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.listado_eventos, R.id.cuenta, R.id.administracion, R.id.fragment_cuenta_info, R.id.cambio_contrasenia, R.id.evento_info, R.id.agregarEvento, R.id.fragment_ubicacion_evento_mapa, R.id.modificar_evento, R.id.fragment_ubicacion_evento_entrada_modificacion)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        renovarToken();
    }

    public void onResume(){
        super.onResume();
    }

    public void RegistrarCuenta(View view) {
        String nombre, contrasenia;
        EditText etUsuario, etContrasenia;
        etUsuario = this.findViewById(R.id.et_nombreusuario);
        etContrasenia = this.findViewById(R.id.et_contraseniausuario);
        nombre = etUsuario.getText().toString();
        contrasenia = etContrasenia.getText().toString();
        UsuarioApi usuarioApi = new UsuarioApi( nombre, contrasenia, null);
        if (!(nombre.trim().isEmpty()) && !(contrasenia.trim().isEmpty())) {

            //Creamos una instancia de Retrofit
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl("http://" + ipApi + "/usuarios/alta/")
                    .addConverterFactory(GsonConverterFactory.create());
            Retrofit retrofit = builder.build();

            //Obtener cliente y crear la llamada para la peticion

            ApiMethods apiMethods = retrofit.create(ApiMethods.class);
            Call<UsuarioApi> llamada = apiMethods.createUser(usuarioApi);
            llamada.enqueue(new Callback<UsuarioApi>() {
                @Override
                public void onResponse(@NonNull Call<UsuarioApi> call, @NonNull Response<UsuarioApi> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Cuenta cargada", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Algo paso" , Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<UsuarioApi> call, @NonNull Throwable t) {
                    //No se por que va por aca, por las dudas continuamos
                    Toast.makeText(getApplicationContext(), "Cuenta cargada", Toast.LENGTH_LONG).show();
                    //Toast.makeText(getApplicationContext(), "Error" + t.getMessage(), Toast.LENGTH_LONG).show();
                    MainActivity.this.IniciarSesion(new View(MainActivity.this));
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Error: Nombre o contraseña vacios", Toast.LENGTH_LONG).show();
        }
    }


    //CHEQUEAR ACA, PODRIA SER QUE SE DEVUELVA EN LOGIN DATOS RESPONDE EL TIPO DE USUARIO, EL TEMA ES QUE NO ACTUALIZA EL OBTENER TIPO USUARIO
    public void IniciarSesion(View view) {
        EditText etUsuario, etContrasenia;
        etUsuario = this.findViewById(R.id.et_nombreusuario);
        etContrasenia = this.findViewById(R.id.et_contraseniausuario);
        nombreIngreso = etUsuario.getText().toString();
        contraseniaIngreso = etContrasenia.getText().toString();
        loginDatosEnvia datosEnvia = new loginDatosEnvia(nombreIngreso, contraseniaIngreso);
        //Aca encriptaremos la contraseña para enviar
        datosEnvia.setNombre(encriptarContraseña(datosEnvia.getNombre()));
        datosEnvia.setContrasenia(encriptarContraseña(datosEnvia.getContrasenia()));


        //Creamos una instancia de Retrofit
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://" + ipApi + "/usuarios/login/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        //Obtener cliente y crear la llamada para la peticion

        ApiMethods apiMethods = retrofit.create(ApiMethods.class);
        Call<loginDatosResponde> llamada = apiMethods.logIn(datosEnvia);
        llamada.enqueue(new Callback<loginDatosResponde>() {
            @Override
            public void onResponse(Call<loginDatosResponde> call, Response<loginDatosResponde> response) {
                if (response.isSuccessful()) {

                    Toast.makeText(getApplicationContext(), "Login exitoso", Toast.LENGTH_LONG).show();
                    //Conexion a DB
                    usuarioIngreso = new Usuario(Long.valueOf("1"), encriptarContraseña(nombreIngreso), encriptarContraseña(contraseniaIngreso), response.body().getToken(), null);
                    cargarUsuarioDB(usuarioIngreso, 1);
                    etUsuario.setText("");
                    etUsuario.setText("");

                } else {
                    Toast.makeText(getApplicationContext(), "Paso algo" + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<loginDatosResponde> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public Usuario getUsuarioIngreso(){
        return this.usuarioIngreso;
    }


    private String encriptarContraseña(String contrasenia) {
        String llave = "ClavePasajeContraseña";
        return encriptar(llave, contrasenia);
    }




    private String encriptar(String llave, String contrasenia) {
        try {
            SecretKeySpec keySpec = crearClave(llave);
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] cadena = contrasenia.getBytes("UTF-8");
            byte[] encriptacion = cipher.doFinal(cadena);
            return new String(Base64.getEncoder().encode(encriptacion));
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Paso algo" + e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        }
    }

    private SecretKeySpec crearClave(String llave) {
        try {
            byte[] cadena = llave.getBytes("UTF-8");
            MessageDigest mDigest = MessageDigest.getInstance("SHA-1");
            cadena = mDigest.digest(cadena);
            cadena = Arrays.copyOf(cadena, 16);
            SecretKeySpec keySpec = new SecretKeySpec(cadena, "AES");
            return keySpec;
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Paso algo con la clave" + e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        }
    }

    public void cerrarSesion(View view) {
        AdminSQLiteOpenHelper admin;
        admin = new AdminSQLiteOpenHelper(this, "adminUsuarios", null, 1);
        SQLiteDatabase DB = admin.getWritableDatabase();
        DB.execSQL("DELETE FROM usuarios");
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.action_fragment_cuenta_info_to_cuenta);
        usuarioIngreso = null;
        DB.close();
        Toast.makeText(getApplicationContext(), "LogOut exitoso", Toast.LENGTH_LONG).show();
    }

    private void cerrarSesionContrasenia() {
        AdminSQLiteOpenHelper admin;
        admin = new AdminSQLiteOpenHelper(this, "adminUsuarios", null, 1);
        SQLiteDatabase DB = admin.getWritableDatabase();
        DB.execSQL("DELETE FROM usuarios");
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.action_cambio_contrasenia_to_cuenta);
        DB.close();
    }

    public void irAcambiarContrasenia(View view) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.action_fragment_cuenta_info_to_cambio_contrasenia);
    }

    public void prepararCambioContrasenia(View view) {
        EditText etPass, etPassRepite;
        etPass = this.findViewById(R.id.et_passwordCambia);
        etPassRepite = this.findViewById(R.id.et_passwordRepite);
        if (etPass.getText().toString().trim() != "" && etPassRepite.getText().toString().trim() != "") {
            if (etPass.getText().toString().equals(etPassRepite.getText().toString())) {
                cambiarContrasenia();
                cerrarSesionContrasenia();
            }
        }

    }

    private void cambiarContrasenia() {
        EditText etPasswordCambia = this.findViewById(R.id.et_passwordCambia);
        String nombre = "", contrasenia = "";
        Usuario usu = obtenerUsuario();
       if (usu != null) {
                nombre = usu.getNombre();
                contrasenia = etPasswordCambia.getText().toString();
                Toast.makeText(getApplicationContext(), "Nombre y contra" + nombre + contrasenia, Toast.LENGTH_LONG).show();
                obtenerUsuario();
                loginDatosEnvia datosEnvia = new loginDatosEnvia(nombre, contrasenia);
                //Aca encriptaremos la contraseña para enviar
                datosEnvia.setNombre(encriptarContraseña(datosEnvia.getNombre()));
                datosEnvia.setContrasenia(encriptarContraseña(datosEnvia.getContrasenia()));


                //Creamos una instancia de Retrofit
                Retrofit.Builder builder = new Retrofit.Builder()
                        .baseUrl("http://" + ipApi + "/usuarios/actualizar/contrasenia/")
                        .addConverterFactory(GsonConverterFactory.create());
                Retrofit retrofit = builder.build();

                //Obtener cliente y crear la llamada para la peticion

                ApiMethods apiMethods = retrofit.create(ApiMethods.class);
                Call<UsuarioApi> llamada = apiMethods.updatePass(datosEnvia);
                llamada.enqueue(new Callback<UsuarioApi>() {
                    @Override
                    public void onResponse(Call<UsuarioApi> call, Response<UsuarioApi> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Contraseña actualizada" + response.message(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Paso algo" + response.message(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UsuarioApi> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Error" + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        }
    }

    /* ****************************************************************************************
    Evento - Listado
     */


    private void configurarRecyclerEventos(ArrayList<item_evento> listaItems){
        RecyclerView recyclerView = findViewById(R.id.rv_listaEventos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        AdaptadorDatos adaptadorDatos = new AdaptadorDatos(this, listaItems);
        adaptadorDatos.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iraEventoInfo(listaItems.get(recyclerView.getChildLayoutPosition(view)).getIdEvento());
            }
        });
        recyclerView.setAdapter(adaptadorDatos);
    }

    public void cargarListaEventosEnCurso() {
        //Creamos una instancia de Retrofit
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://" + ipApi + "/eventos/listado/generalencurso/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        //Obtener cliente y crear la llamada para la peticion

        ApiMethods apiMethods = retrofit.create(ApiMethods.class);
        Call<DTOListadoGeneral> llamada = apiMethods.listaEventosEnCurso();
        llamada.enqueue(new Callback<DTOListadoGeneral>() {
            @Override
            public void onResponse(Call<DTOListadoGeneral> call, Response<DTOListadoGeneral> response) {
                ArrayList<item_evento> listaItems = new ArrayList<>();
                if (response.isSuccessful()) {
                    List<DTOEventoResponse> listaEventos = response.body().getListaEventos();
                    for (int i = 0; i < listaEventos.size(); i++) {
                        item_evento e = null;
                        e = new item_evento(listaEventos.get(i).getId(), listaEventos.get(i).getTipo(), listaEventos.get(i).getUbicacionEvento(), listaEventos.get(i).getEsVerificado());
                        listaItems.add(e);
                    }
                    configurarRecyclerEventos(listaItems);
                } else {
                    Toast.makeText(getApplicationContext(), "No existen eventos en curso", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<DTOListadoGeneral> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void cargarListaAdministracion() {
        //Creamos una instancia de Retrofit
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://" + ipApi + "/eventos/listado/general/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        //Obtener cliente y crear la llamada para la peticion

        ApiMethods apiMethods = retrofit.create(ApiMethods.class);
        Call<DTOListadoGeneral> llamada = apiMethods.listaGeneralEventos();

        llamada.enqueue(new Callback<DTOListadoGeneral>() {
            @Override
            public void onResponse(Call<DTOListadoGeneral> call, Response<DTOListadoGeneral> response) {
                ArrayList<item_evento> listaItems = new ArrayList<>();
                if (response.isSuccessful()) {
                    List<DTOEventoResponse> listaEventos = response.body().getListaEventos();
                    for (int i = 0; i < listaEventos.size(); i++) {
                        item_evento e = null;
                        e = new item_evento(listaEventos.get(i).getId(), listaEventos.get(i).getTipo(), listaEventos.get(i).getUbicacionEvento(), listaEventos.get(i).getEsVerificado());
                        listaItems.add(e);
                    }
                    configurarRecyclerEventosAdministracion(listaItems);
                } else {
                    Toast.makeText(getApplicationContext(), "No existen eventos cargados", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<DTOListadoGeneral> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void iraEventoInfo(Long id) {
        //BUSCAR EVENTO Y GUARDAR EN VARIABLE
        idEventoTemp = id;
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.action_listado_eventos_to_evento_info);
        //navegar al fragmento
    }

    private String ArregloAutoridadesACadena(String[] autoridades) {
        //https://stackoverflow.com/questions/9053685/android-sqlite-saving-string-array
        //serializar
        String retorno = "";
        if(autoridades == null || autoridades.length==0){
            return retorno;
        }
        retorno = autoridades[0];
        for (int i = 1; i < autoridades.length; i++) {
            retorno = retorno +" - " +autoridades[i];
        }
        return retorno;
    }

    /* **************************************************************************************** */


    /* ****************************************************************************************
    Evento - Info
     */

    public void configurarVistaEventoInfo(){
        consultarDatosEvento(idEventoTemp);
    }

    private void consultarDatosEvento(Long id){

        //Creamos una instancia de Retrofit
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://" + ipApi + "/eventos/{id}/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        //Obtener cliente y crear la llamada para la peticion

        ApiMethods apiMethods = retrofit.create(ApiMethods.class);
        Call<DTOEventoResponse> llamada = apiMethods.obtenerEvento(id);
        llamada.enqueue(new Callback<DTOEventoResponse>() {
            @Override
            public void onResponse(Call<DTOEventoResponse> call, Response<DTOEventoResponse> response) {
                if(response.isSuccessful()) {
                    evento = new DTOEventoResponse(response.body().getId(), response.body().getTipo(), response.body().getCantVictimas() , response.body().getAutoridades(), response.body().getAreaInfluencia(), response.body().getUbicacionEvento(), response.body().getTiempoInicio(), response.body().getTiempoFin(), response.body().getUbiLatitud(), response.body().getUbiLongitud(), response.body().getEsVerificado());
                    cargarComponentesEventoInfo();
                }
            }
            @Override
            public void onFailure(Call<DTOEventoResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @SuppressWarnings("deprecation")
    private void cargarComponentesEventoInfo(){
        ImageView ivEventoInfo = findViewById(R.id.iv_eventoInfo);
        TextView tvTipo, tvUbicacion, tvCantidadVictimas, tvAutoridades, tvAreaInfluencia, tvFechaInicio, tvFechaFin;
        tvTipo = findViewById(R.id.tv_tipoEventoInfo);
        tvUbicacion = findViewById(R.id.tv_ubicacionEventoInfo);
        tvCantidadVictimas = findViewById(R.id.tv_cantidadVictimasEventoInfo);
        tvAutoridades = findViewById(R.id.tv_autoridadesEventoInfo);
        tvAreaInfluencia = findViewById(R.id.tv_areaInfluenciaEventoInfo);
        tvFechaInicio = findViewById(R.id.tv_fechaInicioEventoInfo);
        tvFechaFin = findViewById(R.id.tv_fechaFinEventoInfo);
        ImageView ivEsVerificadoInfo = findViewById(R.id.iv_esVerificadoInfo);
        TextView tvEsVerificadoInfo = findViewById(R.id.tv_esVerificadoInfo);
        switch (evento.getTipo()){
            case "Incendio":
                ivEventoInfo.setImageResource(R.drawable.fuego);
                break;
            case "Accidente":
                ivEventoInfo.setImageResource(R.drawable.accidente);
                break;
            case "Calzada en mal estado":
                ivEventoInfo.setImageResource(R.drawable.calzadamalestado);
                break;
            case "Derrumbe":
                ivEventoInfo.setImageResource(R.drawable.colapsocamino);
                break;
            case "Neblina":
                ivEventoInfo.setImageResource(R.drawable.fog);
                break;
            case "Estructura colapsada":
                ivEventoInfo.setImageResource(R.drawable.colapsoestructural);
                break;
            case "Incendio estructural":
                ivEventoInfo.setImageResource(R.drawable.building);
                break;
            case "Incendio forestal":
                ivEventoInfo.setImageResource(R.drawable.fire);
                break;
            case "Catastrofe":
                ivEventoInfo.setImageResource(R.drawable.desastre);
                break;
            case "Otro":
                ivEventoInfo.setImageResource(R.drawable.otro);
                break;
        }
        Date ti = new Date(evento.getTiempoInicio());
        if(evento.getTiempoFin() != null){
            Date tf = new Date(evento.getTiempoFin());
            LocalDateTime ldtTf = LocalDateTime.of(tf.getYear(), tf.getMonth(), tf.getDate(), tf.getHours(), tf.getMinutes());
            tvFechaFin.setText(ldtTf.toString());
        }else{
            tvFechaFin.setText("Evento en curso");
        }
        LocalDateTime ldtTi = LocalDateTime.of(ti.getYear(), ti.getMonth(), ti.getDate(), ti.getHours(), ti.getMinutes());
        tvTipo.setText(evento.getTipo());
        tvAutoridades.setText(ArregloAutoridadesACadena(evento.getAutoridades()));
        tvUbicacion.setText(evento.getUbicacionEvento());
        tvCantidadVictimas.setText(String.valueOf(evento.getCantVictimas()));
        tvAreaInfluencia.setText(evento.getAreaInfluencia());
        tvFechaInicio.setText(ldtTi.toString());
        if(evento.getEsVerificado()){
            ivEsVerificadoInfo.setVisibility(View.VISIBLE);
            tvEsVerificadoInfo.setVisibility(View.VISIBLE);
        }else{
            ivEsVerificadoInfo.setVisibility(View.INVISIBLE);
            tvEsVerificadoInfo.setVisibility(View.INVISIBLE);
        }
    }

    public void iraEventoMapa(View view){

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.action_evento_info_to_fragment_ubicacion_evento_mapa);
    }

    /* **************************************************************************************** */

        /* ****************************************************************************************
    Evento - Mapa
     */

    public DTOEventoResponse getEvento(){
        return this.evento;
    }

    public void setEvento(DTOEventoResponse e){
        this.evento = e;
    }

    public Long obtenerEventoTempId(){
        return this.idEventoTemp;
    }



/* **************************************************************************************** */

        /* ****************************************************************************************
    Evento - Listado administracion
     */

    public void configurarRecyclerEventosAdministracion(ArrayList<item_evento> listaItems){
        RecyclerView recyclerView = findViewById(R.id.rv_listadoAdministracion);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        AdaptadorDatos adaptadorDatos = new AdaptadorDatos(this, listaItems);
        adaptadorDatos.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {;
                irAModificarEvento(listaItems.get(recyclerView.getChildLayoutPosition(view)).getIdEvento());
            }
        });
        recyclerView.setAdapter(adaptadorDatos);
    }


    public void iraCrearEvento(View view){
        if(obtenerUsuario().getTipoUsuario() == 0) {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.action_administracion_to_agregarEvento);
            //navegar al fragmento
        }else{
            Toast.makeText(getApplicationContext(), "Validadores no pueden agregar eventos", Toast.LENGTH_SHORT).show();
        }
    }

    private void irAModificarEvento(Long idEvento){
        idEventoModifica = idEvento;
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.action_administracion_to_modificar_evento);
    }

    public void cargarListaAdministracionFiltrada(View view) {
        CheckBox cbTodos, cbEnCurso, cbVerificados;
        cbTodos = findViewById(R.id.cb_todos);
        cbEnCurso = findViewById(R.id.cb_solo_en_curso);
        cbVerificados = findViewById(R.id.cb_solo_verificados);
        Call<DTOListadoGeneral> llamada = null;
        if(cbTodos.isChecked()){
            //Creamos una instancia de Retrofit
            //listado general
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl("http://" + ipApi + "/eventos/listado/general/")
                    .addConverterFactory(GsonConverterFactory.create());
            Retrofit retrofit = builder.build();
            ApiMethods apiMethods = retrofit.create(ApiMethods.class);
            llamada = apiMethods.listaGeneralEventos();
            Toast.makeText(this, "Listado general", Toast.LENGTH_SHORT).show();
        } else if (cbEnCurso.isChecked() && cbVerificados.isChecked()) {
            //Creamos una instancia de Retrofit
            //listado verificados y en curso
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl("http://" + ipApi + "/eventos/listado/generalencurso/")
                    .addConverterFactory(GsonConverterFactory.create());
            Retrofit retrofit = builder.build();
            ApiMethods apiMethods = retrofit.create(ApiMethods.class);
            llamada = apiMethods.listaEventosEnCurso();
            Toast.makeText(this, "Eventos verificados y en curso", Toast.LENGTH_SHORT).show();
        } else if (cbEnCurso.isChecked() && !(cbVerificados.isChecked())) {
            //Creamos una instancia de Retrofit
            //listado no verificados y en curso
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl("http://" + ipApi + "/eventos/listado/generalencursosinverificar/")
                    .addConverterFactory(GsonConverterFactory.create());
            Retrofit retrofit = builder.build();
            ApiMethods apiMethods = retrofit.create(ApiMethods.class);
            llamada = apiMethods.listaEventosEnCursoSinVerificar(obtenerTokenUsuario());
            Toast.makeText(this, "Eventos sin verificar en curso", Toast.LENGTH_SHORT).show();
        } else if (!cbEnCurso.isChecked() && cbVerificados.isChecked()) {
            //Creamos una instancia de Retrofit
            //listado verificados
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl("http://" + ipApi + "/eventos/listado/verificados/")
                    .addConverterFactory(GsonConverterFactory.create());
            Retrofit retrofit = builder.build();
            ApiMethods apiMethods = retrofit.create(ApiMethods.class);
            llamada = apiMethods.listaEventosVerificados(obtenerTokenUsuario());
            Toast.makeText(this, "Eventos verificados", Toast.LENGTH_SHORT).show();
        }else{
            //Creamos una instancia de Retrofit
            //listado general
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl("http://" + ipApi + "/eventos/listado/general/")
                    .addConverterFactory(GsonConverterFactory.create());
            Retrofit retrofit = builder.build();
            ApiMethods apiMethods = retrofit.create(ApiMethods.class);
            llamada = apiMethods.listaGeneralEventos();
            Toast.makeText(this, "Listado general", Toast.LENGTH_SHORT).show();
        }


        //Obtener cliente y crear la llamada para la peticion

        llamada.enqueue(new Callback<DTOListadoGeneral>() {
            @Override
            public void onResponse(Call<DTOListadoGeneral> call, Response<DTOListadoGeneral> response) {
                ArrayList<item_evento> listaItems = new ArrayList<>();
                if (response.isSuccessful()) {
                    List<DTOEventoResponse> listaEventos = response.body().getListaEventos();
                    for (int i = 0; i < listaEventos.size(); i++) {
                        item_evento e = null;
                        e = new item_evento(listaEventos.get(i).getId(), listaEventos.get(i).getTipo(), listaEventos.get(i).getUbicacionEvento(), listaEventos.get(i).getEsVerificado());
                        listaItems.add(e);
                    }
                    configurarRecyclerEventosAdministracion(listaItems);
                } else {
                    Toast.makeText(getApplicationContext(), "No existen eventos cargados", Toast.LENGTH_LONG).show();
                    configurarRecyclerEventosAdministracion(listaItems);
                }
            }

            @Override
            public void onFailure(Call<DTOListadoGeneral> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }




        /* ****************************************************************************************
    Evento - Agregar evento
     */

    public Double getLatitudIngreso() {
        return latitudIngreso;
    }

    public void setLatitudIngreso(Double latitudIngreso) {
        this.latitudIngreso = latitudIngreso;
    }

    public Double getLongitudIngreso() {
        return longitudIngreso;
    }

    public void setLongitudIngreso(Double longitudIngreso) {
        this.longitudIngreso = longitudIngreso;
    }

    public void nuevoEvento(View view){
        AlertDialog dialog;
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setTitle("¿Ingresó bien los datos?");

        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String tipo, areaInfluencia, ubicacion;
                Double latitud = latitudIngreso, longitud = longitudIngreso;
                String[] autoridades;
                Integer cantVictimas;
                Long fechaInicio = null, fechafin = null;
                Date fi, ff;
                Spinner spListaTipo = findViewById(R.id.sp_listaTipo);
                Spinner splistaMedida = findViewById(R.id.sp_medidaAreaInfluencia);
                EditText etAreaInfluencia = findViewById(R.id.et_areaInfluencia);
                EditText etUbicacion = findViewById(R.id.et_ubicacion);
                EditText etCantVictimas = findViewById(R.id.et_cantVictimas);
                tipo = spListaTipo.getSelectedItem().toString();
                areaInfluencia = etAreaInfluencia.getText()+ " " + splistaMedida.getSelectedItem().toString();
                ubicacion  = etUbicacion.getText().toString();
                autoridades = cargarAutoridadesIngreso();
                cantVictimas = Integer.valueOf(etCantVictimas.getText().toString());
                if(fechaInicioIngreso!= null) {
                    fi = Date.from(fechaInicioIngreso.atZone(ZoneId.systemDefault()).toInstant());
                    fechaInicio = fi.getTime();
                    if(fechaFinIngreso!=null) {
                        ff = Date.from(fechaFinIngreso.atZone(ZoneId.systemDefault()).toInstant());
                        fechafin = ff.getTime();
                    }
                }
                if(!areaInfluencia.trim().isEmpty()){
                    if(!ubicacion.trim().isEmpty()){
                        if(latitud !=0){
                            if(longitud != 0){
                                if(cantVictimas>=0){
                                    if(fechaInicioIngreso!=null){
                                        DTOEventoResponse evento = new DTOEventoResponse(null, tipo,cantVictimas,autoridades,areaInfluencia,ubicacion,fechaInicio,fechafin,latitud,longitud, false);
                                        grabarEventoApi(evento);
                                        Toast.makeText(getApplicationContext(), "Evento agregado con exito", Toast.LENGTH_SHORT).show();
                                        limpiarCampos();
                                        volveraAdministracion();
                                    }else{
                                        //fechainiciovacia
                                        Toast.makeText(getApplicationContext(), "Ingrese fecha de inicio", Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    //cantVictimasvacia
                                    Toast.makeText(getApplicationContext(), "Ingrese cantidad de victimas", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                //longitudvacia
                                Toast.makeText(getApplicationContext(), "Ingrese longitud", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            //latitudvacia
                            Toast.makeText(getApplicationContext(), "Ingrese latitud", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        //ubicacionvacia
                        Toast.makeText(getApplicationContext(), "Ingrese ubicacion", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    //areainfluenciavacia
                    Toast.makeText(getApplicationContext(), "Ingrese area de influencia", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //No se hara nada
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    private void volveraAdministracion() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.action_agregarEvento_to_administracion);
    }

    private void limpiarCampos() {
        CheckBox cbPolicia, cbBomberos, cbParamedicos, cbGendarmeria, cbTecnicos, cbOtro;
        EditText etFechaInicio = findViewById(R.id.et_fechaInicio);
        EditText etFechaFin = findViewById(R.id.et_fechaFin);
        Spinner spListaTipo = findViewById(R.id.sp_listaTipo);
        Spinner splistaMedida = findViewById(R.id.sp_medidaAreaInfluencia);
        EditText etAreaInfluencia = findViewById(R.id.et_areaInfluencia);
        EditText etUbicacion = findViewById(R.id.et_ubicacion);
        EditText etCantVictimas = findViewById(R.id.et_cantVictimas);
        cbPolicia = findViewById(R.id.cb_policia);
        cbBomberos = findViewById(R.id.cb_bomberos);
        cbParamedicos = findViewById(R.id.cb_paramedicos);
        cbGendarmeria = findViewById(R.id.cb_gendarmeria);
        cbTecnicos = findViewById(R.id.cb_tecnicos);
        cbOtro = findViewById(R.id.cb_otros);
        cbPolicia.setChecked(false);
        cbBomberos.setChecked(false);
        cbParamedicos.setChecked(false);
        cbGendarmeria.setChecked(false);
        cbTecnicos.setChecked(false);
        cbOtro.setChecked(false);
        etFechaInicio.setText("");
        etFechaFin.setText("");
        etAreaInfluencia.setText("");
        etUbicacion.setText("");
        latitudIngreso = 0.0;
        longitudIngreso = 0.0;
        etCantVictimas.setText("");
        fechaInicioIngreso = null;
        fechaFinIngreso = null;
    }

    private void grabarEventoApi(DTOEventoResponse e){
        //Creamos una instancia de Retrofit
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://" + ipApi + "/eventos/alta/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        //Obtener cliente y crear la llamada para la peticion

        ApiMethods apiMethods = retrofit.create(ApiMethods.class);
        Call<DTOEventoResponse> llamada = apiMethods.altaEvento(obtenerTokenUsuario() ,e);
        llamada.enqueue(new Callback<DTOEventoResponse>() {
            @Override
            public void onResponse(Call<DTOEventoResponse> call, Response<DTOEventoResponse> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Evento dado de alta en API", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "No se pudo dar de alta en API" + response.message(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<DTOEventoResponse> call, Throwable t) {
                //Pasa por aqui, ver el backend
                // Toast.makeText(getApplicationContext(), "Error de conexion con API", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String obtenerTokenUsuario() {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getApplicationContext(), "adminUsuarios", null, 1);
        SQLiteDatabase DB = admin.getWritableDatabase(); //abrir el flujo para escribir en la db
        Cursor registro = DB.rawQuery("SELECT token FROM usuarios where idUsuario = 1", null);
        registro.moveToFirst();
        return registro.getString(0);
    }

    private String[] cargarAutoridadesIngreso() {
        CheckBox cbPolicia, cbBomberos, cbParamedicos, cbGendarmeria, cbTecnicos, cbOtro;
        cbPolicia = findViewById(R.id.cb_policia);
        cbBomberos = findViewById(R.id.cb_bomberos);
        cbParamedicos = findViewById(R.id.cb_paramedicos);
        cbGendarmeria = findViewById(R.id.cb_gendarmeria);
        cbTecnicos = findViewById(R.id.cb_tecnicos);
        cbOtro = findViewById(R.id.cb_otros);
        ArrayList<String> listaSeleccionados = new ArrayList<>();
        if (cbPolicia.isChecked()) {
            listaSeleccionados.add("Policia");
        }
        if (cbBomberos.isChecked()) {
            listaSeleccionados.add("Bomberos");
        }
        if (cbParamedicos.isChecked()) {
            listaSeleccionados.add("Paramedicos");
        }
        if (cbGendarmeria.isChecked()) {
            listaSeleccionados.add("Gendarmeria");
        }
        if (cbTecnicos.isChecked()) {
            listaSeleccionados.add("Tecnicos");
        }
        if (cbOtro.isChecked()) {
            listaSeleccionados.add("Otros");
        }
        String[] retorno = new String[listaSeleccionados.size()];
        retorno = listaSeleccionados.toArray(retorno);

        return retorno;
    }

    public void iniciarSpinnerTipo(){
        Spinner spinner = findViewById(R.id.sp_listaTipo);
        ArrayList<String> listaDeTipos = new ArrayList();
        listaDeTipos.add("Incendio");
        listaDeTipos.add("Accidente");
        listaDeTipos.add("Calzada en mal estado");
        listaDeTipos.add("Derrumbe");
        listaDeTipos.add("Neblina");
        listaDeTipos.add("Estructura colapsada");
        listaDeTipos.add("Incendio estructural");
        listaDeTipos.add("Incendio forestal");
        listaDeTipos.add("Catastrofe");
        listaDeTipos.add("Otro");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item, listaDeTipos);
        spinner.setAdapter(adapter);
    }

    public void iniciarSpinnerMedidaAreaInfluencia(){
        Spinner spinner = findViewById(R.id.sp_medidaAreaInfluencia);
        ArrayList<String> listaDeMedidas = new ArrayList();
        listaDeMedidas.add("metros");
        listaDeMedidas.add("kilometros");
        listaDeMedidas.add("hectareas");
        listaDeMedidas.add("metros cuadrados");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item, listaDeMedidas);
        spinner.setAdapter(adapter);
    }

    public void mostrarCalendario(View v){
        if(v.getId() == R.id.btn_fiDatePicker) {
            EditText etFechaInicio = findViewById(R.id.et_fechaInicio);
            DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    etFechaInicio.setText(String.valueOf(day) + "/" + String.valueOf(month - 1) + "/" + String.valueOf(year));
                    fechaInicioIngreso = LocalDateTime.of(year, month-1, day, 0,0);
                }
            }, LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue() - 1, LocalDateTime.now().getDayOfMonth());
            dialog.show();
        }else{
            EditText etFechaFin = findViewById(R.id.et_fechaFin);
            DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    etFechaFin.setText(String.valueOf(day) + "/" + String.valueOf(month - 1) + "/" + String.valueOf(year));
                    fechaFinIngreso = LocalDateTime.of(year, month-1, day, 0,0);
                }
            }, LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue() - 1, LocalDateTime.now().getDayOfMonth());
            dialog.show();
        }
    }

    public void mostrarReloj(View v) {
            if (v.getId() == R.id.btn_fiHourPicker && fechaInicioIngreso != null) {
                EditText etFechaInicio = findViewById(R.id.et_fechaInicio);
                TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        fechaInicioIngreso = LocalDateTime.of(fechaInicioIngreso.getYear(), fechaInicioIngreso.getMonthValue(), fechaInicioIngreso.getDayOfMonth(), hour, minute);
                        etFechaInicio.setText(String.valueOf(fechaInicioIngreso.getDayOfMonth()+"/"+fechaInicioIngreso.getMonthValue()+"/"+fechaInicioIngreso.getYear()+" "+fechaInicioIngreso.getHour()+":"+fechaInicioIngreso.getMinute()));
                    }
                }, LocalDateTime.now().getHour(), LocalDateTime.now().getMinute(), true);
                dialog.show();
            } else {
                if (fechaFinIngreso != null) {
                    EditText etFechaFin = findViewById(R.id.et_fechaFin);
                    TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                            fechaFinIngreso = LocalDateTime.of(fechaFinIngreso.getYear(), fechaInicioIngreso.getMonthValue(), fechaFinIngreso.getDayOfMonth(), hour, minute);
                            etFechaFin.setText(String.valueOf(fechaFinIngreso.getDayOfMonth()+"/"+fechaFinIngreso.getMonthValue()+"/"+fechaFinIngreso.getYear()+" "+fechaFinIngreso.getHour()+":"+fechaFinIngreso.getMinute()));
                        }
                    }, LocalDateTime.now().getHour(), LocalDateTime.now().getMinute(), true);
                    dialog.show();
                }
            }
    }

    public void iraUbicarEnMapa(View view) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.action_agregarEvento_to_fragment_ubicacion_evento_entrada_modificacion);
    }


            /* ****************************************************************************************
    Evento - Modificar evento
     */

    public Double getLatitudIngresoModifica() {
        return latitudIngresoModifica;
    }

    public void setLatitudIngresoModifica(Double latitudIngresoModifica) {
        this.latitudIngresoModifica = latitudIngresoModifica;
    }

    public Double getLongitudIngresoModifica() {
        return longitudIngresoModifica;
    }

    public void setLongitudIngresoModifica(Double longitudIngresoModifica) {
        this.longitudIngresoModifica = longitudIngresoModifica;
    }

    public void obtenerEventoModifica(){
        Long id = idEventoModifica;
        //Creamos una instancia de Retrofit
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://" + ipApi + "/eventos/{id}/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        //Obtener cliente y crear la llamada para la peticion

        ApiMethods apiMethods = retrofit.create(ApiMethods.class);
        Call<DTOEventoResponse> llamada = apiMethods.obtenerEvento(id);
        llamada.enqueue(new Callback<DTOEventoResponse>() {
            @Override
            public void onResponse(Call<DTOEventoResponse> call, Response<DTOEventoResponse> response) {
                if(response.isSuccessful()) {
                    eventoModifica = new DTOEventoResponse(response.body().getId(), response.body().getTipo(), response.body().getCantVictimas() , response.body().getAutoridades(), response.body().getAreaInfluencia(), response.body().getUbicacionEvento(), response.body().getTiempoInicio(), response.body().getTiempoFin(), response.body().getUbiLatitud(), response.body().getUbiLongitud(), response.body().getEsVerificado());
                    cargarControlesModificarEvento();
                }
            }
            @Override
            public void onFailure(Call<DTOEventoResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @SuppressWarnings("deprecation")
    private void cargarControlesModificarEvento(){
        ConstraintLayout layAdmin, layValidador;
        layAdmin = findViewById(R.id.lay_modif_admin);
        layValidador = findViewById(R.id.lay_modif_validador);
        if(obtenerUsuario().getTipoUsuario()==0) {
            // 0 = administrador
            layAdmin.setVisibility(View.VISIBLE);
            layValidador.setVisibility(View.INVISIBLE);
            try {
                fechaInicioIngreso = null;
                fechaFinIngreso = null;
                Button btnVerificacionEvento = findViewById(R.id.btn_verificacionEvento);
                Button btnVerificacionEventoValidador = findViewById(R.id.btn_verificacionEventoValidador);
                LocalDateTime fechaInicioModifica, fechaFinModifica;
                Date di, df;
                EditText etFechaInicioModifica = findViewById(R.id.et_fechaInicioModifica);
                EditText etFechaFinModifica = findViewById(R.id.et_fechaFinModifica);
                EditText etCantVictimasModifica = findViewById(R.id.et_cantVictimasModifica);
                EditText etUbicacion = findViewById(R.id.et_ubicacionModifica);
                latitudIngreso = eventoModifica.getUbiLatitud();
                longitudIngreso = eventoModifica.getUbiLongitud();
                btnVerificacionEvento.setVisibility(View.INVISIBLE);
                cargarSpinnerTipoModifica();
                configurarAreaInfluenciaModifica();
                configurarCheckBoxModificar();
                etCantVictimasModifica.setText(String.valueOf(eventoModifica.getCantVictimas()));
                etUbicacion.setText(eventoModifica.getUbicacionEvento());
                di = new Date(eventoModifica.getTiempoInicio());
                if (eventoModifica.getTiempoFin() != null) {
                    df = new Date(eventoModifica.getTiempoFin());
                    fechaFinModifica = LocalDateTime.of(df.getYear(), df.getMonth(), df.getDate(), df.getHours(), df.getMinutes());
                    etFechaFinModifica.setText(fechaFinModifica.toString());
                    fechaFinIngreso = fechaFinModifica;
                } else {
                    etFechaFinModifica.setText("");
                }
                fechaInicioModifica = LocalDateTime.of(di.getYear(), di.getMonth(), di.getDate(), di.getHours(), di.getMinutes());
                etFechaInicioModifica.setText(fechaInicioModifica.toString());
                fechaInicioIngreso = fechaInicioModifica;
                if (eventoModifica.getEsVerificado()) {
                    btnVerificacionEvento.setText("Quitar verificacion");
                    btnVerificacionEventoValidador.setText("Quitar verificacion");
                } else {
                    btnVerificacionEvento.setText("Verificar");
                    btnVerificacionEventoValidador.setText("Verificar");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            Button btnVerificacionEvento = findViewById(R.id.btn_verificacionEvento);
            Button btnVerificacionEventoValidador = findViewById(R.id.btn_verificacionEventoValidador);
            layAdmin.setVisibility(View.INVISIBLE);
            layValidador.setVisibility(View.VISIBLE);
            if (eventoModifica.getEsVerificado()) {
                btnVerificacionEvento.setText("Quitar verificacion");
                btnVerificacionEventoValidador.setText("Quitar verificacion");
            } else {
                btnVerificacionEvento.setText("Verificar");
                btnVerificacionEventoValidador.setText("Verificar");
            }
        }
    }

    public void setEventoModifica(DTOEventoResponse e){
        eventoModifica = e;
    }

    private void configurarAreaInfluenciaModifica() {
        EditText etAreaInfluenciaModifica;
        etAreaInfluenciaModifica = findViewById(R.id.et_areaInfluenciaModifica);
        StringBuilder sb = new StringBuilder();
        char[] cadenaAreaInfluencia = eventoModifica.getAreaInfluencia().toCharArray();
        String letras = "";
        String numeros = "";
        for (int i = 0; i < cadenaAreaInfluencia.length; i++) {
            if(Character.isLetter(cadenaAreaInfluencia[i])){
                letras = letras + cadenaAreaInfluencia[i];
            } else if (Character.isDigit(cadenaAreaInfluencia[i])) {
                numeros = numeros + cadenaAreaInfluencia[i];
            }
        }
        etAreaInfluenciaModifica.setText(numeros);
        configurarSpinnerAreaInfluenciaModifica(letras);
    }

    private void configurarSpinnerAreaInfluenciaModifica(String letras) {
        iniciarSpinnerMedidaAreaInfluenciaModifica();
        Spinner spTipo = findViewById(R.id.sp_medidaAreaInfluenciaModifica);
        switch (letras){
            case "metros":
                spTipo.setSelection(0);
                break;
            case "kilometros":
                spTipo.setSelection(1);
                break;
            case "hectareas":
                spTipo.setSelection(2);
                break;
            case "metros cuadrados":
                spTipo.setSelection(3);
                break;
        }

    }

    private void cargarSpinnerTipoModifica() {
        iniciarSpinnerTipoModifica();
        Spinner spTipo = findViewById(R.id.sp_listaTipoModifica);
        switch (eventoModifica.getTipo()){
            case "Incendio":
                spTipo.setSelection(0);
                break;
            case "Accidente":
                spTipo.setSelection(1);
                break;
            case "Calzada en mal estado":
                spTipo.setSelection(2);
                break;
            case "Derrumbe":
                spTipo.setSelection(3);
                break;
            case "Neblina":
                spTipo.setSelection(4);
                break;
            case "Estructura colapsada":
                spTipo.setSelection(5);
                break;
            case "Incendio estructural":
                spTipo.setSelection(6);
                break;
            case "Incendio forestal":
                spTipo.setSelection(7);
                break;
            case "Catastrofe":
                spTipo.setSelection(8);
                break;
            case "Otro":
                spTipo.setSelection(9);
                break;
        }
    }

    private void configurarCheckBoxModificar() {
        CheckBox cbPolicia, cbBomberos, cbParamedicos, cbOtro, cbGendarmeria, cbTecnicos;
        cbPolicia = findViewById(R.id.cb_policiaModifica);
        cbBomberos = findViewById(R.id.cb_bomberosModifica);
        cbParamedicos = findViewById(R.id.cb_paramedicosModifica);
        cbOtro = findViewById(R.id.cb_otrosModifica);
        cbGendarmeria = findViewById(R.id.cb_gendarmeriaModifica);
        cbTecnicos = findViewById(R.id.cb_tecnicosModifica);
        if (eventoModifica.getAutoridades()!=null&&eventoModifica.getAutoridades().length>0) {
            for (int i = 0; i < eventoModifica.getAutoridades().length; i++) {
                switch (eventoModifica.getAutoridades()[i]) {
                    case "Policia":
                        cbPolicia.setChecked(true);
                        break;
                    case "Bomberos":
                        cbBomberos.setChecked(true);
                        break;
                    case "Paramedicos":
                        cbParamedicos.setChecked(true);
                        break;
                    case "Otros":
                        cbOtro.setChecked(true);
                        break;
                    case "Gendarmeria":
                        cbGendarmeria.setChecked(true);
                        break;
                    case "Tecnicos":
                        cbTecnicos.setChecked(true);
                        break;
                }
            }
        }
    }

    public void iniciarSpinnerTipoModifica(){
        Spinner spinner = findViewById(R.id.sp_listaTipoModifica);
        ArrayList<String> listaDeTipos = new ArrayList();
        listaDeTipos.add("Incendio");
        listaDeTipos.add("Accidente");
        listaDeTipos.add("Calzada en mal estado");
        listaDeTipos.add("Derrumbe");
        listaDeTipos.add("Neblina");
        listaDeTipos.add("Estructura colapsada");
        listaDeTipos.add("Incendio estructural");
        listaDeTipos.add("Incendio forestal");
        listaDeTipos.add("Catastrofe");
        listaDeTipos.add("Otro");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item, listaDeTipos);
        spinner.setAdapter(adapter);
    }

    public void iniciarSpinnerMedidaAreaInfluenciaModifica(){
        Spinner spinner = findViewById(R.id.sp_medidaAreaInfluenciaModifica);
        ArrayList<String> listaDeMedidas = new ArrayList();
        listaDeMedidas.add("metros");
        listaDeMedidas.add("kilometros");
        listaDeMedidas.add("hectareas");
        listaDeMedidas.add("metros cuadrados");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item, listaDeMedidas);
        spinner.setAdapter(adapter);
    }


    public void editarEvento(View view){
        if(obtenerUsuario().getTipoUsuario() == 0) {
            String tipo, areaInfluencia, ubicacion;
            Double latitud = 0.0, longitud = 0.0;
            String[] autoridades;
            Integer cantVictimas;
            Long fechaInicio = null, fechafin = null;
            Date fi, ff;
            Spinner spListaTipo = findViewById(R.id.sp_listaTipoModifica);
            Spinner splistaMedida = findViewById(R.id.sp_medidaAreaInfluenciaModifica);
            EditText etAreaInfluencia = findViewById(R.id.et_areaInfluenciaModifica);
            EditText etUbicacion = findViewById(R.id.et_ubicacionModifica);
            EditText etCantVictimas = findViewById(R.id.et_cantVictimasModifica);
            tipo = spListaTipo.getSelectedItem().toString();
            areaInfluencia = etAreaInfluencia.getText() + " " + splistaMedida.getSelectedItem().toString();
            ubicacion = etUbicacion.getText().toString();
            latitud = latitudIngreso;
            longitud = longitudIngreso;
            autoridades = cargarAutoridadesModifica();
            cantVictimas = Integer.valueOf(etCantVictimas.getText().toString());
            if (fechaInicioIngreso != null) {
                fi = new Date(fechaInicioIngreso.getYear(), fechaInicioIngreso.getMonth().getValue(), fechaInicioIngreso.getDayOfMonth(), fechaInicioIngreso.getHour(), fechaInicioIngreso.getMinute());
                fechaInicio = fi.getTime();
                if (fechaFinIngreso != null) {
                    ff = new Date(fechaFinIngreso.getYear(), fechaFinIngreso.getMonth().getValue(), fechaFinIngreso.getDayOfMonth(), fechaFinIngreso.getHour(), fechaFinIngreso.getMinute());
                    fechafin = ff.getTime();
                } else {
                    fechafin = null;
                }
            }
            if(latitudIngresoModifica != null && longitudIngresoModifica != null) {
                if (latitudIngresoModifica != 0.0 && longitudIngresoModifica != 0.0) {
                    latitud = latitudIngresoModifica;
                    longitud = longitudIngresoModifica;
                }
            }else{
                latitudIngresoModifica = eventoModifica.getUbiLatitud();
                longitudIngresoModifica = eventoModifica.getUbiLongitud();
            }
            if (!areaInfluencia.trim().isEmpty()) {
                if (!ubicacion.trim().isEmpty()) {
                    if (latitud != 0) {
                        if (longitud != 0) {
                            if (cantVictimas >= 0) {
                                if (fechaInicioIngreso != null) {
                                    DTOEventoResponse evento = new DTOEventoResponse(eventoModifica.getId(), tipo, cantVictimas, autoridades, areaInfluencia, ubicacion, fechaInicio, fechafin, latitud, longitud, eventoModifica.getEsVerificado());
                                    grabarEventoApiModifica(evento);
                                    Toast.makeText(this, "Evento editado con exito", Toast.LENGTH_SHORT).show();
                                    limpiarCamposModifica();
                                    volveraAdministracionDesdeModificar();
                                } else {
                                    //fechainiciovacia
                                    Toast.makeText(getApplicationContext(), "Ingrese fecha de inicio", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                //cantVictimasvacia
                                Toast.makeText(getApplicationContext(), "Ingrese cantidad de victimas", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            //longitudvacia
                            Toast.makeText(getApplicationContext(), "Ingrese longitud", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        //latitudvacia
                        Toast.makeText(getApplicationContext(), "Ingrese latitud", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //ubicacionvacia
                    Toast.makeText(getApplicationContext(), "Ingrese ubicacion", Toast.LENGTH_SHORT).show();
                }
            } else {
                //areainfluenciavacia
                Toast.makeText(getApplicationContext(), "Ingrese area de influencia", Toast.LENGTH_SHORT).show();
            }
        }else{
            DTOEventoResponse evento = new DTOEventoResponse(eventoModifica.getId(), eventoModifica.getTipo(), eventoModifica.getCantVictimas(), eventoModifica.getAutoridades(), eventoModifica.getAreaInfluencia(), eventoModifica.getUbicacionEvento(), eventoModifica.getTiempoInicio(), eventoModifica.getTiempoFin(), eventoModifica.getUbiLatitud(), eventoModifica.getUbiLongitud(), eventoModifica.getEsVerificado());
            grabarEventoApiModifica(evento);
            Toast.makeText(this, "Evento editado con exito", Toast.LENGTH_SHORT).show();
            limpiarCamposModifica();
            volveraAdministracionDesdeModificar();
        }
    }

    public void verificacionEvento(View v){
        Button btnVerificacionEvento = findViewById(R.id.btn_verificacionEvento);
        Button btnVerificacionEventoValidador = findViewById(R.id.btn_verificacionEventoValidador);
        if(eventoModifica.getEsVerificado()){
            eventoModifica.setEsVerificado(false);
        }else{
            eventoModifica.setEsVerificado(true);
        }
        if(eventoModifica.getEsVerificado()){
            btnVerificacionEvento.setText("Quitar verificacion");
            btnVerificacionEventoValidador.setText("Quitar verificacion");
        }else{
            btnVerificacionEvento.setText("Verificar");
            btnVerificacionEventoValidador.setText("Verificar");
        }
    }


    public void mostrarCalendarioModifica(View v){
        if(v.getId() == R.id.btn_fiDatePickerModifica) {
            EditText etFechaInicio = findViewById(R.id.et_fechaInicioModifica);
            DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    etFechaInicio.setText(String.valueOf(day) + "/" + String.valueOf(month - 1) + "/" + String.valueOf(year));
                    fechaInicioIngreso = LocalDateTime.of(year, month-1, day, 0,0);
                }
            }, LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue() - 1, LocalDateTime.now().getDayOfMonth());
            dialog.show();
        }else{
            EditText etFechaFin = findViewById(R.id.et_fechaFinModifica);
            DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    etFechaFin.setText(String.valueOf(day) + "/" + String.valueOf(month - 1) + "/" + String.valueOf(year));
                    fechaFinIngreso = LocalDateTime.of(year, month-1, day, 0,0);
                }
            }, LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue() - 1, LocalDateTime.now().getDayOfMonth());
            dialog.show();
        }
    }

    public void mostrarRelojModifica(View v) {
        if (v.getId() == R.id.btn_fiHourPickerModifica && fechaInicioIngreso != null) {
            EditText etFechaInicio = findViewById(R.id.et_fechaInicioModifica);
            TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                    fechaInicioIngreso = LocalDateTime.of(fechaInicioIngreso.getYear(), fechaInicioIngreso.getMonthValue(), fechaInicioIngreso.getDayOfMonth(), hour, minute);
                    etFechaInicio.setText(String.valueOf(fechaInicioIngreso.getDayOfMonth()+"/"+fechaInicioIngreso.getMonthValue()+"/"+fechaInicioIngreso.getYear()+" "+fechaInicioIngreso.getHour()+":"+fechaInicioIngreso.getMinute()));
                }
            }, LocalDateTime.now().getHour(), LocalDateTime.now().getMinute(), true);
            dialog.show();
        } else {
            if (fechaFinIngreso != null) {
                EditText etFechaFin = findViewById(R.id.et_fechaFinModifica);
                TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        fechaFinIngreso = LocalDateTime.of(fechaFinIngreso.getYear(), fechaInicioIngreso.getMonthValue(), fechaFinIngreso.getDayOfMonth(), hour, minute);
                        etFechaFin.setText(String.valueOf(fechaFinIngreso.getDayOfMonth()+"/"+fechaFinIngreso.getMonthValue()+"/"+fechaFinIngreso.getYear()+" "+fechaFinIngreso.getHour()+":"+fechaFinIngreso.getMinute()));
                    }
                }, LocalDateTime.now().getHour(), LocalDateTime.now().getMinute(), true);
                dialog.show();
            }
        }
    }

    private void grabarEventoApiModifica(DTOEventoResponse e){
        //Creamos una instancia de Retrofit
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://" + ipApi + "/eventos/actualizacion/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        //Obtener cliente y crear la llamada para la peticion

        ApiMethods apiMethods = retrofit.create(ApiMethods.class);
        Call<DTOEventoResponse> llamada = apiMethods.modificarEvento(obtenerTokenUsuario() ,e);
        llamada.enqueue(new Callback<DTOEventoResponse>() {
            @Override
            public void onResponse(Call<DTOEventoResponse> call, Response<DTOEventoResponse> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Evento modificado en API", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "No se pudo modificar en API" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DTOEventoResponse> call, Throwable t) {
                //Viene para aca, hay que ver el back lo que responde
                //Toast.makeText(getApplicationContext(), "Error de conexion con API", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void limpiarCamposModifica() {
        CheckBox cbPolicia, cbBomberos, cbParamedicos, cbGendarmeria, cbTecnicos, cbOtro;
        EditText etFechaInicio = findViewById(R.id.et_fechaInicioModifica);
        EditText etFechaFin = findViewById(R.id.et_fechaFinModifica);
        EditText etAreaInfluencia = findViewById(R.id.et_areaInfluenciaModifica);
        EditText etUbicacion = findViewById(R.id.et_ubicacionModifica);
        EditText etCantVictimas = findViewById(R.id.et_cantVictimasModifica);
        cbPolicia = findViewById(R.id.cb_policiaModifica);
        cbBomberos = findViewById(R.id.cb_bomberosModifica);
        cbParamedicos = findViewById(R.id.cb_paramedicosModifica);
        cbGendarmeria = findViewById(R.id.cb_gendarmeriaModifica);
        cbTecnicos = findViewById(R.id.cb_tecnicosModifica);
        cbOtro = findViewById(R.id.cb_otrosModifica);
        cbPolicia.setChecked(false);
        cbBomberos.setChecked(false);
        cbParamedicos.setChecked(false);
        cbGendarmeria.setChecked(false);
        cbTecnicos.setChecked(false);
        cbOtro.setChecked(false);
        etFechaInicio.setText("");
        etFechaFin.setText("");
        etAreaInfluencia.setText("");
        etUbicacion.setText("");
        latitudIngreso = 0.0;
        longitudIngreso = 0.0;
        etCantVictimas.setText("");
        fechaInicioIngreso = null;
        fechaFinIngreso = null;
        longitudIngresoModifica = 0.0;
        longitudIngresoModifica = 0.0;
    }

    private void volveraAdministracionDesdeModificar() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.action_modificar_evento_to_administracion);
    }

    private String[] cargarAutoridadesModifica() {
        CheckBox cbPolicia, cbBomberos, cbParamedicos, cbGendarmeria, cbTecnicos, cbOtro;
        cbPolicia = findViewById(R.id.cb_policiaModifica);
        cbBomberos = findViewById(R.id.cb_bomberosModifica);
        cbParamedicos = findViewById(R.id.cb_paramedicosModifica);
        cbGendarmeria = findViewById(R.id.cb_gendarmeriaModifica);
        cbTecnicos = findViewById(R.id.cb_tecnicosModifica);
        cbOtro = findViewById(R.id.cb_otrosModifica);
        ArrayList<String> listaSeleccionados = new ArrayList<>();
        if (cbPolicia.isChecked()) {
            listaSeleccionados.add("Policia");
        }
        if (cbBomberos.isChecked()) {
            listaSeleccionados.add("Bomberos");
        }
        if (cbParamedicos.isChecked()) {
            listaSeleccionados.add("Paramedicos");
        }
        if (cbGendarmeria.isChecked()) {
            listaSeleccionados.add("Gendarmeria");
        }
        if (cbTecnicos.isChecked()) {
            listaSeleccionados.add("Tecnicos");
        }
        if (cbOtro.isChecked()) {
            listaSeleccionados.add("Otros");
        }
        String[] retorno = new String[listaSeleccionados.size()];
        retorno = listaSeleccionados.toArray(retorno);

        return retorno;
    }

    public void iraUbicarEnMapaModificar(View view) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.action_modificar_evento_to_fragment_ubicacion_evento_entrada_modificacion);
    }



            /* ****************************************************************************************
    Evento - Borrar evento
     */

    public void borrarEvento(View view){

        AlertDialog dialog;
        AlertDialog.Builder builder;

        builder = new AlertDialog.Builder(this);
        builder.setTitle("¿Eliminar evento?");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Creamos una instancia de Retrofit
                Retrofit.Builder builder = new Retrofit.Builder()
                        .baseUrl("http://" + ipApi + "/eventos/{id}/")
                        .addConverterFactory(GsonConverterFactory.create());
                Retrofit retrofit = builder.build();

                //Obtener cliente y crear la llamada para la peticion

                ApiMethods apiMethods = retrofit.create(ApiMethods.class);
                Call<DTOEventoResponse> llamada = apiMethods.borrarEvento(obtenerTokenUsuario() ,idEventoModifica);
                llamada.enqueue(new Callback<DTOEventoResponse>() {
                    @Override
                    public void onResponse(Call<DTOEventoResponse> call, Response<DTOEventoResponse> response) {
                        if(response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Evento eliminado con exito", Toast.LENGTH_SHORT).show();
                            limpiarCamposModifica();
                            volveraAdministracionDesdeModificar();
                        }else{
                            Toast.makeText(getApplicationContext(), "Evento eliminado con exito", Toast.LENGTH_SHORT).show();
                            limpiarCamposModifica();
                            volveraAdministracionDesdeModificar();
                        }
                    }
                    @Override
                    public void onFailure(Call<DTOEventoResponse> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Evento eliminado con exito", Toast.LENGTH_SHORT).show();
                        limpiarCamposModifica();
                        volveraAdministracionDesdeModificar();
                        t.printStackTrace();
                    }
                });
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // no hacer nada
            }
        });

        dialog = builder.create();
        dialog.show();
    }

    public String getIpApi() {
        return ipApi;
    }

//**************************************************************************************************
// RENOVAR TOKEN DE USUARIO CADA VEZ QUE INICIA LA APP

    private void renovarToken() {
        Usuario u = obtenerUsuario();
        if(u != null) {
            nombreIngreso = u.getNombre();
            contraseniaIngreso = u.getContrasenia();
            loginDatosEnvia datosEnvia = new loginDatosEnvia(nombreIngreso, contraseniaIngreso);
            //Aca encriptaremos la contraseña para enviar
            datosEnvia.setNombre(encriptarContraseña(datosEnvia.getNombre()));
            datosEnvia.setContrasenia(encriptarContraseña(datosEnvia.getContrasenia()));


            //Creamos una instancia de Retrofit
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl("http://" + ipApi + "/usuarios/login/")
                    .addConverterFactory(GsonConverterFactory.create());
            Retrofit retrofit = builder.build();

            //Obtener cliente y crear la llamada para la peticion

            ApiMethods apiMethods = retrofit.create(ApiMethods.class);
            Call<loginDatosResponde> llamada = apiMethods.logIn(datosEnvia);
            llamada.enqueue(new Callback<loginDatosResponde>() {
                @Override
                public void onResponse(Call<loginDatosResponde> call, Response<loginDatosResponde> response) {
                    if (response.isSuccessful()) {
                        //Conexion a DB
                        usuarioIngreso = new Usuario(Long.valueOf("1"), encriptarContraseña(nombreIngreso), encriptarContraseña(contraseniaIngreso), response.body().getToken(), null);
                        cargarUsuarioDB(usuarioIngreso, 0);
                        Toast.makeText(getApplicationContext(), "Token renovado", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Paso algo" + response.message(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<loginDatosResponde> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Error" + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void cargarUsuarioDB(Usuario usuario, int estado) {
        //ESTADO 1 = LOGIN
        //ESTADO 0 = RENOVACION TOKEN

        int estadoUsuario = estado;

        //Creamos una instancia de Retrofit
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://" + ipApi + "/usuarios/tipousuario/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        //Obtener cliente y crear la llamada para la peticion

        ApiMethods apiMethods = retrofit.create(ApiMethods.class);
        Call<UsuarioApi> llamada = apiMethods.obtenerTipoUsuario(usuario.getToken(), new UsuarioApi(usuario.getNombre(), usuario.getContrasenia(), null));
        llamada.enqueue(new Callback<UsuarioApi>() {
            @Override
            public void onResponse(Call<UsuarioApi> call, Response<UsuarioApi> response) {
                if(response.isSuccessful()) {
                    AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getApplicationContext(), "adminUsuarios", null, 1);
                    SQLiteDatabase DB = admin.getWritableDatabase(); //abrir el flujo para escribir en la db
                    ContentValues registro = new ContentValues();
                    registro.put("idUsuario", 1);
                    registro.put("nombre", desencriptarContrasenia(usuario.getNombre()));
                    registro.put("pass", desencriptarContrasenia(usuario.getContrasenia()));
                    registro.put("token", usuario.getToken());
                    registro.put("tipousuario", response.body().getTipoUsuario());
                    if(estadoUsuario == 1) {
                        DB.insert("usuarios", null, registro);
                        NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment_activity_main);
                        navController.navigate(R.id.action_cuenta_to_fragment_cuenta_info);
                        DB.close();
                    }else{
                        DB.update("usuarios",registro, null, null);
                        DB.close();
                    }
                }
            }

            @Override
            public void onFailure(Call<UsuarioApi> call, Throwable t) {

            }
        });
    }

    private String desencriptarContrasenia(String contrasenia) {
        String llave = "ClavePasajeContraseña";
        return desencriptar(llave, contrasenia);
    }

    private String desencriptar(String llave, String contrasenia) {
        try {
            SecretKeySpec keySpec = crearClave(llave);
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] cadena = Base64.getDecoder().decode(contrasenia);
            byte[] desencriptacion = cipher.doFinal(cadena);
            return new String(desencriptacion, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public Usuario obtenerUsuario() {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getApplicationContext(), "adminUsuarios", null, 1);
        SQLiteDatabase DB = admin.getWritableDatabase(); //abrir el flujo para escribir en la db
        Cursor registro = DB.rawQuery("SELECT * FROM usuarios where idUsuario = 1", null);
        Usuario u = null;
        if(registro.moveToFirst()){
            u = new Usuario(null, registro.getString(1), registro.getString(2), registro.getString(3), registro.getInt(4));
        }
        DB.close();
        return u;
    }

    //**********************************************************************************************


}





