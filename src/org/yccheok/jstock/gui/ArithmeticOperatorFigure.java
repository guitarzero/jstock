/*
 * ArithmeticOperatorFigure.java
 *
 * Created on May 20, 2007, 12:14 AM
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

package org.yccheok.jstock.gui;

import java.io.IOException;
import java.awt.geom.*;
import java.beans.*;
import static org.jhotdraw.draw.AttributeKeys.*;
import java.util.*;
import org.jhotdraw.draw.*;
import org.jhotdraw.geom.*;
import org.jhotdraw.util.*;
import org.jhotdraw.xml.*;
import org.yccheok.jstock.analysis.*;

/**
 *
 * @author yccheok
 */
public class ArithmeticOperatorFigure extends OperatorFigure {
    
    public ArithmeticOperatorFigure() {
        super(new ArithmeticOperator());
        ArithmeticOperator operator = (ArithmeticOperator)getOperator();
        this.setName("Arithmetic");
        this.setAttribute(operator.getArithmetic().toString());
        this.setValue("");
    }
}

