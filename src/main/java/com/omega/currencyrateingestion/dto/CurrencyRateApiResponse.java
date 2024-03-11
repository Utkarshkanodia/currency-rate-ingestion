package com.omega.currencyrateingestion.dto;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrencyRateApiResponse {
	
	private Map<String,Double> data;
	
}
