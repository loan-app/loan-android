package com.mod.jishidai.ui.activity.feedback;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.lib.core.utils.OnItemClickListener;
import com.luck.picture.lib.entity.LocalMedia;
import com.mod.jishidai.R;

import java.util.List;

/**
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

    private List<LocalMedia> photoPaths;
    private LayoutInflater inflater;

    private Context mContext;

    public final static int TYPE_ADD = 1;
    public final static int TYPE_PHOTO = 2;

    public final static int MAX = 3;
    OnItemClickListener onItemClickListener;

    public PhotoAdapter(Context mContext, List<LocalMedia> photoPaths) {
        this.photoPaths = photoPaths;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);

    }


    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        switch (viewType) {
            case TYPE_ADD:
                itemView = inflater.inflate(R.layout.item_add, parent, false);
                break;
            case TYPE_PHOTO:
                itemView = inflater.inflate(R.layout.picker_item_photo, parent, false);
                break;
        }
        return new PhotoViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, final int position) {

        if (getItemViewType(position) == TYPE_PHOTO) {
            LocalMedia media = photoPaths.get(position);
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.color.color_f6f6f6)
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(mContext)
                    .load(media.getPath())
                    .apply(options)
                    .into(holder.ivPhoto);
            holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (media != null && photoPaths.contains(media)) {
                        photoPaths.remove(media);
                        notifyDataSetChanged();
                    }
                }
            });
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(v, position, (getItemViewType(position) == TYPE_PHOTO) ? photoPaths.get(position) : null);

            }
        });
    }


    @Override
    public int getItemCount() {
        int count = (photoPaths != null ? photoPaths.size() : 0) + 1;
        if (count > MAX) {
            count = MAX;
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == photoPaths.size() && position != MAX) ? TYPE_ADD : TYPE_PHOTO;
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPhoto;
        private ImageView iv_delete;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);
            iv_delete = (ImageView) itemView.findViewById(R.id.iv_delete);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;

    }

    public void setList(List<LocalMedia> list) {
        this.photoPaths = list;
    }

}
