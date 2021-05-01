package com.cos.starbucks.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyFood {

    private int id;
    private int foodId;
    private int userId;
    private String foodName;
    private int price;
    private Timestamp createDate;
    private String kind;
}
