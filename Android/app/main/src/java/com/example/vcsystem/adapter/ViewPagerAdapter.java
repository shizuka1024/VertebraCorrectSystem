package com.example.vcsystem.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.vcsystem.ui.BarChartFragment;
import com.example.vcsystem.ui.ChartFragment;
import com.example.vcsystem.ui.PieChartFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter  {

    private List<Fragment> fragmentList=new ArrayList<>();

    public ViewPagerAdapter(@NonNull @NotNull Fragment fragment) {
        super(fragment);
        addFragment(new BarChartFragment());
        addFragment(new PieChartFragment());
    }

    @NotNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = null;
        fragment = fragmentList.get(position);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }

    public void addFragment(Fragment fragment){
        fragmentList.add(fragment);
    }
}
