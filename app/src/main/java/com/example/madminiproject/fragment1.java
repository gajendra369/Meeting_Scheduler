package com.example.madminiproject;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class fragment1 extends Fragment {
    EditText date, time, agenda;
    DataBaseConn dbc;
    Button btn;
    TextView reci;
    boolean[] selectedLanguage;
    ArrayList<String> rec= new ArrayList<String>();
    Calendar c = Calendar.getInstance();
    int mY = c.get(Calendar.YEAR);
    int mM = c.get(Calendar.MONTH);
    int mD = c.get(Calendar.DAY_OF_MONTH);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment1, container, false);
        date = view.findViewById(R.id.txtDate);
        time = view.findViewById(R.id.txtTime);
        agenda = view.findViewById(R.id.txtAgenda);
        btn = view.findViewById(R.id.btn1);
        reci=view.findViewById(R.id.reci);
        dbc = new DataBaseConn(getContext());
        date.setInputType(0);
        time.setInputType(0);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datedailog();
            }
        });
        in();
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timedailog();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mdate, mTime, mAgenda;
                mdate = date.getText().toString();
                mTime = time.getText().toString();
                mAgenda = agenda.getText().toString();
                Boolean insert = dbc.insertvalue(mdate, mTime, mAgenda);
                if (insert == true) {
                    Toast.makeText(getActivity(), "Data Inserted", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getActivity(), "Data NOT Inserted", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
    private  void datedailog(){
        DatePickerDialog dt=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int y, int m, int d) {
                date.setText(String.format(Locale.getDefault(), "%02d/%02d/%04d", d, m+1,y));
            }
        },mY,mM,mD);
        dt.show();
    }
    private  void timedailog(){
        TimePickerDialog tm=new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int h, int m) {
                int hour = h % 12;
                if (hour == 0) hour = 12;
                String _AM_PM = (h > 12) ? "PM" : "AM";
                time.setText(String.format(Locale.getDefault(), "%02d:%02d %s", hour, m, _AM_PM));
            }

    },00,00,false);
        tm.show();

}

    public void in(){
        String s="a",n="a1";
        dbc = new DataBaseConn(getContext());
        Boolean insert = dbc.ins(s,n);
        //Boolean insert1 = dbc.ins("b","ben");
        if (insert == true) {
            Toast.makeText(getActivity(), "Data Inserted", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(getActivity(), "Data NOT Inserted", Toast.LENGTH_SHORT).show();
    }
//        int count = c.getCount();
//        c.moveToFirst();
//        if (count > 0) {
//            do {
//                res.append(c.getString(c.getColumnIndex("agenda")) + "\t" + "at" + "\t" + c.getString(c.getColumnIndex("time")));
//                res.append("\n");
//            } while (c.moveToNext());
//            Toast.makeText(getActivity(), res, Toast.LENGTH_LONG).show();
//        } else {
//            Toast.makeText(getActivity(), "No Meeting on This Day....", Toast.LENGTH_LONG).show();
//        }
//    }

}
