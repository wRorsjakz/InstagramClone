package com.example.nicho.instagramclone.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.nicho.instagramclone.Model.PhoneImage;
import com.example.nicho.instagramclone.R;
import com.example.nicho.instagramclone.Util.MyViewHelper;

import java.util.List;

public class ProfileRecyclerViewAdapter extends RecyclerView.Adapter<ProfileRecyclerViewAdapter.MyViewHolder>
    {
        private List<PhoneImage> listOfPhoneImages;
        private Context context;

        public ProfileRecyclerViewAdapter(List<PhoneImage> listOfPhoneImages, Context context)
            {
                this.listOfPhoneImages = listOfPhoneImages;
                this.context = context;
            }

        public class MyViewHolder extends RecyclerView.ViewHolder
            {
                private ImageView imageView;
                public MyViewHolder(View itemView)
                    {
                        super(itemView);

                        imageView = itemView.findViewById(R.id.profilefragment_recyclerview_imageview);
                    }
            }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(context).inflate(R.layout.profilefragment_recyclerview_item, parent, false);

                MyViewHolder viewHolder = new MyViewHolder(view);
                return viewHolder;
            }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
            {
                int screenWidth = MyViewHelper.GetScreenWidth(context);
                int itemWidth = screenWidth/ 3;
                int itemHeight = itemWidth;
                holder.imageView.getLayoutParams().width = itemWidth;
                holder.imageView.getLayoutParams().height = itemHeight;

                RequestOptions requestOptions = new RequestOptions();
                requestOptions.placeholder(R.drawable.ic_launcher_background).centerCrop();
                Glide.with(context).load(listOfPhoneImages.get(position).getImageUrl()).apply(requestOptions).into(holder.imageView);
            }

        @Override
        public int getItemCount()
            {
                return listOfPhoneImages.size();
            }



    }
