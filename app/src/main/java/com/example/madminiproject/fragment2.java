package com.example.madminiproject;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;
import java.util.Locale;

public class fragment2 extends Fragment {
    EditText date;
    Button btn1;
    DataBaseConn dbc;
    Calendar c = Calendar.getInstance();
    int mY = c.get(Calendar.YEAR);
    int mM = c.get(Calendar.MONTH);
    int mD = c.get(Calendar.DAY_OF_MONTH);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment2, container, false);
        date = view.findViewById(R.id.editTextDate);
        btn1 = view.findViewById(R.id.btn2);
        dbc = new DataBaseConn(getContext());
        date.setInputType(0);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datedailog();
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("Range")
            @Override
            public void onClick(View v) {
                String d1 = date.getText().toString();
                StringBuffer res = new StringBuffer();
                Cursor c = dbc.fetch(d1);
                int count = c.getCount();
                c.moveToFirst();
                if (count > 0) {
                    do {
                        res.append(c.getString(c.getColumnIndex("agenda")) + "\t" + "at" + "\t" + c.getString(c.getColumnIndex("time")));
                        res.append("\n");
                    } while (c.moveToNext());
                    Toast.makeText(getActivity(), res, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "No Meeting on This Day....", Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
}