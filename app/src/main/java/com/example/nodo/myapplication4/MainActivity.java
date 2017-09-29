package com.example.nodo.myapplication4;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.example.nodo.myapplication4.entities.Repositories;
import com.example.nodo.myapplication4.entities.Repository;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements Validator.ValidationListener{

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        validator.setValidationListener(this);
        ButterKnife.bind(this);

        //Metodo botao pesquisa teclado
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    onClickSearch();
                    return true;
                }
                return false;
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

    }

    @OnClick(R.id.button_search)
    public void onClickSearch() {

        validator.validate();

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


    public void onValidationSucceeded() {

        rv.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);
        closeInput(editText);

        repositoryInterface = RepositoryApi.conect();
        Call<Repositories> call = repositoryInterface.getRepos(editText.getText().toString());

        call.enqueue(new Callback<Repositories>() {
            @Override
            public void onResponse(Call<Repositories> call, Response<Repositories> response) {

                if (response.isSuccessful()) {
                    Repositories repositories = response.body();

                    CardAdapter adapter = new CardAdapter(repositories);
                    rv.setAdapter(adapter);
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


    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

}
