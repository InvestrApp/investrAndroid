package com.investrapp.investr.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.investrapp.investr.R;
import com.investrapp.investr.interfaces.AssetAdapterListener;
import com.investrapp.investr.models.Asset;
import com.investrapp.investr.models.Cryptocurrency;
import com.investrapp.investr.models.Stock;

import java.util.List;



public class AssetAdapter extends RecyclerView.Adapter<AssetAdapter.ViewHolder> {

    private List<Asset> mAssets;
    private Context mContext;

    private AssetAdapterListener assetAdapterListener;

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvName;
        public TextView tvTicker;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvTicker = (TextView) itemView.findViewById(R.id.tvTicker);


            // Attach a click listener to the entire row view
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                Asset asset = mAssets.get(position);
                // We can access the data within the views
                if (asset instanceof Cryptocurrency) {
                    assetAdapterListener.assetSelected(position);
                } else if (asset instanceof Stock) {
                    //TODO
                }

            }
        }
    }

    public AssetAdapter(List<Asset> assets, AssetAdapterListener assetAdapterListener) {
        this.mAssets = assets;
        this.assetAdapterListener = assetAdapterListener;
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        this.mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View vAsset = inflater.inflate(R.layout.item_asset, parent, false);
        ViewHolder vhAsset = new ViewHolder(vAsset);
        return vhAsset;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Asset asset = mAssets.get(position);
        TextView tvName = holder.tvName;
        TextView tvTicker = holder.tvTicker;
        tvName.setText(asset.getName());
        tvTicker.setText(asset.getTicker());
    }

    @Override
    public int getItemCount() {
        return mAssets.size();
    }
}
