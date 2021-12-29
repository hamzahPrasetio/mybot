package com.example.mybot;

import com.example.mybot.util.PDFtoImage;
import com.example.mybot.util.SplunkPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;


public class bot extends TelegramWebhookBot {

    Logger logger = LoggerFactory.getLogger(bot.class);
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10, new SecureRandom());


    private Boolean waitPassword = false;
    private String chatIdFilePath;
    private String hashedPassword;

    private String webHookPath;
    private String botUserName;
    private String botToken;
    private List<String> allowedChatId;
    private String imagepath = "D:/Job/Splunk/cat.jpg";

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

    public BotApiMethod<?> SendCpuAlert(String alertType, SplunkPayload payload) {
        try {
            System.out.println(payload.getSearch_name());
            switch (alertType) {
                case "30%":
                    execute(new SendMessage(allowedChatId.get(0), "30% alert is working!"));
                    execute(new SendMessage(allowedChatId.get(0), payload.getSearch_name()));
                default:
                    execute(new SendMessage(allowedChatId.get(0), "Default alert is working!"));
                    execute(new SendMessage(allowedChatId.get(0), payload.getSearch_name()));
            }
//            execute(new SendMessage(chatId.get(0), String.valueOf(payload.getResult().getCount())));
        } catch (TelegramApiException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        if (update.getMyChatMember() != null) {
            if (update.getMyChatMember().getNewChatMember().getStatus().toLowerCase().equals("kicked")) {
                logger.info("kicked, removing user: " + update.getMyChatMember().getFrom().getUserName());
                removeAllowedChatId(update.getMyChatMember().getChat().getId().toString());
            }
        }
        if (update.getMessage() != null && update.getMessage().hasText()) {
            String chat_id = update.getMessage().getChatId().toString();
            String username = update.getMessage().getChat().getUserName();
            String firstname = update.getMessage().getChat().getFirstName();
            String message = update.getMessage().getText();

//            System.out.println(username);
//            System.out.println(update.getMessage().getText());
//            DefaultBotOptions options = ApiContext
//                    .getInstance(DefaultBotOptions.class);

            try {
                System.out.println("user chat_id is = " + chat_id);
                System.out.println("allowed chat id is = " + this.allowedChatId);
                if(waitPassword) {
                    if(verifyPassword(message)) {
                        addAllowedChatId(chat_id);
                        execute(defaultCustomButton(chat_id, firstname));
                        waitPassword = false;
                    } else {
                        execute(new SendMessage(chat_id, "password tidak sesuai, silahkan coba lagi"));
                    }
                } else if (this.allowedChatId.contains(chat_id)) {
                    if (message.charAt(0) == '/') {
                        if(message.equals("/rest")) {
//                            Quote quote = restTemplate.getForObject("https://quoters.apps.pcfone.io/api/random", Quote.class);
                            execute(new SendMessage(chat_id, "Rest command placeholder"));
                        } else if(message.equals("/image")) {
//                            Quote quote = restTemplate.getForObject("https://quoters.apps.pcfone.io/api/random", Quote.class);
                            execute(new SendPhoto(chat_id, new InputFile( new File(imagepath))));
                        } else if(message.toLowerCase().contains("/start")) {
                            execute(new SendMessage(chat_id, "Anda telah login sebagai " + username + ".\n" +
                                    "Silahkan gunakan command /stop untuk logout dan login sebagai pengguna lain."));
                        } else if(message.toLowerCase().equals("/stop")) {
                            removeAllowedChatId(chat_id);
                            execute(startCustomButton(chat_id));
                        } else {
                            execute(new SendMessage(chat_id, "Unknown command"));
                        }
                    } else {
                        if(message.toLowerCase().equals("stop")) {
                            removeAllowedChatId(chat_id);
                            execute(startCustomButton(chat_id));
                        } else if(message.toLowerCase().equals("report")) {
                            execute(reportCustomButton(chat_id));
                        } else if(message.toLowerCase().contains("start")) {
                            execute(new SendMessage(chat_id, "Anda telah login sebagai " + username + ".\n" +
                                    "Silahkan gunakan command /stop untuk logout dan login sebagai pengguna lain."));
                        } else if(message.equals("image")) {
//                            Quote quote = restTemplate.getForObject("https://quoters.apps.pcfone.io/api/random", Quote.class);
                            execute(new SendPhoto(chat_id, new InputFile(new File(imagepath))));
                        } else if(message.equals("smile")) {
                            execute(new SendPhoto(chat_id, new InputFile(new File("D:/Job/Splunk/smile.webp"))));
                        } else if(message.equals("pdf")) {
                            String image = PDFtoImage.generateImageFromPDF("D:/Job/Splunk/tes.pdf", 1);
                            execute(new SendPhoto(chat_id, new InputFile(new File(image))));
                        } else {
                            execute(report(chat_id, message.toLowerCase()));
                        }
                    }
                } else {
                    logger.info("Unauthorized attempt from username:" + username);
                    if(message.toLowerCase().equals("/start")) {
                        SendMessage sendMessage = new SendMessage(chat_id, "Silakan masukkan password untuk melanjutkan");
                        sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
                        execute(sendMessage);
                        waitPassword = true;
                    } else if(message.toLowerCase().equals("start")) {
                        SendMessage sendMessage = new SendMessage(chat_id, "Silakan masukkan password untuk melanjutkan");
                        sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
                        execute(sendMessage);
                        waitPassword = true;
                    } else {
                        execute(new SendMessage(chat_id, "You don't have access"));
                    }
                }
            } catch (TelegramApiException e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

    public SendMessage report(String chatId, String report) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        KeyboardButton button = new KeyboardButton();
        row.add("Report");
        row.add("Stop");
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);

        System.out.println("message is = "+report);
        switch(report.toLowerCase()) {
            case "mobile":
                message.setText("Berikut report mobile");
                break;
            case "transaksi":
                message.setText("Berikut report transaksi");
                break;
            case "transaksi gagal":
                message.setText("Berikut report transaksi gagal");
                break;
            case "transaksi - lama":
                message.setText("Berikut report transaksi - lama");
                break;
            case "nasabah":
                message.setText("Berikut report nasabah");
                break;
            case "network":
                message.setText("Berikut report network");
                break;
            default: message.setText("Unknown command");
        }

        return message;
    }

    public SendMessage defaultCustomButton(String chatId, String firstName) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Selamat datang "+firstName);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Report");
        row.add("Stop");
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);

        return message;
    }

    public SendMessage startCustomButton(String chatId) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Sampai jumpa lagi");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Start");
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);

        return message;
    }

    public SendMessage reportCustomButton(String chatId) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Tentukan report untuk ditampilkan:");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Mobile");
        row.add("Transaksi");
        keyboard.add(row);
        row = new KeyboardRow();
        row.add("Transaksi Gagal");
        row.add("Transaksi - Lama");
        keyboard.add(row);
        row = new KeyboardRow();
        row.add("Nasabah");
        row.add("Network");
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);

        return message;
    }

    public void setChatIdFilePath(String filePath) {
        this.chatIdFilePath = filePath;
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

    public void addAllowedChatId(String chatId) {
        if(!allowedChatId.contains(chatId)) {
            this.allowedChatId.add(chatId);
            try {
                writeChatId(chatId);
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        } else {
            logger.info("attempted to add an already allowed chat id");
        }
    }

    public void removeAllowedChatId(String chatId) {
        if(allowedChatId.contains(chatId)) {
            this.allowedChatId.remove(chatId);
            try {
                rewriteChatId(this.allowedChatId);
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }

    public void readAllowedChatId() {
        fileMustExist();
        try {
            this.allowedChatId = new ArrayList<String>();
            System.out.println("chat id files is contain: "+Files.readAllLines(Paths.get(chatIdFilePath)));
            for (String line: Files.readAllLines(Paths.get(chatIdFilePath))) {
                System.out.println("line is = "+line);
                this.allowedChatId.add(line);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        System.out.println(this.allowedChatId.toString());
    }

    public void writeChatId(String chatId) throws IOException {
        fileMustExist();
        List<String> forWriting = new ArrayList<String>();
        forWriting.add(chatId);
        System.out.println("writing: "+forWriting.toString());
        Files.write(Paths.get(chatIdFilePath), forWriting, StandardOpenOption.APPEND);
    }

    public void rewriteChatId(List<String> chatId) throws IOException {
        fileMustExist();
        List<String> forWriting = new ArrayList<String>();
        for (String id: chatId) {
            forWriting.add(id);
        }
        System.out.println("rewriting with: "+forWriting.toString());
        Files.write(Paths.get(chatIdFilePath), forWriting, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public void fileMustExist() {
        if (!Files.exists(Paths.get(chatIdFilePath))) {
            logger.info("file with path = "+chatIdFilePath+" doesn't exist. Creating file...");
            try {
                Files.createFile(Paths.get(chatIdFilePath));
            } catch (IOException e) {
                logger.error("failed to create file");
            }
        }
    }

    public boolean verifyPassword(String password) {
        System.out.println("comparing this ["+password+"] with this ["+hashedPassword+"].");
        if (encoder.matches(password, hashedPassword)) {
            return true;
        }
        return false;
    }

    public void setAllowedChatId(List<String> allowedChatId) { this.allowedChatId = allowedChatId; }

    public void setHashedPassword(String hashedPassword) { this.hashedPassword = hashedPassword; }
}
