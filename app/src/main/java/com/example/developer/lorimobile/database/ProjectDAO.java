package com.example.developer.lorimobile.database;

import com.example.developer.lorimobile.model.Project;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

/**
 * Created by developer on 22.12.2017.
 */

public class ProjectDAO extends BaseDaoImpl<Project, String> {

    protected ProjectDAO(ConnectionSource connectionSource,
                      Class<Project> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

}
