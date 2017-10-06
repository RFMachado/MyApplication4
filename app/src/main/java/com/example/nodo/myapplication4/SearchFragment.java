package com.example.nodo.myapplication4;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.nodo.myapplication4.entities.Repository;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by nodo on 06/10/17.
 */

public class SearchFragment extends Fragment {

    private String title;
    private int page;


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
    int DELAY = 500;

    CardAdapter adapter;


    public static SearchFragment newInstance(int page, String title) {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View  view = inflater.inflate(R.layout.fragment_search, container, false);

        adapter = new CardAdapter(items);
        rv.setAdapter(adapter);


        RxTextView.textChanges(editText)
                .debounce(DELAY, TimeUnit.MILLISECONDS)
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


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(layoutManager);
        rv.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));

        return view;

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

        }, DELAY);

    }

}
