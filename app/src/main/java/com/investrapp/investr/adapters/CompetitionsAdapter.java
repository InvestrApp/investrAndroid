package com.investrapp.investr.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.investrapp.investr.R;
import com.investrapp.investr.models.Competition;

import java.util.List;

public class CompetitionsAdapter extends RecyclerView.Adapter<CompetitionsAdapter.ViewHolder> {

    private List<Competition> mCompetitions;
    private Context mContext;

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView competitionNameTextView;
        public TextView endDateTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            competitionNameTextView = (TextView) itemView.findViewById(R.id.tvCompetitionName);
            endDateTextView = (TextView) itemView.findViewById(R.id.tvEndDate);
        }
    }

    public CompetitionsAdapter(Context context, List<Competition> competitions) {
        mCompetitions = competitions;
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
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
    }

    @Override
    public int getItemCount() {
        return mCompetitions.size();
    }

}
