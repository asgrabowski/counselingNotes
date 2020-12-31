package com.counseling.app;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FragAddSession extends DialogFragment {


    private Button _cancelButton;
    private Button _submitButton;
    private EditText _editTextDate;
    private EditText _editTextStartTime;
    private EditText _editTextEndTime;
    private EditText _editTextNote;
    private Spinner _spinnerPatients;
    private FragAddSession _thisFrag;
    private Date _date;


    static FragAddSession newInstance() {
        FragAddSession f = new FragAddSession();
        return f;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_session_frag, container, false);
        _thisFrag = this;
        _cancelButton = view.findViewById(R.id.buttonCancel);
        _submitButton = view.findViewById(R.id.buttonSubmit);
        _editTextDate = view.findViewById(R.id.editTextDate);
        _editTextEndTime = view.findViewById(R.id.editTextEndTIme);
        _editTextNote = view.findViewById(R.id.editTextNote);
        _editTextStartTime = view.findViewById(R.id.editTextStartTime);
        _spinnerPatients = view.findViewById(R.id.spinnerPatients);
        loadSpinner();
        _submitButton.setOnClickListener(new SubmitListener());
        _cancelButton.setOnClickListener(new CancelListener());
        _editTextDate.setOnClickListener(new CalendarListener());
        _editTextStartTime.setOnClickListener(new ClockListener());
        _editTextEndTime.setOnClickListener(new ClockListener());
//        ButterKnife.bind(this, view);
        return view;
    }

    private class SubmitListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(isValid(_editTextDate) && isValid(_editTextEndTime) && isValid(_editTextStartTime) && _spinnerPatients.getSelectedItem().toString().trim().length() > 0){
                DatabaseHelper helper = new DatabaseHelper(getActivity());
                Patient patient = helper.getPatientIdByName(_spinnerPatients.getSelectedItem().toString());
                Session session = new Session(patient.Id, _date, _editTextStartTime.getText().toString(), _editTextEndTime.getText().toString(), _editTextNote.getText().toString());
                boolean submitted = false;
                submitted = helper.addSession(session);
                if(submitted)
                    Toast.makeText(getActivity(), "Successfully Added", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getActivity(), "Submission Failed.", Toast.LENGTH_LONG).show();
                if(getActivity() instanceof MainActivity){
                    ((MainActivity) getActivity()).fillSpinner();
                }
                _thisFrag.dismiss();
            }
            else {
                Toast.makeText(getActivity(), "Missing Information for Session", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class CalendarListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            final int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    c.set(Calendar.MONTH, month);
                    c.set(Calendar.YEAR, year);
                    c.set(Calendar.MILLISECOND, 0);
                    c.set(Calendar.SECOND, 0);
                    c.set(Calendar.MINUTE, 0);
                    c.set(Calendar.HOUR_OF_DAY, 0);
                    _date = c.getTime();
                    _editTextDate.setText(month+1+"/"+dayOfMonth+"/"+year);
                }
            }, year, month, day);
            datePickerDialog.show();
        }
    }

    private class ClockListener implements View.OnClickListener{

        @Override
        public void onClick(final View v) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String amOrPm = hourOfDay < 12 ? "AM" : "PM";
                    if(hourOfDay == 0)
                        hourOfDay = 12;
                    if(hourOfDay > 12)
                        hourOfDay = hourOfDay - 12;
                    if(v == _editTextEndTime || v == _editTextStartTime){
                        ((EditText) v).setText(hourOfDay+":"+minute+" "+amOrPm);
                    }
                }
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
            timePickerDialog.show();
        }
    }

    private boolean isValid(EditText editText){
        return editText.getText().toString().trim().length() >0;
    }

    private class CancelListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            _thisFrag.dismiss();
        }
    }

    private void loadSpinner(){
        List<String> patientItems = new ArrayList<>();
        patientItems.add("");
        DatabaseHelper helper = new DatabaseHelper(getActivity());
        List<Patient> patientList = helper.getAllPatients();
        if(patientList != null)
            for(Patient patient : patientList){
                patientItems.add(patient.Name);
            }
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.custom_spinner_item, patientItems);
        _spinnerPatients.setAdapter(adapter);
    }
}
