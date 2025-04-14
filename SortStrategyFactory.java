package com.bookshop.sorting;

public class SortStrategyFactory {

    public static SortStrategy getStrategy(String sortBy) {
        if (sortBy == null) return null;

        return switch (sortBy.toLowerCase()) {
            case "title" -> new TitleSortStrategy();
            case "price" -> new PriceSortStrategy();
            default -> null;
        };
    }
}

