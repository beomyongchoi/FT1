package kr.co.fintalk.fintalkone.ui;

/**
 * Created by BeomyongChoi on 7/22/16
 */
public class MainList {
    private int header;
    private int headerIcon;
    private int firstMenu;
    private int secondMenu;
    private int thirdMenu;

    public MainList(int header, int headerIcon, int firstMenu, int secondMenu, int thirdMenu) {
        this.header = header;
        this.headerIcon = headerIcon;
        this.firstMenu = firstMenu;
        this.secondMenu = secondMenu;
        this.thirdMenu = thirdMenu;
    }

    public int getHeader() {
        return header;
    }

    public int getHeaderIcon() {
        return headerIcon;
    }

    public int getFirstMenu() {
        return firstMenu;
    }

    public int getSecondMenu() {
        return secondMenu;
    }

    public int getThirdMenu() {
        return thirdMenu;
    }
}
