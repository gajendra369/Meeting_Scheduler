package com.example.madminiproject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
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
import java.util.Collections;
import java.util.Locale;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class fragment1 extends Fragment {
    EditText date, time, agenda;
    DataBaseConn dbc;
    Button btn,btn2;
    TextView reci;
    InternetAddress[] recipientAddress;
    ArrayList<Integer> rec= new ArrayList<Integer>();
    Calendar c = Calendar.getInstance();
    String[] req,emails;
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
        btn2=view.findViewById(R.id.btn3);
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
                if(mdate.length()!=0&&mTime.length()!=0&&mAgenda.length()!=0&&reci.getText().toString().length()!=0) {
                    Boolean insert = dbc.insertvalue(mdate, mTime, mAgenda);
                    if (insert == true) {
                        Toast.makeText(getActivity(), "Data Inserted", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getActivity(), "Data NOT Inserted", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getActivity(), "Fill all contents", Toast.LENGTH_SHORT).show();
                }
            }
        });
        reci.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("Range")
            @Override
            public void onClick(View v) {
                Cursor c = dbc.getr();
                int count = c.getCount();
                boolean[] selreci;
                c.moveToFirst();
                String[] recip=new String[count];
                recipientAddress = new InternetAddress[count];
                req=new String[count];
                emails=new String[count];
                if (count > 0) {
                    int i=0;
                    do {
                        req[i]=recip[i]=c.getString(c.getColumnIndex("name"));
                        i++;
                    } while (c.moveToNext());
                } else {
                    Toast.makeText(getActivity(), "No recipients", Toast.LENGTH_LONG).show();
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                selreci=new boolean[recip.length];
                // set title
                builder.setTitle("Select user");

                // set dialog non cancelable
                builder.setCancelable(false);

                builder.setMultiChoiceItems(recip, selreci, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        // check condition
                        if (b) {
                            // when checkbox selected
                            rec.add(i);
                            // Sort array list
                            Collections.sort(rec);
                        } else {

                            rec.remove(Integer.valueOf(i));
                        }
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int j = 0; j < rec.size(); j++) {
                            stringBuilder.append(recip[rec.get(j)]);
                            if (j != rec.size() - 1) {
                                stringBuilder.append(", ");
                            }
                        }
                        reci.setText(stringBuilder.toString());
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // use for loop
                        for (int j = 0; j < selreci.length; j++) {
                            selreci[j] = false;
                            rec.clear();
                            reci.setText("");
                        }
                    }
                });
                builder.show();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                @SuppressLint("Range")
                String uname = "myextest36@gmail.com";
                String pass = "igdinafbolmznfqa";
                String email;
                for(int j=0;j<req.length;j++) {
                    Cursor c = dbc.getemail(req[j]);
                    c.moveToFirst();
                    int count = c.getCount();
                    if (count > 0) {
                        email = c.getString(c.getColumnIndex("email"));
                        emails[j]=email;
                    } else {
                        Toast.makeText(getActivity(), "No recipients", Toast.LENGTH_LONG).show();
                    }
                }
                StringBuilder stringBuilder = new StringBuilder();
                for (int j = 0; j < emails.length; j++) {
                    stringBuilder.append(emails[j]);
                    if (j != emails.length - 1) {
                        stringBuilder.append(",");
                    }
                }
                email=stringBuilder.toString();

                Properties prop = new Properties();
                prop.put("mail.smtp.auth", "true");
                prop.put("mail.smtp.starttls.enable", "true");
                prop.put("mail.smtp.host", "smtp.gmail.com");
                prop.put("mail.smtp.port", "587");
                Session s = Session.getInstance(prop, new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(uname, pass);
                    }
                });
                try {
//                    int counter = 0;
//                    for (String recipient : emails) {
//                        recipientAddress[counter] = new InternetAddress(recipient.trim());
//                        counter++;
//                    }
                    agenda.setText(email);
                    Message m = new MimeMessage(s);
                    m.setFrom(new InternetAddress(uname));
                    m.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
                    m.setSubject("hi");
                    m.setText("hello");
                    Transport.send(m);
                    Toast.makeText(getContext(), "successful", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                }
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
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
        tm.show();}

}
