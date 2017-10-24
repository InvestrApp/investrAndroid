package com.investrapp.investr.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.investrapp.investr.R;
import com.investrapp.investr.apis.ParseClient;
import com.investrapp.investr.models.Competition;
import com.investrapp.investr.models.Player;
import com.investrapp.investr.models.Transaction;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.text.NumberFormat;
import java.util.Date;
import java.util.List;


public class AssetTransactionDialogFragment extends DialogFragment {

    private View view;
    private TextView tvAssetName;
    private TextView tvAssetPrice;
    private EditText etSelectNumberOfUnits;
    private TextView tvCurrentAssetCount;
    private TextView tvTransactionPrice;
    private TextView tvCashRemaining;
    private Button btnSubmitTransaction;
    private Competition competition;
    private Player player;
    private String ticker;
    private String name;
    private int totalUnits;
    private double cash;
    private double price;
    private String action;

    public AssetTransactionDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static AssetTransactionDialogFragment newInstance(Competition competition, Player player,
                                                             String name, String ticker, int totalUnits,
                                                             double price, String action) {
        AssetTransactionDialogFragment frag = new AssetTransactionDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable("competition", competition);
        args.putParcelable("player", player);
        args.putString("ticker", ticker);
        args.putString("name", name);
        args.putInt("totalUnits", totalUnits);
        args.putDouble("price", price);
        args.putString("action", action);
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
        this.view = view;
        setupViews();

        etSelectNumberOfUnits.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                String input = s.toString();
                if (!input.equals("")) {
                    int tradeVolume = Integer.parseInt(s.toString());
                    double totalCost = tradeVolume * price;
                    double updatedCash = 0;
                    if (action.equals("BUY")) {
                        updatedCash = cash - totalCost;
                    } else {
                        updatedCash = cash + totalCost;
                    }
                    tvTransactionPrice.setText("Total cost: " + NumberFormat.getCurrencyInstance().format(totalCost));
                    tvCashRemaining.setText(NumberFormat.getCurrencyInstance().format(updatedCash) + " cash left");
                } else {
                    tvCashRemaining.setText(NumberFormat.getCurrencyInstance().format(cash) + " cash left");
                }
            }
        });

        getItemsFromParcelable();
        getCashForPlayerInCompetition();
        setViewInformation();

        btnSubmitTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (action.equals("BUY")) {
                    buySelected();
                } else {
                    sellSelected();
                }
            }
        });
    }

    private void setupViews() {
        this.tvAssetName = view.findViewById(R.id.tvTransactionAssetName);
        this.tvAssetPrice = view.findViewById(R.id.tvTransactionAssetCurrentPrice);
        this.tvCurrentAssetCount = view.findViewById(R.id.tvCurrentAssetCount);
        this.tvTransactionPrice = view.findViewById(R.id.tvTransactionPrice);
        this.tvCashRemaining = view.findViewById(R.id.tvCashRemaining);
        this.btnSubmitTransaction = view.findViewById(R.id.btnSubmitTransaction);
        this.etSelectNumberOfUnits = view.findViewById(R.id.etSelectNumberOfUnits);
    }

    private void getItemsFromParcelable() {
        this.competition = getArguments().getParcelable("competition");
        this.player = getArguments().getParcelable("player");
        this.ticker = getArguments().getString("ticker");
        this.name = getArguments().getString("name");
        this.totalUnits = getArguments().getInt("totalUnits");
        this.price = getArguments().getDouble("price");
        this.action = getArguments().getString("action");
    }

    public void getCashForPlayerInCompetition() {
        ParseClient.getCashForPlayerInCompetition(player, competition, new FindCallback<Transaction>() {
            @Override
            public void done(List<Transaction> objects, ParseException e) {
                //there is only one cash unit per player and it is contained in a single transaction
                cash = 0;
                for (Transaction t : objects) {
                    cash += t.getPrice();
                }
                tvCashRemaining.setText(NumberFormat.getCurrencyInstance().format(cash) + " cash left");
            }
        });
    }

    public void setViewInformation() {
        tvAssetName.setText(name);
        tvAssetPrice.setText(NumberFormat.getCurrencyInstance().format(price));
        tvCurrentAssetCount.setText("You currently own " + totalUnits + " " + ticker + ".");
        tvTransactionPrice.setText("Total cost: $0.00");
        tvCashRemaining.setText(NumberFormat.getCurrencyInstance().format(cash) + " cash left");

        if (action.equals("BUY")) {
            btnSubmitTransaction.setText("Buy");
        } else {
            btnSubmitTransaction.setText("Sell");
        }
    }

    public void buySelected() {
        final int units = Integer.parseInt(etSelectNumberOfUnits.getText().toString());
        //buy the cryptocurrency
        Transaction transaction = new Transaction(player, competition, "cryptocurrency", ticker,
                new Date(), Transaction.TransactionAction.BUY, price, units);
        ParseClient.addTransaction(transaction);
        //subtract the purchase total from the cash asset
        ParseClient.updateCash(player, competition, new FindCallback<Transaction>() {
            @Override
            public void done(List<Transaction> objects, ParseException e) {
                for (Transaction cashTransaction : objects) {
                    double newCashValue = cash - price * units;
                    cashTransaction.setPrice(newCashValue);
                    cashTransaction.saveInBackground();
                }
            }
        });
        sendNotificationToCompetitors();
        dismiss();
    }

    public void sellSelected() {
        final int units = -1*Integer.parseInt(etSelectNumberOfUnits.getText().toString());
        //buy the cryptocurrency
        Transaction transaction = new Transaction(player, competition, "cryptocurrency", ticker,
                new Date(), Transaction.TransactionAction.SELL, price, units);
        ParseClient.addTransaction(transaction);
        //subtract the purchase total from the cash asset
        ParseClient.updateCash(player, competition, new FindCallback<Transaction>() {
            @Override
            public void done(List<Transaction> objects, ParseException e) {
                for (Transaction cashTransaction : objects) {
                    double newCashValue = cash - price * units;
                    cashTransaction.setPrice(newCashValue);
                    cashTransaction.saveInBackground();
                }
            }
        });
        sendNotificationToCompetitors();
        dismiss();
    }

    private void sendNotificationToCompetitors() {
        ParseClient.sendPushToAllCompetitors(competition, player, "Someone just traded " + ticker + "! What do you think?");
    }

}
