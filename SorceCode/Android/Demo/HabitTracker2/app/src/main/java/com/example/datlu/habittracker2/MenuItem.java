package com.example.datlu.habittracker2;

public class MenuItem {
    private String name;
    private String amount;
    private int menuIc;

    public MenuItem(String name, String amount, int ic) {
        this.name = name;
        this.amount = amount;
        this.menuIc = ic;
    }

    public String getName() {
        return name;
    }

    public String getAmount() {
        return amount;
    }

    public int getMenuIc() {
        return menuIc;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setMenuIc(int menuIc) {
        this.menuIc = menuIc;
    }
}
