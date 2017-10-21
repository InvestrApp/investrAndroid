package com.investrapp.investr.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.investrapp.investr.R;


public class AssetTransactionDialogFragment extends DialogFragment  {

    TextView tvAssetName;
    TextView tvAssetTicker;
    TextView tvAssetPrice;

    TextView tvCurrentAssetCount;

    TextView tvTransactionPrice;

    Button btnSubmitTransaction;




    public AssetTransactionDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static AssetTransactionDialogFragment newInstance() {
        AssetTransactionDialogFragment frag = new AssetTransactionDialogFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_asset_transaction, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        this.tvAssetName = view.findViewById(R.id.tvAssetName);
        this.tvAssetTicker = view.findViewById(R.id.tvAssetTicker);
        this.tvAssetPrice = view.findViewById(R.id.tvAssetPrice);
        this.tvCurrentAssetCount = view.findViewById(R.id.tvCurrentAssetCount);
        this.tvTransactionPrice = view.findViewById(R.id.tvTransactionPrice);
        this.btnSubmitTransaction = view.findViewById(R.id.btnSubmitTransaction);

    }
}
