package com.example.client.dto;

import lombok.Data;

@Data
public class Info {
    private Integer number;
    private String word;

    public Info(Integer number, String word) {
        this.number = number;
        this.word = word;
    }
}
