package com.investrapp.investr.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.investrapp.investr.R;
import com.investrapp.investr.adapters.PortfolioPagerAdapter;
import com.investrapp.investr.apis.ParseClient;
import com.investrapp.investr.models.Competition;
import com.investrapp.investr.models.Player;
import com.investrapp.investr.models.Portfolio;
import com.investrapp.investr.interfaces.PortfolioValueListener;
import com.investrapp.investr.models.Transaction;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class PortfolioFragment extends Fragment implements PortfolioValueListener {

    private View view;
    private Portfolio mPortfolio;
    private Competition competition;
    private Player player;
    private ImageView ivPlayerProfile;
    private TextView tvName;
    private TextView tvPortfolioValue;
    private TextView tvCashValue;
    private FragmentPagerAdapter adapterViewPager;
    private TabLayout tabLayout;

    public PortfolioFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.player = getArguments().getParcelable("player");
        this.competition = getArguments().getParcelable("competition");
        mPortfolio = new Portfolio(player, competition);
        mPortfolio.setPortfolioListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_portfolio, container, false);
        setupView();
        ViewPager vpPager = (ViewPager) view.findViewById(R.id.vpPager);
        adapterViewPager = new PortfolioPagerAdapter(getChildFragmentManager(), competition, player);
        vpPager.setAdapter(adapterViewPager);
        tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vpPager);
        getTransactions();
        return view;
    }

    private void setupView() {
        ivPlayerProfile = (ImageView) view.findViewById(R.id.ivPlayerProfile);
        tvName = (TextView) view.findViewById(R.id.tvName);
        tvPortfolioValue = (TextView) view.findViewById(R.id.tvPortfolioValue);
        tvCashValue = (TextView) view.findViewById(R.id.tvCashValue);

        tvName.setText(mPortfolio.getPlayer().getName());
        Glide.with(getApplicationContext())
                .load(mPortfolio.getPlayer().getProfileImageUrl())
                .into(ivPlayerProfile);
    }

    public static PortfolioFragment newInstance(Player player, Competition competition) {
        PortfolioFragment fragment = new PortfolioFragment();
        Bundle args = new Bundle();
        args.putParcelable("player", player);
        args.putParcelable("competition", competition);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onValueUpdate() {
        if(getActivity() == null) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                tvPortfolioValue.setText(mPortfolio.getValueFormatted() + " portfolio value");
                tvCashValue.setText(mPortfolio.getCashFormatted() + " cash");
            }
        });
    }

    void getTransactions() {
        ParseClient.getTransactionsForPlayerInCompetition(mPortfolio.getPlayer(), mPortfolio.getCompetition(), new FindCallback<Transaction>() {
            @Override
            public void done(List<Transaction> objects, ParseException e) {
                mPortfolio.addTransactions(objects);
                mPortfolio.calculateAvailableCash();
                mPortfolio.calculateTotalPortfolioValue();
            }
        });
    }
}
