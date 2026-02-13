package com.example.hachimi.View.adapter;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hachimi.R;
import com.example.hachimi.repository.model.Chat1FragData;
import com.example.hachimi.utils.SecurityHandle;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * 包名称： com.example.hachimi.View.adapter
 * 类名称：Chat1FragAdapter
 * 类描述：适配器，使用了三方库glide展示图片，管理员用户设置了点击事件，点击item后可以选择是否删除。
 * 创建人：韦西波
 */
public class Chat1FragAdapter extends RecyclerView.Adapter<Chat1FragAdapter.ViewHolder> {
    public ArrayList<Chat1FragData> dataList;
    private static final ExecutorService imageLoad = Executors.newFixedThreadPool(10);

    public Chat1FragAdapter(ArrayList<Chat1FragData> dataList) {
        this.dataList = dataList;
    }

    private Chat1Listener listener;

    public interface Chat1Listener {
        void onClickListener(Chat1FragData data);
    }

    public void setChat1Listener(Chat1Listener listener) {
        this.listener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat1_model, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Chat1FragAdapter.ViewHolder holder, int position) {
        holder.bind(dataList.get(position), holder);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvTime;
        TextView tvTitle;
        TextView tvDescribe;
        ImageView ivProfile;
        ImageView ivCover;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.chat1model_tv_title);
            tvDescribe = itemView.findViewById(R.id.chat1model_tv_describe);
            tvName = itemView.findViewById(R.id.chat1model_tv_name);
            tvTime = itemView.findViewById(R.id.chat1model_tv_time);
            ivCover = itemView.findViewById(R.id.chat1model_iv_Cover);
            ivProfile = itemView.findViewById(R.id.chat1model_iv_profile);

            int role = -1;
            try {
                SharedPreferences sp = SecurityHandle.getEncryptedSharedPreferences(itemView.getContext());
                role = sp.getInt("role", -1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (role == 1) {
                itemView.setOnClickListener(view -> {
                    int pos = getAdapterPosition();
                    Chat1FragData data = dataList.get(pos);
                    listener.onClickListener(data);
                });
            }

        }

        public void bind(Chat1FragData data, ViewHolder holder) {
            tvTitle.setText(data.getTitle());
            tvDescribe.setText(data.getDescribe());
            tvName.setText(data.getAuthorName());
            tvTime.setText(data.getTime());
            if (data.getProfile() != null && !data.getProfile().isEmpty()) {
                String profileUri = "data:image/png;base64," + data.getProfile();
                Glide.with(holder.ivProfile)
                        .load(profileUri)
                        .placeholder(R.drawable.ic_default_iv)
                        .error(R.drawable.ic_default_iv)
                        .override(80, 80)
                        .centerCrop()
                        .into(holder.ivProfile);
            } else {
                holder.ivProfile.setImageResource(R.drawable.ic_default_iv);
            }

            if (data.getCover() != null && !data.getCover().isEmpty()) {
                String coverUri = "data:image/png;base64," + data.getCover();
                Glide.with(holder.ivCover)
                        .load(coverUri)
                        .placeholder(R.drawable.ic_default_iv)
                        .error(R.drawable.ic_default_iv)
                        .override(360, 360)
                        .centerCrop()
                        .into(holder.ivCover);
            } else {
                holder.ivCover.setImageResource(R.drawable.ic_default_iv);
            }

        }
    }
}
