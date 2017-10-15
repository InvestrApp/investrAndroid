package com.investrapp.investr.interfaces;


import com.investrapp.investr.models.Cryptocurrency;
import com.investrapp.investr.models.Stock;

import java.util.ArrayList;

/**
 * Created by michaelsignorotti on 10/13/17.
 */

public interface AlphaAvantageClientListener {
    void stockPriceSearchListener(Stock stock);
}
