package com.investrapp.investr.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.investrapp.investr.R;
import com.investrapp.investr.apis.ParseClient;
import com.investrapp.investr.models.Cash;
import com.investrapp.investr.models.Competition;
import com.investrapp.investr.models.CompetitionPlayer;
import com.investrapp.investr.models.Player;
import com.investrapp.investr.models.Price;
import com.investrapp.investr.models.Transaction;
import com.investrapp.investr.utils.DistanceUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;


public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.ViewHolder> {

    private List<Player> mPlayers;
    private Context mContext;
    private Player currentPlayer;
    private Competition competition;

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPlayerImage;
        private TextView tvPlayerName;
        private TextView tvPlayerDistance;
        public ViewHolder(View itemView) {
            super(itemView);
            ivPlayerImage = (ImageView) itemView.findViewById(R.id.ivPlayerImage);
            tvPlayerName = (TextView) itemView.findViewById(R.id.tvPlayerName);
            tvPlayerDistance= (TextView) itemView.findViewById(R.id.tvPlayerDistance);
        }
    }

    public PlayerAdapter(Context context, List<Player> players, Competition competition, Player currentPlayer) {
        this.mContext = context;
        this.mPlayers = players;
        this.currentPlayer = currentPlayer;
        this.competition = competition;
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View vPlayers = inflater.inflate(R.layout.item_player, parent, false);
        ViewHolder vhPlayers = new ViewHolder(vPlayers);
        return vhPlayers;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Player player = mPlayers.get(position);

        TextView tvName = holder.tvPlayerName;
        TextView tvDistance = holder.tvPlayerDistance;
        ImageView ivProfilePicture = holder.ivPlayerImage;

        Glide.with(getApplicationContext())
                .load(player.getProfileImageUrl())
                .centerCrop()
                .into(ivProfilePicture);
        tvName.setText(player.getName());

        double otherPlayerLatitude = player.getLatitude();
        double otherPlayerLongitude = player.getLongitude();


        double currentPlayerLatitude = currentPlayer.getLatitude();
        double currentPlayerLongitude = currentPlayer.getLongitude();

        double distance = DistanceUtils.distFrom(currentPlayerLatitude, currentPlayerLongitude,
                otherPlayerLatitude, otherPlayerLongitude);
        //round the distance to one decimal place
        distance = Math.round(distance * 10.0) / 10.0;
        String distanceMessage = "" + distance + " miles away";
        tvDistance.setText(distanceMessage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = new GregorianCalendar();
                Date currentDate = calendar.getTime();

                CompetitionPlayer competitionPlayer = new CompetitionPlayer(competition, player);

                Double initialCash = 10000.00;
                if (competition.getInitialAmount() != null) {
                    initialCash = competition.getInitialAmount();
                }

                Transaction transaction = new Transaction(player, competition, Cash.ASSET_TYPE, Cash.TICKER,
                        currentDate, Transaction.TransactionAction.BUY, initialCash, 1);
                ParseClient.addTransaction(transaction);

                ParseClient.addPlayerToCompetition(competitionPlayer);
                ParseClient.sendPushToAllCompetitors(competition, player, "A new player has joined " + competition.getName());


                String message = "Added " + player.getName() + " to " + competition.getName();
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPlayers.size();
    }

}
