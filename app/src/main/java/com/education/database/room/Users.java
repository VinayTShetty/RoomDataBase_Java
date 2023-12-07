package com.education.database.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Users {

    @PrimaryKey(autoGenerate = true)
    public int id;
    private String name;


    public Users(String name) {
        this.name = name;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
