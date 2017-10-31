package com.investrapp.investr.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {

    public interface FinishSelectionDialogListener {
        void onFinishDatePicker(Calendar c, String dateType);
    }

    String dateType;

    public static DatePickerFragment newInstance(String dateType) {
        DatePickerFragment frag = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putString("date_type", dateType);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        this.dateType = getArguments().getString("date_type");
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), ondate, year, month, day);
    }


    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            CreateCompetitionDialogFragment target = (CreateCompetitionDialogFragment) getTargetFragment();
            Calendar c = Calendar.getInstance();
            c.set(year, monthOfYear, dayOfMonth);

            target.onFinishDatePicker(c, dateType);
            dismiss();

        }

    };

}