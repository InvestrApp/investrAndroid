package com.investrapp.investr.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.investrapp.investr.R;
import com.investrapp.investr.models.Cash;
import com.investrapp.investr.models.Transaction;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private List<Transaction> mTransactions;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTicker;
        public TextView tvValue;
        public TextView tvDate;
        public TextView tvUnits;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTicker = (TextView) itemView.findViewById(R.id.tvTicker);
            tvValue = (TextView) itemView.findViewById(R.id.tvValue);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvUnits = (TextView) itemView.findViewById(R.id.tvUnits);
        }
    }

    public TransactionAdapter(Context context, List<Transaction> transactions) {
        this.context = context;
        mTransactions = transactions;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View transactionView = inflater.inflate(R.layout.item_transaction, parent, false);
        ViewHolder viewHolder = new ViewHolder(transactionView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Transaction transaction = mTransactions.get(position);

        TextView tvTicker = holder.tvTicker;
        TextView tvValue = holder.tvValue;
        TextView tvDate = holder.tvDate;
        TextView tvUnits = holder.tvUnits;

        tvTicker.setText(transaction.getAssetTicker());
        if (transaction.getAssetType().equals(Cash.ASSET_TYPE)) {
            tvValue.setText(transaction.getValueFormatted());
        } else {
            String value = transaction.getAction().toString() + " @ " + transaction.getValueFormatted();
            tvValue.setText(value);
            android.text.format.DateFormat df = new android.text.format.DateFormat();
            tvDate.setText( df.format("MM/dd/yyyy", transaction.getDate()));
            tvUnits.setText(Integer.toString(transaction.getUnits()) + " units");
        }

    }

    @Override
    public int getItemCount() {
        return mTransactions.size();
    }

}
