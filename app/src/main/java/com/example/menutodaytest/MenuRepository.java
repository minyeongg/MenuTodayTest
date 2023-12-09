package com.example.menutodaytest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;

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

    public Observable<List<MenuItem>> getMenuItems() {
        return Observable.create((ObservableOnSubscribe<List<MenuItem>>) emitter -> {
                    List<MenuItem> menuItems = new ArrayList<>();
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

    private MenuItem getMenuItem(String pageUrl, int totalPages, int remainder) throws IOException {
        Document doc = Jsoup.connect(pageUrl).get();
        Elements titleElements = doc.getElementsByAttributeValue("class", "common_sp_caption_tit line2");
        Elements thumbElements = doc.getElementsByAttributeValue("class", "common_sp_link");

        int n = (int) (Math.random() * 40);
        if (pageUrl.endsWith(String.valueOf(totalPages))) {
            n = (int) (Math.random() * remainder);
        }
        String title = titleElements.get(n).text();
        String imgUrl = thumbElements.get(n).select("> img").attr("src");
        String link = "https://www.10000recipe.com" + thumbElements.get(n).attr("href");

        URL url = new URL(imgUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoInput(true);
        conn.connect();

        InputStream is = conn.getInputStream();
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        return new MenuItem(title, bitmap, link);
    }

    public class MenuItem {
        private String title;
        private Bitmap bitmap;
        private String link;

        public MenuItem(String title, Bitmap bitmap, String link) {
            this.title = title;
            this.bitmap = bitmap;
            this.link = link;
        }

        public String getTitle() {
            return title;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public String getLink() {
            return link;
        }
    }


    public Observable<List<MenuItem>> getRecipes(String orglink) {
        return Observable.create((ObservableOnSubscribe<List<MenuItem>>) emitter -> {
                    List<MenuItem> menuItems = new ArrayList<>();
                    try {
                        Document firstPageDoc = Jsoup.connect(orglink).get();
                        int totalItems = Integer.parseInt(firstPageDoc.getElementsByAttributeValue("class", "m_list_tit").select("b").text().replaceAll(",", ""));

                        // Calculate the number of pages
                        int totalPages = (int) Math.ceil((double) totalItems / ITEMS_PER_PAGE);
                        Log.d("totalPages", String.valueOf(totalPages)); // 5277인지 확인
                        int remainder = (int) Math.ceil((double) totalItems % ITEMS_PER_PAGE);
                        Log.d("remaider", String.valueOf(remainder));

                        int page; // 랜덤으로 페이지 한 개 추출
                        page = (int) (Math.random() * totalPages) + 1;
                        Log.d("test", "pages: " + String.valueOf(page));
                        String pageUrl = orglink.substring(0, orglink.length() - 1) + page;
                        try {
                            Document doc = Jsoup.connect(pageUrl).get();
                            Elements title = doc.getElementsByAttributeValue("class", "common_sp_caption_tit line2");
                            Elements thumb = doc.getElementsByAttributeValue("class", "common_sp_link");
                            for (int i = 0; i < 4; i++) {
                                String menu = title.get(i).text();
                                String imgUrl = thumb.get(i).select("> img").attr("src");
                                String link = "https://www.10000recipe.com" + thumb.get(i).attr("href");
                                Log.d("test", "menu: " + menu);
                                Log.d("test", "Image URL: " + imgUrl);
                                try {
                                    URL url = new URL(imgUrl);

                                    // web에서 이미지를 가져와 ImageView에 저장할 Bitmap을 만든다.
                                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                    conn.setDoInput(true); // 서버로부터 응답 수신
                                    conn.connect();

                                    InputStream is = conn.getInputStream(); //inputStream 값 가져오기
                                    Bitmap bitmap = BitmapFactory.decodeStream(is); // Bitmap으로 변환
                                    menuItems.add(new MenuItem(menu, bitmap, link));
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
                    emitter.onNext(menuItems);
                    emitter.onComplete();
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}

