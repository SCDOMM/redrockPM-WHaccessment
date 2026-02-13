package com.example.hachimi.View.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.hachimi.R;
import com.example.hachimi.View.adapter.ChatFragAdapter;
import com.example.hachimi.utils.ChatFragViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
/**
 * 包名称： com.example.hachimi.View.fragment
 * 类名称：ChatFragment
 * 类描述：使用VP2+fragment获得数据，使用ViewModel传递搜索关键词
 * 创建人：韦西波
 */
public class ChatFragment extends Fragment {
    private ViewPager2 cVP2;
    private SearchView cSV;
    private TabLayout cTL;
    private FloatingActionButton cFAB;
    private ChatFragViewModel viewModel;
    public ChatFragAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_fragment, container, false);
        //准备viewModel传搜索内容
        viewModel = new ViewModelProvider(requireActivity()).get(ChatFragViewModel.class);
        //绑定ID，，
        cVP2 = view.findViewById(R.id.chat_vp2_default);
        cTL = view.findViewById(R.id.chat_tabs_label);
        cFAB = view.findViewById(R.id.chat_fab_submit);
        cSV = view.findViewById(R.id.chat_sv_search);
        //初始化其他逻辑
        initEvent();
        initSearch();
        return view;
    }

    //配置适配器
    private void initEvent() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new Chat1Fragment());
        fragments.add(new Chat2Fragment());
        fragments.add(new Chat3Fragment());
        adapter = new ChatFragAdapter(this, fragments);
        cVP2.setAdapter(adapter);
        //点击悬浮按钮跳到发布页面
        cFAB.setOnClickListener(view -> cVP2.setCurrentItem(2));

        //使用TabLayout
        new TabLayoutMediator(cTL, cVP2, (tab, i) -> {
            if (i == 0) {
                tab.setText("发现动态");
            } else if (i == 1) {
                tab.setText("动态分类");
            } else if (i == 2) {
                tab.setText("发布动态");
            }
        }).attach();
    }

    //搜索逻辑
    private void initSearch() {
        cSV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String s) {
                //搜索栏清空，，，
                if (s.isEmpty()) {
                    viewModel.setString("");
                }
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String s) {
                //搜索自动跳转到chat1
                if (cVP2.getCurrentItem() != 0) {
                    cVP2.setCurrentItem(0);
                    //延时一点，预加载
                    new Handler(Looper.getMainLooper()).postDelayed(() -> viewModel.setString(s), 200);
                } else {
                    viewModel.setString(s);
                }
                Toast.makeText(getContext(), "搜索：" + s, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

    }

    public ViewPager2 getcVP2Instance() {
        return cVP2;
    }
}