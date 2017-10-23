package com.investrapp.investr.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.investrapp.investr.R;
import com.investrapp.investr.activities.HomeActivity;
import com.investrapp.investr.models.Competition;

import java.util.Calendar;
import java.util.Date;


public class CreateCompetitionDialogFragment extends DialogFragment implements DatePickerFragment.FinishSelectionDialogListener {

    EditText etCreateCompetitionName;
    EditText etCreateCompetitionStartDate;
    EditText etCreateCompetitionEndDate;
    SeekBar sbCreateCompetitionStartingAmount;
    TextView tvCreateCompetitionStartingAmount;

    Button btnCreateCompetitionCancel;
    Button btnCreateCompetitionNext;

    Date startDate;
    Date endDate;

    public interface FinishCreateCompetitionDetailsListener {
        void onFinishCompetitionDetails(Competition c);
    }

    public CreateCompetitionDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static CreateCompetitionDialogFragment newInstance() {
        CreateCompetitionDialogFragment frag = new CreateCompetitionDialogFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_competition, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        this.etCreateCompetitionName = view.findViewById(R.id.etCreateCompetitionName);
        this.etCreateCompetitionStartDate = view.findViewById(R.id.etCreateCompetitionStartDate);
        this.etCreateCompetitionEndDate = view.findViewById(R.id.etCreateCompetitionEndDate);
        this.sbCreateCompetitionStartingAmount = view.findViewById(R.id.sbCreateCompetitionStartingAmount);
        this.tvCreateCompetitionStartingAmount = view.findViewById(R.id.tvCreateCompetitionStartingAmount);
        this.btnCreateCompetitionCancel = view.findViewById(R.id.btnCreateCompetitionCancel);
        this.btnCreateCompetitionNext = view.findViewById(R.id.btnCreateCompetitionNext);

        //set the change listener for the SeekBar
        sbCreateCompetitionStartingAmount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {


            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                updateSeekBarLabel();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        etCreateCompetitionStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance("START");
                datePickerFragment.setTargetFragment(CreateCompetitionDialogFragment.this, 300);
                datePickerFragment.show(getFragmentManager(), "datePicker");
            }
        });

        etCreateCompetitionEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance("END");
                datePickerFragment.setTargetFragment(CreateCompetitionDialogFragment.this, 300);
                datePickerFragment.show(getFragmentManager(), "datePicker");
            }
        });


        btnCreateCompetitionNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etCreateCompetitionName.getText().toString();

                Competition competition = new Competition(name, startDate, endDate);
                competition.saveInBackground();

                HomeActivity listener = (HomeActivity) getActivity();
                listener.onFinishCompetitionDetails(competition);
                // Close the dialog and return back to the parent activity
                dismiss();

            }
        });

        btnCreateCompetitionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        updateSeekBarLabel();
    }


    public int convertStartingValue(int val) {
        return val * 10000;
    }

    public void updateSeekBarLabel() {
        int progress = sbCreateCompetitionStartingAmount.getProgress();
        String message = "Starting Amount:  $" + convertStartingValue(progress);
        tvCreateCompetitionStartingAmount.setText(message);
    }


    @Override
    public void onFinishDatePicker(Calendar c, String dateType) {
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        String dateString = month + "/" + day + "/" + year;
        if (dateType.equals("START")) {
            etCreateCompetitionStartDate.setText(dateString);
            startDate = c.getTime();
        } else {
            etCreateCompetitionEndDate.setText(dateString);
            endDate = c.getTime();
        }
    }
}