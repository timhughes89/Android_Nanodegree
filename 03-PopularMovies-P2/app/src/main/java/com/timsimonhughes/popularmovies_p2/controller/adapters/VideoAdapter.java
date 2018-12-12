package com.timsimonhughes.popularmovies_p2.controller.adapters;

import android.content.Context;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.timsimonhughes.popularmovies_p2.R;
import com.timsimonhughes.popularmovies_p2.model.Video;
import com.timsimonhughes.popularmovies_p2.utils.MovieUtils;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.MyViewHolder> {

    private Context mContext;
    private List<Video> mVideoList;

    public VideoAdapter(Context mContext, List<Video> trailerList){
        this.mContext = mContext;
        this.mVideoList = trailerList;
    }

    @Override
    public VideoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_video_card, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final VideoAdapter.MyViewHolder holder, int position) {
        final Video video = mVideoList.get(position);

        holder.mTextViewVideoTitle.setText(video.getName());
        holder.mTextViewVideoType.setText(video.getType());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieUtils.launchVideo(mContext, video.getKey());
            }
        });
    }

    @Override
    public int getItemCount(){
        return mVideoList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView mTextViewVideoTitle, mTextViewVideoType;
        ImageView mImageViewVideo;

        MyViewHolder(View view){
            super(view);

            mImageViewVideo = (ImageView) view.findViewById(R.id.iv_video_image);
            mTextViewVideoTitle = (TextView) view.findViewById(R.id.tv_video_name);
            mTextViewVideoType = (TextView) view.findViewById(R.id.tv_video_type);
        }
    }
}
