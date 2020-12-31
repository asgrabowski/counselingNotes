package com.counseling.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ActivityViewHistory extends AppCompatActivity {

    private String _patientName;
    private TextView _textViewPatient;
    private ListView _listViewSessions;
    private Button _buttonClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history);
    }

    @Override
    protected void onResume() {
        super.onResume();
        _patientName = getIntent().getStringExtra("PATIENT_NAME");
        _textViewPatient = findViewById(R.id.textviewPatientName);
        _listViewSessions = findViewById(R.id.listviewSessions);
        _buttonClose = findViewById(R.id.buttonCancel);
        if(_patientName != null){
            _textViewPatient.setText(_patientName);
            setSessionList();
        }
        _buttonClose.setOnClickListener(new CancelClickListener());
    }

    public static void start(Context context, String patientName){
        Intent intent = new Intent(context, ActivityViewHistory.class);
        intent.putExtra("PATIENT_NAME", patientName);
        context.startActivity(intent);
    }

    private class CancelClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            MainActivity.start(getBaseContext());
        }
    }

    public void setSessionList(){
        DatabaseHelper helper = new DatabaseHelper(this);
        Patient patient = helper.getPatientIdByName(_patientName);
        List<Session> sessionList = helper.getAllSessionsForPatient(patient);
        List<SessionItem> sessionItems = new ArrayList<>();
        if(sessionList != null){
            for(Session session : sessionList){
                SessionItem item = new SessionItem(session, this);
                sessionItems.add(item);
            }
        }
        HistoryAdapter adapter = new HistoryAdapter(this, R.layout.session_item, sessionItems);
        _listViewSessions.setAdapter(adapter);
    }
}
