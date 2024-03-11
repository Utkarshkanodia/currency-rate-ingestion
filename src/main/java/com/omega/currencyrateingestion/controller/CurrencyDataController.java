package com.omega.currencyrateingestion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.omega.currencyrateingestion.service.CurrencyDataService;

@RestController
@RequestMapping("/currencies")
public class CurrencyDataController {

  @Autowired
  private CurrencyDataService service;

  @PostMapping("/latest")
  public ResponseEntity<Void> ingestCurrencies() {
    return service.ingestCurrencies();
  }

}
