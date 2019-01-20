package top.happing.mybatis.plugin;

import java.util.Collections;
import java.util.List;

public class Page<T> extends Pagination {

    /**
     * 查询数据列表
     */
    private List<T> list = Collections.emptyList();

    public Page() {

    }

    public Page(int curr, int size) {
        super(curr, size);
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
