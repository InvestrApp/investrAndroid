package com.investrapp.investr.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.investrapp.investr.R;
import com.investrapp.investr.models.Competition;

import java.util.Date;
import java.util.List;

import static com.investrapp.investr.application.InvestrApplication.context;

public class CompetitionsAdapter extends RecyclerView.Adapter<CompetitionsAdapter.ViewHolder> {

    private List<Competition> mCompetitions;
    private List<Competition> mPlayerCompetitions;;
    private Context mContext;

    class ViewHolder extends RecyclerView.ViewHolder {
        public View mItemView;
        public TextView competitionNameTextView;
        public TextView endDateTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            competitionNameTextView = (TextView) itemView.findViewById(R.id.tvCompetitionName);
            endDateTextView = (TextView) itemView.findViewById(R.id.tvEndDate);
        }
    }

    public CompetitionsAdapter(List<Competition> competitions, List<Competition> playerCompetitions) {
        mCompetitions = competitions;
        mPlayerCompetitions = playerCompetitions;
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View competitionsView = inflater.inflate(R.layout.item_competition, parent, false);
        ViewHolder viewHolder = new ViewHolder(competitionsView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Competition competition = mCompetitions.get(position);
        TextView competitionNameTextView = holder.competitionNameTextView;
        TextView competitionEndDateTextView = holder.endDateTextView;
        competitionNameTextView.setText(competition.getName());

        android.text.format.DateFormat df = new android.text.format.DateFormat();
        competitionEndDateTextView.setText("Ends " + df.format("MM/dd/yyyy", competition.getEndDate()));

        if (mPlayerCompetitions.size() > 0) {
            if (Competition.isPlayerInCompetition(competition, mPlayerCompetitions) || competition.getEndDate().before(new Date())) {
                holder.itemView.setBackgroundColor(getContext().getResources().getColor(R.color.colorWhite));
            } else {
                holder.itemView.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimaryLight));
            }
        } else {
            holder.itemView.setBackgroundColor(getContext().getResources().getColor(R.color.colorWhite));
        }
    }

    @Override
    public int getItemCount() {
        return mCompetitions.size();
    }

    public void clear() {
        mCompetitions.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Competition> list) {
        mCompetitions.addAll(list);
        notifyDataSetChanged();
    }

}
