package com.example.nicho.instagramclone.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

public class ShareRecyclerViewAdapter extends RecyclerView.Adapter<ShareRecyclerViewAdapter.MyViewHolder>
    {
        private List<PhoneImage> listOfPhoneImage;
        private Context context;
        private ShareRecyclerViewInterface shareRecyclerViewInterface;

        private static String TAG = "ShareRecyclerViewAdapter";

        public ShareRecyclerViewAdapter(List<PhoneImage> listOfPhoneImage, Context context, ShareRecyclerViewInterface recyclerViewInterface )
            {
                this.listOfPhoneImage = listOfPhoneImage;
                this.context = context;
                this.shareRecyclerViewInterface = recyclerViewInterface;
            }

        public class MyViewHolder extends RecyclerView.ViewHolder
            {
                private ImageView imageView;

                public MyViewHolder(View itemView)
                    {
                        super(itemView);
                        imageView = itemView.findViewById(R.id.profilefragment_recyclerview_imageview);

                        imageView.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                    {
                                        shareRecyclerViewInterface.OnViewClicked(v, getAdapterPosition());

                                    }
                            });
                    }
            }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(context).inflate(R.layout.sharefragment_recyclerview_item, parent, false);
                MyViewHolder holder = new MyViewHolder(view);
                return holder;
            }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
            {
                int screenWidth = MyViewHelper.GetScreenWidth(context);
                int imageWidth = screenWidth / 3;
                int imageHeight = imageWidth;

                holder.imageView.getLayoutParams().width = imageWidth;
                holder.imageView.getLayoutParams().height = imageHeight;
                RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.ic_launcher_background);
                Glide.with(context).load(listOfPhoneImage.get(position).getImageUrl()).apply(requestOptions).into(holder.imageView);
            }

        @Override
        public int getItemCount()
            {
                return listOfPhoneImage.size();
            }

        /**
         * This interface's OnViewClicked passes the position of the image clicked and is triggered in MyViewHolder's onClickListener.
         */
        public interface ShareRecyclerViewInterface
        {
            void OnViewClicked(View v, int position);
        }


    }
