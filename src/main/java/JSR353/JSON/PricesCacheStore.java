/*
  The GNU General Public License (GPL)

  Version 2, June 1991

  Copyright (C) 1989, 1991 Free Software Foundation, Inc.
  59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

  Everyone is permitted to copy and distribute verbatim copies of this license
  document, but changing it is not allowed.

  Copyright (c) 2013, Mani Sarkar <sadhak001@gmail.com> All rights reserved.

  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.

  This code is free software; you can redistribute it and/or modify it
  under the terms of the GNU General Public License version 2 only, as
  published by the Free Software Foundation.  Oracle designates this
  particular file as subject to the "Classpath" exception as provided
  by Oracle in the LICENSE file that accompanied this code.

  This code is distributed in the hope that it will be useful, but WITHOUT
  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
  FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
  version 2 for more details (a copy is included in the LICENSE file that
  accompanied this code).

  You should have received a copy of the GNU General Public License version
  2 along with this work; if not, write to the Free Software Foundation,
  Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package JSR353.JSON;

import java.util.concurrent.ConcurrentHashMap;

public enum PricesCacheStore {

    INSTANCE;

    private volatile String lastSymbolUpdated;

    // will contain Symbols and prices stored and constantly updated
    private ConcurrentHashMap<String, Double> pricesCache = new ConcurrentHashMap<>();

    public void addPrice(String symbol, Double price) {
        if (symbol != null && price != null) {
            pricesCache.put(symbol, price);
            lastSymbolUpdated = symbol;
        }
    }

    public Double getPrice(String symbol) {
        if (symbol == null) {
            return 0d;
        }
        Double price = pricesCache.get(symbol);
        return price == null ? 0d : price;
    }

    // Object is threading is safe so can be returned as it is
    public ConcurrentHashMap<String, Double> getAllPrices() {
        return pricesCache;
    }

    public String getLastSymbolUpdated() {
        return new String(lastSymbolUpdated);
    }
}
