/*
 * JStock - Free Stock Market Software
 * Copyright (C) 2015 Yan Cheng Cheok <yccheok@yahoo.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package org.yccheok.jstock.portfolio;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;

import org.yccheok.jstock.engine.Code;
import org.yccheok.jstock.engine.currency.Currency;
import org.yccheok.jstock.engine.currency.CurrencyPair;
import org.yccheok.jstock.file.ThreadSafeFileLock;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author yccheok
 */
public class PortfolioRealTimeInfo {
    // Avoid using interface class, so that our gson serialization &
    // deserialization can work correctly.

    // TODO mettre Stock au lieu de Double pour voir comment cela est remplit.
    // mais on casse la sérialisation (qui est débile en soi car pourquoi sauver cela ?
    
    public final ConcurrentHashMap<Code, Double> stockPrices = new ConcurrentHashMap<>();
    public transient ConcurrentHashMap<Code, Double> stockPercents = new ConcurrentHashMap<>();

    public final ConcurrentHashMap<CurrencyPair, Double> exchangeRates = new ConcurrentHashMap<>();

    public final ConcurrentHashMap<Code, Currency> currencies = new ConcurrentHashMap<>();

    public long stockPricesTimestamp = 0;
    public long exchangeRatesTimestamp = 0;

    public transient volatile boolean stockPricesDirty = false;
    public transient volatile boolean exchangeRatesDirty = false;
    public transient volatile boolean currenciesDirty = false;

    private static final Logger log = LoggerFactory.getLogger(PortfolioRealTimeInfo.class);

    public PortfolioRealTimeInfo() {
    }

    public PortfolioRealTimeInfo(PortfolioRealTimeInfo portfolioRealTimeInfo) {
        copy(portfolioRealTimeInfo);
    }

    private void copy(PortfolioRealTimeInfo portfolioRealTimeInfo) {
        log.debug("Copy, In");
        
        stockPrices.clear();
        stockPercents.clear();
        
        exchangeRates.clear();
        currencies.clear();

        stockPrices.putAll(portfolioRealTimeInfo.stockPrices);
        stockPercents.putAll(portfolioRealTimeInfo.stockPercents);
        exchangeRates.putAll(portfolioRealTimeInfo.exchangeRates);
        currencies.putAll(portfolioRealTimeInfo.currencies);

        stockPricesTimestamp = portfolioRealTimeInfo.stockPricesTimestamp;
        exchangeRatesTimestamp = portfolioRealTimeInfo.exchangeRatesTimestamp;

        // I don't know what to do with dirty flags at this moment. At this moment, I just copy
        // them.
        this.stockPricesDirty = portfolioRealTimeInfo.stockPricesDirty;
        this.exchangeRatesDirty = portfolioRealTimeInfo.exchangeRatesDirty;
        this.currenciesDirty = portfolioRealTimeInfo.currenciesDirty;
    }

    // http://stackoverflow.com/questions/16921012/gson-handles-case-when-synchronized-hashmap-as-class-member
    private static class ConcurrentHashMapInstanceCreator<K, V> implements
            InstanceCreator<Map<K, V>> {

        @Override
        public Map<K, V> createInstance(final Type type) {
            return new ConcurrentHashMap<>();
        }
    }

    // For debugging purpose
    private String getString(File file) throws FileNotFoundException {
        StringBuilder fileContents = new StringBuilder((int)file.length());
        Scanner scanner = new Scanner(file);
        String lineSeparator = ";";

        try {
            while (scanner.hasNextLine()) {
                fileContents.append(scanner.nextLine()).append(lineSeparator);
            }
            return fileContents.toString();
        } finally {
            scanner.close();
        }
    }


    public boolean load(File file) {
        log.debug("load, In");
        
        assert(file != null);

        if (false == file.isFile()) {
            return false;
        }

        final Gson gson = new GsonBuilder().
            registerTypeAdapter(
                new TypeToken<ConcurrentHashMap<Code, Double>>() {}.getType(),
                new ConcurrentHashMapInstanceCreator<Code, Double>())
            .registerTypeAdapter(
                new TypeToken<ConcurrentHashMap<CurrencyPair, Double>>() {}.getType(),
                new ConcurrentHashMapInstanceCreator<CurrencyPair, Double>())
            .registerTypeAdapter(
                new TypeToken<ConcurrentHashMap<Code, Currency>>() {}.getType(),
                new ConcurrentHashMapInstanceCreator<Code, Currency>())
            .create();


        PortfolioRealTimeInfo portfolioRealTimeInfo = null;
        
        final ThreadSafeFileLock.Lock lock = ThreadSafeFileLock.getLock(file);
        if (lock == null) {
            return false;
        }
        // http://stackoverflow.com/questions/10868423/lock-lock-before-try
        ThreadSafeFileLock.lockRead(lock);
        
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            try {
                portfolioRealTimeInfo = gson.fromJson(reader, PortfolioRealTimeInfo.class);

                if (portfolioRealTimeInfo == null) {
                    return false;
                }

                // Check for data corruption if we load from legacy file.
                for (Map.Entry<Code, Double> entry : portfolioRealTimeInfo.stockPrices.entrySet())
                {
                    Code code = entry.getKey();
                    Double value = entry.getValue();
                    if (code == null || value == null) {
                        return false;
                    }

                    if (code.toString() == null) {
                        return false;
                    }
                }

                for (Map.Entry<CurrencyPair, Double> entry : portfolioRealTimeInfo.exchangeRates.entrySet())
                {
                    CurrencyPair currencyPair = entry.getKey();
                    Double value = entry.getValue();
                    if (currencyPair == null || value == null) {
                        return false;
                    }

                    if (currencyPair.to() == null || currencyPair.from() == null) {
                        return false;
                    }

                    if (currencyPair.to().toString() == null || currencyPair.from().toString() == null) {
                        return false;
                    }
                }

                for (Map.Entry<Code, Currency> entry : portfolioRealTimeInfo.currencies.entrySet())
                {
                    Code code = entry.getKey();
                    Currency currency = entry.getValue();
                    if (code == null || currency == null) {
                        return false;
                    }

                    if (code.toString() == null || currency.toString() == null) {
                        return false;
                    }
                }
            } finally {
                reader.close();
            }
        } catch (IOException ex){
            log.error(null, ex);
        } catch (com.google.gson.JsonSyntaxException ex) {
            log.error(null, ex);
        } catch (Exception ex) {
            log.error(null, ex);
        } finally {
            ThreadSafeFileLock.unlockRead(lock);
            ThreadSafeFileLock.releaseLock(lock);
        }

        if (portfolioRealTimeInfo == null) {
            return false;
        }

        copy(portfolioRealTimeInfo);

        return true;
    }

    public boolean save(File file) {
        log.debug("save, In");
        GsonBuilder builder = new GsonBuilder();
        builder.enableComplexMapKeySerialization();
        Gson gson = builder.create();
        String string = gson.toJson(this);

        final ThreadSafeFileLock.Lock lock = ThreadSafeFileLock.getLock(file);
        if (lock == null) {
            return false;
        }
        // http://stackoverflow.com/questions/10868423/lock-lock-before-try
        ThreadSafeFileLock.lockWrite(lock);
        
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
            try {
                writer.write(string);
            } finally {
                writer.close();
            }
        } catch (IOException ex){
            log.error(null, ex);
            return false;
        } finally {
            ThreadSafeFileLock.unlockWrite(lock);
            ThreadSafeFileLock.releaseLock(lock);
        }

        return true;
    }
}