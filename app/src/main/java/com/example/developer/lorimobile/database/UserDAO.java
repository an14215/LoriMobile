package com.example.developer.lorimobile.database;

import com.example.developer.lorimobile.model.User;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public class UserDAO extends BaseDaoImpl<User, String> {

    protected UserDAO(ConnectionSource connectionSource,
                      Class<User> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

}