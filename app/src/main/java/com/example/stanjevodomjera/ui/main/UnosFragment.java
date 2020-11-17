package com.example.stanjevodomjera.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.stanjevodomjera.R;

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
                String vodomjer = String.valueOf(stanjeVodomjeraEditText.getText());

                if (vodomjer.length() == 0)
                {
                    Toast.makeText(getActivity(),"UNESITE STANJE VODOMJERA",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Log.d("UNOS ", dan + " / " + mjesec + " / " + godina + " VODOMJER : " + vodomjer);
                    Toast.makeText(getActivity(),"UNOS SPREMLJEN",Toast.LENGTH_SHORT).show();
                }
            }
        });
         // Inflate the layout for this fragment
        return view;
    }
}