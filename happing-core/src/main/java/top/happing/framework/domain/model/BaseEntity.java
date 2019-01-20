
package top.happing.framework.domain.model;

import java.io.Serializable;
import java.util.Date;

import top.happing.mybatis.plugin.Page;
import top.happing.mybatis.sqlsupplier.annotation.NotColumn;

public class BaseEntity<T> implements Serializable {

    private Long id;

   // private Long createUser;

    private Date createDate;

  //  private Long updateUser;

    private Date updateDate;

    @NotColumn
    private Page<T> page;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

   /* public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }*/

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /*public Long getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }*/

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Page<T> getPage() {
        return page;
    }

    public void setPage(Page<T> page) {
        this.page = page;
    }
}
