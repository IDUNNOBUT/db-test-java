package com.example.mydbapp;

import androidx.annotation.NonNull;

public class Book {
    private int _id;
    private String _author;
    private int _disk;
    private int _amount;
    private int _year;

    public Book(){}

    public Book(int id, String author, int disk, int amount, int year) {
        this._id = id;
        this._author = author;
        this._disk = disk;
        this._amount = amount;
        this._year = year;
    }

    public Book(String author, int disk, int amount, int year) {
        this._author = author;
        this._disk = disk;
        this._amount = amount;
        this._year = year;
    }

    public int getID() {
        return _id;
    }

    public void setID(int id) {
        this._id = id;
    }

    public String getAuthor() {
        return _author;
    }

    public void setAuthor(String author) {
        this._author = author;
    }

    public int isDisk() {
        return _disk;
    }

    public void setDisk(int disk) {
        this._disk = disk;
    }

    public int getAmount() {
        return _amount;
    }

    public void setAmount(int amount) {
        this._amount = amount;
    }

    public int getYear() {
        return _year;
    }

    public void setYear(int year) {
        this._year = year;
    }
}
