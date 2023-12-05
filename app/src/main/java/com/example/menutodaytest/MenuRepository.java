package com.example.menutodaytest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.Pair;

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

public class MenuRepository {

    private static final int ITEMS_PER_PAGE = 40;

    public Observable<List<Pair<String, Bitmap>>> getMenuItems() {
        return Observable.create((ObservableOnSubscribe<List<Pair<String, Bitmap>>>) emitter -> {
                    List<Pair<String, Bitmap>> menuItems = new ArrayList<>();
                    try {
                        Document firstPageDoc = Jsoup.connect("https://www.10000recipe.com/recipe/list.html?order=reco&page=1").get();
                        int totalItems = Integer.parseInt(firstPageDoc.getElementsByAttributeValue("class", "m_list_tit").select("b").text().replaceAll(",", ""));
                        int totalPages = (int) Math.ceil((double) totalItems / ITEMS_PER_PAGE);
                        int remainder = (int) Math.ceil((double) totalItems % ITEMS_PER_PAGE);

                        int page1 = (int) (Math.random() * totalPages) + 1;
                        int page2 = (int) (Math.random() * totalPages) + 1;
                        int page3 = (int) (Math.random() * totalPages) + 1;
                        String pageUrl1 = "https://www.10000recipe.com/recipe/list.html?cat=&order=reco&page=" + page1;
                        String pageUrl2 = "https://www.10000recipe.com/recipe/list.html?cat=&order=reco&page=" + page2;
                        String pageUrl3 = "https://www.10000recipe.com/recipe/list.html?cat=&order=reco&page=" + page3;

                        try {
                            menuItems.add(getMenuItem(pageUrl1, totalPages, remainder));
                            menuItems.add(getMenuItem(pageUrl2, totalPages, remainder));
                            menuItems.add(getMenuItem(pageUrl3, totalPages, remainder));
                        } catch (IOException e) {
                            emitter.onError(e);
                            return;
                        }
                    } catch (IOException e) {
                        emitter.onError(e);
                        return;
                    }
                    emitter.onNext(menuItems);
                    emitter.onComplete();
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Pair<String, Bitmap> getMenuItem(String pageUrl, int totalPages, int remainder) throws IOException {
        Document doc = Jsoup.connect(pageUrl).get();
        Elements titleElements = doc.getElementsByAttributeValue("class", "common_sp_caption_tit line2");
        Elements thumbElements = doc.getElementsByAttributeValue("class", "common_sp_link");
        int n = (int) (Math.random() * 40);
        if (pageUrl.endsWith(String.valueOf(totalPages))) {
            n = (int) (Math.random() * remainder);
        }
        String title = titleElements.get(n).text();
        String imgUrl = thumbElements.get(n).select("> img").attr("src");

        URL url = new URL(imgUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoInput(true);
        conn.connect();

        InputStream is = conn.getInputStream();
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        return Pair.create(title, bitmap);
    }
}

