package com.investrapp.investr.interfaces;

import com.investrapp.investr.models.Asset;

import com.investrapp.investr.models.Stock;

/**
 * Created by michaelsignorotti on 10/13/17.
 */

public interface AlphaAvantageClientListener {
    void stockPriceSearchListener(Asset stock);
}
