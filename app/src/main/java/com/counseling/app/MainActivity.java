package com.counseling.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button _addPatientButton;
    private Button _addSessionButton;
    private Button _viewHistoryButton;
    private Spinner _patientSpinner;
    private TextView _textViewWeeklyHours;
    private TextView _textViewTotalHours;
    private List<Patient> _patientList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        createTables();
        _addPatientButton = findViewById(R.id.buttonAddPatient);
        _addSessionButton = findViewById(R.id.buttonAddSession);
        _viewHistoryButton = findViewById(R.id.buttonViewHistory);
        _patientSpinner = findViewById(R.id.patientSpinner);
        _textViewWeeklyHours = findViewById(R.id.textviewWeeklyHoursValue);
        _textViewTotalHours = findViewById(R.id.textviewTotalHoursValue);
        fillSpinner();
        _patientSpinner.setOnItemSelectedListener(new PatientSelectedListener());
        _addSessionButton.setOnClickListener(new ButtonClickListener());
        _addPatientButton.setOnClickListener(new ButtonClickListener());
        _viewHistoryButton.setOnClickListener(new ButtonClickListener());

    }

    public void fillSpinner(){
        List<String> patientItems = new ArrayList<>();
        patientItems.add("All Patients");
        DatabaseHelper helper = new DatabaseHelper(this);
        _patientList = helper.getAllPatients();
        if(_patientList != null)
            for(Patient patient : _patientList){
                patientItems.add(patient.Name);
            }
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.custom_spinner_item, patientItems);
            _patientSpinner.setAdapter(adapter);
    }

    private class PatientSelectedListener implements Spinner.OnItemSelectedListener{
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String selectedPatient = _patientSpinner.getSelectedItem().toString();
            DatabaseHelper helper = new DatabaseHelper(getBaseContext());
            if(selectedPatient.equals("All Patients")){
                List<Session> weeklySessions = helper.getAllSessionsForThisWeek();
                List<Session> totalSessions = helper.getAllSessions();
                int numWeeklySessions = weeklySessions != null ? weeklySessions.size() : 0;
                int numTotalSessions = totalSessions != null ? totalSessions.size() : 0;
                _textViewWeeklyHours.setText(numWeeklySessions+"");
                _textViewTotalHours.setText(numTotalSessions+"");
            }
            else {
                Patient patient = helper.getPatientIdByName(selectedPatient);
                if(patient != null){
                    List<Session> weeklySessions = helper.getAllSessionsForPatientForThisWeek(patient);
                    List<Session> totalSessions = helper.getAllSessionsForPatient(patient);
                    int numWeeklySessions = weeklySessions != null ? weeklySessions.size() : 0;
                    int numTotalSessions = totalSessions != null ? totalSessions.size() : 0;
                    _textViewWeeklyHours.setText(numWeeklySessions+"");
                    _textViewTotalHours.setText(numTotalSessions+"");
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class ButtonClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if(v == _addPatientButton){
                String FRAG_TAG = "FRAGADDWORD";
                androidx.fragment.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment prev = getSupportFragmentManager().findFragmentByTag(FRAG_TAG);
                if(prev != null) {
                    ft.remove(prev);
                    ft.addToBackStack(null);
                }
                DialogFragment dialog = new FragAddPatient();
                dialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                dialog.show(ft, FRAG_TAG);
            }
            if(v == _addSessionButton){
                String FRAG_TAG = "FRAGADDSESSION";
                androidx.fragment.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment prev = getSupportFragmentManager().findFragmentByTag(FRAG_TAG);
                if(prev != null) {
                    ft.remove(prev);
                    ft.addToBackStack(null);
                }
                DialogFragment dialog = new FragAddSession();
                dialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                dialog.show(ft, FRAG_TAG);
            }
            if(v == _viewHistoryButton){
                if(!_patientSpinner.getSelectedItem().toString().equals("All Patients")){
                    ActivityViewHistory.start(getBaseContext(), _patientSpinner.getSelectedItem().toString());
                }
            }
        }
    }


    public static void start(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    private void createTables(){
        DatabaseHelper helper = new DatabaseHelper(this);
    }

}
