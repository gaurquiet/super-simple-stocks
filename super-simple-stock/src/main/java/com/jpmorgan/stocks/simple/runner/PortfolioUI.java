/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpmorgan.stocks.simple.runner;

import java.util.Scanner;

import org.apache.log4j.Logger;

import com.jpmorgan.stocks.simple.backend.StocksEntityManager;
import com.jpmorgan.stocks.simple.services.SimpleStockService;

/**
 *
 * @author
 */
public class PortfolioUI extends UIScan {

	Logger logger = Logger.getLogger(PortfolioUI.class);

	/**
	 *
	 * @param reader,
	 *            stockService, entityManager
	 */
	public PortfolioUI(Scanner reader, SimpleStockService stockService, StocksEntityManager entityManager) {
		super(reader, stockService, entityManager);
	}

	/**
	 *
	 * @param
	 */
	public void calculateDividendYield() {
		try {
			System.out.println("Insert stock symbol to calculate divident yield:");
			String stockSymbol = reader.nextLine();
			stockService.calculateDividendYield(stockSymbol);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Add trades in stocks to get divident yield. Choose option 8 to add trades automatically.");
		}
	}

	/**
	 *
	 * @param
	 */
	public void calculatePERatio() {
		try {
			System.out.println("Insert stock symbol to get PERatio:");
			String stockSymbol = reader.nextLine();
			stockService.calculatePERatio(stockSymbol);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Add trades in stocks to get PERatio. Choose option 8 to add trades automatically.");
		}
	}

	/**
	 *
	 * @param
	 */
	public void calculateStockPrice() {
		try {
			System.out.println("Insert stock symbol to calculate Stock Price: ");
			String stockSymbol = reader.nextLine();
			stockService.calculateStockPrice(stockSymbol);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Add trades in stocks to get stock price. Choose option 8 to add trades automatically.");
		}
	}

	/**
	 *
	 * @param
	 */
	public void calculateGBCEAllShareIndex() {
		try {
			System.out.println("Calculation of the GBCE All Share Index: ");
			System.out.println("GBCE All Share Index: " + stockService.calculateGBCEAllShareIndex());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(
					"Add trades in stocks to get GBCE All Share Index. Choose option 8 to add trades automatically.");
		}
	}
}
