package com.bookshop.sorting;

import com.bookshop.entity.Book;
import java.util.List;

public interface SortStrategy {
    List<Book> sort(List<Book> books);
}

