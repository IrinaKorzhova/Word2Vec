package com.example.server.dto;

import lombok.Data;

@Data
public class Word {
    private String label;

    public Word(String label) {
        this.label = label;
    }
}
