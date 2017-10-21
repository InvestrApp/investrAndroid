package com.investrapp.investr.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.investrapp.investr.R;
import com.investrapp.investr.models.Player;
import com.investrapp.investr.models.Ranking;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class RankingsAdapter extends RecyclerView.Adapter<RankingsAdapter.ViewHolder> {

    private List<Ranking> mRankings;
    private Context mContext;

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPlayerProfile;
        private TextView tvName;
        private TextView tvPortfolioValue;
        private TextView tvPlayerRank;

        public ViewHolder(View itemView) {
            super(itemView);
            ivPlayerProfile = (ImageView) itemView.findViewById(R.id.ivPlayerProfile);
            tvPlayerRank = (TextView) itemView.findViewById(R.id.tvPlayerRank);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvPortfolioValue = (TextView) itemView.findViewById(R.id.tvPortfolioValue);
        }
    }

    public RankingsAdapter(Context context, List<Ranking> rankings) {
        mContext = context;
        mRankings = rankings;
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View vRanking = inflater.inflate(R.layout.item_ranking, parent, false);
        ViewHolder vhRanking = new ViewHolder(vRanking);
        return vhRanking;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Ranking ranking = mRankings.get(position);
        Player player = ranking.getPlayer();

        TextView tvPlayerRank = holder.tvPlayerRank;
        TextView tvName = holder.tvName;
        TextView tvPortfolioValue = holder.tvPortfolioValue;
        ImageView ivPlayerProfile = holder.ivPlayerProfile;

        tvName.setText(player.getName());
        tvPortfolioValue.setText(ranking.getValueFormatted());
        tvPlayerRank.setText(String.valueOf(ranking.getRanking()));
        Glide.with(getApplicationContext())
                .load(player.getProfileImageUrl())
                .into(ivPlayerProfile);
    }

    @Override
    public int getItemCount() {
        return mRankings.size();
    }

}
