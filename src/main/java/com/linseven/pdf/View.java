package main.java.com.linseven.pdf;

import java.util.List;

/**
 * @author Tyrion
 * @version 1.0
 * @date 2020/12/28 10:49
 */
public class View {
    protected float x;
    protected float y;
    protected float width;
    protected float height;
    protected String backgroundColor;
    protected String type;
    protected List<View> kids;
    protected Integer id;
    protected Integer parentId;
    protected Integer pageNo;
    protected String group;
    protected boolean enable;
    protected boolean initial;


    public boolean isInitial() {
        return initial;
    }

    public void setInitial(boolean initial) {
        this.initial = initial;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public List<View> getKids() {
        return kids;
    }

    public void setKids(List<View> kids) {
        this.kids = kids;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
