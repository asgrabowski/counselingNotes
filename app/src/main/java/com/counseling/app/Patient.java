package com.counseling.app;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Patients")
public class Patient {

    public Patient(String name){
        Name = name;
    }

    public Patient(){

    }

    @DatabaseField (columnName = "id", generatedId = true, allowGeneratedIdInsert = true)
    public int Id;

    @DatabaseField (columnName = "name")
    public String Name;
}
