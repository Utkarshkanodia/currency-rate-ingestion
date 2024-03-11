package com.omega.currencyrateingestion.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrencyDataApiResponse {
    private String symbol;
    private String name;
    private String symbolNative;
    private int decimalDigits;
    private int rounding;
    private String code;
    private String namePlural;
    private String type;
}
