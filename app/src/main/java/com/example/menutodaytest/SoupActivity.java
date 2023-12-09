package com.example.menutodaytest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class SoupActivity extends AppCompatActivity {
    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    ImageView imageView4;
    CardView cardView1;
    CardView cardView2;
    CardView cardView3;
    CardView cardView4;

    private MenuRepository menuRepository = new MenuRepository();
    private CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soup);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar (); //앱바 제어를 위해 툴바 액세스
        actionBar.setDisplayHomeAsUpEnabled(true);
        textView1 = findViewById(R.id.textView7);
        textView2 = findViewById(R.id.textView8);
        textView3 = findViewById(R.id.textView9);
        textView4 = findViewById(R.id.textView10);
        imageView1 = findViewById(R.id.imageView4);
        imageView2 = findViewById(R.id.imageView5);
        imageView3 = findViewById(R.id.imageView6);
        imageView4 = findViewById(R.id.imageView7);
        cardView1 = findViewById(R.id.cardView1);
        cardView2 = findViewById(R.id.cardView2);
        cardView3 = findViewById(R.id.cardView3);
        cardView4 = findViewById(R.id.cardView4);
        List<String> recipeLinks = loadRecipes();
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(recipeLinks.get(0)));
                startActivity(intent);
            }
        });

        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(recipeLinks.get(1)));
                startActivity(intent);
            }
        });

        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(recipeLinks.get(2)));
                startActivity(intent);
            }
        });

        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(recipeLinks.get(3)));
                startActivity(intent);
            }
        });

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId ()) {
            case android.R.id.home: //툴바 뒤로가기버튼 눌렸을 때 동작
                finish ();
                return true;
            default:
                return super.onOptionsItemSelected (item);
        }
    }

    private List<String> loadRecipes() {
        List<String> links = new ArrayList<>();
        disposables.add(menuRepository.getRecipes("https://www.10000recipe.com/recipe/list.html?cat4=55&order=reco&page=1")
                .subscribe(
                        menuItems -> {
                            textView1.setText(menuItems.get(0).getTitle());
                            textView2.setText(menuItems.get(1).getTitle());
                            textView3.setText(menuItems.get(2).getTitle());
                            textView4.setText(menuItems.get(3).getTitle());
                            imageView1.setImageBitmap(menuItems.get(0).getBitmap());
                            imageView2.setImageBitmap(menuItems.get(1).getBitmap());
                            imageView3.setImageBitmap(menuItems.get(2).getBitmap());
                            imageView4.setImageBitmap(menuItems.get(3).getBitmap());
                            links.add(menuItems.get(0).getLink());
                            links.add(menuItems.get(1).getLink());
                            links.add(menuItems.get(2).getLink());
                            links.add(menuItems.get(3).getLink());
                        },
                        throwable -> {
                            Log.e("RxJava", "Error loading menu items", throwable);
                            throwable.printStackTrace();
                        }
                ));
        return links;
    }
}
