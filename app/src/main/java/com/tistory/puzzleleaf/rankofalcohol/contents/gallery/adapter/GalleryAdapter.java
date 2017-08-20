package com.tistory.puzzleleaf.rankofalcohol.contents.gallery.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tistory.puzzleleaf.rankofalcohol.R;
import com.tistory.puzzleleaf.rankofalcohol.model.RankObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cmtyx on 2017-08-14.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    public interface OnGalleryObjectClickListener{
        public void onGalleryObjectSelected(RankObject obj);
    }

    private OnGalleryObjectClickListener onGalleryObjectClickListener;
    private LayoutInflater mInflater;
    private List<RankObject> res;


    public GalleryAdapter(Context context, List<RankObject> res){
        this.mInflater = LayoutInflater.from(context);
        this.res = res;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_gallery,parent,false);
        return new ViewHolder(view);
    }

    public void setOnGalleryObjectClickListener(OnGalleryObjectClickListener onGalleryObjectClickListener){
        this.onGalleryObjectClickListener = onGalleryObjectClickListener;
    }


    @Override
    public int getItemCount() {
        return res.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        rankRatingBarInit(holder);
        Glide.with(mInflater.getContext())
                .load(res.get(position).getImgKey())
                .apply(new RequestOptions().placeholder(R.drawable.image_loading))
                .into(holder.galleryImageView);

        holder.galleryBrandName.setText(res.get(position).getBrandName());
        holder.galleryRating.setText(String.format("%.2f",res.get(position).getScore()));
        holder.galleryRatingBar.setRating(Float.parseFloat(String.valueOf(res.get(position).getScore())));
        holder.galleryDegree.setText(String.valueOf(res.get(position).getAlcoholDegree()));
        galleryItemClick(holder, position);

    }

    private void galleryItemClick(ViewHolder holder, final int position){
        holder.galleryItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGalleryObjectClickListener.onGalleryObjectSelected(res.get(position));
            }
        });
    }

    private void rankRatingBarInit(ViewHolder holder){
        LayerDrawable stars = (LayerDrawable)holder.galleryRatingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(ContextCompat.getColor(mInflater.getContext(),R.color.colorStar), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.gallery_item) CardView galleryItem;
        @BindView(R.id.gallery_image) ImageView galleryImageView;
        @BindView(R.id.gallery_rating_bar) RatingBar galleryRatingBar;
        @BindView(R.id.gallery_brand_name) TextView galleryBrandName;
        @BindView(R.id.gallery_alcohol_degree) TextView galleryDegree;
        @BindView(R.id.gallery_rating) TextView galleryRating;
        private ViewHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
        }
    }
}
