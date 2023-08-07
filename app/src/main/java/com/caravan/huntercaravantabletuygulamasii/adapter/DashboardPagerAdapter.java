package com.caravan.huntercaravantabletuygulamasii.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.caravan.huntercaravantabletuygulamasii.fragments.AnasayfaFragment;
import com.caravan.huntercaravantabletuygulamasii.fragments.AyarlarFragment;
import com.caravan.huntercaravantabletuygulamasii.fragments.AydinlatmaFragment;
import com.caravan.huntercaravantabletuygulamasii.fragments.DengeSistemiFragment;
import com.caravan.huntercaravantabletuygulamasii.fragments.EnerjiFragment;
import com.caravan.huntercaravantabletuygulamasii.fragments.GelismisUygulamaAyarlari;
import com.caravan.huntercaravantabletuygulamasii.fragments.GostergelerFragment;
import com.caravan.huntercaravantabletuygulamasii.fragments.KapatmabuttonFragment;
import com.caravan.huntercaravantabletuygulamasii.fragments.KontrollerFragment;
import com.caravan.huntercaravantabletuygulamasii.fragments.KullaniciUygulamaAyarlari;

public class DashboardPagerAdapter extends FragmentStateAdapter {

    private final int ITEMS= 8;

    public DashboardPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position){

            case 0:
                return new AnasayfaFragment();
            case 1:
                return new AydinlatmaFragment();
            case 2:
                return new EnerjiFragment();
            case 3:
                return new GostergelerFragment();
            case 4:
                return new KontrollerFragment();
            case 5:
                return new DengeSistemiFragment();
            case 6: return new KullaniciUygulamaAyarlari();


            default: return  new KapatmabuttonFragment();


        }

    }

    @Override
    public int getItemCount() {
        return ITEMS;
    }
}
