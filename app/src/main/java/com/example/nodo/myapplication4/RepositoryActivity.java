package com.example.nodo.myapplication4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.nodo.myapplication4.entities.Download;
import com.example.nodo.myapplication4.entities.Repositories;
import com.example.nodo.myapplication4.entities.Repository;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import us.feras.mdv.MarkdownView;

public class RepositoryActivity extends AppCompatActivity {

    RepositoryInterface repositoryInterface;

    @BindView(R.id.mark_down)
    MarkdownView markdownView;

    @BindView(R.id.progress_bar_md)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository);
        ButterKnife.bind(this);

        Repository repository = (Repository) getIntent().getSerializableExtra("download");

        repositoryInterface = RepositoryApi.conect();

        Call<Download> call = repositoryInterface.getDownload(repository.owner.login,repository.name);

        call.enqueue(new Callback<Download>() {

            @Override
            public void onResponse(Call<Download> call, Response<Download> response) {

                if(response.isSuccessful()){

                    Download download = response.body();
                    markdownView.loadMarkdownFile(download.urldonwload);
                    progressBar.setVisibility(View.GONE);
                    markdownView.setVisibility(View.VISIBLE);

                }else{

                    Toast.makeText(RepositoryActivity.this,"No text",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RepositoryActivity.this, MainActivity.class);
                    startActivity(intent);

                }

            }

            @Override
            public void onFailure(Call<Download> call, Throwable t) {
                Toast.makeText(RepositoryActivity.this,"Connection Fail",Toast.LENGTH_SHORT).show();
            }
        });

    }

}
