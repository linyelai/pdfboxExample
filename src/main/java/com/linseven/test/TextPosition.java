package main.java.com.linseven.test;

import lombok.Data;

/**
 * @author Tyrion
 * @version 1.0
 * @date 2021/1/17 16:13
 */
@Data
public class TextPosition {
    private float x;
    private float y;
    private String content;
    private int tokenIndex;
}
