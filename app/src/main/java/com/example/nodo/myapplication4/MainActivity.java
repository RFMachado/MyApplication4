package com.example.nodo.myapplication4;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AndroidRuntimeException;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.nodo.myapplication4.entities.Repositories;
import com.example.nodo.myapplication4.entities.Repository;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.AsyncSubject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity{

    @BindView(R.id.button_search)
    Button buttonSearch;

    @BindView(R.id.loading)
    ProgressBar progressBar;

    @BindView(R.id.imageView)
    ImageView imageView;

    @BindView(R.id.textview)
    TextView textView;

    @NotEmpty
    @BindView(R.id.edittext)
    EditText editText;

    @BindView(R.id.rv)
    RecyclerView rv;

    @Inject
    RepositoryInterface repositoryInterface ;


    List<Repository> items = new ArrayList<>();

    CardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((MyApplication) getApplication()).getNetComponent().inject(this);
        ButterKnife.bind(this);

        adapter = new CardAdapter(items);
        rv.setAdapter(adapter);


        RxTextView.textChanges(editText)
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(text -> {
                    if (TextUtils.isEmpty(text)) {
                        showEmptyState();
                        return Observable.empty();
                    }

                    showLoading();
                    return repositoryInterface.getRepos(text.toString())
                            .onErrorResumeNext(Observable.empty())
                            .flatMapIterable(l -> l.items)
                            .toSortedList((a, b) ->
                                    a.owner.login.compareTo(b.owner.login)
                            )
                            .toObservable()
                            .subscribeOn(Schedulers.io());

                })

                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        data -> {
                            hideLoading();
                            adapter.repositories = data;
                            adapter.notifyDataSetChanged();
                        },
                        throwable -> {
                            throwable.printStackTrace();
                        });

        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                //onClickSearch();
                return true;
            }
            return false;
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

    }

    private void showEmptyState() {
        imageView.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);
        rv.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    private void showLoading() {
        rv.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);
        closeInput(editText);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
        rv.setVisibility(View.VISIBLE);
    }

    public void closeInput(final View caller) {
        if (caller == null)
            return;

        caller.postDelayed(() -> {
            InputMethodManager imm = (InputMethodManager) caller.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(caller.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        }, 100);
    }

}
