package com.investrapp.investr.models;

/**
 * Created by michaelsignorotti on 10/14/17.
 */

public interface Asset {

    String assetType();
    String getName();
    String getTicker();
    void setName(String name);
    void setTicker(String ticker);

}
