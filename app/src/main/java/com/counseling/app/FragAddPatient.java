package com.counseling.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

public class FragAddPatient extends DialogFragment {


    private Button _cancelButton;
    private Button _submitButton;
    private EditText _editTextNewPatient;
    private FragAddPatient _thisFrag;


    static FragAddPatient newInstance() {
        FragAddPatient f = new FragAddPatient();
        return f;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_patient_frag, container, false);
        _thisFrag = this;
        _cancelButton = view.findViewById(R.id.buttonCancel);
        _submitButton = view.findViewById(R.id.buttonSubmit);
        _editTextNewPatient = view.findViewById(R.id.editTextNewPatient);
        _submitButton.setOnClickListener(new SubmitListener());
        _cancelButton.setOnClickListener(new CancelListener());
//        ButterKnife.bind(this, view);
        return view;
    }

    private class SubmitListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(_editTextNewPatient.getText().toString().trim().length()>0){
                String name = _editTextNewPatient.getText().toString();
                boolean submitted = false;
                DatabaseHelper helper = new DatabaseHelper(getActivity());
                submitted = helper.addPatient(new Patient(name));
                if(submitted)
                    Toast.makeText(getActivity(), "Successfully Added", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getActivity(), "Submission Failed.", Toast.LENGTH_LONG).show();
                if(getActivity() instanceof MainActivity){
                    ((MainActivity) getActivity()).fillSpinner();
                }
                _thisFrag.dismiss();
            }
        }
    }

    private class CancelListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            _thisFrag.dismiss();
        }
    }
}
