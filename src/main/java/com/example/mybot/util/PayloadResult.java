package com.example.mybot.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class PayloadResult {
    private String sourcetype;
    private int count;

    public PayloadResult(String sourcetype, int count) {
        this.sourcetype = sourcetype;
        this.count = count;
    }
}
