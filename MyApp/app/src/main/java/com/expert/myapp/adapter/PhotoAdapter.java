package com.expert.myapp.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.expert.myapp.R;
import com.expert.myapp.pojo.Photo;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>{

    private List<Photo> items;

    public PhotoAdapter(List<Photo> items){
        this.items = items;
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;

        private TextView comentary;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            comentary = (TextView) itemView.findViewById(R.id.comentary);

        }
    }

    @Override
    public PhotoAdapter.PhotoViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.photo_adapter, viewGroup, false);
        return new PhotoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PhotoAdapter.PhotoViewHolder holder, int position) {

        holder.image.setImageBitmap(toBitmap(items.get(position).getImage()));
        holder.comentary.setText(items.get(position).getComment());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private Bitmap toBitmap(byte[] foto) {

        Bitmap bmp = BitmapFactory.decodeByteArray(foto, 0, foto.length);
        return Bitmap.createScaledBitmap(bmp, 100, 150, false);

    }

}
