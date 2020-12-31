package com.counseling.app;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "Sessions")
public class Session {

    public Session(int patientId, Date date, String startTime, String endTime, String note){
       PatientId = patientId;
       Date = date;
       StartTime = startTime;
       EndTime = endTime;
       Note = note;
    }

    public Session(){

    }

    @DatabaseField(columnName = "id", generatedId = true, allowGeneratedIdInsert = true)
    public int Id;

    @DatabaseField (columnName = "patientid")
    public int PatientId;

    @DatabaseField (columnName = "date")
    public Date Date;

    @DatabaseField (columnName = "starttime")
    public String StartTime;

    @DatabaseField (columnName = "endtime")
    public String EndTime;

    @DatabaseField (columnName = "note")
    public String Note;
}
