package com.example.appfala;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class ViewHolder extends RecyclerView.ViewHolder {

    View view;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
    }

    public void setdetails(Context context, String title, String image){

        TextView textView = view.findViewById(R.id.tv_nomeItem);
        ImageView imageView = view.findViewById(R.id.iv_imagemItem);

        textView.setText(title);
        Picasso.get().load(image).into(imageView);

        //Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        //itemView.startAnimation(animation);

    }

}
