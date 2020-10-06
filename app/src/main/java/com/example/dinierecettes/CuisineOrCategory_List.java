package com.example.dinierecettes;

class CuisineOrCategory_List {

    private String c_name;
    private int c_image;

    public CuisineOrCategory_List(String c_name, int c_image) {
        this.c_name = c_name;
        this.c_image = c_image;
    }

    public String getC_name() {
        return c_name;
    }

    public int getC_image() {
        return c_image;
    }
}

