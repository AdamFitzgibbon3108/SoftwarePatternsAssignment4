package com.bookshop.sorting;

import com.bookshop.entity.Book;
import java.util.Comparator;
import java.util.List;

public class TitleSortStrategy implements SortStrategy {

    @Override
    public List<Book> sort(List<Book> books) {
        books.sort(Comparator.comparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER));
        return books;
    }
}
