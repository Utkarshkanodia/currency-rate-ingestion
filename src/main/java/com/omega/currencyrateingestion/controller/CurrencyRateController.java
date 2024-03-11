package com.omega.currencyrateingestion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.omega.currencyrateingestion.service.CurrencyRateService;

import io.github.resilience4j.retry.annotation.Retry;

@RestController
@RequestMapping("/currency-rates")
public class CurrencyRateController {

    @Autowired
    private CurrencyRateService service;

    @PostMapping("/latest")
    @Retry(name = "default")
    public ResponseEntity<Void> ingestLatest() {
        return service.ingestLatestRates();
    }

}
