package com.example.menutodaytest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class WesternFoodActivity extends AppCompatActivity {

    private static final int ITEMS_PER_PAGE = 40;
    private ArrayList<String> menus = new ArrayList<>();
    private List<String> imageUrls = new ArrayList<>();

    private List<Bitmap> bitmaps = new ArrayList<>();

    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    ArrayList<String> rowsArrayList = new ArrayList<>();
    List<Pair<String, Bitmap>> menuItems = new ArrayList<>();

    TextView textView;
    boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_westernfood);
        textView = findViewById(R.id.tvItem);
        recyclerView = findViewById(R.id.recyclerView);
        populateData();
        initAdapter();
        initScrollListener();
    }

    private void populateData() {
        try {
            Document firstPageDoc = Jsoup.connect("https://www.10000recipe.com/recipe/list.html?cat4=65&order=reco&page=1").get();
            int totalItems = Integer.parseInt(firstPageDoc.getElementsByAttributeValue("class", "m_list_tit").select("b").text().replaceAll(",", ""));
            int totalPages = (int) Math.ceil((double) totalItems / ITEMS_PER_PAGE);
            int remainder = (int) Math.ceil((double) totalItems % ITEMS_PER_PAGE);

            int page = (int) (Math.random() * totalPages) + 1;
            String pageUrl = "https://www.10000recipe.com/recipe/list.html?cat4=65&order=reco&page=" + page;

            Observable.create((ObservableOnSubscribe<List<Pair<String, Bitmap>>>) emitter -> {
                List<Pair<String, Bitmap>> menuItems = new ArrayList<>();
                try {
                    menuItems.add(getMenuItem(pageUrl, totalPages, remainder));
                } catch (IOException e) {
                    emitter.onError(e);
                    return;
                }
                emitter.onNext(menuItems);
                emitter.onComplete();
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(menuItems -> {
                int i = 0;
                while (i < 6) {
                    textView.setText(menuItems.get(i).first);
                    i++;
                }
            }, throwable -> {
                throwable.printStackTrace();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private Pair<String, Bitmap> getMenuItem(String pageUrl, int totalPages, int remainder) throws IOException {
            Document doc = Jsoup.connect(pageUrl).get();
            Elements titleElements = doc.getElementsByAttributeValue("class", "common_sp_caption_tit line2");
            Elements thumbElements = doc.getElementsByAttributeValue("class", "common_sp_link");
            for(int i = 0; i < 6; i++) {
                String menu = titleElements.get(i).text();
                String imgUrl = thumbElements.get(i).select("> img").attr("src");
                try {
                    URL url = new URL(imgUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    return Pair.create(menu, bitmap);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

    private void initAdapter() {

        recyclerViewAdapter = new RecyclerViewAdapter(rowsArrayList);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == rowsArrayList.size() - 1) {
                        //bottom of list!
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });


    }

    private void loadMore() {
        menuItems.add(null);
        recyclerViewAdapter.notifyItemInserted(rowsArrayList.size() - 1);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                menuItems.remove(menuItems.size() - 1);
                int scrollPosition = menuItems.size();
                recyclerViewAdapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize + 6;

                while (currentSize - 1 < nextLimit) {
                    rowsArrayList.add("Item " + currentSize);
                    currentSize++;
                }                    // 이부분만 populate처럼 바꾸면 될 것 같은데...

                recyclerViewAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);


    }
}
