package com.counseling.app;

import android.view.LayoutInflater;
import android.view.View;

interface ISessionItem {
    View getView(LayoutInflater inflater, View view);
}
