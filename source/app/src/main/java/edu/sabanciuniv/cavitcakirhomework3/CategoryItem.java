package edu.sabanciuniv.cavitcakirhomework3;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;


/**
 * Created by atanaltay on 28/03/2017.
 */

public class CategoryItem implements Serializable{

    private String name;
    private int id;

    @NonNull
    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CategoryItem(String name, int id) {
        this.name = name;
        this.id = id;
    }
}
