package com.caravan.huntercaravantabletuygulamasii;

import static android.view.View.VISIBLE;
import static java.lang.Math.round;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.caravan.huntercaravantabletuygulamasii.adapter.DashboardPagerAdapter;
import com.caravan.huntercaravantabletuygulamasii.fragments.AnasayfaFragment;
import com.caravan.huntercaravantabletuygulamasii.fragments.DengeSistemiFragment;
import com.caravan.huntercaravantabletuygulamasii.fragments.GostergelerFragment;
import com.caravan.huntercaravantabletuygulamasii.fragments.KapatmabuttonFragment;
import com.caravan.huntercaravantabletuygulamasii.fragments.KullaniciUygulamaAyarlari;
import com.google.android.material.tabs.TabLayout;

import java.util.Locale;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import fun.observe.touchy.MotionEventBroadcaster;
import fun.observe.touchy.MotionEventReceiver;

public class HomeScreen extends AppCompatActivity {
    TabLayout tabLayout;
    int time_out;
    private ViewPager2 viewPager2;

    private ImageView homeimage;


    int brightnessValue = 255;
    // Content resolver used as a handle to the system's settings
    private ContentResolver cResolver;
    // Window object, that will store a reference to the current window
    private Window window;
    BluetoothAdapter myBluetoothAdapter;
    Intent btEnablingIntent;

    KullaniciUygulamaAyarlari kullaniciUygulamaAyarlari;
    int requestCodeForeEnable;

    BluetoothDevice[] btArray;
    Timer timer;
    ListView pairedlist;
    SharedPreferences shared_time_out;
    public static int time_out_cnt = 0;
    private Set<BluetoothDevice> pairedDevice;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private DashboardPagerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        int currentApiVersion = Build.VERSION.SDK_INT;
        MotionEventBroadcaster.registerReceiver(new MotionEventReceiver() {
            @Override
            protected void onReceive(MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN)
                {
                    time_out_cnt=0;
                    Log.d("Timeout","reset:"+time_out_cnt);
                }
            }
        });
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
        shared_time_out = getSharedPreferences("Timeout", MODE_PRIVATE);
        time_out = shared_time_out.getInt("timeout", 0);
        Log.d("Timeout", "" + time_out);
        /*timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                HomeScreen.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        time_out = shared_time_out.getInt("timeout", 0);
                        if (time_out > 0) {
                            time_out_cnt++;
                            Log.d("Timeout", "val:" + time_out_cnt);
                            if (time_out_cnt > (time_out*60)) {
                                KapatmabuttonFragment.from_power_off=true;
                                Intent intent = new Intent(HomeScreen.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }
                    }
                });
            }
        }, 0, 1000);*/

        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


        btEnablingIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        requestCodeForeEnable = 1;


        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager);
        homeimage = findViewById(R.id.homeimage);


        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.anasayfabutton));
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.aydinlatmabutton));
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.enerjibutton));
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.gosterge_button));
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.kontrollerbutton));
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.dengesistemibutton));
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.ayarlarbutton));
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.kapatmabutton));


        loadLocale();


        FragmentManager fragmentmanager = getSupportFragmentManager();
        adapter = new DashboardPagerAdapter(fragmentmanager, getLifecycle());
        viewPager2.setAdapter(adapter);

        tabLayout.setVisibility(VISIBLE);
        homeimage.setImageResource(R.drawable.re);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition(), false);
                int position = tab.getPosition();
                switch (position) {
                    case 7:
                        Brightness();

                        tabLayout.setVisibility(View.INVISIBLE);
                        homeimage.setImageResource(R.color.black);

                        break;


                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {


            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
        viewPager2.setUserInputEnabled(false);
        SharedPreferences shared_dt_w = getSharedPreferences("KirlisuSwitch", MODE_PRIVATE);
        String kirlisu_enable = shared_dt_w.getString("kirlisu", "");
        if (kirlisu_enable.equals("N/A")) {
            Log.d("Kirli_su", "disabled");
            GostergelerFragment.enable_dt_view = false;
            AnasayfaFragment.enable_dt_view = false;
        } else {
            Log.d("Kirli_su", "enabled");
            GostergelerFragment.enable_dt_view = true;
            AnasayfaFragment.enable_dt_view = true;
        }

        SharedPreferences shared = getSharedPreferences("dengesistemi", MODE_PRIVATE);
        String deger = shared.getString("dengesistemi", "");
        if (deger.equals("false")) {

            tabLayout.getTabAt(5).view.setVisibility(View.GONE);


        } else if (deger.equals("true")) {
            tabLayout.getTabAt(5).view.setVisibility(VISIBLE);


        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                HomeScreen.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        time_out = shared_time_out.getInt("timeout", 0);
                        if (time_out > 0) {
                            time_out_cnt++;
                            Log.d("Timeout", "val:" + time_out_cnt);
                            if (time_out_cnt > (time_out*60)) {
                                KapatmabuttonFragment.from_power_off=true;
                                Intent intent = new Intent(HomeScreen.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }
                    }
                });
            }
        }, 0, 1000);
        SharedPreferences shared = getSharedPreferences("dengesistemi", MODE_PRIVATE);
        String deger = shared.getString("dengesistemi", "");
        Log.e("deger", "" + deger);
        if (deger.equals("false")) {

            tabLayout.getTabAt(5).view.setVisibility(View.GONE);


        } else if (deger.equals("true")) {
            tabLayout.getTabAt(5).view.setVisibility(VISIBLE);


        }

        SharedPreferences shared_dt_w = getSharedPreferences("KirlisuSwitch", MODE_PRIVATE);
        String kirlisu_enable = shared_dt_w.getString("kirlisu", "");
        if (kirlisu_enable.equals("N/A")) {
            Log.d("Kirli_su", "disabled");
            GostergelerFragment.enable_dt_view = false;
            AnasayfaFragment.enable_dt_view = false;
        } else {
            Log.d("Kirli_su", "enabled");
            GostergelerFragment.enable_dt_view = true;
            AnasayfaFragment.enable_dt_view = true;
        }
    }


    private void Brightness() {
        Context context = getApplicationContext();

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

    private void setLocale(String s) {
        Locale locale = new Locale(s);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("setting", MODE_PRIVATE).edit();
        editor.putString("my lang", s);
        editor.putString("languagetext", s);
        if (s.equals("en")) {
            editor.putInt("image", R.drawable.ingilizce);

        } else if (s.equals("tr")) {
            editor.putInt("image", R.drawable.turkiye);


        } else {

            editor.putInt("image", R.drawable.almanca);


        }
        editor.apply();

    }

    public void loadLocale() {
        SharedPreferences prefs = getSharedPreferences("setting", MODE_PRIVATE);
        String language = prefs.getString("my lang", "");
        setLocale(language);
    }
    // Timer'ı durdurma işlemi.
    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    protected void onDestroy() {
        stopTimer();
        super.onDestroy();
    }
    @Override
    protected void onPause() {
        stopTimer();
        super.onPause();
    }
}