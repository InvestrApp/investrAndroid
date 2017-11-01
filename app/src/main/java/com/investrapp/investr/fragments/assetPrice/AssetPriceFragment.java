package com.investrapp.investr.fragments.assetPrice;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.investrapp.investr.R;
import com.investrapp.investr.adapters.PriceAdapter;
import com.investrapp.investr.models.CryptocurrencyPriceTimeSeries;
import com.investrapp.investr.models.Price;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.investrapp.investr.R.id.chart;

public abstract class AssetPriceFragment extends Fragment {

    protected PriceAdapter priceAdapter;
    protected ArrayList<Price> mPrices;
    protected RecyclerView rvPrices;
    protected LinearLayoutManager linearLayoutManager;
    protected String ticker;
    protected LineChart mChart;
    protected List<Entry> chartEntries;
    protected boolean chartEnabled;

    // Define the listener of the interface type
    // listener will the activity instance containing fragment
    private OnPriceTimeSeriesResponseListener listener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_asset_price, container, false);
        rvPrices = (RecyclerView) v.findViewById(R.id.rvAssetPrices);
        mChart = (LineChart) v.findViewById(chart);
        mPrices = new ArrayList<>();
        chartEntries = new ArrayList<>();
        chartEnabled = true;
        rvPrices.setVisibility(View.INVISIBLE);

        priceAdapter = new PriceAdapter(getContext(), mPrices);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rvPrices.setLayoutManager(linearLayoutManager);
        rvPrices.setAdapter(priceAdapter);

        this.ticker = getArguments().getString("ticker");
        loadPrices(ticker);
        setHasOptionsMenu(true);
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_assetprice_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_toggle) {
            if (chartEnabled) {
                mChart.setVisibility(View.INVISIBLE);
                rvPrices.setVisibility(View.VISIBLE);
                chartEnabled = false;
            } else {
                mChart.setVisibility(View.VISIBLE);
                rvPrices.setVisibility(View.INVISIBLE);
                chartEnabled = true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    // Store the listener (activity) that will have events fired once the fragment is attached
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPriceTimeSeriesResponseListener) {
            listener = (OnPriceTimeSeriesResponseListener) context;
        } else {
            throw new ClassCastException(context.toString());
        }
    }

    protected abstract void loadPrices(String assetTicker);

    protected void updatePrices(final CryptocurrencyPriceTimeSeries cryptocurrencyPriceTimeSeries) {
        if(getActivity() == null) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                listener.onPriceTimeSeriesResponse(cryptocurrencyPriceTimeSeries);
                mPrices.addAll(cryptocurrencyPriceTimeSeries.getPriceList());
                createChartData();
                Collections.reverse(mPrices);
                priceAdapter.notifyDataSetChanged();
            }
        });
    }

    public interface OnPriceTimeSeriesResponseListener {
        void onPriceTimeSeriesResponse(CryptocurrencyPriceTimeSeries cryptocurrencyPriceTimeSeries);
    }

    protected void createChartData() {
        int daysBack = 12;
        List<Price> prices = new ArrayList<>(mPrices);
        prices = prices.subList(prices.size() - daysBack, prices.size());
        Long minTime = Long.MAX_VALUE;

        for (Price price : prices) {
            minTime = Math.min(price.getDateInTime(), minTime);
        }
        for (Price price : prices) {
            long time = price.getDateInTime();
            long x = price.getDateInTime() - minTime;
            chartEntries.add(new Entry(x, (float) price.getPrice()));
        }

        IAxisValueFormatter xAxisFormatter = getFormatter(minTime);
        XAxis xAxis = mChart.getXAxis();
        xAxis.setValueFormatter(xAxisFormatter);
        LineDataSet dataSet = new LineDataSet(chartEntries, "price");
        dataSet.setLineWidth(3f);
        dataSet.setColor(getResources().getColor(R.color.colorAccent));
        dataSet.setCircleColor(getResources().getColor(R.color.colorPrimaryDark));
        dataSet.setCircleColorHole(getResources().getColor(R.color.colorPrimaryDark));
        LineData lineData = new LineData(dataSet);
        mChart.setData(lineData);
        Description description = new Description();
        description.setText("");
        mChart.setDescription(description);
        mChart.setDrawBorders(false);
        mChart.getLegend().setEnabled(false);
        mChart.invalidate();
    }

    protected abstract IAxisValueFormatter getFormatter(Long mintime);

}

