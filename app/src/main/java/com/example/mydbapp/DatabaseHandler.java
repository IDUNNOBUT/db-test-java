package com.example.mydbapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper implements AutoCloseable {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "BooksManager";

    public static class BookEntry implements BaseColumns {
        public static final String TABLE_NAME = "books";
        public static final String COLUMN_NAME_AUTHOR = "author";
        public static final String COLUMN_NAME_DISK = "disk";
        public static final String COLUMN_NAME_AMOUNT = "amount";
        public static final String COLUMN_NAME_YEAR = "year";
    }

    private static final String SQL_CREATE_BOOKS = String.format(
            "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s INTEGER, %s INTEGER, %s INTEGER)",
            BookEntry.TABLE_NAME,
            BookEntry._ID,
            BookEntry.COLUMN_NAME_AUTHOR,
            BookEntry.COLUMN_NAME_DISK,
            BookEntry.COLUMN_NAME_AMOUNT,
            BookEntry.COLUMN_NAME_YEAR);

    private static final String SQL_DELETE_BOOKS = "DROP TABLE IF EXISTS " + BookEntry.TABLE_NAME;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_BOOKS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_BOOKS);
        onCreate(db);
    }

    public void addBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_NAME_AUTHOR, book.getAuthor());
        values.put(BookEntry.COLUMN_NAME_DISK, book.isDisk());
        values.put(BookEntry.COLUMN_NAME_AMOUNT, book.getAmount());
        values.put(BookEntry.COLUMN_NAME_YEAR, book.getYear());
        db.insert(BookEntry.TABLE_NAME, null, values);
        db.close();
    }

    public void deleteBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(BookEntry.TABLE_NAME, BookEntry._ID + " = ?", new String[]{String.valueOf(book.getID())});
        db.close();
    }

    public void updateBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_NAME_AUTHOR, book.getAuthor());
        values.put(BookEntry.COLUMN_NAME_DISK, book.isDisk());
        values.put(BookEntry.COLUMN_NAME_AMOUNT, book.getAmount());
        values.put(BookEntry.COLUMN_NAME_YEAR, book.getYear());
        db.update(BookEntry.TABLE_NAME, values, BookEntry._ID + " = ?", new String[]{String.valueOf(book.getID())});
    }

    public List<Book> getAllBooks() {
        List<Book> bookList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + BookEntry.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Book book = new Book(
                        cursor.getInt(cursor.getColumnIndexOrThrow(BookEntry._ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(BookEntry.COLUMN_NAME_AUTHOR)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(BookEntry.COLUMN_NAME_DISK)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(BookEntry.COLUMN_NAME_AMOUNT)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(BookEntry.COLUMN_NAME_YEAR))
                );
                bookList.add(book);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return bookList;
    }

    public List<String> getAuthorsSortedByYear() {
        List<String> authors = new ArrayList<>();
        String selectQuery = "SELECT " + BookEntry.COLUMN_NAME_AUTHOR + " FROM " + BookEntry.TABLE_NAME + " ORDER BY " + BookEntry.COLUMN_NAME_YEAR;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                authors.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return authors;
    }

    public List<String> getAuthorsDependsOnDisk(int isDisk) {
        List<String> authors = new ArrayList<>();
        String selectQuery = "SELECT " + BookEntry.COLUMN_NAME_AUTHOR + " FROM " + BookEntry.TABLE_NAME + " WHERE " + BookEntry.COLUMN_NAME_DISK + " = " + isDisk;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                authors.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return authors;
    }

    public List<String> getAuthorsWithBooksAfter2007AndMinAmount3000() {
        List<String> authors = new ArrayList<>();
        String selectQuery = "SELECT " + BookEntry.COLUMN_NAME_AUTHOR + " FROM " + BookEntry.TABLE_NAME + " WHERE " + BookEntry.COLUMN_NAME_YEAR + " >= 2007 AND " + BookEntry.COLUMN_NAME_AMOUNT + " >= 3000";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                authors.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return authors;
    }

    public List<String> getAuthorsInAlphabeticalOrder() {
        List<String> authors = new ArrayList<>();
        String selectQuery = "SELECT " + BookEntry.COLUMN_NAME_AUTHOR + " FROM " + BookEntry.TABLE_NAME + " ORDER BY " + BookEntry.COLUMN_NAME_AUTHOR;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                authors.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return authors;
    }

    public List<Book> getBooksByAuthorFilter(String authorFilter) {
        List<Book> bookList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + BookEntry.TABLE_NAME + " WHERE " + BookEntry.COLUMN_NAME_AUTHOR + " LIKE ?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{"%" + authorFilter + "%"});

        if (cursor.moveToFirst()) {
            do {
                Book book = new Book(
                        cursor.getInt(cursor.getColumnIndexOrThrow(BookEntry._ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(BookEntry.COLUMN_NAME_AUTHOR)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(BookEntry.COLUMN_NAME_DISK)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(BookEntry.COLUMN_NAME_AMOUNT)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(BookEntry.COLUMN_NAME_YEAR))
                );
                bookList.add(book);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return bookList;
    }


}
