package com.example.menutodaytest;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RegionActivity extends AppCompatActivity {
    EditText editText;
    TextView textView;

    static RequestQueue requestQueue;

    RecyclerView recyclerView;
    NutritionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region);


        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textView5);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                makeRequest();
            }
        });

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new NutritionAdapter();
        recyclerView.setAdapter(adapter);

    }

    public void makeRequest() {
        if(!isValidRegionCode(editText.getText().toString())) {
            showToast("지역코드를 정확히 입력하세요.");
            return;
        } else {
            String url = "https://www.kamis.or.kr/service/price/xml.do?action=dailyCountyList&p_cert_key=e7bec3d5-7cd4-4f90-889d-3a1ef88aca63&p_cert_id=3880&p_returntype=json&p_countycode=" + editText.getText().toString();
            StringRequest request = new StringRequest(
                    Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            println("응답 -> " + response);

                            processResponse(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            println("에러 -> " + error.getMessage());
                            showToast("에러: " + error.getMessage());
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String, String>();

                    return params;
                }
            };

            request.setShouldCache(false);
            requestQueue.add(request);
            println("요청 보냄.");
        }


    }

    private boolean isValidRegionCode(String code) {
        List<String> validCodes = Arrays.asList("1101", "2100", "2200", "2300", "2401", "2601", "3111", "3311", "3511", "3911", "3714", "3814", "3145");
        return validCodes.contains(code);
    }
    public void println(String data) {
        Log.d("RegionActivity", data);
    }

    private void showToast(String message) {
        Toast.makeText(RegionActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    public void processResponse(String response) {
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(response).getAsJsonObject();
        JsonArray priceArray = jsonObject.getAsJsonArray("price");
        ArrayList<Nutrition> nutritionList = new ArrayList<>();

        int[] randomNumbers = generateRandomNumbers(101, 0, 100);
        for(int i : randomNumbers) {
            JsonObject priceObject = priceArray.get(i).getAsJsonObject();
            Nutrition nutrition = new Nutrition();
            nutrition.setCounty_code(getStringOrNull(priceObject, "county_code"));
            nutrition.setCounty_name(getStringOrNull(priceObject, "county_name"));
            nutrition.setProduct_cls_code(getStringArray(priceObject, "product_cls_code"));
            nutrition.setProduct_cls_name(getStringArray(priceObject, "product_cls_name"));
            nutrition.setCategory_code(getStringOrNull(priceObject, "category_code"));
            nutrition.setCategory_name(getStringOrNull(priceObject, "category_name"));
            nutrition.setProductno(getStringOrNull(priceObject, "productno"));
            nutrition.setLastest_date(getStringOrNull(priceObject, "lastest_date"));
            nutrition.setProductName(getStringOrNull(priceObject, "productName"));
            nutrition.setItem_name(getStringOrNull(priceObject, "item_name"));
            nutrition.setUnit(getStringOrNull(priceObject, "unit"));
            nutrition.setDay1(getStringOrNull(priceObject, "day1"));
            nutrition.setDpr1(getStringOrFirstElementArray(priceObject, "dpr1"));
            nutrition.setDay2(getStringOrNull(priceObject, "day2"));
            nutrition.setDpr2(getStringOrFirstElementArray(priceObject, "dpr2"));
            nutrition.setDay3(getStringOrNull(priceObject, "day3"));
            nutrition.setDpr3(getStringOrFirstElementArray(priceObject, "dpr3"));
            nutrition.setDay4(getStringOrNull(priceObject, "day4"));
            nutrition.setDpr4(getStringOrFirstElementArray(priceObject, "dpr4"));
            nutrition.setDirection(getStringOrNull(priceObject, "direction"));
            nutrition.setValue(getStringOrNull(priceObject, "value"));
            nutritionList.add(nutrition);
        }


        println("품목 수 : " + nutritionList.size());

        for(int i = 0; i < nutritionList.size(); i++) {
            Nutrition nutrition = nutritionList.get(i);
            adapter.addItem(nutrition);
        }

        adapter.notifyDataSetChanged();
    }

    private static int[] generateRandomNumbers(int count, int min, int max) {
        Random random = new Random();
        int[] randomNumbers = new int[count];

        for (int i = 0; i < count; i++) {
            randomNumbers[i] = random.nextInt(max - min + 1) + min;
        }

        return randomNumbers;
    }
    private static String getStringOrNull(JsonObject jsonObject, String key) {
        JsonElement element = jsonObject.get(key);
        if (element == null || element.isJsonNull()) {
            return null;
        }
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
            return element.getAsString();
        } else {
            return null;
        }
    }

    private static String[] getStringArray(JsonObject jsonObject, String key) {
        JsonElement element = jsonObject.get(key);
        if (element != null && element.isJsonArray()) {
            JsonArray jsonArray = element.getAsJsonArray();
            List<String> stringList = new ArrayList<>();
            for (JsonElement arrayElement : jsonArray) {
                if (arrayElement.isJsonPrimitive() && arrayElement.getAsJsonPrimitive().isString()) {
                    stringList.add(arrayElement.getAsString());
                }
            }
            return stringList.toArray(new String[0]);
        }
        return null;
    }

    private static String getStringOrFirstElementArray(JsonObject jsonObject, String key) {
        JsonElement element = jsonObject.get(key);
        if (element == null) {
            return null;
        }

        if (element.isJsonArray()) {
            JsonArray jsonArray = element.getAsJsonArray();
            if (jsonArray.size() > 0) {
                return jsonArray.get(0).getAsString();
            }
        } else if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
            return element.getAsString();
        }

        return null;
    }


}
