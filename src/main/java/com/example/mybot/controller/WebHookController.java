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

    @RequestMapping(value = "/FailAlert", method = RequestMethod.POST)
    public BotApiMethod<?> splunkFailAlert(@RequestParam(required = false) String jenisTrx,
           @RequestParam(required = false) String  jumlahTrx, @RequestParam(required = false) String tWaktu,
           @RequestParam(required = false) String  satuanWaktu, @RequestBody SplunkPayload payload) {
        return telegramBot.SendFailAlert(jenisTrx, jumlahTrx, tWaktu, satuanWaktu, payload);
    }

    @RequestMapping(value = "/UsageAlert", method = RequestMethod.POST)
    public BotApiMethod<?> splunkUsageAlert(@RequestParam(required = false) String resource, @RequestParam(required = false) int threshold,
                                              @RequestParam(required = false) String  satuanThreshold, @RequestParam(required = false) String tWaktu,
                                              @RequestParam(required = false) String  satuanWaktu, @RequestBody SplunkPayload payload) {
        return telegramBot.SendUsageAlert(resource, threshold, satuanThreshold, tWaktu, satuanWaktu, payload);
    }

    @RequestMapping(value = "/ExecutionAlert", method = RequestMethod.POST)
    public BotApiMethod<?> splunkExecutionAlert(@RequestParam(required = false) String resource,
                                              @RequestParam(required = false) String  execution, @RequestParam(required = false) String tWaktu,
                                              @RequestParam(required = false) String  satuanWaktu, @RequestBody SplunkPayload payload) {
        return telegramBot.SendExecutionAlert(resource, execution, tWaktu, satuanWaktu, payload);
    }

}