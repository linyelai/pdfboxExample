package main.java.com.linseven.pdf;

/**
 * @author Tyrion
 * @version 1.0
 * @date 2020/12/30 17:40
 */
public class Radio extends  View {
    private String groupName;
    private String selectValue;
    private Integer selectIndex;
    private String color;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getSelectValue() {
        return selectValue;
    }

    public void setSelectValue(String selectValue) {
        this.selectValue = selectValue;
    }

    public Integer getSelectIndex() {
        return selectIndex;
    }

    public void setSelectIndex(Integer selectIndex) {
        this.selectIndex = selectIndex;
    }
}
