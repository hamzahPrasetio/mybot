package com.example.mybot;

import com.example.mybot.model.Splunk;
import com.example.mybot.util.PDFtoImage;
import com.example.mybot.util.SplunkPayload;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.reactive.function.BodyInserters;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class bot extends TelegramWebhookBot {

    Logger logger = LoggerFactory.getLogger(bot.class);
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10, new SecureRandom());

    @Autowired
    private Splunk splunk;

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

    public BotApiMethod<?> SendCpuAlert(String param1, SplunkPayload payload) {
        try {
            log.info(payload.getSearch_name());
            param1=(param1 == null)?"default":param1;
            for (String chatId : allowedChatId) {
                switch (param1) {
                    case "30%":
                        execute(new SendMessage(chatId, "30% alert is triggered!"));
                        execute(new SendMessage(chatId, payload.getSearch_name()));
                        break;
                    default:
                        execute(new SendMessage(chatId, "Default alert is working!"));
                        execute(new SendMessage(chatId, payload.getSearch_name()));
                        break;
                }
            }
//            execute(new SendMessage(chatId.get(0), String.valueOf(payload.getResult().getCount())));
        } catch (TelegramApiException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return null;
    }

    public BotApiMethod<?> SendFailedInquiryAlert(String param1, SplunkPayload payload) {
        try {
            log.info(payload.getSearch_name());
            param1=(param1 == null)?"default":param1;
            for (String chatId : allowedChatId) {
                switch (param1) {
                    case "100":
                        execute(new SendMessage(chatId, "Alert! 100 Failed Inquiry in a Row."));
                        execute(new SendMessage(chatId, payload.getSearch_name()));
                        break;
                    case "10":
                        execute(new SendMessage(chatId, "Alert! 10 Failed Inquiry in a Row."));
                        execute(new SendMessage(chatId, payload.getSearch_name()));
                        break;
                    default:
                        execute(new SendMessage(chatId, "Alert! User Login Failed!"));
                        execute(new SendMessage(chatId, payload.getSearch_name()));
                        break;
                }
            }
//            execute(new SendMessage(chatId.get(0), String.valueOf(payload.getResult().getCount())));
        } catch (TelegramApiException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return null;
    }

    public BotApiMethod<?> SendFailedLoginAlert(String param1, SplunkPayload payload) {
        try {
            log.info(payload.getSearch_name());
            param1=(param1 == null)?"default":param1;
            for (String chatId : allowedChatId) {
                switch (param1) {
                    case "100":
                        execute(new SendMessage(chatId, "Alert! 100 Failed Login in a Row."));
                        execute(new SendMessage(chatId, payload.getSearch_name()));
                        break;
                    case "10":
                        execute(new SendMessage(chatId, "Alert! 10 Failed Login in a Row."));
                        execute(new SendMessage(chatId, payload.getSearch_name()));
                        break;
                    default:
                        execute(new SendMessage(chatId, "Alert! User Login Failed!"));
                        execute(new SendMessage(chatId, payload.getSearch_name()));
                        break;
                }
            }
//            execute(new SendMessage(chatId.get(0), String.valueOf(payload.getResult().getCount())));
        } catch (TelegramApiException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return null;
    }

    public BotApiMethod<?> SendFailedTransactionAlert(String param1, SplunkPayload payload) {
        try {
            log.info(payload.getSearch_name());
            param1=(param1 == null)?"default":param1;
            for (String chatId : allowedChatId) {
                switch (param1) {
                    case "100_transfer":
                        execute(new SendMessage(chatId, "Alert! 100 operasi transfer gagal dilaksanakan!"));
                        break;
                    case "100_top_up":
                        execute(new SendMessage(chatId, "Alert! 100 operasi top up gagal dilaksanakan!"));
                        break;
                    case "100_bayar":
                        execute(new SendMessage(chatId, "Alert! 100 operasi pembayaran gagal dilaksanakan!"));
                        break;
                    case "100_beli":
                        execute(new SendMessage(chatId, "Alert! 100 operasi pembelian gagal dilaksanakan!"));
                        break;
                    default:
                        execute(new SendMessage(chatId, "Alert! Transaction Failed!"));
                        execute(new SendMessage(chatId, payload.getSearch_name()));
                        break;
                }
            }
//            execute(new SendMessage(chatId.get(0), String.valueOf(payload.getResult().getCount())));
        } catch (TelegramApiException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return null;
    }

    public BotApiMethod<?> SendRegistrationAlert(String param1, SplunkPayload payload) {
        try {
            log.info(payload.getSearch_name());
            param1=(param1 == null)?"default":param1;
            for (String chatId : allowedChatId) {
                switch (param1) {
                    case "30%":
                        execute(new SendMessage(chatId, "!"));
                        execute(new SendMessage(chatId, payload.getSearch_name()));
                        break;
                    default:
                        execute(new SendMessage(chatId, "Alert! User Registration Failed!"));
                        execute(new SendMessage(chatId, payload.getSearch_name()));
                        break;
                }
            }
//            execute(new SendMessage(chatId.get(0), String.valueOf(payload.getResult().getCount())));
        } catch (TelegramApiException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return null;
    }

    public BotApiMethod<?> testAlert(String param1, SplunkPayload payload) {
//        try {
        log.info(payload.getSearch_name());
        log.info(payload.getResult().toString());
//        param1=(param1 == null)?"default":param1;
//            for (String chatId : allowedChatId) {
//                switch (param1) {
//                    case "30%":
//                        execute(new SendMessage(chatId, "!"));
//                        execute(new SendMessage(chatId, payload.getSearch_name()));
//                          break;
//                    default:
//                        execute(new SendMessage(chatId, "Alert! User Registration Failed!"));
//                        execute(new SendMessage(chatId, payload.getSearch_name()));
//                          break;
//                }
//            }
//            execute(new SendMessage(chatId.get(0), String.valueOf(payload.getResult().getCount())));
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//            System.out.println(e.getMessage());
//        }
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
        String string;
        List<String> listString;
        List<List<String>> listListString;
        String search;
        String text = null;
        String uri = "/servicesNS/-/BJBS/search/jobs";
        BodyInserters.FormInserter<String> bodyInserters;
        switch(report.toLowerCase()) {
            case "mobile":
                try {
                    text = "Laporan Mobile - " + LocalDateTime.now().toLocalDate().toString() + "\n";

                    search = "search index=* sourcetype=mgate trx_type=LOG response_code=00| stats count";
                    bodyInserters = BodyInserters.fromFormData("search",search).with("exec_mode","oneshot").with("earliest_time","-24h");
                    string = splunk.callRestAPI(search, uri, HttpMethod.POST, bodyInserters).getOneValue();
                    if (string != null) text += "\nLogin Success = " + string;

                    search = "search index=* sourcetype=mgate trx_type=LOG NOT response_code=00| stats count";
                    bodyInserters = BodyInserters.fromFormData("search",search).with("exec_mode","oneshot").with("earliest_time","-24h");
                    string = splunk.callRestAPI(search, uri, HttpMethod.POST, bodyInserters).getOneValue();
                    if (string != null) text += "\nLogin Failed = " + string;

                    search = "search index=* sourcetype=mgate trx_type=LOG NOT response_code=00 response_msg=*| stats count by response_msg";
                    bodyInserters = BodyInserters.fromFormData("search",search).with("exec_mode","oneshot").with("earliest_time","-24h");
                    listListString = splunk.callRestAPI(search, uri, HttpMethod.POST, bodyInserters).extractAllFieldFromXml();
                    if (listListString != null) {
                        text += "\nwith Login Fail Code :";
                        for (List<String> templist : listListString) {
                            text += "\n\t" + templist.get(1) + " " + templist.get(0);
                        }
                    }

                    search = "search index=* sourcetype=mgate trx_type=CRT response_code=00| stats count";
                    bodyInserters = BodyInserters.fromFormData("search",search).with("exec_mode","oneshot").with("earliest_time","-24h");
                    string = splunk.callRestAPI(search, uri, HttpMethod.POST, bodyInserters).getOneValue();
                    if (string != null) text += "\nRegistration Success = " + string;

                    search = "search index=* sourcetype=mgate trx_type=CRT NOT response_code=00| stats count";
                    bodyInserters = BodyInserters.fromFormData("search",search).with("exec_mode","oneshot").with("earliest_time","-24h");
                    string = splunk.callRestAPI(search, uri, HttpMethod.POST, bodyInserters).getOneValue();
                    if (string != null) text += "\nRegistration Failed = " + string;

                    search = "search index=* sourcetype=mgate trx_type=CRT NOT response_code=00 response_msg| stats count by response_msg";
                    bodyInserters = BodyInserters.fromFormData("search",search).with("exec_mode","oneshot").with("earliest_time","-24h");
                    listListString = splunk.callRestAPI(search, uri, HttpMethod.POST, bodyInserters).extractAllFieldFromXml();
                    if (listListString != null) {
                        text += "\nwith Registration Fail Code :";
                        for (List<String> templist : listListString) {
                            text += "\n\t" + templist.get(1) + " " + templist.get(0);
                        }
                    }

                    search = "search index=* sourcetype=mgate trx_type=BIN response_code=00| stats count";
                    bodyInserters = BodyInserters.fromFormData("search",search).with("exec_mode","oneshot").with("earliest_time","-24h");
                    string = splunk.callRestAPI(search, uri, HttpMethod.POST, bodyInserters).getOneValue();
                    if (string != null) text += "\nBalance Inquiry Success = " + string;

                    search = "search index=* sourcetype=mgate trx_type=BIN response_code=* NOT response_code=00| stats count";
                    bodyInserters = BodyInserters.fromFormData("search",search).with("exec_mode","oneshot").with("earliest_time","-24h");
                    string = splunk.callRestAPI(search, uri, HttpMethod.POST, bodyInserters).getOneValue();
                    if (string != null) text += "\nBalance Inquiry Failed = " + string;
                } catch (ParserConfigurationException e) {
                    log.error(e.getMessage());
                    text = "Error, sistem gagal melakukan parsing";
                } catch (IOException e) {
                    log.error(e.getMessage());
                    text = "Error, sistem mengalami kegagalan I/O";
                } catch (SAXException e) {
                    log.error(e.getMessage());
                    text = "Error, sistem mengalami kegagalan SAX";
                }
                message.setText(text);
                break;
            case "transaksi":
                try {
                    text = "Laporan Transaksi Berhasil - " + LocalDateTime.now().toLocalDate().toString() + "\n";

                    search = "search index=* sourcetype=mgate trx_type=BLP OR trx_type=BLN OR trx_type=ZIS OR trx_type=TRF OR trx_type=QRP response_code=00| lookup trx_code.csv trx_type OUTPUT trx_name| stats count";
                    bodyInserters = BodyInserters.fromFormData("search",search).with("exec_mode","oneshot").with("earliest_time","-24h");
                    string = splunk.callRestAPI(search, uri, HttpMethod.POST, bodyInserters).getOneValue();
                    if (string != null) text += "\nTransaksi Keuangan = " + string;

                    search = "search index=* sourcetype=mgate log_type=\"reply data\" response_code=00 trx_type=BLP NOT favorites product_code=1157 OR product_code=70002 OR product_code=10045 OR product_code=10003 OR product_code=10006 OR product_code=10033 OR product_code=10017 OR product_code=10050 OR product_code=10035 OR product_code=10002 OR product_code=12001 OR product_code=17003 OR product_code=17000|lookup code_product.csv product_code OUTPUT product_name | stats count";
                    bodyInserters = BodyInserters.fromFormData("search",search).with("exec_mode","oneshot").with("earliest_time","-24h");
                    string = splunk.callRestAPI(search, uri, HttpMethod.POST, bodyInserters).getOneValue();
                    if (string != null) text += "\nTransaksi Pembelian = " + string;

                    search = "search index=* sourcetype=mgate log_type=\"reply data\" response_code=00 trx_type=BLP NOT favorites product_code=30004 OR product_code=2021 OR product_code=1156 OR product_code=90001 OR product_code=30002 OR product_code=70001 OR product_code=70003 OR product_code=1001 OR product_code=10001 OR product_code=210003 OR product_code=17001|lookup code_product.csv product_code OUTPUT product_name | stats count";
                    bodyInserters = BodyInserters.fromFormData("search",search).with("exec_mode","oneshot").with("earliest_time","-24h");
                    string = splunk.callRestAPI(search, uri, HttpMethod.POST, bodyInserters).getOneValue();
                    if (string != null) text += "\nTransaksi Pembayaran = " + string;

                    search = "search index=* sourcetype=mgate log_type=\"reply data\" response_code=00 trx_type=BLP NOT favorites product_code=210001 OR product_code=210002 OR product_code=210041|lookup code_product.csv product_code OUTPUT product_name | stats count";
                    bodyInserters = BodyInserters.fromFormData("search",search).with("exec_mode","oneshot").with("earliest_time","-24h");
                    string = splunk.callRestAPI(search, uri, HttpMethod.POST, bodyInserters).getOneValue();
                    if (string != null) text += "\nTransaksi Top Up = " + string;

                    search = "search index=* sourcetype=mgate NOT favorites NOT menu log_type=\"reply data\" trx_type=TRF dest_bank_code=110 OR dest_bank_code=425 response_code=00| stats count";
                    bodyInserters = BodyInserters.fromFormData("search",search).with("exec_mode","oneshot").with("earliest_time","-24h");
                    string = splunk.callRestAPI(search, uri, HttpMethod.POST, bodyInserters).getOneValue();
                    if (string != null) text += "\nTransfer Antar Rekening = " + string;

                    search = "search index=* sourcetype=mgate NOT favorites NOT menu log_type=\"reply data\" trx_type=TRF NOT dest_bank_code=110 NOT dest_bank_code=425 response_code=00| stats count";
                    bodyInserters = BodyInserters.fromFormData("search",search).with("exec_mode","oneshot").with("earliest_time","-24h");
                    string = splunk.callRestAPI(search, uri, HttpMethod.POST, bodyInserters).getOneValue();
                    if (string != null) text += "\nTransfer Antar Bank = " + string;

//                    search = "search index=* sourcetype=mgate NOT favorites NOT menu log_type=\"reply data\" trx_type=TRF dest_bank_code=* response_code=00| lookup \"bank_code.csv\" bank_code AS dest_bank_code OUTPUT bank_name| stats count by bank_name| sort count";
//                    bodyInserters = BodyInserters.fromFormData("search",search).with("exec_mode","oneshot").with("earliest_time","-24h");
//                    listListString = splunk.tesFunc(search, uri, HttpMethod.POST, bodyInserters).extractAllFieldFromXml();
//                    text += "\nPembagian Transfer Berdasarkan Bank Tujuan :";
//                    for (List<String> templist: listListString) {
//                        text += templist.get(0) + " - " + templist.get(1);
//                    }
                } catch (ParserConfigurationException e) {
                    log.error(e.getMessage());
                    text = "Error, sistem gagal melakukan parsing";
                } catch (IOException e) {
                    log.error(e.getMessage());
                    text = "Error, sistem mengalami kegagalan I/O";
                } catch (SAXException e) {
                    log.error(e.getMessage());
                    text = "Error, sistem mengalami kegagalan SAX";
                }
                message.setText(text);
                break;
            case "transaksi gagal":
                try {
                    text = "Laporan Transaksi Gagal - " + LocalDateTime.now().toLocalDate().toString() + "\n";

                    search = "search index=* sourcetype=mgate trx_type=BLP OR trx_type=BLN OR trx_type=ZIS OR trx_type=TRF OR trx_type=QRP NOT response_code=00| lookup trx_code.csv trx_type OUTPUT trx_name| stats count";
                    bodyInserters = BodyInserters.fromFormData("search",search).with("exec_mode","oneshot").with("earliest_time","-24h");
                    string = splunk.callRestAPI(search, uri, HttpMethod.POST, bodyInserters).getOneValue();
                    if (string != null) text += "\nTransaksi Keuangan = " + string;

                    search = "search index=* sourcetype=mgate log_type=\"reply data\" NOT response_code=00 trx_type=BLP NOT favorites product_code=1157 OR product_code=70002 OR product_code=10045 OR product_code=10003 OR product_code=10006 OR product_code=10033 OR product_code=10017 OR product_code=10050 OR product_code=10035 OR product_code=10002 OR product_code=12001 OR product_code=17003 OR product_code=17000|lookup code_product.csv product_code OUTPUT product_name | stats count";
                    bodyInserters = BodyInserters.fromFormData("search",search).with("exec_mode","oneshot").with("earliest_time","-24h");
                    string = splunk.callRestAPI(search, uri, HttpMethod.POST, bodyInserters).getOneValue();
                    if (string != null) text += "\nTransaksi Pembelian = " + string;

                    search = "search index=* sourcetype=mgate log_type=\"reply data\" NOT response_code=00 trx_type=BLP NOT favorites product_code=30004 OR product_code=2021 OR product_code=1156 OR product_code=90001 OR product_code=30002 OR product_code=70001 OR product_code=70003 OR product_code=1001 OR product_code=10001 OR product_code=210003 OR product_code=17001|lookup code_product.csv product_code OUTPUT product_name | stats count";
                    bodyInserters = BodyInserters.fromFormData("search",search).with("exec_mode","oneshot").with("earliest_time","-24h");
                    string = splunk.callRestAPI(search, uri, HttpMethod.POST, bodyInserters).getOneValue();
                    if (string != null) text += "\nTransaksi Pembayaran = " + string;

                    search = "search index=* sourcetype=mgate log_type=\"reply data\" NOT response_code=00 trx_type=BLP NOT favorites product_code=210001 OR product_code=210002 OR product_code=210041|lookup code_product.csv product_code OUTPUT product_name | stats count";
                    bodyInserters = BodyInserters.fromFormData("search",search).with("exec_mode","oneshot").with("earliest_time","-24h");
                    string = splunk.callRestAPI(search, uri, HttpMethod.POST, bodyInserters).getOneValue();
                    if (string != null) text += "\nTransaksi Top Up = " + string;

                    search = "search index=* sourcetype=mgate NOT favorites NOT menu log_type=\"reply data\" trx_type=TRF dest_bank_code=110 OR dest_bank_code=425 NOT response_code=00| stats count";
                    bodyInserters = BodyInserters.fromFormData("search",search).with("exec_mode","oneshot").with("earliest_time","-24h");
                    string = splunk.callRestAPI(search, uri, HttpMethod.POST, bodyInserters).getOneValue();
                    if (string != null) text += "\nTransfer Antar Rekening = " + string;

                    search = "search index=* sourcetype=mgate NOT favorites NOT menu log_type=\"reply data\" trx_type=TRF NOT dest_bank_code=110 NOT dest_bank_code=425 NOT response_code=00| stats count";
                    bodyInserters = BodyInserters.fromFormData("search",search).with("exec_mode","oneshot").with("earliest_time","-24h");
                    string = splunk.callRestAPI(search, uri, HttpMethod.POST, bodyInserters).getOneValue();
                    if (string != null) text += "\nTransfer Antar Bank = " + string;

//                    search = "search index=* sourcetype=mgate NOT favorites NOT menu log_type=\"reply data\" trx_type=TRF dest_bank_code=* response_code=00| lookup \"bank_code.csv\" bank_code AS dest_bank_code OUTPUT bank_name| stats count by bank_name| sort count";
//                    bodyInserters = BodyInserters.fromFormData("search",search).with("exec_mode","oneshot").with("earliest_time","-24h");
//                    listListString = splunk.tesFunc(search, uri, HttpMethod.POST, bodyInserters).extractAllFieldFromXml();
//                    text += "\nPembagian Transfer Berdasarkan Bank Tujuan :";
//                    for (List<String> templist: listListString) {
//                        text += templist.get(0) + " - " + templist.get(1);
//                    }
                } catch (ParserConfigurationException e) {
                    log.error(e.getMessage());
                    text = "Error, sistem gagal melakukan parsing";
                } catch (IOException e) {
                    log.error(e.getMessage());
                    text = "Error, sistem mengalami kegagalan I/O";
                } catch (SAXException e) {
                    log.error(e.getMessage());
                    text = "Error, sistem mengalami kegagalan SAX";
                }
                message.setText(text);
                break;
            case "transaksi - lama":
                try {
                    text = "Laporan Transaksi Lama - " + LocalDateTime.now().toLocalDate().toString() + "\n";

                    search = "search index=* sourcetype=* log_type=\"reply data\" trx_type=TRF OR trx_type=QRP OR trx_type=ZIS OR trx_type=BLP OR trx_type=AUP response_msg=success|stats count";
                    bodyInserters = BodyInserters.fromFormData("search",search).with("exec_mode","oneshot").with("earliest_time","-24h");
                    string = splunk.callRestAPI(search, uri, HttpMethod.POST, bodyInserters).getOneValue();
                    if (string != null) text += "\nTransaction Approved = " + string;

                    search = "search index=* sourcetype=* log_type=\"reply data\" trx_type=TRF OR trx_type=QRP OR trx_type=ZIS OR trx_type=BLP OR trx_type=AUP NOT response_msg=success|stats count";
                    bodyInserters = BodyInserters.fromFormData("search",search).with("exec_mode","oneshot").with("earliest_time","-24h");
                    string = splunk.callRestAPI(search, uri, HttpMethod.POST, bodyInserters).getOneValue();
                    if (string != null) text += "\nTransaction Rejected = " + string;

                    search = "search index=* sourcetype=* log_type=\"reply data\" trx_type=TRF OR trx_type=QRP OR trx_type=ZIS OR trx_type=BLP OR trx_type=AUP response_msg=success|lookup trx_code.csv trx_type OUTPUT trx_name|eval gabung=trx_name|chart count by gabung|sort -count| eval gabung=mvindex(gabung,0,10)| table gabung";
                    bodyInserters = BodyInserters.fromFormData("search",search).with("exec_mode","oneshot").with("earliest_time","-24h");
                    listString = splunk.callRestAPI(search, uri, HttpMethod.POST, bodyInserters).getOneCol();
                    if (listString != null) {
                        text += "\nTop 10 Transaction Approved :";
                        int temprank = 1;
                        for (String temp : listString) {
                            text += "\n\t" + temprank + ". " + temp;
                            temprank++;
                        }
                    }

                    search = "search index=* sourcetype=* log_type=\"reply data\" trx_type=TRF OR trx_type=QRP OR trx_type=ZIS OR trx_type=BLP OR trx_type=AUP NOT response_msg=success| lookup trx_code.csv trx_type OUTPUT trx_name|eval gabung=trx_name|chart count by gabung|sort -count| eval gabung=mvindex(gabung,0,10)| table gabung";
                    bodyInserters = BodyInserters.fromFormData("search",search).with("exec_mode","oneshot").with("earliest_time","-24h");
                    listString = splunk.callRestAPI(search, uri, HttpMethod.POST, bodyInserters).getOneCol();
                    if (listString != null) {
                        text += "\nTop 10 Transaction Rejected :";
                        int temprank = 1;
                        for (String temp : listString) {
                            text += "\n\t" + temprank + ". " + temp;
                            temprank++;
                        }
                    }
                } catch (ParserConfigurationException e) {
                    log.error(e.getMessage());
                    text = "Error, sistem gagal melakukan parsing";
                } catch (IOException e) {
                    log.error(e.getMessage());
                    text = "Error, sistem mengalami kegagalan I/O";
                } catch (SAXException e) {
                    log.error(e.getMessage());
                    text = "Error, sistem mengalami kegagalan SAX";
                }
                message.setText(text);
                break;
            case "nasabah":
                try {
                    text = "Laporan Nasabah - " + LocalDateTime.now().toLocalDate().toString() + "\n";

                    search = "search index=* sourcetype=mgate trx_type=LOG response_code=00\n" +
                            "| stats count";
                    bodyInserters = BodyInserters.fromFormData("search",search).with("exec_mode","oneshot").with("earliest_time","-24h");
                    string = splunk.callRestAPI(search, uri, HttpMethod.POST, bodyInserters).getOneValue();
                    if (string != null) text += "\nRekening Aktif = " + string;

                    search = "search index=* sourcetype=mgate trx_type=LOG response_code=00\n" +
                            "| stats count";
                    bodyInserters = BodyInserters.fromFormData("search",search).with("exec_mode","oneshot").with("earliest_time","-24h");
                    string = splunk.callRestAPI(search, uri, HttpMethod.POST, bodyInserters).getOneValue();
                    if (string != null) text += "\nRekening Diblokir = " + string;

                    search = "search index=* sourcetype=mgate trx_type=LOG response_code=00\n" +
                            "| stats count";
                    bodyInserters = BodyInserters.fromFormData("search",search).with("exec_mode","oneshot").with("earliest_time","-24h");
                    string = splunk.callRestAPI(search, uri, HttpMethod.POST, bodyInserters).getOneValue();
                    if (string != null) text += "\nRekening Dibuka = " + string;

                    search = "search index=* sourcetype=mgate trx_type=LOG response_code=00\n" +
                            "| stats count";
                    bodyInserters = BodyInserters.fromFormData("search",search).with("exec_mode","oneshot").with("earliest_time","-24h");
                    string = splunk.callRestAPI(search, uri, HttpMethod.POST, bodyInserters).getOneValue();
                    if (string != null) text += "\nRekening Ditutup = " + string;
                } catch (ParserConfigurationException e) {
                    log.error(e.getMessage());
                    text = "Error, sistem gagal melakukan parsing";
                } catch (IOException e) {
                    log.error(e.getMessage());
                    text = "Error, sistem mengalami kegagalan I/O";
                } catch (SAXException e) {
                    log.error(e.getMessage());
                    text = "Error, sistem mengalami kegagalan SAX";
                }
                message.setText(text);
                break;
            case "network":
                try {
                    uri = "/servicesNS/-/network/search/jobs";
                    text = "Laporan Network - " + LocalDateTime.now().toLocalDate().toString() + "\n";

                    search = "search index=snmp sourcetype=\"cpu*\" (cpmCPUTotal5minRev=* OR hrProcessorLoad=*) | eval cpu_used=if(sourcetype==\"cpu_ios\",cpmCPUTotal5minRev,hrProcessorLoad) | lookup snmp_list IP as hostname OUTPUT device_name as device_name | search device_name=\"*\" hostname=\"*\" | stats latest(cpu_used) as cpu_used by _time hostname | stats sum(cpu_used) as cpu_used_total by _time hostname | stats avg(cpu_used_total) as avg_cpu, max(cpu_used_total) as max_cpu";
                    bodyInserters = BodyInserters.fromFormData("search",search).with("exec_mode","oneshot").with("earliest_time","-24h");
                    listString = splunk.callRestAPI(search, uri, HttpMethod.POST, bodyInserters).getOneRow();
                    if (listString != null) text += "\nAverage CPU Used = " + listString.get(0) + "%, with a Maximum of " + listString.get(1) + "%";

                    search = "search index=snmp sourcetype=\"memory_ios\" (ciscoMemoryPoolFree=* OR ciscoMemoryPoolUsed=*) | lookup snmp_list IP as hostname OUTPUT device_name as device_name| search device_name=\"*\" hostname=\"*\"| stats  latest(ciscoMemoryPoolFree) as Memory_Free latest(ciscoMemoryPoolUsed) as Memory_Used by _time, hostname | stats  sum(Memory_Used) as Total_Memory_Used sum(Memory_Free) as Total_Memory_Free by _time, hostname | eval  Capacity = Total_Memory_Used+Total_Memory_Free, usage_pct = round(Total_Memory_Used/Capacity*100,2)  | stats  latest(usage_pct) as usage_pct by hostname _time| appendcols  [| search index=snmp (sourcetype=\"memory_mikrotik\" hrStorageUsed=* OR hrStorageSize=*)| lookup snmp_list IP as hostname OUTPUT device_name as device_name| search device_name=\"*\" hostname=\"*\"| stats  latest(hrStorageUsed) as usage latest(hrStorageSize) as size by _time hostname| eval  usage_pct=round(usage/size*100,2) | stats  latest(usage_pct) as usage_pct by hostname _time]| stats avg(usage_pct) as avg_usage, max(usage_pct) as max_usage";
                    bodyInserters = BodyInserters.fromFormData("search",search).with("exec_mode","oneshot").with("earliest_time","-24h");
                    listString = splunk.callRestAPI(search, uri, HttpMethod.POST, bodyInserters).getOneRow();
                    if (listString != null) text += "\nAverage Memory Used = " + listString.get(0) + "%, with a Maximum of " + listString.get(1) + "%";

                    search = "search index=snmp sourcetype=\"bandwidth\" ifSpeed=* OR ifOutOctets=* OR ifInOctets=* OR ifDescr=* hostname=\"*\"| stats latest(ifDescr) as ifDescr latest(ifSpeed) as ifSpeed, latest(ifOutOctets) as ifOutOctets, latest(ifInOctets) as ifInOctets by hostname, _time| search NOT ifDescr=unroute NOT ifDescr=Null* | streamstats window=2 global=false current=true range(ifOutOctets) as deltaifOutOctets range(ifInOctets) as deltaifInOctets range(_time) as sec by hostname| where sec>0 | eval mbpsIn=round((deltaifInOctets*8/sec)/1000000,4),  mbpsOut=round((deltaifOutOctets*8/sec)/1000000,4)| eval throughput=mbpsIn+mbpsOut| eval usage=throughput/(ifSpeed/1000000)*100, hostname_ifDescr=hostname.\"/\".ifDescr | lookup snmp_list IP as hostname OUTPUT device_name as device_name| search device_name=\"*\" hostname=\"*\"| stats latest(throughput) as throughput by hostname_ifDescr _time| stats avg(throughput) as avg_trpt, max(throughput) as max_trpt";
                    bodyInserters = BodyInserters.fromFormData("search",search).with("exec_mode","oneshot").with("earliest_time","-24h");
                    listString = splunk.callRestAPI(search, uri, HttpMethod.POST, bodyInserters).getOneRow();
                    if (listString != null) text += "\nAverage IOPS Used = " + listString.get(0) + " Mbps, with a Maximum of " + listString.get(1) + " Mbps";

                    search = "search index=\"network\" sourcetype=\"ping:pusat\"| eval status=if(action==\"ping succeeded\",\"UP\",\"DOWN\")| lookup snmp_list IP as dst_ip OUTPUT device_name as device_name| rename dst_ip as \"hostname\"| search device_name=\"*\" hostname=\"*\"| stats latest(status) as Status by hostname device_name| rename hostname as \"IP Adress\"| fields - count| stats count by Status | sort Status";
                    bodyInserters = BodyInserters.fromFormData("search",search).with("exec_mode","oneshot").with("earliest_time","-24h");
                    listListString = splunk.callRestAPI(search, uri, HttpMethod.POST, bodyInserters).extractAllFieldFromXml();
                    if (listListString != null) text += "\nNetwork Device Status = " + listListString.get(1).get(1) + " Up, " + listListString.get(0).get(1) + " Down";
                } catch (ParserConfigurationException e) {
                    log.error(e.getMessage());
                    text = "Error, sistem gagal melakukan parsing";
                } catch (IOException e) {
                    log.error(e.getMessage());
                    text = "Error, sistem mengalami kegagalan I/O";
                } catch (SAXException e) {
                    log.error(e.getMessage());
                    text = "Error, sistem mengalami kegagalan SAX";
                }
                message.setText(text);
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
