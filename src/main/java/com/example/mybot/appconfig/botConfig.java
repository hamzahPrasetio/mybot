package com.example.mybot.appconfig;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultBotOptions;
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
    private List<Long> chatId;
    private String chatIdFilePath;
    private String hashedPassword;

    private DefaultBotOptions.ProxyType proxyType;
    private String proxyHost;
    private int proxyPort;

    @Bean
    public bot MySuperTelegramBot() {
        DefaultBotOptions options = new DefaultBotOptions();

//        options.setBaseUrl("http://localhost:8081/bot");
//        options.setProxyHost(proxyHost);
//        options.setProxyPort(proxyPort);
//        options.setProxyType(proxyType);

        bot mySuperTelegramBot = new bot(options);
        mySuperTelegramBot.setBotUserName(botUserName);
        mySuperTelegramBot.setBotToken(botToken);
        mySuperTelegramBot.setWebHookPath(webHookPath);
        mySuperTelegramBot.setChatIdFilePath(chatIdFilePath);
        mySuperTelegramBot.setHashedPassword(hashedPassword);
        mySuperTelegramBot.readAllowedChatId();

        return mySuperTelegramBot;
    }
}