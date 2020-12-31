package com.counseling.app;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.temporal.TemporalAdjusters.next;
import static java.time.temporal.TemporalAdjusters.previous;
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private Context _context;
    private Dao<Patient, Integer> _patientDao = null;
    private Dao<Session, Integer> _sessionDao = null;

    public DatabaseHelper(Context context) {
        super(context, "counseling.db", null, 1);
        _context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        createDatabases(connectionSource);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        createDatabases(connectionSource);
    }

    @Override
    public void close() {
        _patientDao = null;
        _sessionDao = null;
        DaoManager.clearCache();
        super.close();
    }

    private void createDatabases(ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, Session.class);
            TableUtils.createTableIfNotExists(connectionSource, Patient.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Dao<Patient, Integer> getPatientDao() throws SQLException{
        if (_patientDao == null) {
            _patientDao = getDao(Patient.class);
        }
        return _patientDao;
    }

    public Dao<Session, Integer> getSessionDao() throws SQLException {
        if (_sessionDao == null) {
            _sessionDao = getDao(Session.class);
        }
        return _sessionDao;
    }

    public List<Patient> getAllPatients(){
        List<Patient> retVal = null;
        try {
            Dao<Patient, Integer> patientDao = getPatientDao();
            retVal = patientDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public List<Session> getAllSessions(){
        List<Session> retVal = null;
        try {
            Dao<Session, Integer> sessionDao = getSessionDao();
            retVal = sessionDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public List<Session> getAllSessionsForPatient(Patient patient){
        List<Session> retVal = null;
        try {
            Dao<Session, Integer> sessionDao = getSessionDao();
            QueryBuilder<Session, Integer>  qb = sessionDao.queryBuilder();
            qb.where().eq("patientid", patient.Id);
            qb.orderBy("date", false);
            retVal = qb.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public List<Session> getAllSessionsForPatientForThisWeek(Patient patient){
        List<Session> retVal = null;
        try {
            Dao<Session, Integer> sessionDao = getSessionDao();
            QueryBuilder<Session, Integer>  qb = sessionDao.queryBuilder();
            qb.where().eq("patientid", patient.Id).and().ge("date", getBeginningOfWeek()).and().lt("date", getEndOfWeek());
            retVal = qb.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public Patient getPatientIdByName(String name){
        Patient retVal = null;
        try {
            Dao<Patient, Integer> patientDao = getPatientDao();
            QueryBuilder<Patient, Integer> qb = patientDao.queryBuilder();
            qb.where().eq("name", name);
            retVal = qb.queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public List<Session> getAllSessionsForThisWeek(){
        List<Session> retVal = null;
        try {
            Dao<Session, Integer> sessionDao = getSessionDao();
            QueryBuilder<Session, Integer>  qb = sessionDao.queryBuilder();
            qb.where().ge("date", getBeginningOfWeek()).and().lt("date", getEndOfWeek());
            retVal = qb.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public boolean addSession(Session session){
        boolean retVal = false;
        try{
            if(session != null){
                Dao<Session, Integer> sessionDao = getSessionDao();
                sessionDao.create(session);
                retVal = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            clearORM(this);
        }
        return retVal;
    }

    private Date getBeginningOfWeek(){
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        LocalDate sunday = null;
        if(dayOfWeek != 1){
            sunday = LocalDate.now().with(previous(SUNDAY));
        }
        else {
            sunday = LocalDate.now();
        }
        return  Date.from(sunday.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private Date getEndOfWeek(){
        LocalDate sunday = LocalDate.now().with(next(SUNDAY));
        return  Date.from(sunday.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public boolean addPatient(Patient patient){
        boolean retVal = false;
        try{
            if(patient != null){
                Dao<Patient, Integer> patientDao = getPatientDao();
                patientDao.create(patient);
                retVal = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            clearORM(this);
        }
        return retVal;
    }

    private void clearORM(DatabaseHelper dbHelper) {
        clearDaoCache();
        if(dbHelper != null) dbHelper.close();
    }

    private void clearDaoCache() {
        DaoManager.clearCache();
    }

    public void deleteSession(Session session) {
        try{
            if(session != null){
                Dao<Session, Integer> sessionDao = getSessionDao();
                sessionDao.delete(session);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            clearORM(this);
        }
    }
}