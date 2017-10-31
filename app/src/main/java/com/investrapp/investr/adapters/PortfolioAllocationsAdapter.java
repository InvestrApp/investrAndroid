package com.investrapp.investr.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.investrapp.investr.R;
import com.investrapp.investr.models.Allocation;

import java.util.List;

import static com.investrapp.investr.R.id.tvAssetAllocationTicker;

public class PortfolioAllocationsAdapter extends RecyclerView.Adapter<PortfolioAllocationsAdapter.ViewHolder> {

    private List<Allocation> mAllocations;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvVAssetAllocationTicker;
        public TextView tvAssetAllocationUnitCount;

        public ViewHolder(View itemView) {
            super(itemView);
            tvVAssetAllocationTicker = (TextView) itemView.findViewById(tvAssetAllocationTicker);
            tvAssetAllocationUnitCount = (TextView) itemView.findViewById(R.id.tvAssetAllocationUnitCount);
        }
    }

    public PortfolioAllocationsAdapter(Context context, List<Allocation> allocations) {
        this.context = context;
        this.mAllocations = allocations;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View transactionView = inflater.inflate(R.layout.item_allocation, parent, false);
        ViewHolder viewHolder = new ViewHolder(transactionView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Allocation allocation = mAllocations.get(position);
        TextView tvAssetTicker = holder.tvVAssetAllocationTicker;
        TextView tvAssetUnitCount = holder.tvAssetAllocationUnitCount;
        String ticker = allocation.getTicker();
        int unitCount = allocation.getUnits();

        tvAssetTicker.setText(ticker);
        if (unitCount == 1) {
            tvAssetUnitCount.setText("" + unitCount + " unit");
        } else {
            tvAssetUnitCount.setText("" + unitCount + " units");
        }
    }

    @Override
    public int getItemCount() {
        return mAllocations.size();
    }

}
