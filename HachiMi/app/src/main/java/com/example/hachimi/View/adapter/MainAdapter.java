package com.example.hachimi.View.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.hachimi.MainActivity;

import java.util.ArrayList;
/**
 * 包名称： com.example.hachimi.View.adapter
 * 类名称：MainFragAdapter
 * 类描述：适配器，由于是Fragment+VP2，其结构相当简单
 * 创建人：韦西波
 */
public class MainAdapter extends FragmentStateAdapter {
    public final ArrayList<Fragment> fragments;

    public MainAdapter(@NonNull MainActivity mainActivity, ArrayList<Fragment> fragments) {
        super(mainActivity);
        this.fragments = fragments;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }
}
