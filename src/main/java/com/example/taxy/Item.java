package com.example.taxy;

public class Item {
    private final String filePath;
    private final String action;

    public Item(String filePath) {
        this.filePath = filePath;
        this.action = null;
    }
    public Item(String filePath, String action) {
        this.filePath = filePath;
        this.action = action;
    }

    public String getFilePath() {
        return filePath;
    }
    
    public String getAction() {
        return action;
    }

}
