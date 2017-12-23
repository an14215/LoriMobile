package com.example.developer.lorimobile.database;

import com.example.developer.lorimobile.model.Task;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

/**
 * Created by developer on 23.12.2017.
 */

public class TaskDAO extends BaseDaoImpl<Task, String> {

    protected TaskDAO(ConnectionSource connectionSource,
                         Class<Task> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

}
