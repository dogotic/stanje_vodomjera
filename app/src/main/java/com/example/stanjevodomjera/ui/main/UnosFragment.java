package com.example.stanjevodomjera.ui.main;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.stanjevodomjera.DbHandler;
import com.example.stanjevodomjera.R;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UnosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UnosFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button unosButton;
    private EditText stanjeVodomjeraEditText;
    private DatePicker datePicker;

    public UnosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UnosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UnosFragment newInstance(String param1, String param2) {
        UnosFragment fragment = new UnosFragment();
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
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_unos, container, false);
        unosButton = view.findViewById(R.id.buttonUnos);
        datePicker = view.findViewById(R.id.date_picker);
        stanjeVodomjeraEditText = view.findViewById(R.id.editTextNumberStanjeVodomjera);
        unosButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // dohvati datum i stanje vodomjera
                String dan = String.valueOf(datePicker.getDayOfMonth());
                String mjesec = String.valueOf(datePicker.getMonth());
                String godina = String.valueOf(datePicker.getYear());
                String stanje = String.valueOf(stanjeVodomjeraEditText.getText());

                if (stanje.length() == 0)
                {
                    Toast.makeText(getActivity(),"UNESITE STANJE VODOMJERA",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String datum = dan + " / " + mjesec + " / " + godina;
                    Toast.makeText(getActivity(),"UNOS SPREMLJEN",Toast.LENGTH_SHORT).show();

                    ListView listView = getActivity().findViewById(R.id.listViewIspis);

                    int id = listView.getCount();
                    Log.d("INSERT","ID = " + id);

                    DbHandler dbHandler = new DbHandler(getContext());
                    dbHandler.insertEntry(id,datum,stanje);
                    ArrayList<String> items = dbHandler.GetEntries();

                    ArrayAdapter<String> adapter =
                            new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,items);
                    listView.setAdapter((ListAdapter) adapter);

                    // now try to store the entry to the remote DB via HTTP post
                    String data = "datum="+datum+"&stanje="+stanje;
                    URL url = null;
                    try {
                        url = new URL("http://cactus-iot.com.hr/vodomjer/unos_podatka.php");
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    HttpURLConnection con = null;
                    try {
                        con = (HttpURLConnection) url.openConnection();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        con.setRequestMethod("POST");
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    }
                    con.setDoOutput(true);
                    try {
                        con.getOutputStream().write(data.getBytes("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        con.getInputStream();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
         // Inflate the layout for this fragment
        return view;
    }
}