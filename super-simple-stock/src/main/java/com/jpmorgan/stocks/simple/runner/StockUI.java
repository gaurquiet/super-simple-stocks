/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpmorgan.stocks.simple.runner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map.Entry;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.jpmorgan.stocks.simple.arch.SpringService;
import com.jpmorgan.stocks.simple.backend.StocksEntityManager;
import com.jpmorgan.stocks.simple.model.Stock;
import com.jpmorgan.stocks.simple.model.StockType;
import com.jpmorgan.stocks.simple.model.Trade;
import com.jpmorgan.stocks.simple.model.TradeIndicator;
import com.jpmorgan.stocks.simple.services.SimpleStockService;

/**
 *
 * @author
 */
public class StockUI extends UIScan {

	Logger logger = Logger.getLogger(StockUI.class);

	/**
	 *
	 * @param reader,
	 *            stockService, entityManager
	 */
	public StockUI(Scanner reader, SimpleStockService stockService, StocksEntityManager entityManager) {
		super(reader, stockService, entityManager);
	}

	/**
	 *
	 */
	public void newTrade() {
		System.out.println("Insert stock symbol:");
		String symbol = reader.nextLine();
		if (entityManager.getStockBySymbol(symbol) != null) {
			System.out.println("Insert share quantity:");
			String shares = reader.nextLine();
			System.out.println("Insert price:");
			String price = reader.nextLine();
			System.out.println("Insert indicator (1=buy|2=sell):");
			String indicator = reader.nextLine();

			try {
				Trade trade = new Trade();
				trade.setPrice(Double.parseDouble(price));
				trade.setSharesQuantity(Integer.parseInt(shares));
				trade.setStock(entityManager.getStockBySymbol(symbol));
				trade.setTimeStamp(new Date());
				if (indicator.equalsIgnoreCase("1")) {
					trade.setTradeIndicator(TradeIndicator.BUY);
				} else {
					trade.setTradeIndicator(TradeIndicator.SELL);
				}

				stockService.recordTrade(trade);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Error --> Incorrect information entered. Insert again ..");
				newTrade();
			}

		} else {
			logger.error("invalid stock symbol");
			// check if stock exists, if exists then call newTrade method
			// else get user out mentioning no stock exists
			newTrade();
		}
	}

	/**
	 *
	 */
	public void updateStockData() {
		System.out.println("Insert stock symbol:");
		String symbol = reader.nextLine();
		if (entityManager.getStockBySymbol(symbol) != null) {
			try {
				Stock stock = entityManager.getStockBySymbol(symbol);
				System.out.println("Stock type: " + stock.getStockType());
			} catch (Exception e) {
				e.printStackTrace();
				updateStockData();
			}
		} else {
			logger.error("stock doesn't exists. Insert a new stock");
			newStock();
		}
	}

	/**
	 *
	 */
	public void newStock() {
		System.out.println("Insert stock symbol:");
		String symbol = reader.nextLine();
		System.out.println("Insert stock type (1:common|2:preferred):");
		String type = reader.nextLine();
		System.out.println("Insert stock last dividend:");
		String lastDividend = reader.nextLine();
		System.out.println("Insert stock par value:");
		String parValue = reader.nextLine();
		System.out.println("Insert stock price:");
		String price = reader.nextLine();
		System.out.println("Insert stock fixed dividend:");
		String fixedDividend = reader.nextLine();

		try {
			Stock stock = new Stock();
			stock.setStockSymbol(symbol);
			stock.setLastDividend(Double.parseDouble(lastDividend));
			stock.setFixedDividend(Double.parseDouble(fixedDividend));
			stock.setParValue(Integer.parseInt(parValue));
			if ("1".equals(type)) {
				stock.setStockType(StockType.COMMON);
			} else {
				stock.setStockType(StockType.PREFERRED);
			}
			stock.setTickerPrice(Double.parseDouble(price));
		} catch (Exception e) {
			e.printStackTrace();
			newStock();
		}
	}

	/**
	 *
	 */
	public void stockReport() {
		if (entityManager.getStocks().size() > 0) {
			System.out.println("--------------------------------------------------------------");
			System.out.println("----------------------- Stock Report ---------------------------------------");
			for (Entry<String, Stock> entry : entityManager.getStocks().entrySet()) {
				System.out.println("--------------------------------------------------------------");
				System.out.println("Symbol: " + entry.getKey() + " | price: " + entry.getValue().getTickerPrice()
						+ " | last dividend: " + entry.getValue().getLastDividend() + " | fixed dividend: "
						+ entry.getValue().getFixedDividend() + " | par value: " + entry.getValue().getParValue()
						+ " | dividend yield: " + entry.getValue().getDividendYield() + " | P/E ratio: "
						+ entry.getValue().getPeRatio());
			}
			System.out.println("--------------------------------------------------------------");
		} else {
			logger.error("0 registered stocks in system.");
		}
	}

	/**
	 *
	 */
	public void tradeReport() {
		System.out.println("Insert stock symbol:");
		String symbol = reader.nextLine();
		tradeReport(symbol);
	}

	/**
	 *
	 */
	private void tradeReport(String symbol) {
		if (entityManager.getStockBySymbol(symbol) != null) {

			SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			int i = 0;
			System.out.println("--------------------------------------------------------------");
			System.out.println("---------- Trade report for stock [" + symbol + "] -----------");
			System.out.println("--------------------------------------------------------------");
			for (Trade trade : entityManager.getTrades()) {
				if (symbol.equals(trade.getStock().getStockSymbol())) {
					System.out.println(++i + "- time: " + format1.format(trade.getTimeStamp().getTime()) + " | shares:"
							+ trade.getSharesQuantity() + " | price:" + trade.getPrice() + " | buy/sell:"
							+ trade.getTradeIndicator());
				}
			}
			System.out.println("--------------------------------------------------------------");

		} else {
			logger.error("invalid stock symbol");
		}
	}

	/**
	 * @throws Exception
	 *
	 */
	public void addAllTradeFromConfigFile() {
		logger.info("-- Starting adding trades in application from config file");
		int tradesNumber = entityManager.getTrades().size();
		logger.info("Total Trades exists in application: " + tradesNumber);

		@SuppressWarnings("unchecked")
		ArrayList<Trade> tradeList = SpringService.INSTANCE.getBean("tradeList", ArrayList.class);

		// Insert many trades in the stock system
		for (Trade trade : tradeList) {
			try {
				stockService.recordTrade(trade);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// After record trades, the number of trades should be equal to the
		// trades list
		tradesNumber = entityManager.getTrades().size();
		logger.info("Total Trades exists in application: " + tradesNumber);
		logger.info("Follwing trades added in the system: ");
		for (Entry<String, Stock> entry : entityManager.getStocks().entrySet()) {
			tradeReport(entry.getKey());
			System.out.println();
		}
	}
}
