package com.bookshop.iterator;

import com.bookshop.entity.User;

public interface CustomerIterator {
    boolean hasNext();
    User next();
}

