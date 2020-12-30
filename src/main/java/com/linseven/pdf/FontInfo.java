package main.java.com.linseven.pdf;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.font.PDFont;

/**
 * @author Tyrion
 * @version 1.0
 * @date 2020/12/28 17:56
 */
public class FontInfo {
    private COSName font;
    private int fontSize;

    public COSName getFont() {
        return font;
    }

    public void setFont(COSName font) {
        this.font = font;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }
}
