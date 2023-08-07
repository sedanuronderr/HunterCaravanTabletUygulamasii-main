package com.caravan.huntercaravantabletuygulamasii.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.ClipDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;


import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.window.SplashScreen;

import com.caravan.huntercaravantabletuygulamasii.MainActivity;
import com.caravan.huntercaravantabletuygulamasii.R;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class AnasayfaFragment extends Fragment {
    private Handler handler = new Handler();
    public static boolean enable_dt_view;
    Thread Thread_refresh = null;
    TextView cl_water_txt,dt_water_txt,vbatt_txt,vbatt_perc_txt,tin_txt, tout_txt;
    KullaniciUygulamaAyarlari kullaniciUygulamaAyarlari;

    ClipDrawable drawable1,drawable;

  int akugelensayi= 15;
  int temizsugelensayi= 5;
    TextView saat;
    TextView dakika;
    TextView Gun,guntext;
    TextView Ay;
    TextView Yil,atiksuanasayfa;
    String[] splitDate;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_anasayfa, container, false);

    }





    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ConstraintLayout layout = view.findViewById(R.id.layoutscreen);
        ImageView akuimage =  view.findViewById(R.id.akudolum);
        ImageView temizsuimage = view.findViewById(R.id.temizsudolum);
        saat = view.findViewById(R.id.saat);
        dakika = view.findViewById(R.id.dk);
        Gun = view.findViewById(R.id.gun);
        Ay = view.findViewById(R.id.ay);
        Yil = view.findViewById(R.id.yil);
        guntext = view.findViewById(R.id.guntext);


        //**************************************************






       //**********************************************

        cl_water_txt=(TextView)view.findViewById(R.id.textView67);
        dt_water_txt=(TextView)view.findViewById(R.id.atiksuanasayfa);
        vbatt_txt=(TextView)view.findViewById(R.id.textView57);
        vbatt_perc_txt=(TextView)view.findViewById(R.id.textView32);
        tin_txt=(TextView)view.findViewById(R.id.textView51);
        tout_txt=(TextView)view.findViewById(R.id.textView52);

        drawable = (ClipDrawable) akuimage.getDrawable();
        drawable.setLevel(0);

        drawable.setLevel(drawable.getLevel()+ (500*akugelensayi));


        drawable1 = (ClipDrawable) temizsuimage.getDrawable();
        drawable1.setLevel(0);

        Thread_refresh = new Thread(new AnasayfaFragment.refresh_Task());
        Thread_refresh.start();


        loadLocale();
    }
    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences Timee = getActivity().getSharedPreferences("Time",MODE_PRIVATE);
        String time = Timee.getString("time","00");
        String dk = Timee.getString("dk","00");


        String day = Timee.getString("gun","00");
        String ay = Timee.getString("ay","00");
        String yil = Timee.getString("yil","00");

        SharedPreferences pref = requireActivity().getSharedPreferences("KirlisuSwitch",MODE_PRIVATE);
        String kirlisu = pref.getString("kirlisu","");

        //  saat.setText(time);
        // dakika.setText(dk);
        //  Gun.setText(day);
        //   Ay.setText(ay);
        // Yil.setText(yil);

    }




    private void setLocale(String s) {
        Locale locale = new Locale(s);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config,getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("setting",MODE_PRIVATE).edit();
        editor.putString("my lang",s);
        editor.putString("languagetext",s);

        if(s.equals("en")){
            SharedPreferences sharedPref = getActivity().getSharedPreferences("enDate",MODE_PRIVATE);
            SharedPreferences.Editor editor1 = sharedPref.edit();

            SharedPreferences sharedtime = getActivity().getSharedPreferences("Time",MODE_PRIVATE);
            SharedPreferences.Editor editortime = sharedtime.edit();
            //**************************************
            editor.putInt("image", R.drawable.ingilizce);
            Date curentTime = Calendar.getInstance().getTime();
            String formattedDate =   DateFormat.getDateInstance(DateFormat.FULL).format(curentTime);
            splitDate = formattedDate.split(",");
            String[] splitDate1 = formattedDate.split("\\s+");
            //*************************
            Ay.setText(splitDate[1]);
            Gun.setText("");
            Yil.setText(splitDate[2]);
            guntext.setText(splitDate[0]);
            //*********************
            DateFormat dateFormat1 = DateFormat.getTimeInstance(DateFormat.DEFAULT, locale);
            String date = dateFormat1.format(new Date());
            String[]  splittime = date.split(":");
            saat.setText(splittime[0].trim());
            dakika.setText(splittime[1].trim());

            String hour = saat.getText().toString();
            String minute = dakika.getText().toString();
            editortime.putString("hour",hour);
            editortime.putString("minute",minute);
            editortime.apply();
            //***************************************

            String ayy = Ay.getText().toString();
            String gunn =    Gun.getText().toString();
            Yil.setText(splitDate[2].trim());
            String guntexttt=  guntext.getText().toString();
            editor1.putString("ay",ayy);
            editor1.putString("gun",guntexttt);

            editor1.apply();
         //**********************************


        }
        else if(s.equals("tr")){

            SharedPreferences sharedPref1 = getActivity().getSharedPreferences("trDate",MODE_PRIVATE);
            SharedPreferences.Editor editor2 = sharedPref1.edit();

            SharedPreferences sharedtime = getActivity().getSharedPreferences("Time",MODE_PRIVATE);
            SharedPreferences.Editor editortime = sharedtime.edit();
            //********************************************
            editor.putInt("image", R.drawable.turkiye);
            Date curentTime = Calendar.getInstance().getTime();
            String formattedDate =   DateFormat.getDateInstance(DateFormat.FULL).format(curentTime);
            splitDate = formattedDate.split("\\s+");
            //**********************************
            Ay.setText(splitDate[1].trim());
            Gun.setText(splitDate[0].trim());
            Yil.setText(splitDate[2].trim());
            guntext.setText(splitDate[3].trim());
           //**********************************
            DateFormat dateFormat1 = DateFormat.getTimeInstance(DateFormat.DEFAULT, locale);
            String date = dateFormat1.format(new Date());
            String[]  splittime = date.split(":");
            saat.setText(splittime[0].trim());
            dakika.setText(splittime[1].trim());

            String hour = saat.getText().toString();
            String minute = dakika.getText().toString();
            editortime.putString("hour",hour);
            editortime.putString("minute",minute);
            editortime.apply();
      //**********************************
            String ayy = Ay.getText().toString();
        String gunn = Gun.getText().toString();
        Yil.setText(splitDate[2].trim());
        String guntexttt=  guntext.getText().toString();

         editor2.putString("gunsayi",ayy);
         editor2.putString("ay",gunn);
         editor2.putString("gun",guntexttt);

            editor2.apply();
        //**********************************

        }
        else {
            SharedPreferences sharedPref2 = getActivity().getSharedPreferences("deDate",MODE_PRIVATE);
            SharedPreferences.Editor editor3 = sharedPref2.edit();

            SharedPreferences sharedtime = getActivity().getSharedPreferences("Time",MODE_PRIVATE);
            SharedPreferences.Editor editortime = sharedtime.edit();
            //************************************
            editor.putInt("image", R.drawable.almanca);
            Date curentTime = Calendar.getInstance().getTime();
            String formattedDate =   DateFormat.getDateInstance(DateFormat.FULL).format(curentTime);
            splitDate = formattedDate.split(",");
            String[] splitDate1 = formattedDate.split("\\s+");
            //***********************
            Ay.setText(splitDate1[2]);
            Gun.setText(splitDate1[1]);
            Yil.setText(splitDate1[3]);
            guntext.setText(splitDate[0]);
           //***************
            DateFormat dateFormat1 = DateFormat.getTimeInstance(DateFormat.DEFAULT, locale);
            String date = dateFormat1.format(new Date());
            String[]  splittime = date.split(":");
            saat.setText(splittime[0].trim());
            dakika.setText(splittime[1].trim());

            String hour = saat.getText().toString();
            String minute = dakika.getText().toString();
            editortime.putString("hour",hour);
            editortime.putString("minute",minute);
            editortime.apply();
           //************************
            String ayy = Ay.getText().toString();
            String gunn =    Gun.getText().toString();
          //  Yil.setText(splitDate[2].trim());
            String guntexttt=  guntext.getText().toString();
            editor3.putString("gunsayi",gunn);
            editor3.putString("ay",ayy);
            editor3.putString("gun",guntexttt);

            editor3.apply();


        }
        editor.apply();

    }
    public void loadLocale(){
        SharedPreferences prefs = getActivity().getSharedPreferences("setting",MODE_PRIVATE);
        String language = prefs.getString("my lang","");
        setLocale(language);
    }
    class refresh_Task implements Runnable {
        public void run() {
            while(true) {
                set_input_views();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    long map(long x, long in_min, long in_max, long out_min, long out_max) {
        if (((in_max - in_min) + out_min) != 0) {
            return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
        } else return 0;
    }
    public void set_input_views()
    {
        handler.post(new Runnable() {
            public void run() {
                drawable1.setLevel(MainActivity.cl_water_lvl*100);
                cl_water_txt.setText(""+MainActivity.cl_water_lvl);

                if(enable_dt_view) {
                    dt_water_txt.setText("" + MainActivity.dt_water_lvl);
                }
                else {
                    dt_water_txt.setText("N/A");
                }
                long vbatt_perc_val=map(MainActivity.v_batt,11000,12600,0,100);
                if (vbatt_perc_val>100)vbatt_perc_val=100;
                if (vbatt_perc_val<0)vbatt_perc_val=0;
                drawable.setLevel((int)(vbatt_perc_val*100));
                vbatt_perc_txt.setText(""+(int)(vbatt_perc_val));
                String s = String.format("%.1f", (float)MainActivity.v_batt/1000);
                vbatt_txt.setText(s);
                s = String.format("%.0f", (float)MainActivity.dht_temp/10);
                tin_txt.setText(s);
                s = String.format("%.0f", (float)MainActivity.t_out/100);
                tout_txt.setText(s);
            }
        });
    }
}