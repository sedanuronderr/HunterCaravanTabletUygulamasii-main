package com.caravan.huntercaravantabletuygulamasii.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.caravan.huntercaravantabletuygulamasii.HomeScreen;
import com.caravan.huntercaravantabletuygulamasii.MainActivity;
import com.caravan.huntercaravantabletuygulamasii.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class GelismisUygulamaAyarlari extends AppCompatActivity {

    private Handler handler = new Handler();
    Thread Thread_refresh = null;
    ImageView diagnostikbtn;
    ImageView eslesmebtn;
    TextView eslesmeText,batt_curr_txt;
    BluetoothAdapter myBluetoothAdapter;
    Intent btEnablingIntent;
    int requestCodeForeEnable;
    BluetoothDevice[] btArray;
    private Switch switchView;
    ListView pairedlist;
    ArrayList<String> list = new ArrayList<String>();
    Button kaydet;


    String deger;
    ArrayList<String> list1 = new ArrayList<String>();
    private Set<BluetoothDevice> pairedDevice;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    Switch kirlisuswitch,dengesistemiswitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gelismis_uygulama_ayarlari);
        int currentApiVersion = Build.VERSION.SDK_INT;

        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(flags);
            final View decorView = getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(visibility -> {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    decorView.setSystemUiVisibility(flags);
                }
            });
        }
        diagnostikbtn = findViewById(R.id.diagnostikbtn);
        switchView = findViewById(R.id.bluetooth);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        eslesmebtn = findViewById(R.id.eslesmebtn);
        eslesmeText = findViewById(R.id.eslesmetext);
        batt_curr_txt = findViewById(R.id.textView27);

        kaydet = findViewById(R.id.button);
        kirlisuswitch = findViewById(R.id.kirlisugostergesiswitch);
        dengesistemiswitch = findViewById(R.id.dengesistemiswitch);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        btEnablingIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        requestCodeForeEnable = 1;
        kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),R.string.Kaydedildi,Toast.LENGTH_SHORT).show();

            }
        });


        diagnostikbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GelismisUygulamaAyarlari.this, DiagnostikMenu.class);
                startActivity(intent);
                finish();
            }
        });


        switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // on below line we are checking
                // if switch is checked or not.
                if (isChecked) {
                    // on below line we are setting text
                    // if switch is checked.
                    if (myBluetoothAdapter == null) {
                        Toast.makeText(getApplicationContext(), "Bluetooth does not support", Toast.LENGTH_LONG).show();


                    } else {

                        if (!myBluetoothAdapter.isEnabled()) {

                            startActivityForResult(btEnablingIntent, requestCodeForeEnable);


                        }


                    }
                } else {


                    if (ActivityCompat.checkSelfPermission(GelismisUygulamaAyarlari.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        myBluetoothAdapter.disable();

                        return;
                    }

                    Toast.makeText(getApplicationContext(), "kapat", Toast.LENGTH_LONG).show();


                    // on below line we are setting text
                    // if switch is unchecked.

                }
            }
        });
        kirlisuswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    SharedPreferences shared = getSharedPreferences("KirlisuSwitch",MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();
                     editor.putString("kirlisu","");
                     editor.apply();
                     Log.d("Kirli_su","enabled");

                }else {
                    SharedPreferences shared = getSharedPreferences("KirlisuSwitch",MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();
                     editor.putString("kirlisu","N/A");
                     editor.apply();
                    Log.d("Kirli_su","disabled");
                }
            }
        });
        SharedPreferences shared = getSharedPreferences("dengesistemi",MODE_PRIVATE);
       deger = shared.getString("dengesistemi","");

      dengesistemiswitch.setChecked(Boolean.parseBoolean(deger));
        SharedPreferences shared_dt_w = getSharedPreferences("KirlisuSwitch",MODE_PRIVATE);
        String kirlisu_enable = shared_dt_w.getString("kirlisu","");
        if(kirlisu_enable.equals("N/A")) kirlisuswitch.setChecked(false);
        else kirlisuswitch.setChecked(true);

        dengesistemiswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    SharedPreferences shared = getSharedPreferences("dengesistemi",MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();
                     editor.putString("dengesistemi","true");
                     editor.apply();

                }else {
                    SharedPreferences shared = getSharedPreferences("dengesistemi",MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();

                      editor.putString("dengesistemi", "false");
                     editor.apply();
                }
            }
        });


        SharedPreferences prefs = getSharedPreferences("Bluetoothcihazii", MODE_PRIVATE);
        String cihazid = prefs.getString("cihazId", "");
        eslesmeText.setText(cihazid);

        SharedPreferences pre = getSharedPreferences("Bluetoothcihazadi", MODE_PRIVATE);
        String cihazadi = pre.getString("cihazadi", "");


        // @string/bluetootheslestirme

        eslesmebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listDevice();


            }
        });
        Thread_refresh = new Thread(new GelismisUygulamaAyarlari.refresh_Task());
        Thread_refresh.start();

    }

    private void listDevice() {
        list.clear();
        list1.clear();
        if (myBluetoothAdapter.isEnabled()) {
            pairedDevice = myBluetoothAdapter.getBondedDevices();
            if (pairedDevice.size() > 0) {
                // There are paired devices. Get the name and address of each paired device.
                for (BluetoothDevice bt : pairedDevice) {
                    String deviceName = bt.getName();
                    Log.e("BT", "GELENN" + deviceName + "\n" + bt.getAddress());
                    list.add(bt.getName());
                    list1.add(bt.getAddress());
                }
            } else {
                Toast.makeText(this, "Eşleşmiş cihaz yok", Toast.LENGTH_SHORT).show();
            }
        }
        if (myBluetoothAdapter.isEnabled()) {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
            mBuilder.setTitle("Choose Device");

            mBuilder.setItems(list.toArray(new String[0]), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    SharedPreferences.Editor editor = getSharedPreferences("Bluetoothcihazadi", MODE_PRIVATE).edit();
                    editor.putString("cihazadi", String.valueOf(list.get(i)));
                    editor.apply();

                    SharedPreferences.Editor editor2 = getSharedPreferences("Bluetoothcihazii", MODE_PRIVATE).edit();
                    editor2.putString("cihazId", String.valueOf(list1.get(i)));
                    editor2.apply();

                    SharedPreferences prefs = getSharedPreferences("Bluetoothcihazii", MODE_PRIVATE);
                    String cihazid = prefs.getString("cihazId", "");
                    eslesmeText.setText(cihazid);


                }
            });
            AlertDialog mDialog = mBuilder.create();
            mDialog.show();
        } else {
            Toast.makeText(this, "Bluetooth not connect", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
    public void set_input_views()
    {
        handler.post(new Runnable() {
            public void run() {
                String s = String.format("%.1f", (float)MainActivity.batt_curr/1000);
                batt_curr_txt.setText(s+"A");
            }
        });
    }
}