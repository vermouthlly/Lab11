package com.example.dell.lab11;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {
    private EditText userSearch;
    private ProgressBar progressBar;

    private CardAdapter mCardAdapter;

    public final static String KEY_EXTRA = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.main_progress_bar);
        Button clearButton = findViewById(R.id.clear);
        Button fetchButton = findViewById(R.id.fetch);
        userSearch = findViewById(R.id.name);

        RecyclerView mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCardAdapter = new CardAdapter();
        mRecyclerView.setAdapter(mCardAdapter);

        clearButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mCardAdapter.removeAllItem();
            }
        });

        fetchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);

                String userName = userSearch.getText().toString();
                ServiceFactory.createRetrofit("https://api.github.com")
                        .create(GithubService.class)
                        .getUser(userName)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Github>() {
                            public final void onCompleted() {
                                progressBar.setVisibility(View.GONE);
                            }

                            public void onError(Throwable throwable) {
                                Toast.makeText(MainActivity.this, "请确认你搜索的用户存在", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                throwable.printStackTrace();
                            }

                            public void onNext(Github github) {
                                mCardAdapter.appendItem(github);
                            }
                        });
            }
        });

        mCardAdapter.setOnItemClickListener(new CardAdapter.OnRecyclerViewItemClickListener() {
            public void onClick(int position) {
                Intent intent = new Intent(MainActivity.this, detail.class);
                intent.putExtra(KEY_EXTRA, mCardAdapter.getItem(position).getLogin());
                MainActivity.this.startActivity(intent);
            }

            public void onLongClick(int position) {
                mCardAdapter.removeItem(position);
            }
        });
    }
}
