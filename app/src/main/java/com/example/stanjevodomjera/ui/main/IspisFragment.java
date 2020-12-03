package com.example.stanjevodomjera.ui.main;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.stanjevodomjera.DbHandler;
import com.example.stanjevodomjera.R;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static android.R.layout.simple_list_item_1;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IspisFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IspisFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DbHandler dbHandler;
    private ListView listViewIspis;

    public IspisFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IspisFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IspisFragment newInstance(String param1, String param2) {
        IspisFragment fragment = new IspisFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ispis, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        dbHandler = new DbHandler(getContext());
        ArrayList<String> items = dbHandler.GetEntries();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, items);
        ListView listView = getActivity().findViewById(R.id.listViewIspis);
        listView.setAdapter((ListAdapter) adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                String item  = (String) parent.getItemAtPosition(position);
                String[] item_split = item.split("              ");
                String datum = item_split[0];
                String stanje = item_split[1];

                dbHandler.DeleteEntry(datum,stanje);

                // delete entry from remote database
                // now try to store the entry to the remote DB via HTTP post
                String data = "command=DELETE&datum="+datum+"&stanje="+stanje;
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

                ArrayList<String> items = dbHandler.GetEntries();
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_list_item_1, android.R.id.text1, items);
                ListView listView = getActivity().findViewById(R.id.listViewIspis);
                listView.setAdapter((ListAdapter) adapter);

                Toast.makeText(getContext(), "BRISANJE USPJESNO", 1).show();

                return true;
            }
        });

        super.onActivityCreated(savedInstanceState);
    }
}