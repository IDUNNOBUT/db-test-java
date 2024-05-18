package com.example.mydbapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    DatabaseHandler db = new DatabaseHandler(this);
    List<Book> books;
    TextView textView, total;
    Button addButton, deleteButton, updateButton;
    RecyclerView list;

    TextInputEditText filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db.onUpgrade(db.getWritableDatabase(), 0, 0);
        initialState();

        textView = findViewById(R.id.textView);
        textView.setMovementMethod(new ScrollingMovementMethod());
        total = findViewById(R.id.total);
        addButton = findViewById(R.id.addButton);
        deleteButton = findViewById(R.id.deleteButton);
        updateButton = findViewById(R.id.updateButton);
        list = findViewById(R.id.list);
        filter = findViewById(R.id.filter);

        addButton.setOnClickListener(v -> addBook());
        deleteButton.setOnClickListener(v -> deleteBook());
        updateButton.setOnClickListener(v -> updateBook());

        filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateRecyclerView(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        setupRecyclerView();
        getUniqueResults();
    }

    protected void initialState() {
        db.addBook(new Book("Горнаков", 1, 2000, 2008));
        db.addBook(new Book("Буткевич", 0, 3000, 2006));
        db.addBook(new Book("Пирумян", 0, 3000, 2003));
        db.addBook(new Book("Моррисон", 1, 1000, 2006));
        db.addBook(new Book("Любавин", 1, 3000, 2007));
        db.addBook(new Book("Романьков", 1, 3500, 2007));
        db.addBook(new Book("Монахов", 1, 2000, 2008));
        db.addBook(new Book("Будилов", 0, 3000, 2003));
    }

    private void setupRecyclerView() {
        books = db.getAllBooks();
        CustomRecyclerAdapter adapter = new CustomRecyclerAdapter(books);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
        total.setText(String.format("total: %d", books.size()));
    }

    private void addBook() {
        db.addBook(new Book("Иванов", 1, 5000, 2024));
        Toast.makeText(this, "Book added", Toast.LENGTH_SHORT).show();
        updateRecyclerView();
    }

    private void deleteBook() {
        db.deleteBook(books.get(0));
        Toast.makeText(this, "Book deleted", Toast.LENGTH_SHORT).show();
        updateRecyclerView();
    }

    private void updateBook() {
        Book firstBook = books.get(0);
        firstBook.setAuthor("Тестово-апдейтов");
        db.updateBook(firstBook);
        Toast.makeText(this, "Book updated", Toast.LENGTH_SHORT).show();
        updateRecyclerView();
    }

    private String resultToString(String title, List<String> authors) {
        StringBuilder sb = new StringBuilder();
        sb.append(title).append("\n");
        for (String author : authors) {
            sb.append(author).append("\n");
        }
        return sb.toString();
    }

    private void getUniqueResults() {
        StringBuilder sb = new StringBuilder();
        sb.append(resultToString("---Фамилии авторов книг в порядке года издания книги---", db.getAuthorsSortedByYear()));
        sb.append(resultToString("---Фамилии авторов книг с дисками---", db.getAuthorsDependsOnDisk(1)));
        sb.append(resultToString("---Фамилии авторов книг без дисков---", db.getAuthorsDependsOnDisk(0)));
        sb.append(resultToString("---Фамилии  авторов книг, изданных не ранее 2007 года " +
                "тиражом не менее 3000 экземпляров---", db.getAuthorsWithBooksAfter2007AndMinAmount3000()));
        sb.append(resultToString("---Фамилии авторов книг в алфавитном порядке---", db.getAuthorsInAlphabeticalOrder()));
        textView.setText(sb.toString());
    }

    private void updateRecyclerView(String authorFilter) {
        List<Book> filteredBooks;
        if (authorFilter.trim().isEmpty()) {
            filteredBooks = db.getAllBooks();
        } else {
            filteredBooks = db.getBooksByAuthorFilter(authorFilter);
        }
        CustomRecyclerAdapter adapter = new CustomRecyclerAdapter(filteredBooks);
        list.setAdapter(adapter);
        total.setText(String.format("total: %d", filteredBooks.size()));
    }

    private void updateRecyclerView() {
        books = db.getAllBooks();
        CustomRecyclerAdapter adapter = new CustomRecyclerAdapter(books);
        list.setAdapter(adapter);
        total.setText(String.format("total: %d", books.size()));
    }
}
