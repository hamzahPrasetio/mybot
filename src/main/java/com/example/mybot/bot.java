package com.example.mybot;

import com.example.mybot.util.PDFtoImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;


public class bot extends TelegramWebhookBot {

    Logger logger = LoggerFactory.getLogger(bot.class);
    private RestTemplate restTemplate;

    private String webHookPath;
    private String botUserName;
    private String botToken;
    private List<String> allowedUser;
    private String imagepath = "D:\\Job\\Splunk\\cat.jpg";

    public bot(DefaultBotOptions botOptions) { super(botOptions); }

    @Override
    public String getBotUsername() { return botUserName; }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotPath() {
        return webHookPath;
    }

    public List<String> getAllowedUser() { return allowedUser; }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        if (update.getMessage() != null && update.getMessage().hasText()) {
            long chat_id = update.getMessage().getChatId();
            String username = update.getMessage().getChat().getUserName();
            String message = update.getMessage().getText();
//            System.out.println(username);
//            System.out.println(update.getMessage().getText());
//            DefaultBotOptions options = ApiContext
//                    .getInstance(DefaultBotOptions.class);

            try {
                if (this.allowedUser.contains(username)) {
                    if (message.charAt(0) == '/') {
                        if(message.contains("/dashboard")) {
                            if(message.substring(10).contains("rawr")) {
                                execute(new SendPhoto()
                                        .setPhoto(new File(imagepath))
                                        .setChatId(chat_id)
                                        .setCaption("Rawrrr"));
                            } else if(message.substring(10).contains("smile")) {
                                execute(new SendPhoto()
                                        .setPhoto(new File("D:\\Job\\Splunk\\smile.webp"))
                                        .setChatId(chat_id)
                                        .setCaption("Smile :) it could be your last day!"));
                            } else {
                                execute(new SendMessage(chat_id, "Uhhh, you must've mean smile right!"));
                                execute(new SendPhoto()
                                        .setPhoto(new File(imagepath))
                                        .setChatId(chat_id)
                                        .setCaption("Smile :) it could be your last day!"));
                            }
                        } else if(message.equalsIgnoreCase("/test")) {
                            execute(new SendMessage(chat_id, "Test success :D"));
                        } else if(message.equals("/rest")) {
//                            Quote quote = restTemplate.getForObject("https://quoters.apps.pcfone.io/api/random", Quote.class);
                            execute(new SendMessage(chat_id, "Rest command placeholder"));
                        } else if(message.equals("/image")) {
//                            Quote quote = restTemplate.getForObject("https://quoters.apps.pcfone.io/api/random", Quote.class);
                            execute(new SendPhoto()
                                    .setPhoto(new File(imagepath))
                                    .setChatId(chat_id)
                                    .setCaption("Rawrrr"));
                        } else {
                            execute(new SendMessage(chat_id, "This command is unavailable"));
                        }
                    } else {
                        if(message.equals("image")) {
                            //                            Quote quote = restTemplate.getForObject("https://quoters.apps.pcfone.io/api/random", Quote.class);
                            execute(new SendPhoto()
                                    .setPhoto(new File(imagepath))
                                    .setChatId(chat_id));
                        } else if(message.equals("smile")) {
                            execute(new SendPhoto()
                                    .setPhoto(new File("D:\\Job\\Splunk\\smile.webp"))
                                    .setChatId(chat_id)
                                    .setCaption("Smile :) it could be your last day!"));
                        } else if(message.equals("pdf")) {
                            String image = PDFtoImage.generateImageFromPDF("D:\\Job\\Splunk\\tes.pdf", 1);
                            execute(new SendPhoto()
                                    .setPhoto(new File(image))
                                    .setChatId(chat_id)
                                    .setCaption("Corvette Succede!!!"));
                        } else {
                            execute(new SendMessage(chat_id, "Hi " + update.getMessage().getText()));
//                            execute(new SendMessage(chat_id, "Hi " + options.getBaseUrl()));
                        }
                    }
                } else {
                    logger.info("Unauthorized attempt from username:" + username);
                    execute(new SendMessage(chat_id, "You don't have access"));
                }
            } catch (TelegramApiException | IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

    public void setWebHookPath(String webHookPath) {
        this.webHookPath = webHookPath;
    }

    public void setBotUserName(String userName) {
        this.botUserName = userName;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    public void setAllowedUser(List<String> allowedUser) { this.allowedUser = allowedUser; }

    public void addAllowedUser(String allowedUser) { this.allowedUser.add(allowedUser); }
}
