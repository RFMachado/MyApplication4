package com.example.nodo.myapplication4;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapDrawableTransformation;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.example.nodo.myapplication4.entities.Repositories;
import com.example.nodo.myapplication4.entities.Repository;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nodo on 29/09/17.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {


    public List<Repository> repositories;


    public CardAdapter(List<Repository> repositories) {
        this.repositories = repositories ;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.layout_card, parent, false);

        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Context context = holder.itemView.getContext();
        final Repository repository = repositories.get(position);

        holder.textViewId.setText(repository.owner.id);
        holder.textViewLogin.setText(repository.owner.login);
        Glide.with(context)
                .load(repository.owner.avatarUrl)
                .apply(RequestOptions.circleCropTransform())  //Crop sobre a imagem
                .into(holder.imageView);



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RepositoryActivity.class);

                intent.putExtra("download", repository);

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return repositories.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textViewId)
        TextView textViewId;

        @BindView(R.id.textViewLogin)
        TextView textViewLogin;

        @BindView(R.id.imageView)
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }

    }

}
