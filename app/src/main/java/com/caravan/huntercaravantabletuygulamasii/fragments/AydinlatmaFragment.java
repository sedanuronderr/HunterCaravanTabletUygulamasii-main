package com.caravan.huntercaravantabletuygulamasii.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.caravan.huntercaravantabletuygulamasii.MainActivity;
import com.caravan.huntercaravantabletuygulamasii.R;

import java.util.Timer;
import java.util.TimerTask;


public class AydinlatmaFragment extends Fragment {
    private Handler handler = new Handler();
    public static Switch OUTPUT_VIEWS[] = new Switch[12];
    public char inputsdat,old_inputsdat;
    Switch mutfakisiklari;
    Switch oturmaalanisiklariM;
    Switch yatakodasiisiklari1;
    Switch mutfaktavanisiklari;
    Switch oturmaalaniisiklari;
    Switch yatakodasiisiklari2;
    Thread Thread_refresh = null;
    Switch disisiklarsol;

    Switch disisiklaron;

    Switch parkisiklarisag;

    Switch disisiklarisag;

    Switch disisiklararka;

    Switch parkisiklarisol;



        // Required empty public constructor


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_aydinlatma, container, false);
    }



    public void set_input_views(char dat)
    {
        handler.post(new Runnable() {
            public void run() {
                for(int j=0;j<12;j++)
                {
                    if((dat&(1<<j))>0) OUTPUT_VIEWS[j].setChecked(true);
                    else OUTPUT_VIEWS[j].setChecked(false);
                }
            }
        });
    }
    class refresh_Task implements Runnable {
        public void run() {
            while(true) {
                inputsdat= (char) (MainActivity.inputsdat&0xFFF);
                if (inputsdat != old_inputsdat) {
                    old_inputsdat = inputsdat;
                    set_input_views(inputsdat);
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        OUTPUT_VIEWS[0] = view.findViewById(R.id.mutfakisiklari);
        OUTPUT_VIEWS[1] = view.findViewById(R.id.oturmaalanisikM);
        OUTPUT_VIEWS[2] = view.findViewById(R.id.yatakodaisik1);
        OUTPUT_VIEWS[3] = view.findViewById(R.id.mutfaktavanisik);
        OUTPUT_VIEWS[4] = view.findViewById(R.id.oturmaalanisiklari);
        OUTPUT_VIEWS[5] = view.findViewById(R.id.yatakodasiisik2);
        OUTPUT_VIEWS[6] = view.findViewById(R.id.disisiklarisol);
        OUTPUT_VIEWS[7] = view.findViewById(R.id.disisiklaron);
        OUTPUT_VIEWS[8] = view.findViewById(R.id.parkisiklarisag);
        OUTPUT_VIEWS[9] = view.findViewById(R.id.disisiklarsag);
        OUTPUT_VIEWS[10] =view.findViewById(R.id.disisiklararka);
        OUTPUT_VIEWS[11] =view.findViewById(R.id.parkisiklarisol);

        mutfakisiklari = view.findViewById(R.id.mutfakisiklari);
        oturmaalanisiklariM=view.findViewById(R.id.oturmaalanisikM);
        yatakodasiisiklari1 = view.findViewById(R.id.yatakodaisik1);
        mutfaktavanisiklari= view.findViewById(R.id.mutfaktavanisik);
        oturmaalaniisiklari = view.findViewById(R.id.oturmaalanisiklari);
        yatakodasiisiklari2=view.findViewById(R.id.yatakodasiisik2);


        disisiklarsol = view.findViewById(R.id.disisiklarisol);
        disisiklaron = view.findViewById(R.id.disisiklaron);
        parkisiklarisag = view.findViewById(R.id.parkisiklarisag);
        disisiklarisag = view.findViewById(R.id.disisiklarsag);
        disisiklararka= view.findViewById(R.id.disisiklararka);
        parkisiklarisol= view.findViewById(R.id.parkisiklarisol);


        for (int i = 0; i < OUTPUT_VIEWS.length; i++) {
            int finalI = i;
            OUTPUT_VIEWS[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                    if (isChecked) MainActivity.outputs_data = (char) ((char) ((MainActivity.outputs_data | (1 << finalI))));
                    else MainActivity.outputs_data = (char) ((char) ((MainActivity.outputs_data & (~(1 << finalI)))));
                    MainActivity.output_update=true;
                }
            });
        }
        set_input_views(MainActivity.inputsdat);
        Thread_refresh = new Thread(new AydinlatmaFragment.refresh_Task());
        Thread_refresh.start();
        //Timer timer = new Timer();
        //timer.schedule(refresh_timerTask,0,100);
    }
}