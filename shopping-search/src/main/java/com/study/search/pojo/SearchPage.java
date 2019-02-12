package com.study.search.pojo;

public class SearchPage {
    private String key;// 搜索条件

//    当前页  默认为1
    private Integer pageNo=1;

//    每页个数   默认为20
    private Integer pageSize=20;

//    开始行
    private Integer startRow;

//    排序字段
    private String sortBy;

//    是否降序
    private Boolean descending;

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public Boolean getDescending() {
        return descending;
    }

    public void setDescending(Boolean descending) {
        this.descending = descending;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    /**
     * 每当页数改变时  第一条改变
     * @param pageNo
     */
    public void setPageNo(Integer pageNo) {
        this.startRow=(pageNo-1)*this.pageSize+1;
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    /**
     * 每当页面总条数改变时  第一条改变
     * @param pageSize
     */
    public void setPageSize(Integer pageSize) {
        this.startRow=(this.pageNo-1)*pageSize+1;
        this.pageSize = pageSize;
    }

    public Integer getStartRow() {
        return startRow;
    }

    public void setStartRow(Integer startRow) {
        this.startRow = startRow;
    }
}
