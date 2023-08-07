package com.caravan.huntercaravantabletuygulamasii;

import static java.lang.Math.round;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;



import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ClipDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;


import com.caravan.huntercaravantabletuygulamasii.fragments.AydinlatmaFragment;
import com.caravan.huntercaravantabletuygulamasii.fragments.KapatmabuttonFragment;
import com.harrysoft.androidbluetoothserial.BluetoothManager;
import com.harrysoft.androidbluetoothserial.BluetoothSerialDevice;
import com.harrysoft.androidbluetoothserial.SimpleBluetoothDeviceInterface;

import java.sql.Time;
import java.util.Collection;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    public static char outputs_data;
    public static boolean output_update,humidty_set_update=false;
    AnimationDrawable drawableAnimation;
 // After completion of 2000 ms, the next activity will get started.

    Context context = this;
    private SimpleBluetoothDeviceInterface deviceInterface;
    BluetoothManager bluetoothManager = BluetoothManager.getInstance();
    String my_device_mac,my_device_name;
    //char outputs_data = 0x0000;
    char[] ouput_update_buf={0x55,0x74,0x00,0x00};
    char[] input_read_buf={0x55,0x41,0x00,0x00};

    char[] humidity_set_update_buf={0x55,0x53,0x00,0x00};
    boolean my_device_exist=false;
    //boolean output_update=false;
    Thread Thread_Comm = null;
    Boolean bluetooth_connected=false;
    public static char inputsdat,old_inputsdat;


    public static int v_batt;
    public static int v_solar;
    public static int cl_water_lvl;
    public static int dt_water_lvl;
    public static int t_in;
    public static int t_out;
    public static int dht_temp;
    public static int dht_humidty;

    public static int batt_curr;

    public static int solar_curr;
    private static final int SPLASH_SCREEN_TIME_OUT = 1900; // After completion of 2000 ms, the next activity will get started.
    ImageView black;
    ClipDrawable drawable;
    private  boolean start_loading=false;
    Handler isleyici1 = new Handler();
    Handler isleyici2 = new Handler();
    private boolean handle_release=true;
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            black.setVisibility(View.INVISIBLE);
            start_loading=true;
            Log.d("Touch", "press");
            changeBrightness(context,255);
        }
        if ((event.getAction() == MotionEvent.ACTION_UP)&&handle_release)
        {

            start_loading=false;
            black.setVisibility(View.VISIBLE);
            drawable.setLevel(0);
            Brightness();
            Log.d("Touch", "release");
        }

        return super.onTouchEvent(event);
    }
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView image = (ImageView) findViewById(R.id.progressBar);
        black= (ImageView) findViewById(R.id.black_screen);
        drawable = (ClipDrawable) image.getDrawable();
        drawable.setLevel(0);
        loadLocale();
        if (!hasWritePermission(context))
        {
            changeWritePermission(context);
        }
        if(KapatmabuttonFragment.from_power_off)
        {
            black.setVisibility(View.VISIBLE);
            Brightness();
            start_loading=false;
        }
        else
        {
            changeBrightness(context,255);
            black.setVisibility(View.INVISIBLE);
            start_loading=true;
        }
        Runnable bekleme2 = new Runnable() {
            @Override
            public void run() {
                // Intent is used to switch from one activity to another.
                Intent i = new Intent(MainActivity.this, HomeScreen.class);
                startActivity(i); // invoke the SecondActivity.
                HomeScreen.time_out_cnt=0;
                finish(); // the current activity will get finished.
            }
        };
        Runnable bekleme1 = new Runnable() {
            @Override
            public void run() {
                nextActivity();
                isleyici2.postDelayed(bekleme2, SPLASH_SCREEN_TIME_OUT);
            }
        };
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(start_loading) {
                            drawable.setLevel(drawable.getLevel() + 100);
                            if (drawable.getLevel() == 10000)
                            {
                                handle_release=false;
                                isleyici1.postDelayed(bekleme1, 0);
                            }
                        }
                    }
                });
            }
        }, 10, 15);

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
        SharedPreferences prefs = getSharedPreferences("Bluetoothcihazii", MODE_PRIVATE);
        my_device_mac = prefs.getString("cihazId", "");
        my_device_mac=my_device_mac.replace("[","");
        my_device_mac=my_device_mac.replace("]","");
        SharedPreferences pre = getSharedPreferences("Bluetoothcihazadi", MODE_PRIVATE);
        my_device_name = pre.getString("cihazadi", "");
        my_device_name=my_device_name.replace("[","");
        my_device_name=my_device_name.replace("]","");

        if (bluetoothManager == null) {
            // Bluetooth unavailable on this device :( tell the user
            Toast.makeText(context, "Bluetooth not available.", Toast.LENGTH_LONG).show(); // Replace context with your context instance.
            finish();
        }

        Log.d("BT_device", "MAC:" + my_device_mac+" NAME:"+my_device_name);
        Collection<BluetoothDevice> pairedDevices = bluetoothManager.getPairedDevices();
        Log.d("Devices_size",""+pairedDevices.size());
        for (BluetoothDevice device : pairedDevices) {
            Log.d("Founded_BT",device.getName()+" >> "+device.getAddress());
            if (device.getAddress().equals(my_device_mac)) {
                my_device_exist = true;
                Log.d("Device", "My device found:" + my_device_mac);
            }
        }
        if (my_device_exist) {
            connectDevice(my_device_mac);
        } else {
            Toast.makeText(context, my_device_name+" not found.", Toast.LENGTH_LONG).show();
        }






        Thread_Comm = new Thread(new MainActivity.Comm_Task());
        Thread_Comm.start();

    }
    private void Brightness() {

        // Check whether has the write settings permission or not.
        boolean settingsCanWrite = hasWritePermission(context);

        // If do not have then open they Can modify system settings panel.
        if (!settingsCanWrite) {
            changeWritePermission(context);
        } else {
            changeBrightness(context, 1);
        }
    }

    private void changeBrightness(Context context, int i) {
        // Change the screen brightness change mode to manual.
        Settings.System.putInt(
                context.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL
        );
        // Apply the screen brightness value to the system, this will change
        // the value in Settings ---> Display ---> Brightness level.
        // It will also change the screen brightness for the device.
        Settings.System.putInt(
                context.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS, i
        );
    }

    private void changeWritePermission(Context context) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        startActivity(intent);

    }
    @RequiresApi(Build.VERSION_CODES.M)
    private boolean hasWritePermission(Context context) {
        boolean ret = true;
        // Get the result from below code.
        ret = Settings.System.canWrite(context);
        return ret;
    }



    public void nextActivity(){
        ImageView imageView = (ImageView) findViewById(R.id.maske);
        ImageView image = findViewById(R.id.progressBar);
        ImageView Glass1=findViewById(R.id.mask);
        ImageView hunter = findViewById(R.id.hunter);

        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.move);
        Glass1.startAnimation(animation1);
         hunter.startAnimation(animation1);

        Glass1.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.INVISIBLE);
        image.setVisibility(View.INVISIBLE);
    }


    class Comm_Task implements Runnable {
        public void run() {
            while(true) {
                while(bluetooth_connected) {
                    String message;
                    if (output_update) {
                        output_update = false;
                        ouput_update_buf[3] = (char) ((outputs_data & 0xFF00) >> 8);
                        ouput_update_buf[2] = (char) (outputs_data & 0x00FF);
                        message = new String(ouput_update_buf);
                    }
                    else if(humidty_set_update)
                    {
                        SharedPreferences set_prefs = getSharedPreferences("set_values", MODE_PRIVATE);
                        humidity_set_update_buf[2]=(char)set_prefs.getInt("humidty_set",55);
                        message = new String(humidity_set_update_buf);
                        humidty_set_update=false;
                    }
                    else {
                        message = new String(input_read_buf);

                    }
                    try {
                        deviceInterface.sendMessage(message);
                    } catch (Exception e) {
                        bluetooth_connected=false;
                    }
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    private void connectDevice(String mac) {
        bluetoothManager.openSerialDevice(mac)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onConnected, this::onError);
    }
    private void onConnected(BluetoothSerialDevice connectedDevice) {
        deviceInterface = connectedDevice.toSimpleDeviceInterface();
        deviceInterface.setListeners(this::onMessageReceived, this::onMessageSent, this::onError);
        //Toast.makeText(context, "Menar IO Module Connected.", Toast.LENGTH_LONG).show();
        bluetooth_connected=true;
    }

    private void onMessageSent(String message) {

    }

    private void onMessageReceived(String message) {
        char[] in_buf=message.toCharArray();
        if(in_buf[24]==0x55)
        {
            if(in_buf[25]==0x42)
            {
                Log.d("BT_length", "Input buffer length:"+in_buf.length);
                //Log.d("BT_buffer:",""+Integer.toHexString(in_buf[0])+"-"+Integer.toHexString(in_buf[1])+"-"+Integer.toHexString(in_buf[2])+"-"+Integer.toHexString(in_buf[3])+"-"+Integer.toHexString(in_buf[4])+"-"+Integer.toHexString(in_buf[5])+"-"+Integer.toHexString(in_buf[6])+"-"+Integer.toHexString(in_buf[7])+
                //        "-"+Integer.toHexString(in_buf[8])+"-"+Integer.toHexString(in_buf[9])+"-"+Integer.toHexString(in_buf[10])+"-"+Integer.toHexString(in_buf[11])+"-"+Integer.toHexString(in_buf[12])+"-"+Integer.toHexString(in_buf[13])+"-"+Integer.toHexString(in_buf[14])+"-"+Integer.toHexString(in_buf[15])+
                //        "-"+Integer.toHexString(in_buf[16])+"-"+Integer.toHexString(in_buf[17])+"-"+Integer.toHexString(in_buf[18])+"-"+Integer.toHexString(in_buf[19])+"-"+Integer.toHexString(in_buf[20])+"-"+Integer.toHexString(in_buf[21]));

                //Log.d("BT", "Input buffer length:"+in_buf.length);
                inputsdat= (char) ((in_buf[3]<<8)|in_buf[2]);
                v_batt=(int)((in_buf[5]<<8)|in_buf[4]);
                v_solar=(int)((in_buf[7]<<8)|in_buf[6]);
                cl_water_lvl=(int)((in_buf[9]<<8)|in_buf[8]);
                dt_water_lvl=(int)((in_buf[11]<<8)|in_buf[10]);
                t_in=(int)((in_buf[13]<<8)|in_buf[12]);
                t_out=(int)((in_buf[15]<<8)|in_buf[14]);
                dht_temp=(int)((in_buf[17]<<8)|in_buf[16]);
                dht_temp-=30;
                dht_humidty=(int)((in_buf[19]<<8)|in_buf[18]);
                batt_curr=(int)((in_buf[21]<<8)|in_buf[20]);
                solar_curr=(int)((in_buf[23]<<8)|in_buf[22]);
                if(inputsdat!=old_inputsdat)
                {
                    outputs_data=inputsdat;
                    old_inputsdat=inputsdat;
                }

                Log.d("Reading","Outputs:"+Integer.toHexString(inputsdat)+" Vbatt:"+v_batt+" v_solar:"+v_solar+" cl_water_lvl:"+cl_water_lvl+" dt_water_lvl:"+dt_water_lvl+" t_in:"+t_in+" t_out:"+t_out+" dht_temp:"+dht_temp+" dht_humidty:"+dht_humidty+" batt_curr:"+batt_curr+" solar_curr:"+solar_curr);
            }
        }
    }
    private void onError(Throwable error) {
        bluetoothManager.closeDevice(my_device_mac);
        //Toast.makeText(context, "Menar IO Module Disonnected.", Toast.LENGTH_LONG).show();
        if(my_device_exist)
        {
            connectDevice(my_device_mac);
        }
        else
        {
            Toast.makeText(context, "Menar IO Module not found.", Toast.LENGTH_LONG).show();
        }
        Log.d("BT_exception","Bağlantı kesildi yeniden bağlanılıyor",error);
        // Handle the error
    }
    private void setLocale(String s) {
        Locale locale = new Locale(s);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config,getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("setting",MODE_PRIVATE).edit();
        editor.putString("my lang",s);
        editor.putString("languagetext",s);
        if(s.equals("en")){
            editor.putInt("image", R.drawable.ingilizce);

        }
        else if(s.equals("tr")){
            editor.putInt("image", R.drawable.turkiye);

        }
        else {

            editor.putInt("image", R.drawable.almanca);


        }
        editor.apply();

    }
    public void loadLocale(){
        SharedPreferences prefs = getSharedPreferences("setting",MODE_PRIVATE);
        String language = prefs.getString("my lang","");
        setLocale(language);
    }
}