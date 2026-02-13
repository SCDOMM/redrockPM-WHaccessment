package com.example.hachimi.View.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hachimi.R;
import com.example.hachimi.repository.model.HomeFragData;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * 包名称： com.example.hachimi.View.adapter
 * 类名称：HomeFragAdapter
 * 类描述：适配器，使用了三方库glide展示图片，同时设定了点击事件，展示详细的档案数据。
 * 创建人：韦西波
 */
public class HomeFragAdapter extends RecyclerView.Adapter<HomeFragAdapter.ViewHolder> {
    private ArrayList<HomeFragData> homeDataList;
    private static final ExecutorService imageLoad = Executors.newFixedThreadPool(4);

    //接口，，，
    public interface HomeFragListener {
        void onClick(HomeFragData data);
    }

    private HomeFragListener listener;

    public void setHomeFragListener(HomeFragListener listener) {
        this.listener = listener;
    }


    public HomeFragAdapter(ArrayList<HomeFragData> dataList) {
        this.homeDataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_model, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(homeDataList.get(position), holder);
    }

    @Override
    public int getItemCount() {
        return homeDataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvDescribe;
        ImageView ivIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.homeModel_title);
            tvDescribe = itemView.findViewById(R.id.homeModel_description);
            ivIcon = itemView.findViewById(R.id.homeModel_image);
            itemView.setOnClickListener(view -> {
                //点击方法，，
                int pos = getAdapterPosition();
                HomeFragData data = homeDataList.get(pos);
                listener.onClick(data);
            });
        }

        public void bind(HomeFragData data, ViewHolder holder) {
            tvTitle.setText(data.getTitle());
            tvDescribe.setText(data.getDescribe());

            if (data.getImage() != null && !data.getImage().isEmpty()) {
                String imageUri = "data:image/png;base64," + data.getImage();
                Glide.with(holder.ivIcon)
                        .load(imageUri)
                        .placeholder(R.drawable.ic_default_iv)
                        .error(R.drawable.ic_default_iv)
                        .override(180, 180)
                        .centerCrop()
                        .into(holder.ivIcon);
            } else {
                holder.ivIcon.setImageResource(R.drawable.ic_default_iv);
            }

        }
    }
}
