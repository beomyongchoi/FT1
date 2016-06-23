package kr.co.fintalk.fintalkone.common.model;

/**
 * Created by BeomyongChoi on 6/23/16
 */
public class CalculatorDataSet {
    /**
     * 그룹 여부. (true : 그룹, false : 이외.)
     */
    private transient byte isGroup;

    private String sectionTitle;
    private String menuTitle;

    public boolean isGroup() {
        if (isGroup == 0) {
            return false;
        } else {
            return true;
        }
    }

    public void setGroup(boolean isGroup) {
        if (isGroup) {
            this.isGroup = 1;
        } else {
            this.isGroup = 0;
        }
    }

    public void setIsGroup(byte isGroup) {
        this.isGroup = isGroup;
    }

    public String getSectionTitle() {
        return sectionTitle;
    }

    public void setSectionTitle(String sectionTitle) {
        this.sectionTitle = sectionTitle;
    }

    public String getMenuTitle() {
        return menuTitle;
    }

    public void setMenuTitle(String menuTitle) {
        this.menuTitle = menuTitle;
    }
}
