package com.example.developer.lorimobile.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.developer.lorimobile.model.Project;
import com.example.developer.lorimobile.model.ProjectParticipant;
import com.example.developer.lorimobile.model.Task;
import com.example.developer.lorimobile.model.TimeEntry;
import com.example.developer.lorimobile.model.User;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "lori.db";

    private static final int DATABASE_VERSION = 1;

    private TimeEntryDAO timeEntryDAO = null;
    private UserDAO userDAO = null;
    private ProjectDAO projectDAO = null;
    private TaskDAO taskDAO = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Project.class);
            TableUtils.createTable(connectionSource, Task.class);
            TableUtils.createTable(connectionSource, User.class);
            TableUtils.createTable(connectionSource, TimeEntry.class);
        } catch (SQLException e) {
            Log.e(TAG, "error creating DB " + DATABASE_NAME);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVer,
                          int newVer) {
        try {
            TableUtils.dropTable(connectionSource, TimeEntry.class, true);
            TableUtils.dropTable(connectionSource, ProjectParticipant.class, true);
            TableUtils.dropTable(connectionSource, User.class, true);
            TableUtils.dropTable(connectionSource, Task.class, true);
            TableUtils.dropTable(connectionSource, Project.class, true);
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(TAG, "error upgrading db " + DATABASE_NAME + "from ver " + oldVer);
            throw new RuntimeException(e);
        }
    }

    public TimeEntryDAO getTimeEntryDAO() throws SQLException {
        if (timeEntryDAO == null) {
            timeEntryDAO = new TimeEntryDAO(getConnectionSource(), TimeEntry.class);
        }
        return timeEntryDAO;
    }
    public TaskDAO getTaskDAO() throws SQLException {
        if (taskDAO == null) {
            taskDAO = new TaskDAO(getConnectionSource(), Task.class);
        }
        return taskDAO;
    }
    public ProjectDAO getProjectDAO() throws SQLException {
        if (projectDAO == null) {
            projectDAO = new ProjectDAO(getConnectionSource(), Project.class);
        }
        return projectDAO;
    }
    public UserDAO getUserDAO() throws SQLException {
        if (userDAO == null) {
            userDAO = new UserDAO(getConnectionSource(), User.class);
        }
        return userDAO;
    }
    @Override
    public void close() {
        super.close();
        timeEntryDAO = null;
        userDAO = null;
        projectDAO = null;
        taskDAO = null;
    }
}
