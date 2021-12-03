package com.example.mybot.appconfig;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import com.example.mybot.bot;
import java.util.List;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "telegrambot")
public class botConfig {
    private String webHookPath;
    private String botUserName;
    private String botToken;
    private List<String> allowedUser;
    private List<Long> chatId;

    private DefaultBotOptions.ProxyType proxyType;
    private String proxyHost;
    private int proxyPort;

    @Bean
    public bot MySuperTelegramBot() {
        DefaultBotOptions options = ApiContext
                .getInstance(DefaultBotOptions.class);

//        options.setBaseUrl("http://localhost:8081/bot");
//        options.setProxyHost(proxyHost);
//        options.setProxyPort(proxyPort);
//        options.setProxyType(proxyType);

        bot mySuperTelegramBot = new bot(options);
        mySuperTelegramBot.setBotUserName(botUserName);
        mySuperTelegramBot.setBotToken(botToken);
        mySuperTelegramBot.setWebHookPath(webHookPath);
        mySuperTelegramBot.setAllowedUser(allowedUser);
        mySuperTelegramBot.setChatId(chatId);

        return mySuperTelegramBot;
    }
}