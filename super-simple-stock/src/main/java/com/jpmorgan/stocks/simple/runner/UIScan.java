/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpmorgan.stocks.simple.runner;

import java.util.Scanner;

import com.jpmorgan.stocks.simple.backend.StocksEntityManager;
import com.jpmorgan.stocks.simple.services.SimpleStockService;

/**
 *
 * @author
 */
public class UIScan {

	/**
	 *
	 */
	protected Scanner reader;

	protected SimpleStockService stockService;

	protected StocksEntityManager entityManager;

	/**
	 *
	 * @param reader
	 */
	public UIScan(Scanner reader, SimpleStockService stockService, StocksEntityManager entityManager) {
		this.reader = reader;
		this.stockService = stockService;
		this.entityManager = entityManager;
	}
}
