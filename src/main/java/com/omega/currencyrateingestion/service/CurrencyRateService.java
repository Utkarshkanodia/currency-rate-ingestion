package com.omega.currencyrateingestion.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omega.currencyglobalcore.dao.CurrencyDataDao;
import com.omega.currencyglobalcore.dao.CurrencyRateDao;
import com.omega.currencyglobalcore.dao.entity.CurrencyDataEntity;
import com.omega.currencyglobalcore.dao.entity.CurrencyRateEntity;
import com.omega.currencyglobalcore.utility.Helper;

@Service
public class CurrencyRateService {

    @Autowired
    private CurrencyRateDao currencyRateDao;

    @Autowired
    private CurrencyDataDao currencyDataDao;

    @Autowired
    RestTemplate restTemplate;

    public static final String BASE_URL = "http://localhost:8080/";

    @SuppressWarnings("null")
    public ResponseEntity<Void> ingestLatestRates() {

        List<CurrencyDataEntity> currencyDataList = currencyDataDao.findAll();
        ParameterizedTypeReference<Map<String, Double>> typeDef = new ParameterizedTypeReference<Map<String, Double>>() {
        };
        List<CurrencyRateEntity> entityList = new ArrayList<>();

        try {
            System.out.println(new ObjectMapper().writeValueAsString(currencyDataList));
        } catch (Exception e) {
            e.printStackTrace();
        }
        currencyDataList.forEach(currencyData -> {
            ResponseEntity<Map<String, Double>> response = restTemplate.exchange(
                    BASE_URL + "latest?baseCurrency=" + currencyData.getCode(), HttpMethod.GET, null, typeDef);
            if (response.getStatusCode().equals(HttpStatus.OK)) {
                Map<String, Double> body = response.getBody();
                body.entrySet().forEach(set -> {
                    entityList.add(
                            CurrencyRateEntity.builder().fromCurrency(currencyData.getCode()).toCurrency(set.getKey())
                                    .coversionRate(set.getValue()).date(Helper.getTodayDate()).build());
                });
            }
            try {
                Thread.sleep(10000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        currencyRateDao.saveAll(entityList);
        return ResponseEntity.ok().build();
    }

}
