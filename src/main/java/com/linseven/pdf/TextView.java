package main.java.com.linseven.pdf;

/**
 * @author Tyrion
 * @version 1.0
 * @date 2020/12/28 14:59
 */
public class TextView  extends View {
    private String text;
    private float fontSize;
    private String font;
    private String color;
    private String backgroundColor;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
