package com.example.menutodaytest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity {
    private static final int ITEMS_PER_PAGE = 40;
    public static final int REQUEST_CODE_MENU = 101;
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

        imageView1 = findViewById(R.id.imageView);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);

        textView1 = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        loadMenuItems();

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
