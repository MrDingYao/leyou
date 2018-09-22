package com.leyou.common.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.List;

/**
 * @ClassName PageResult
 * @Description 进行分页时使用的信息分装类
 * @Author santu
 * @Date 2018/9/21 20:38
 * @Version 1.0
 **/
@Getter
@Setter
@ToString
public class PageResult<T> {
    /**
     * 总共的信息数
     */
    private Long total;

    /**
     * 总共的页数
     */
    private Long totalPage;

    /**
     * 当前页面要显示的数据的集合
     */
    private List<T> items;

    public PageResult() {
    }

    public PageResult(Long total, List<T> items) {
        this.total = total;
        this.items = items;
    }

    public PageResult(Long total, Long totalPage, List<T> items) {
        this.total = total;
        this.totalPage = totalPage;
        this.items = items;
    }
}
