package com.example.hachimi.View.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hachimi.R;
import com.example.hachimi.repository.model.SettingData;

import java.util.ArrayList;

/**
 * 包名称： com.example.hachimi.View.adapter
 * 类名称：SettingAdapter
 * 类描述：适配器，设置了点击事件，以打开对应的设置栏目。
 * 创建人：韦西波
 */
public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.ViewHolder> {
    private ArrayList<SettingData> list;
    private RegisterListener registerListener;

    public SettingAdapter(ArrayList<SettingData> list) {
        this.list = list;
    }

    public interface RegisterListener {
        void onClick(SettingData data);
    }

    public void setRegisterListener(RegisterListener registerListener) {
        this.registerListener = registerListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.setting_model, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(list.get(position), holder);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView settingName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            settingName = itemView.findViewById(R.id.setting_model_tv);
            itemView.setOnClickListener(view -> {
                int pos = getAdapterPosition();
                SettingData data = list.get(pos);
                registerListener.onClick(data);
            });
        }

        public void bindData(SettingData data, ViewHolder holder) {
            settingName.setText(data.getSettingName());
        }
    }
}
