package com.example.mybot.controller;

import com.example.mybot.util.SplunkPayload;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import com.example.mybot.bot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.net.UnknownHostException;


@RestController
public class WebHookController {
    private final bot telegramBot;

    public WebHookController(bot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return telegramBot.onWebhookUpdateReceived(update);
    }

    @RequestMapping(value = "/CpuAlert", method = RequestMethod.POST)
    public BotApiMethod<?> splunkCpuAlert(@RequestParam(required = false) String param1, @RequestBody SplunkPayload payload) {
//        System.out.println("payload received with SID = " + payload.getSid());
        return telegramBot.SendCpuAlert(param1, payload);
    }

    @RequestMapping(value = "/FailedInquiryAlert", method = RequestMethod.POST)
    public BotApiMethod<?> splunkFailedInquiryAlert(@RequestParam(required = false) String param1, @RequestBody SplunkPayload payload) {
        return telegramBot.SendFailedInquiryAlert(param1, payload);
    }

    @RequestMapping(value = "/FailedLoginAlert", method = RequestMethod.POST)
    public BotApiMethod<?> splunkFailedLoginAlert(@RequestParam(required = false) String param1, @RequestBody SplunkPayload payload) {
        return telegramBot.SendFailedLoginAlert(param1, payload);
    }

    @RequestMapping(value = "/FailedTransactionAlert", method = RequestMethod.POST)
    public BotApiMethod<?> splunkFailedTransactionAlert(@RequestParam(required = false) String param1, @RequestBody SplunkPayload payload) {
        return telegramBot.SendFailedTransactionAlert(param1, payload);
    }

    @RequestMapping(value = "/RegistrationAlert", method = RequestMethod.POST)
    public BotApiMethod<?> splunkRegistrationAlert(@RequestParam(required = false) String param1, @RequestBody SplunkPayload payload) {
        return telegramBot.SendRegistrationAlert(param1, payload);
    }

    @RequestMapping(value = "/Test", method = RequestMethod.POST)
    public BotApiMethod<?> splunkTestAlert(@RequestParam(required = false) String param1, @RequestBody SplunkPayload payload) {
        return telegramBot.testAlert(param1, payload);
    }

}