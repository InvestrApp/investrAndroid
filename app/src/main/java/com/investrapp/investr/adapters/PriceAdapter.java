package com.investrapp.investr.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.investrapp.investr.R;
import com.investrapp.investr.models.Price;

import java.util.List;

import static com.investrapp.investr.application.InvestrApplication.context;

public class PriceAdapter extends RecyclerView.Adapter<PriceAdapter.ViewHolder> {

    private List<Price> mPrices;
    private Context mContext;

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvPriceDate;
        public TextView tvPriceAmount;

        public ViewHolder(View itemView) {
            super(itemView);
            tvPriceDate = (TextView) itemView.findViewById(R.id.tvPriceDate);
            tvPriceAmount = (TextView) itemView.findViewById(R.id.tvPriceAmount);
        }
    }

    public PriceAdapter(Context context, List<Price> prices) {
        this.mContext = context;
        this.mPrices = prices;
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View vPrice = inflater.inflate(R.layout.item_price, parent, false);
        ViewHolder vhPrice = new ViewHolder(vPrice);
        return vhPrice;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Price price = mPrices.get(position);
        TextView tvPriceDate = holder.tvPriceDate;
        TextView tvPriceAmount = holder.tvPriceAmount;
        tvPriceDate.setText(price.getDate().toString());
        tvPriceAmount.setText("" + price.getPrice());
    }

    @Override
    public int getItemCount() {
        return mPrices.size();
    }
}
