package top.happing.mybatis.plugin;

import org.apache.ibatis.session.RowBounds;

import java.io.Serializable;

public class Pagination extends RowBounds implements Serializable {
	
	//是否有上一页
	private boolean prev;

	//是否有下一页
	private boolean next;
	
	
    // 当前页
    private int curr = 1;

    // 每页显示条数
    private int size = 10;

    // 总数
    private int total;

    // 总页数
    private int pages;

    public Pagination() {
        super();
    }

    public Pagination(int curr, int size) {
        super(offset(curr, size), size);
        if (curr > 1) {
            this.curr = curr;
        }
        this.size = size;
    }

    public static int offset(int curr, int size) {
        if (curr > 0) {
            return (curr - 1) * size;
        }
        return 0;
    }

    public int getPages() {
        if (this.size == 0) {
            return 0;
        }
        this.pages = this.total / this.size;
        if (this.total % this.size != 0) {
            this.pages++;
        }
        return this.pages;
    }

    public int getOffset() {
        return offset(this.curr, this.size);
    }

	public boolean isPrev() {
		return this.curr > 1;
	}

	public boolean isNext() {
		return this.curr < this.getPages();
	}
	public int getCurr() {
        return curr;
    }

    public void setCurr(int curr) {
        this.curr = curr;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
