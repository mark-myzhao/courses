package io.github.zhaomy6.lab4;

public class StaticListItem {
    private int imgResId;
    private String contentText;

    public StaticListItem(int id, String content) {
        this.imgResId = id;
        this.contentText = content;
    }

    public int getImgResId() {
        return this.imgResId;
    }

    public String getContentText() {
        return this.contentText;
    }
}
