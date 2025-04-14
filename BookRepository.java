package com.bookshop.repository;

import com.bookshop.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    // Search individually 
    List<Book> findByTitleContainingIgnoreCase(String title);
    List<Book> findByAuthorContainingIgnoreCase(String author);
    List<Book> findByPublisherContainingIgnoreCase(String publisher);
    List<Book> findByCategoryContainingIgnoreCase(String category);

    // Search across multiple fields (for main search bar)
    List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrPublisherContainingIgnoreCaseOrCategoryContainingIgnoreCase(
        String title, String author, String publisher, String category
    );
}

