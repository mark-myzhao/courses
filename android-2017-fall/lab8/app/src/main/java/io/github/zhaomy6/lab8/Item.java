package io.github.zhaomy6.lab8;

public class Item {
    private String name;
    private String birthday;
    private String gift;

    public Item(String n, String b, String g) {
        this.name = n;
        this.birthday = b;
        this.gift = g;
    }

    public String getBirthday() {
        return this.birthday;
    }

    public String getGift() {
        return this.gift;
    }

    public String getName() {
        return this.name;
    }

    public void setBirthday(String b) {
        this.birthday = b;
    }

    public void setGift(String g) {
        this.gift = g;
    }
}
