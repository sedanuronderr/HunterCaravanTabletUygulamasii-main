package com.caravan.huntercaravantabletuygulamasii.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.caravan.huntercaravantabletuygulamasii.R;

public class AyarlarFragment extends AppCompatActivity {

    EditText kullanici;
    EditText sifre;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_ayarlar);
        {

            button = (Button) findViewById(R.id.kaydet);
            kullanici = findViewById(R.id.kullanici);
            sifre = findViewById(R.id.sifre);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    updateDetail();
                }
            });


        }



    }

    public void updateDetail() {
        String kullaniciadi = "hunter";
        String sifresi = "karavan";
        if ((kullanici.getText().toString().equals("hunter")) && (sifre.getText().toString().equals("karavan"))) {

            Intent intent = new Intent(this, GelismisUygulamaAyarlari.class);
            startActivity(intent);
            finish();

        } else {
            Toast.makeText(this,"Yanlıs Bilgi Girdiniz.",Toast
                    .LENGTH_SHORT).show();
        }

    }
        }

