package com.example.dell.lab11;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class detail extends AppCompatActivity {
    private ProgressBar bar;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        list = findViewById(R.id.detail_list_view);
        bar = findViewById(R.id.detail_progress_bar);

        String userName = getIntent().getStringExtra(MainActivity.KEY_EXTRA);
        ServiceFactory.createRetrofit("https://api.github.com")
                .create(GithubService.class)
                .getRepos(userName)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Repos>>() {
                    public void onCompleted() {
                        bar.setVisibility(View.GONE);
                        list.setVisibility(View.VISIBLE);
                    }

                    public void onError(Throwable throwable) {
                        Toast.makeText(detail.this, "An Error Occurred", Toast.LENGTH_SHORT).show();
                        bar.setVisibility(View.GONE);
                        list.setVisibility(View.VISIBLE);
                        throwable.printStackTrace();
                    }
                    public void onNext(List<Repos> list) {
                        setupListViewData(list);
                    }
                });
    }

    private void setupListViewData(List<Repos> reposList) {
        List<Map<String, String>> repoListData = new ArrayList<>();
        for (Repos r:reposList) {
            Map<String, String> temp = new LinkedHashMap<>();
            temp.put("name", r.getName());
            temp.put("language", r.getLanguage());
            temp.put("intro", r.getDescription());
            repoListData.add(temp);
        }
        SimpleAdapter mSimpleAdapter = new SimpleAdapter(this, repoListData,
                R.layout.detail_item,
                new String[]{"name", "language", "intro"},
                new int[]{R.id.name, R.id.language, R.id.introduction});
        list.setAdapter(mSimpleAdapter);
    }
}
