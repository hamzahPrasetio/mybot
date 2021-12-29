package com.example.mybot.controller;

import com.example.mybot.util.SplunkPayload;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import com.example.mybot.bot;


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
    public BotApiMethod<?> splunkCpuAlert(@RequestParam String alertType, @RequestBody SplunkPayload payload) {
//        System.out.println("payload received with SID = " + payload.getSid());
        return telegramBot.SendCpuAlert(alertType, payload);
    }

}