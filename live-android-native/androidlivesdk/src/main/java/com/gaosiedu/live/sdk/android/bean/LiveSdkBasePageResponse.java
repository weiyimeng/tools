package com.gaosiedu.live.sdk.android.bean;


import java.util.List;

public class LiveSdkBasePageResponse extends LiveSdkBaseResponse {
    
    
    private Integer pageSize;
    
    private Integer pageNum;
    
    private long total;
    
    private int size;
    //总记录数
    //总页数
    private int pages;
    //第一页
    private int firstPage;
    //前一页
    private int prePage;
    //下一页
    private int nextPage;
    //最后一页
    private int lastPage;
    
    //是否为第一页
    private boolean isFirstPage = false;
    //是否为最后一页
    private boolean isLastPage = false;
    //是否有前一页
    private boolean hasPreviousPage = false;
    //是否有下一页
    private boolean hasNextPage = false;
    //所有导航页号
    private int[] navigatepageNums;
    
    
    public Integer getPageNum() {
        return pageNum;
    }
    
    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }
    
    public Integer getPageSize() {
        return pageSize;
    }
    
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
    
    public Integer getSize() {
        return size;
    }
    
    public void setSize(Integer size) {
        this.size = size;
    }
    
    
    public long getTotal() {
        return total;
    }
    
    public void setTotal(long total) {
        this.total = total;
    }
    
    public void setSize(int size) {
        this.size = size;
    }
    
    public int getPages() {
        return pages;
    }
    
    public void setPages(int pages) {
        this.pages = pages;
    }
    
    public int getFirstPage() {
        return firstPage;
    }
    
    public void setFirstPage(int firstPage) {
        this.firstPage = firstPage;
    }
    
    public int getPrePage() {
        return prePage;
    }
    
    public void setPrePage(int prePage) {
        this.prePage = prePage;
    }
    
    public int getNextPage() {
        return nextPage;
    }
    
    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }
    
    public int getLastPage() {
        return lastPage;
    }
    
    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }
    
    public boolean isFirstPage() {
        return isFirstPage;
    }
    
    public void setFirstPage(boolean firstPage) {
        isFirstPage = firstPage;
    }
    
    public boolean isLastPage() {
        return isLastPage;
    }
    
    public void setLastPage(boolean lastPage) {
        isLastPage = lastPage;
    }
    
    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }
    
    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }
    
    public boolean isHasNextPage() {
        return hasNextPage;
    }
    
    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }
    
    public int[] getNavigatepageNums() {
        return navigatepageNums;
    }
    
    public void setNavigatepageNums(int[] navigatepageNums) {
        this.navigatepageNums = navigatepageNums;
    }
}
