package com.example.mydbapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomRecyclerAdapter extends RecyclerView.Adapter<CustomRecyclerAdapter.ViewHolder> {

    private final List<Book> books;

    public CustomRecyclerAdapter(List<Book> books) {
        this.books = books;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView author;
        public ImageView isDisk;
        public TextView amount;
        public TextView year;

        public ViewHolder(View view) {
            super(view);
            author = view.findViewById(R.id.author);
            isDisk = view.findViewById(R.id.isDisk);
            amount = view.findViewById(R.id.amount);
            year = view.findViewById(R.id.year);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = books.get(position);
        holder.author.setText(book.getAuthor());
        holder.amount.setText(String.valueOf(book.getAmount()));
        holder.year.setText(String.valueOf(book.getYear()));
        if (book.isDisk() == 0) {
            holder.isDisk.setImageResource(android.R.drawable.ic_delete);
        } else {
            holder.isDisk.setImageResource(android.R.drawable.ic_input_add);
        }
    }

    @Override
    public int getItemCount() {
        return books.size();
    }
}
