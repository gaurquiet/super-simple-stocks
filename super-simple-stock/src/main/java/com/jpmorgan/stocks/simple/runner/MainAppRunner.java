package com.jpmorgan.stocks.simple.runner;

import java.util.Scanner;

import com.jpmorgan.stocks.simple.arch.SimpleStockServicesFactory;
import com.jpmorgan.stocks.simple.arch.SpringService;
import com.jpmorgan.stocks.simple.backend.StocksEntityManager;
import com.jpmorgan.stocks.simple.services.SimpleStockService;

/**
 * 
 * @author
 *
 */
public class MainAppRunner {

	private static final Integer EXIT = 9;
	private static Integer option = 0;

	/**
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("== Super Simple Stock Application ==");
		SimpleStockService simpleStockService = SimpleStockServicesFactory.INSTANCE.getSimpleStockService();
		StocksEntityManager stocksEntityManager = SpringService.INSTANCE.getBean("stocksEntityManager",
				StocksEntityManager.class);
		Scanner reader = new Scanner(System.in);
		String scanned = null;

		StockUI stockUi = new StockUI(reader, simpleStockService, stocksEntityManager);
		PortfolioUI portfolioUi = new PortfolioUI(reader, simpleStockService, stocksEntityManager);

		while (EXIT != option) {
			showMenu(scanned, reader);
			switch (option) {
			case 1:
				stockUi.newTrade();
				break;
			case 2:
				portfolioUi.calculateDividendYield();
				break;
			case 3:
				portfolioUi.calculatePERatio();
				break;
			case 4:
				portfolioUi.calculateStockPrice();
				break;
			case 5:
				portfolioUi.calculateGBCEAllShareIndex();
				break;
			case 6:
				stockUi.stockReport();
				break;
			case 7:
				stockUi.tradeReport();
				break;
			case 8:
				stockUi.addAllTradeFromConfigFile();
				break;
			default:
				System.out.println("Insert a valid option");
				break;
			}
		}

	}

	/**
	 *
	 * @param scanned
	 * @param reader
	 */
	public static void showMenu(String scanned, Scanner reader) {
		System.out.println("\n");
		System.out.println("-----------------------------------");
		System.out.println("Please select an option");
		System.out.println("1. New trade");
		System.out.println("2. Calculate Dividend Yield");
		System.out.println("3. Calculate PE Ratio");
		System.out.println("4. Calculate Volume Weighted Stock Price");
		System.out.println("5. Calculate the GBCE All Share Index using the geometric mean of prices for all stocks");
		System.out.println("6. Stock report");
		System.out.println("7. Portfolio report (all the trades in specific stock)");
		System.out.println("8. Add pre-defined trades into system from a config file");
		System.out.println("9. Exit");
		scanned = reader.nextLine();

		option = 0;
		try {
			option = Integer.valueOf(scanned);
		} catch (Exception e) {
			System.out.println("Please insert an option");
		}
	}
}
