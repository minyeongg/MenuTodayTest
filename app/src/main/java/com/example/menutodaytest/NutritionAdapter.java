package com.example.menutodaytest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NutritionAdapter extends RecyclerView.Adapter<NutritionAdapter.ViewHolder> {
        ArrayList<Nutrition> items = new ArrayList<Nutrition>();

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View itemView = inflater.inflate(R.layout.nutrition_item, viewGroup, false);

            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
            Nutrition item = items.get(position);
            viewHolder.setItem(item);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public void addItem(Nutrition item) {
            items.add(item);
        }

        public void setItems(ArrayList<Nutrition> items) {
            this.items = items;
        }

        public Nutrition getItem(int position) {
            return items.get(position);
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView;
            TextView textView2;

            public ViewHolder(View itemView) {
                super(itemView);

                textView = itemView.findViewById(R.id.textView);

                textView2 = itemView.findViewById(R.id.textView2);
            }

            public void setItem(Nutrition item) {
                textView.setText(item.item_name);
//                Log.d("영화명 : ", textView.getText().toString());
                textView2.setText(item.dpr1 + " 원");
            }

        }
}
