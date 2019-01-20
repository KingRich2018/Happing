package top.happing.domain.model;

import top.happing.framework.domain.model.BaseEntity;

/**
 * @Author wangbo
 * @Description
 * @Date $ $
 **/
@SuppressWarnings("serial")
public class One {
    private Long id;
    private String img;
    private String content;
    private String may;
    private String dom;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMay() {
        return may;
    }

    public void setMay(String may) {
        this.may = may;
    }

    public String getDom() {
        return dom;
    }

    public void setDom(String dom) {
        this.dom = dom;
    }
}
