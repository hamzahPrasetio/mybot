package com.example.mybot.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class SplunkPayload {
    private PayloadResult result;
    private String sid;
    private String results_link;
    private String search_name;
    private String owner;
    private String app;

    public SplunkPayload(String sourcetype, int count, String sid, String results_link, String search_name, String owner, String app) {
        this.result = new PayloadResult(sourcetype, count);
        this.sid = sid;
        this.results_link = results_link;
        this.search_name = search_name;
        this.owner = owner;
        this.app = app;
    }
}
