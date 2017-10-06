package com.example.nodo.myapplication4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.nodo.myapplication4.entities.Download;
import com.example.nodo.myapplication4.entities.Repository;

import java.util.Observable;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import us.feras.mdv.MarkdownView;

public class RepositoryActivity extends AppCompatActivity {

    @Inject
    RepositoryInterface repositoryInterface;

    @BindView(R.id.mark_down)
    MarkdownView markdownView;

    @BindView(R.id.progress_bar_md)
    ProgressBar progressBar;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository);

        ((MyApplication) getApplication()).getNetComponent().inject(this);
        ButterKnife.bind(this);
        Repository repository = (Repository) getIntent().getSerializableExtra("download");

        Disposable disposable = repositoryInterface
                .getDownload(repository.owner.login,repository.name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {

                            Download download = data;
                            markdownView.loadMarkdownFile(download.urldonwload);
                            progressBar.setVisibility(View.GONE);
                            markdownView.setVisibility(View.VISIBLE);

                        },
                        throwable -> {
                            Toast.makeText(RepositoryActivity.this,"No text",Toast.LENGTH_SHORT).show();
                            finish();
                            throwable.printStackTrace();
                        });

        compositeDisposable.add(disposable);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

}
