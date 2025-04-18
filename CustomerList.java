package com.bookshop.iterator;

import com.bookshop.entity.User;
import java.util.ArrayList;
import java.util.List;

public class CustomerList implements CustomerCollection {

    private List<User> customers = new ArrayList<>();

    @Override
    public void addCustomer(User user) {
        customers.add(user);
    }

    @Override
    public CustomerIterator createIterator() {
        return new CustomerListIterator(customers);
    }
}

