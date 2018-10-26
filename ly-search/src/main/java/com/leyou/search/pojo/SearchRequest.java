package com.leyou.search.pojo;

import java.util.Map;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 14 15:33
 **/
public class SearchRequest {

    // 搜索条件
    private String key;

    // 当前页
    private Integer page;

    // 排序的条件
    private String sortBy;

    // 升序还是降序
    private Boolean descending;

    // 规格参数过滤项
    private Map<String,String> filter;

    public Map<String, String> getFilter() {
        return filter;
    }

    public void setFilter(Map<String, String> filter) {
        this.filter = filter;
    }

    // 默认页面大小
    private static final Integer DEFAULT_SIZE = 20;

    // 默认页面
    private static final Integer DEFAULT_PAGE = 1;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getPage() {
        if (page == null) {
            return DEFAULT_PAGE;
        }
        // 获取页面时做下校验
        return Math.max(DEFAULT_PAGE,page);
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize(){
        return DEFAULT_SIZE;
    }

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
}
