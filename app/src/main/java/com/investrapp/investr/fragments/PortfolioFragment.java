package com.investrapp.investr.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.investrapp.investr.R;
import com.investrapp.investr.adapters.TransactionAdapter;
import com.investrapp.investr.apis.ParseClient;
import com.investrapp.investr.models.Competition;
import com.investrapp.investr.models.Player;
import com.investrapp.investr.models.Portfolio;
import com.investrapp.investr.models.PortfolioListener;
import com.investrapp.investr.models.Transaction;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class PortfolioFragment extends Fragment implements PortfolioListener {

    private View view;
    private Portfolio mPortfolio;
    private RecyclerView rvTransactions;
    private TransactionAdapter mAdapter;
    private ImageView ivPlayerProfile;
    private TextView tvName;
    private TextView tvPortfolioValue;
    private TextView tvCashValue;

    public PortfolioFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Player player = getArguments().getParcelable("player");
        Competition competition = getArguments().getParcelable("competition");
        mPortfolio = new Portfolio(player, competition);
        mPortfolio.setPortfolioListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_portfolio, container, false);
        setupView();
        setupRecyclerView();
        getTransactions();
        return view;
    }

    private void setupView() {
        rvTransactions = (RecyclerView) view.findViewById(R.id.rvTransactions);
        ivPlayerProfile = (ImageView) view.findViewById(R.id.ivPlayerProfile);
        tvName = (TextView) view.findViewById(R.id.tvName);
        tvPortfolioValue = (TextView) view.findViewById(R.id.tvPortfolioValue);
        tvCashValue = (TextView) view.findViewById(R.id.tvCashValue);

        tvName.setText(mPortfolio.getPlayer().getName());
        Glide.with(getApplicationContext())
                .load(mPortfolio.getPlayer().getProfileImageUrl())
                .into(ivPlayerProfile);
    }

    private void setupRecyclerView() {
        rvTransactions = (RecyclerView) view.findViewById(R.id.rvTransactions);
        mAdapter = new TransactionAdapter(view.getContext(), mPortfolio.getTransactions());
        rvTransactions.setAdapter(mAdapter);
        rvTransactions.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    public static PortfolioFragment newInstance(Player player, Competition competition) {
        PortfolioFragment fragment = new PortfolioFragment();
        Bundle args = new Bundle();
        args.putParcelable("player", player);
        args.putParcelable("competition", competition);
        fragment.setArguments(args);
        return fragment;
    }

    private void getTransactions() {
        ParseClient.getTransactionsForPlayerInCompetition(mPortfolio.getPlayer(), mPortfolio.getCompetition(), new FindCallback<Transaction>() {
            @Override
            public void done(List<Transaction> objects, ParseException e) {
                mPortfolio.addTransactions(objects);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onValueUpdate() {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                tvPortfolioValue.setText(mPortfolio.getValueFormatted() + " portfolio value");
                tvCashValue.setText(mPortfolio.getCashFormatted() + " cash");
            }
        });
    }

}
