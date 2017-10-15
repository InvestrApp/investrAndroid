package com.investrapp.investr.models;

import org.parceler.Parcel;

/**
 * Created by michaelsignorotti on 10/14/17.
 */

public interface Asset {

    String getName();
    String getTicker();
    void setName(String name);
    void setTicker(String ticker);

}
