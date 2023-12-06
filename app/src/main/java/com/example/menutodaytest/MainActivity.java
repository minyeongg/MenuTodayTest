package com.example.menutodaytest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.ActionBar;
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

import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity {
    private static final int ITEMS_PER_PAGE = 40;
    public static final int REQUEST_CODE_MENU = 101;

    Button western;
    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;

    Bitmap bitmap1;
    Bitmap bitmap2;
    Bitmap bitmap3;

    TextView textView1;
    TextView textView2;
    TextView textView3;

    private MenuRepository menuRepository = new MenuRepository();
    private CompositeDisposable disposables = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        western = findViewById(R.id.button6);
        imageView1 = findViewById(R.id.imageView);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);

        textView1 = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        loadMenuItems();
        western.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WesternFoodActivity.class);
                startActivityForResult(intent, REQUEST_CODE_MENU);package com.example.menutoday;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

                public class WesternFoodActivity extends AppCompatActivity {
                    private static final int ITEMS_PER_PAGE = 40;
                    private ArrayList<String> menus = new ArrayList<>();
                    private List<String> imageUrls = new ArrayList<>();

                    private List<Bitmap> bitmaps = new ArrayList<>();

                    RecyclerView recyclerView;
                    RecyclerViewAdapter recyclerViewAdapter;


                    boolean isLoading = false;

                    @Override
                    protected void onCreate(Bundle savedInstanceState) {
                        super.onCreate(savedInstanceState);
                        setContentView(R.layout.activity_westernfood);

                        recyclerView = findViewById(R.id.recyclerView);
                        // 랜덤 페이지에서 메뉴 6개씩 로드
                        loadData();
                        initAdapter();
                        initScrollListener();


                    }

                    // 페이지에 해당하는 데이터를 가져와 menus 및 imageUrls에 추가
                    private void loadData() {
                        // 웹 크롤링한 데이터를 menus와 imageUrls에 추가
                        Thread uThread = new Thread() {
                            @Override
                            public void run() {
                                try {
                                    Document firstPageDoc = Jsoup.connect("https://www.10000recipe.com/recipe/list.html?cat4=65&order=reco&page=1").get();
                                    int totalItems = Integer.parseInt(firstPageDoc.getElementsByAttributeValue("class", "m_list_tit").select("b").text().replaceAll(",", ""));

                                    // Calculate the number of pages
                                    int totalPages = (int) Math.ceil((double) totalItems / ITEMS_PER_PAGE);
                                    Log.d("totalPages", String.valueOf(totalPages)); // 5277인지 확인
                                    int remainder = (int) Math.ceil((double) totalItems % ITEMS_PER_PAGE);
                                    Log.d("remaider", String.valueOf(remainder));

                                    int page; // 랜덤으로 페이지 한 개 추출
                                    page = (int) (Math.random() * totalPages) + 1;
                                    Log.d("test", "pages: " + String.valueOf(page));

                                    String pageUrl = "https://www.10000recipe.com/recipe/list.html?cat4=65&order=reco&page=" + page;
                                    try {
                                        Document doc = Jsoup.connect(pageUrl).get();
                                        Elements title = doc.getElementsByAttributeValue("class", "common_sp_caption_tit line2");
                                        Elements thumb = doc.getElementsByAttributeValue("class", "common_sp_link");
                                        for (int i = 0; i < 6; i++) {
                                            String menu = title.get(i).text();
                                            String imgUrl = thumb.get(i).select("> img").attr("src");
                                            Log.d("test", "menu: " + menu);
                                            menus.add(menu);
                                            Log.d("test", "Image URL: " + imgUrl);
                                            imageUrls.add(imgUrl);
                                            try {
                                                URL url = new URL(imgUrl);

                                                // web에서 이미지를 가져와 ImageView에 저장할 Bitmap을 만든다.
                                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                                conn.setDoInput(true); // 서버로부터 응답 수신
                                                conn.connect();

                                                InputStream is = conn.getInputStream(); //inputStream 값 가져오기
                                                Bitmap bitmap = BitmapFactory.decodeStream(is); // Bitmap으로 변환
                                                bitmaps.add(bitmap);
                                            } catch (MalformedURLException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        };
                        uThread.start();

                    }



                    private void initAdapter() {

                        recyclerViewAdapter = new RecyclerViewAdapter(menus);
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
                                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == menus.size() - 1) {
                                        //bottom of list!
                                        loadMore();
                                        isLoading = true;
                                    }
                                }
                            }
                        });


                    }

                    private void loadMore() {
                        menus.add(null);
                        recyclerViewAdapter.notifyItemInserted(menus.size() - 1);


                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                menus.remove(menus.size() - 1);
                                int scrollPosition = menus.size();
                                recyclerViewAdapter.notifyItemRemoved(scrollPosition);
                                int currentSize = scrollPosition;
                                int nextLimit = currentSize + 6;

                                recyclerViewAdapter.notifyDataSetChanged();
                                isLoading = false;
                            }
                        }, 2000);


                    }
                }


            }
        });

    }

    private void loadMenuItems() {
        disposables.add(menuRepository.getMenuItems()
                .subscribe(
                        menuItems -> {
                            textView1.setText(menuItems.get(0).first);
                            Log.d("check", menuItems.get(0).first);
                            textView2.setText(menuItems.get(1).first);
                            textView3.setText(menuItems.get(2).first);
                            imageView1.setImageBitmap(menuItems.get(0).second);
                            imageView2.setImageBitmap(menuItems.get(1).second);
                            imageView3.setImageBitmap(menuItems.get(2).second);
                        },
                        throwable -> {
                            Log.e("RxJava", "Error loading menu items", throwable);
                            throwable.printStackTrace();
                        }
                ));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.dispose();
    }
}
