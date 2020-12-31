package com.counseling.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

public class SessionItem implements ISessionItem {

    private Session _session;
    private TextView _textviewDate;
    private TextView _textviewStartTime;
    private TextView _textviewEndTime;
    private TextView _textviewNote;
    private Button _buttonDelete;
    private Context _context;

    public SessionItem(Session session, Context context){
        _session = session;
        _context = context;
    }

    @Override
    public View getView(LayoutInflater inflater, View view) {
        view = inflater.inflate(R.layout.session_item, null);
        _textviewDate = view.findViewById(R.id.textviewDate);
        _textviewEndTime = view.findViewById(R.id.textviewEndTime);
        _textviewNote = view.findViewById(R.id.textviewNote);
        _textviewStartTime = view.findViewById(R.id.textviewStartTime);
        _buttonDelete = view.findViewById(R.id.buttonDelete);
        SimpleDateFormat simpleDate =  new SimpleDateFormat("MM/dd/yyyy");
        if(_session!= null){
            if(_session.Date != null){
                String strDt = simpleDate.format(_session.Date);
                _textviewDate.setText(strDt);
            }
            if(_session.StartTime != null)
                _textviewStartTime.setText(_session.StartTime);
            if(_session.EndTime != null)
                _textviewEndTime.setText(_session.EndTime);
            if(_session.Note != null)
                _textviewNote.setText(_session.Note);
        }
        _buttonDelete.setOnClickListener(new DeleteListener(_context));
        return view;
    }

    private class DeleteListener implements View.OnClickListener {

        private Context _context;

        public DeleteListener(Context context){
            _context = context;
        }

        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(_context);

            builder.setMessage("Are you sure you want to delete this session?")
                    .setTitle("Remove Session")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DatabaseHelper helper = new DatabaseHelper(_context);
                            helper.deleteSession(_session);
                            if(_context instanceof ActivityViewHistory)
                                ((ActivityViewHistory) _context).setSessionList();
                            Toast.makeText(_context, "Removed Session", Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}
