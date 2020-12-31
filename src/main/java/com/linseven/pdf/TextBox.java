package main.java.com.linseven.pdf;

/**
 * @author Tyrion
 * @version 1.0
 * @date 2020/12/31 14:40
 */
public class TextBox extends  Text{
    private Integer borderLineWidth;
    private String borderColor;

    public Integer getBorderLineWidth() {
        return borderLineWidth;
    }

    public void setBorderLineWidth(Integer borderLineWidth) {
        this.borderLineWidth = borderLineWidth;
    }

    public String getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
    }
}
