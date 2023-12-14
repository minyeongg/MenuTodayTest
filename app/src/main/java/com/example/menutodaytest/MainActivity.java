package com.example.menutodaytest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

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

    DrawerLayout drawerLayout;

    NavigationView navigationView;

    Button rice;
    Button side;
    Button soup;
    Button noodle;
    Button soup2;
    Button western;
    Button dessert;
    Button region;

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
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        Menu menu = navigationView.getMenu();
        MenuItem menuItem1 = menu.findItem(R.id.menu1);
        rice = findViewById(R.id.button);
        side = findViewById(R.id.button2);
        soup = findViewById(R.id.button3);
        noodle = findViewById(R.id.button4);
        soup2 = findViewById(R.id.button5);
        western = findViewById(R.id.button6);
        dessert = findViewById(R.id.button7);

        imageView1 = findViewById(R.id.imageView);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);

        textView1 = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 여기에 네비게이션 뷰를 여는 코드를 추가
                // 예를 들어, DrawerLayout을 사용하는 경우:
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        List<String> recipeLinks = loadMenuItems();
        rice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RiceActivity.class);
                startActivityForResult(intent, REQUEST_CODE_MENU);
            }
        });
        side.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SideActivity.class);
                startActivityForResult(intent, REQUEST_CODE_MENU);
            }
        });
        soup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SoupActivity.class);
                startActivityForResult(intent, REQUEST_CODE_MENU);
            }
        });
        noodle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NoodleActivity.class);
                startActivityForResult(intent, REQUEST_CODE_MENU);
            }
        });
        soup2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Soup2Activity.class);
                startActivityForResult(intent, REQUEST_CODE_MENU);
            }
        });
        western.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WesternFoodActivity.class);
                startActivityForResult(intent, REQUEST_CODE_MENU);
            }
        });
        dessert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DessertActivity.class);
                startActivityForResult(intent, REQUEST_CODE_MENU);
            }
        });

        menuItem1.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                Intent intent = new Intent(MainActivity.this, RegionActivity.class);
                startActivity(intent);
                return true;
            }
        });

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(recipeLinks.get(0)));
                startActivity(intent);
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(recipeLinks.get(1)));
                startActivity(intent);
            }
        });

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(recipeLinks.get(2)));
                startActivity(intent);
            }
        });

    }

    private List<String> loadMenuItems() {
        List<String> links = new ArrayList<>();
        disposables.add(menuRepository.getMenuItems()
                .subscribe(
                        menuItems -> {
                            textView1.setText(menuItems.get(0).getTitle());
                            Log.d("check", menuItems.get(0).getTitle());
                            textView2.setText(menuItems.get(1).getTitle());
                            textView3.setText(menuItems.get(2).getTitle());
                            imageView1.setImageBitmap(menuItems.get(0).getBitmap());
                            imageView2.setImageBitmap(menuItems.get(1).getBitmap());
                            imageView3.setImageBitmap(menuItems.get(2).getBitmap());
                            links.add(menuItems.get(0).getLink());
                            links.add(menuItems.get(1).getLink());
                            links.add(menuItems.get(2).getLink());
                        },
                        throwable -> {
                            Log.e("RxJava", "Error loading menu items", throwable);
                            throwable.printStackTrace();
                        }
                ));
        return links;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.dispose();
    }
}
