package com.bookshop.iterator;

import com.bookshop.entity.User;
import java.util.List;

public class CustomerListIterator implements CustomerIterator {

    private List<User> customers;
    private int position = 0;

    public CustomerListIterator(List<User> customers) {
        this.customers = customers;
    }

    @Override
    public boolean hasNext() {
        return position < customers.size();
    }

    @Override
    public User next() {
        return customers.get(position++);
    }
}
