package com.omega.currencyrateingestion.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.omega.currencyglobalcore.dao.CurrencyDataDao;
import com.omega.currencyglobalcore.dao.entity.CurrencyDataEntity;
import com.omega.currencyrateingestion.dto.CurrencyDataApiResponse;
import jakarta.transaction.Transactional;

@Service
public class CurrencyDataService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    CurrencyDataDao dao;

    public static final String BASE_URL = "http://localhost:8080/";

    @SuppressWarnings("null")
    @Transactional
    public ResponseEntity<Void> ingestCurrencies() {
        ParameterizedTypeReference<List<CurrencyDataApiResponse>> typeRef = new ParameterizedTypeReference<List<CurrencyDataApiResponse>>() {
        };
        ResponseEntity<List<CurrencyDataApiResponse>> response = restTemplate.exchange(BASE_URL + "/currencies",
                HttpMethod.GET, null,
                typeRef);
        if (response.getStatusCode().equals(HttpStatus.OK)) {
            List<CurrencyDataApiResponse> bodyList = response.getBody();
            Set<String> databaseCodes = dao.getAllCodes();
            List<CurrencyDataEntity> entityList = new ArrayList<>();
            bodyList.forEach(body -> {
                if (!databaseCodes.contains(body.getCode())) {
                    entityList.add(
                            CurrencyDataEntity.builder().code(body.getCode()).decimalDigits(body.getDecimalDigits())
                                    .name(body.getName()).namePlural(body.getNamePlural()).rounding(body.getRounding())
                                    .symbol(body.getSymbol()).symbolNative(body.getSymbolNative()).type(body.getType())
                                    .build());
                }
            });
            if (!entityList.isEmpty())
                dao.saveAll(entityList);
            return ResponseEntity.created(null).build();
        }
        return ResponseEntity.badRequest().build();
    }
}
