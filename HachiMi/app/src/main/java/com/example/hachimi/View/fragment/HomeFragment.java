package com.example.hachimi.View.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.hachimi.R;
import com.example.hachimi.View.activity.HomeDetailsActivity;
import com.example.hachimi.View.activity.HomeUploadActivity;
import com.example.hachimi.View.adapter.HomeFragAdapter;
import com.example.hachimi.repository.model.HomeFragData;
import com.example.hachimi.repository.network.HomeNet;
import com.example.hachimi.utils.HomeFragViewModel;
import com.example.hachimi.utils.SecurityHandle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
/**
 * 包名称： com.example.hachimi.View.fragment
 * 类名称：HomeFragment
 * 类描述：设置了刷新，搜索等操作，以获取对应的item。点击item可以查看详细档案。
 * 创建人：韦西波
 */
public class HomeFragment extends Fragment {
    private RecyclerView hRV;
    private SwipeRefreshLayout hRL;
    private SearchView hSV;
    private FloatingActionButton hFAB;
    public HomeFragAdapter adapter;
    private ArrayList<HomeFragData> homeDataList = new ArrayList<>();
    private ArrayList<HomeFragData> searchDataList = new ArrayList<>();
    private boolean isSearching = false;
    private boolean isRefreshing = false;
    private String searchContent;
    private View rootView;
    private Handler handler;
    private HomeNet homeNet;
    public static HomeFragViewModel viewModel;
    private SwipeRefreshLayout.OnRefreshListener refreshListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.home_fragment, container, false);
        //绑定id
        hRV = rootView.findViewById(R.id.home_rv_default);
        hRL = rootView.findViewById(R.id.home_sfl_refresh);
        hSV = rootView.findViewById(R.id.home_sv_search);
        hFAB = rootView.findViewById(R.id.home_fab_upload);
        //初始化Handler
        handler = new MyHandler(this);
        homeNet = new HomeNet(handler);
        viewModel = new ViewModelProvider(requireActivity()).get(HomeFragViewModel.class);

        //初始化其它事件
        initAdmin();
        initEvent();
        initSearch();
        initRefresh();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //等一会，让网络响应
        hRL.postDelayed(() -> {
            hRL.setRefreshing(true);
            refreshListener.onRefresh();
        }, 300);
    }

    private static class MyHandler extends Handler {
        private final WeakReference<HomeFragment> fragWeak;

        public MyHandler(HomeFragment fragment) {
            this.fragWeak = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            HomeFragment fragment = fragWeak.get();
            if (fragment == null || fragment.isDetached()) {
                return;
            }
            if (msg.what == 1) {
                //主页给数据
                fragment.homeDataList.clear();
                ArrayList<HomeFragData> data = (ArrayList<HomeFragData>) msg.obj;
                fragment.homeDataList.addAll(data);

                if (fragment.isSearching) {
                    fragment.searchEvent(fragment.searchContent);
                } else {
                    fragment.searchDataList.clear();
                    fragment.searchDataList.addAll(fragment.homeDataList);
                    Collections.shuffle(fragment.searchDataList);
                    fragment.adapter.notifyDataSetChanged();
                }
                fragment.hRL.setRefreshing(false);
                fragment.isRefreshing = false;
            } else if (msg.what == 2) {
                //给搜索数据
                ArrayList<HomeFragData> data = (ArrayList<HomeFragData>) msg.obj;
                if (data == null) {
                    //没有数据，则清空原本的数据
                    Log.d("搜索日志:", "Home:the result is empty!");
                    Toast.makeText(fragment.getContext(), "搜索为空！不存在相关内容!", Toast.LENGTH_SHORT).show();
                    fragment.searchDataList.clear();
                } else {
                    //清空原本的数据，添加返回的数据
                    fragment.searchDataList.clear();
                    fragment.searchDataList.addAll(data);
                    Log.d("搜索日志:", "Home:the result is exist!");
                }
            } else if (msg.what == 0) {
                Toast.makeText(fragment.getContext(), "错误!" + msg.obj.toString(), Toast.LENGTH_SHORT).show();
            }
            fragment.adapter.notifyDataSetChanged();
        }
    }

    //设置适配器，启动网络请求
    private void initEvent() {
        searchDataList.addAll(homeDataList);
        adapter = new HomeFragAdapter(searchDataList);
        //点击栏目自动跳转到详细页面
        adapter.setHomeFragListener(data -> {
            //使用viewModel传递封面
            viewModel.setHomeFragLiveData(data);
            //使用intent传递标题和内容
            if (getContext() != null) {
                Intent intent = new Intent(getContext(), HomeDetailsActivity.class);
                getContext().startActivity(intent);
            };
        });
        hRV.setLayoutManager(new LinearLayoutManager(getContext()));
        hRV.setAdapter(adapter);
        if (rootView != null) {
            homeNet.requestData(rootView);
        }
    }

    //初始化搜索
    private void initSearch() {
        hSV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String s) {
                if (s.isEmpty()) {
                    searchDataList.clear();
                    searchDataList.addAll(homeDataList);
                    adapter.notifyDataSetChanged();
                    isSearching = false;
                }
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String s) {
                Toast.makeText(getContext(), "搜索内容：" + s, Toast.LENGTH_SHORT).show();
                searchEvent(s);
                isSearching = true;
                searchContent = s;
                return false;
            }
        });
    }

    //搜索事件，自带空检测。不为空就发起请求
    private void searchEvent(String query) {
        if (query == null || query.isEmpty()) {
            searchDataList.addAll(homeDataList);
            adapter.notifyDataSetChanged();
        } else {
            homeNet.searchData(rootView, query);
        }
    }

    //刷新逻辑，刷新后自动请求一次主页，如果在搜索就触发搜索事件，不在搜索就清空原有的数据随后刷新
    private void initRefresh() {
        refreshListener = () -> {
            homeNet.requestData(rootView);
            isRefreshing = true;
        };
        hRL.setOnRefreshListener(refreshListener);
    }

    private void initAdmin() {
        try {
            SharedPreferences sp = SecurityHandle.getEncryptedSharedPreferences(getContext());
            if (sp.getInt("role", 0) != 1) {
                hFAB.hide();
            } else {

                hFAB.setOnClickListener(view -> {
                    Intent intent = new Intent(view.getContext(), HomeUploadActivity.class);
                    startActivity(intent);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}