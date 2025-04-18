package com.bookshop.iterator;

import com.bookshop.entity.User;

public interface CustomerCollection {
    void addCustomer(User user);
    CustomerIterator createIterator();
}

