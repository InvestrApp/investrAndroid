package com.investrapp.investr.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.investrapp.investr.R;
import com.investrapp.investr.models.Asset;
import com.investrapp.investr.models.Competition;

import java.util.List;

import static com.investrapp.investr.application.InvestrApplication.context;


public class AssetAdapter extends RecyclerView.Adapter<AssetAdapter.ViewHolder> {

    private List<Asset> mAssets;
    private Context mContext;

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public TextView tvTicker;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvTicker = (TextView) itemView.findViewById(R.id.tvTicker);
        }
    }

    public AssetAdapter(List<Asset> assets) {
        mAssets = assets;
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

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
