package org.unse.incendiapp.UI;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;


import org.unse.incendiapp.DB.AdminSQLiteOpenHelper;
import org.unse.incendiapp.Entidades.Usuario;
import org.unse.incendiapp.MainActivity;
import org.unse.incendiapp.R;
import org.unse.incendiapp.io.ApiMethods;
import org.unse.incendiapp.io.entidadesApi.loginDatosEnvia;
import org.unse.incendiapp.io.entidadesApi.loginDatosResponde;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_cuenta#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_cuenta extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View v;

    public fragment_cuenta() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_cuenta.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_cuenta newInstance(String param1, String param2) {
        fragment_cuenta fragment = new fragment_cuenta();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_cuenta, container, false);
        // Inflate the layout for this fragment
        configurarVista();
        return v;
    }
    private void configurarVista() {
        //Si existe una cuenta
        AdminSQLiteOpenHelper admin;
        admin = new AdminSQLiteOpenHelper(getActivity(), "adminUsuarios", null, 1);
        SQLiteDatabase DB = admin.getWritableDatabase();
        Cursor registro = DB.rawQuery("SELECT * FROM usuarios", null);
            if (registro.moveToFirst()) {
                NavController navController = Navigation.findNavController(this.getActivity(), R.id.nav_host_fragment_activity_main);
                navController.navigate(R.id.action_cuenta_to_fragment_cuenta_info);
                DB.close();
            }
            DB.close();
    }



}