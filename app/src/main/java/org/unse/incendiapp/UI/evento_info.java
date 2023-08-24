package org.unse.incendiapp.UI;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unse.incendiapp.io.entidadesApi.DTOEventoResponse;
import org.unse.incendiapp.MainActivity;
import org.unse.incendiapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link evento_info#newInstance} factory method to
 * create an instance of this fragment.
 */
public class evento_info extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String ipApi = "192.168.1.12:8080";

    private DTOEventoResponse evento = null;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Long idEvento;

    private View v;

    public evento_info() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment evento_info.
     */
    // TODO: Rename and change types and number of parameters
    public static evento_info newInstance(String param1, String param2) {
        evento_info fragment = new evento_info();
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
        v = inflater.inflate(R.layout.fragment_evento_info, container, false);


        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).configurarVistaEventoInfo();
    //consultar datos y cargar
    }

}

