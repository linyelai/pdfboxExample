package main.java.com.linseven.pdf;

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
    protected float borderTopSize;
    protected float borderTopColor;
    protected float paddingTopSize;
    protected float paddingTopColor;
    private String type;

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

    public float getBorderTopSize() {
        return borderTopSize;
    }

    public void setBorderTopSize(float borderTopSize) {
        this.borderTopSize = borderTopSize;
    }

    public float getBorderTopColor() {
        return borderTopColor;
    }

    public void setBorderTopColor(float borderTopColor) {
        this.borderTopColor = borderTopColor;
    }

    public float getPaddingTopSize() {
        return paddingTopSize;
    }

    public void setPaddingTopSize(float paddingTopSize) {
        this.paddingTopSize = paddingTopSize;
    }

    public float getPaddingTopColor() {
        return paddingTopColor;
    }

    public void setPaddingTopColor(float paddingTopColor) {
        this.paddingTopColor = paddingTopColor;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
