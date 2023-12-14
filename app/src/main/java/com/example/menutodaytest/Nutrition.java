package com.example.menutodaytest;


public class Nutrition {
    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public void setCounty_code(String county_code) {
        this.county_code = county_code;
    }

    public void setCounty_name(String county_name) {
        this.county_name = county_name;
    }

    public void setProduct_cls_code(String[] product_cls_code) {
        this.product_cls_code = product_cls_code;
    }

    public void setProduct_cls_name(String[] product_cls_name) {
        this.product_cls_name = product_cls_name;
    }

    public void setCategory_code(String category_code) {
        this.category_code = category_code;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public void setProductno(String productno) {
        this.productno = productno;
    }

    public void setLastest_date(String lastest_date) {
        this.lastest_date = lastest_date;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setDay1(String day1) {
        this.day1 = day1;
    }

    public void setDpr1(String dpr1) {
        this.dpr1 = dpr1;
    }

    public void setDay2(String day2) {
        this.day2 = day2;
    }

    public void setDpr2(String dpr2) {
        this.dpr2 = dpr2;
    }

    public void setDay3(String day3) {
        this.day3 = day3;
    }

    public void setDpr3(String dpr3) {
        this.dpr3 = dpr3;
    }

    public void setDay4(String day4) {
        this.day4 = day4;
    }

    public void setDpr4(String dpr4) {
        this.dpr4 = dpr4;
    }

    //    String condition;
//    String price;
    String county_code; // 지역코드
    String county_name; // 지역 명
    String[] product_cls_code; //구분(01:소매, 02:도매)
    String[] product_cls_name;
    String category_code;
    String category_name;
    String productno;
    String lastest_date;
    String productName;
    String item_name;
    String unit;
    String day1;
    String dpr1;
    String day2;
    String dpr2;
    String day3;
    String dpr3;
    String day4;
    String dpr4;
    String direction;  //등락여부( 0:가격하락 1:가격상승 2:등락없음)
    String value;
    String result_code;
}
