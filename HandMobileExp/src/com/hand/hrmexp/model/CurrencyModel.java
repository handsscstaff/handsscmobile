package com.hand.hrmexp.model;

public class CurrencyModel {
	private String currency;
	private String exchangeRate;
	
	public CurrencyModel(String currency, String exchangeRate){
		this.currency = currency;
		this.exchangeRate = exchangeRate;
	}
	
	public String getCurrency(){
		return currency;
	}
	
	public String getExchangeRate(){
		return exchangeRate;
	}
}
