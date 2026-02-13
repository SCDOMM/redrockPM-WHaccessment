package com.example.hachimi.View.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.hachimi.MainActivity;
import com.example.hachimi.R;
import com.example.hachimi.View.adapter.Chat1FragAdapter;
import com.example.hachimi.repository.model.Chat1FragData;
import com.example.hachimi.repository.network.AdminNet;
import com.example.hachimi.repository.network.ChatNet;
import com.example.hachimi.utils.ChatFragViewModel;
import com.example.hachimi.utils.SecurityHandle;
import com.example.hachimi.utils.SharedPreferenceHandler;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
/**
 * 包名称： com.example.hachimi.View.fragment
 * 类名称：Chat1Fragment
 * 类描述：设置了刷新，搜索等操作，以获取对应的item。使用ViewModel接收搜索关键词。
 * 创建人：韦西波
 */
public class Chat1Fragment extends Fragment {
    private RecyclerView c1RV;
    private SwipeRefreshLayout c1SRL;
    private ChatFragViewModel viewModel;
    private Chat1FragAdapter adapter;
    private ArrayList<Chat1FragData> mainDataList = new ArrayList<>();
    private ArrayList<Chat1FragData> searchDataList = new ArrayList<>();
    private boolean isSearching = false;
    private boolean isRefreshing = false;
    private String searchContent;
    private Handler handler;
    private ChatNet chatNet;
    private View rootView;
    private SwipeRefreshLayout.OnRefreshListener refreshListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.chat1_fragment, container, false);
        //绑定id
        c1RV = rootView.findViewById(R.id.chat1_rv_default);
        c1SRL = rootView.findViewById(R.id.chat1_srl_refresh);
        //初始化网络，handler等
        handler = new MyHandler(this);
        chatNet = new ChatNet(handler);
        //初始化其他事件
        initSearch();
        initEvent();
        initRefresh();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //等一会，让网络响应
        c1SRL.postDelayed(() -> {
            c1SRL.setRefreshing(true);
            refreshListener.onRefresh();
        }, 300);
    }

    private static class MyHandler extends Handler {
        private WeakReference<Chat1Fragment> fragWeak;

        public MyHandler(Chat1Fragment fragment) {
            this.fragWeak = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Chat1Fragment fragment = fragWeak.get();
            if (fragment == null || fragment.isDetached()) {
                Log.e("弱引用类调用错误!", "Chat1:can not refer!");
                return;
            }
            if (msg.what == 2) {
                //获得主页数据
                fragment.mainDataList.clear();
                ArrayList<Chat1FragData> list = (ArrayList<Chat1FragData>) msg.obj;
                fragment.mainDataList.addAll(list);

                if (fragment.isSearching) {
                    //如果还在搜索就再执行一次搜索逻辑
                    fragment.searchEvent(fragment.searchContent);
                } else {
                    //如果不在搜索就随机排列数据列表，伪刷新
                    fragment.searchDataList.clear();
                    fragment.searchDataList.addAll(fragment.mainDataList);
                    Collections.shuffle(fragment.searchDataList);
                    fragment.adapter.notifyDataSetChanged();
                }
                fragment.c1SRL.setRefreshing(false);
                fragment.isRefreshing = false;

            } else if (msg.what == 3) {
                //给搜索数据
                ArrayList<Chat1FragData> data = (ArrayList<Chat1FragData>) msg.obj;
                if (data == null) {
                    //没有数据，则清空原本的数据
                    Log.d("搜索数据日志:", "Chat1:the result is empty!");
                    Toast.makeText(fragment.getContext(), "搜索为空！不存在相关内容!", Toast.LENGTH_SHORT).show();
                    fragment.searchDataList.clear();
                } else {
                    //清空原本的数据，添加返回的数据
                    fragment.searchDataList.clear();
                    fragment.searchDataList.addAll(data);
                    Log.d("搜索数据日志:", "Chat1:the result is exist!");
                }
            } else if (msg.what == -1) {
                Toast.makeText(fragment.getContext(), "删除失败!请重新登录!", Toast.LENGTH_SHORT).show();
                SharedPreferenceHandler.clearPersonnel(fragment.getContext());
                SharedPreferenceHandler.clearSecret(fragment.getContext());
                Intent intent = new Intent(fragment.getContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                fragment.getContext().startActivity(intent);
            } else if (msg.what == 1) {
                Toast.makeText(fragment.getContext(), "删除成功!刷新以查看结果。", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 0) {
                Toast.makeText(fragment.getContext(), "出错!" + msg.obj.toString(), Toast.LENGTH_SHORT).show();
            }
            fragment.adapter.notifyDataSetChanged();
        }
    }

    //设定适配器，初始化数据列表
    private void initEvent() {
        searchDataList.addAll(mainDataList);
        adapter = new Chat1FragAdapter(searchDataList);
        c1RV.setAdapter(adapter);
        c1RV.setLayoutManager(new LinearLayoutManager(getContext()));

        int role = -1;
        try {
            SharedPreferences sp = SecurityHandle.getEncryptedSharedPreferences(getContext());
            role = sp.getInt("role", -1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        AdminNet adminNet = new AdminNet(handler);
        if (role == 1) {
            adapter.setChat1Listener(data -> {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this.getContext());
                dialog.setTitle("你确定要删除这条动态吗?");
                dialog.setMessage("这会清除该动态的一切数据!");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确认", (dialogInterface, i) -> adminNet.deleteChatHandler(rootView, data));
                dialog.setNegativeButton("取消", (DialogInterface, i) -> {
                });
                dialog.show();
            });
        }

        if (rootView != null) {
            chatNet.requestData(rootView);
        }
    }

    //搜索逻辑
    private void initSearch() {
        //接收chatFragment传来的搜索要求
        viewModel = new ViewModelProvider(requireActivity()).get(ChatFragViewModel.class);
        //监听搜索操作
        viewModel.getData().observe(getViewLifecycleOwner(), data -> {
            //把搜索内容绑定到全局
            searchContent = data.trim();
            //搜索内容不是空的，执行搜索逻辑
            if (!data.trim().isEmpty()) {
                searchEvent(data.trim());
                isSearching = true;
            } else {
                //搜索内容是空的，回到原始页面
                searchDataList.clear();
                searchDataList.addAll(mainDataList);
                adapter.notifyDataSetChanged();
                isSearching = false;
            }
        });
    }

    //搜索逻辑
    private void searchEvent(String query) {
        if (query == null || query.isEmpty()) {
            searchDataList.addAll(mainDataList);
            adapter.notifyDataSetChanged();
        } else {
            chatNet.searchData(rootView, query);
        }
    }

    private void initRefresh() {
        refreshListener = () -> {
            chatNet.requestData(rootView);
            isRefreshing = true;
        };
        c1SRL.setOnRefreshListener(refreshListener);
    }
}
