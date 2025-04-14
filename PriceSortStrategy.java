package com.bookshop.sorting;

import com.bookshop.entity.Book;
import java.util.Comparator;
import java.util.List;

public class PriceSortStrategy implements SortStrategy {

    @Override
    public List<Book> sort(List<Book> books) {
        books.sort(Comparator.comparingDouble(Book::getPrice));
        return books;
    }
}
