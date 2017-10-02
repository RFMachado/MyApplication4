package com.example.nodo.myapplication4;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import com.example.nodo.myapplication4.entities.Repositories;
import com.example.nodo.myapplication4.entities.Repository;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import java.util.logging.LogRecord;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.internal.DebouncingOnClickListener;
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

    Validator validator = new Validator(this);
    RepositoryInterface repositoryInterface ;

    @BindView(R.id.rv)
    RecyclerView rv;

    Repositories repositories;
    CardAdapter adapter;
    Call<Repositories> call;
    long DELAY = 1000;

    final android.os.Handler handler = new android.os.Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);



        adapter = new CardAdapter(repositories);
        rv.setAdapter(adapter);

        //Metodo botao pesquisa teclado

        editText.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //validator.validate();

                rv.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                closeInput(editText);

                handler.removeCallbacksAndMessages(null);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadData();
                    }
                }, DELAY);

            }
        });



        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //onClickSearch();
                    return true;
                }
                return false;
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

    }

    private void loadData() {
        repositoryInterface = RepositoryApi.conect();

        if (call != null && call.isCanceled())
            call.cancel();

        call = repositoryInterface.getRepos(editText.getText().toString());

        call.enqueue(new Callback<Repositories>() {
            @Override
            public void onResponse(Call<Repositories> call, Response<Repositories> response) {

                if (response.isSuccessful()) {
                    repositories = response.body();
                    adapter.repositories = repositories;

                    adapter.notifyDataSetChanged();

                    System.out.println("Connection OK !");
                }

                progressBar.setVisibility(View.GONE);
                rv.setVisibility(View.VISIBLE);

            }

            @Override
            public void onFailure(Call<Repositories> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(MainActivity.this,"Connection Fail",Toast.LENGTH_SHORT).show();
            }
        });

    }



    public void closeInput(final View caller) {
        if (caller == null)
            return;

        caller.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) caller.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(caller.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            }
        }, 100);
    }



}
