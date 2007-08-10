/*
 * MalaysiaMarket.java
 *
 * Created on May 6, 2007, 4:00 AM
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * Copyright (C) 2007 Cheok YanCheng <yccheok@yahoo.com>
 */

package org.yccheok.jstock.engine;

import java.util.*;

/**
 *
 * @author yccheok
 */
public class MalaysiaMarket implements Market {
    
    /** Creates a new instance of MalaysiaMarket */
    public MalaysiaMarket(
            double mainBoardIndex, double mainBoardChange, 
            double secondBoardIndex, double secondBoardChange,
            double mesdaqIndex, double mesdaqChange,
            int up, int down, int unchange, long volume,
            double value
            ) 
    {
        indexMap.put(Stock.Board.Main, mainBoardIndex);
        indexMap.put(Stock.Board.Second, secondBoardIndex);
        indexMap.put(Stock.Board.Mesdaq, mesdaqIndex);

        changeMap.put(Stock.Board.Main, mainBoardChange);
        changeMap.put(Stock.Board.Second, secondBoardChange);
        changeMap.put(Stock.Board.Mesdaq, mesdaqChange);
        
        numMap.put(ChangeType.Up, up);
        numMap.put(ChangeType.Down, down);
        numMap.put(ChangeType.Unchange, unchange);    
        
        this.volume = volume;
        this.value = value;
    }
    
    public double getIndex(Stock.Board board)
    {
        return indexMap.get(board);
    }
    
    public double getChange(Stock.Board board)
    {
        return changeMap.get(board);
    }
    
    public int getNumOfStockChange(ChangeType type)
    {
        return numMap.get(type);
    }

    public long getVolume()
    {
        return volume;
    }
    
    public double getValue() {
        return value;
    }
    
    private Map<Stock.Board, Double> indexMap = new HashMap<Stock.Board, Double>();
    private Map<Stock.Board, Double> changeMap = new HashMap<Stock.Board, Double>();
    private Map<ChangeType, Integer> numMap = new HashMap<ChangeType, Integer>();
    private long volume;
    private double value;
}
