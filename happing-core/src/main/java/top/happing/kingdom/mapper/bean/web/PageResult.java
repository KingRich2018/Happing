package top.happing.kingdom.mapper.bean.web;


import java.util.List;

public class PageResult extends ResponseResult<PageResult> {

    private Integer total;

    private List rows;

    public PageResult() {}

    public PageResult(Integer total, List rows) {
        this.total = total;
        this.rows = rows;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }
}
