package com.example.mybot.appconfig;

import com.example.mybot.model.Splunk;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "splunk")
public class splunkConfig {

    private String username;
    private String password;
    private String address;

    @Bean
    public Splunk splunk() {
        Splunk splunk = new Splunk();
        if (username != null) splunk.setUsername(username);
        if (password != null) splunk.setPassword(password);
        if (address != null) splunk.setAddress(address);
        return splunk;
    }
}
