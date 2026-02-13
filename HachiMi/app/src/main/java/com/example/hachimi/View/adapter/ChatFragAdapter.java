package com.example.hachimi.View.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
/**
 * 包名称： com.example.hachimi.View.adapter
 * 类名称：ChatFragAdapter
 * 类描述：适配器，由于是Fragment+VP2，其结构相当简单
 * 创建人：韦西波
 */
public class ChatFragAdapter extends FragmentStateAdapter {
    public final ArrayList<Fragment> fragments;

    public ChatFragAdapter(@NonNull Fragment fragment, ArrayList<Fragment> fragments) {
        super(fragment);
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
