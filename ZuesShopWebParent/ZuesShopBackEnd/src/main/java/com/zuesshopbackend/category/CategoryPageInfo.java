package com.zuesshopbackend.category;

public class CategoryPageInfo {
    private int totalPages;
    private long totalElements;

    public CategoryPageInfo(){}

    public CategoryPageInfo(int totalPages, long totalElements) {
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }
}
