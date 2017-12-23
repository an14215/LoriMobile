package com.example.developer.lorimobile.database;

import com.example.developer.lorimobile.model.Task;
import com.example.developer.lorimobile.model.TimeEntry;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

public class TimeEntryDAO extends BaseDaoImpl<TimeEntry, String> {

    protected TimeEntryDAO(ConnectionSource connectionSource,
                         Class<TimeEntry> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<TimeEntry> getAllTimeEntryOfDay(String date, String userId)  throws SQLException{
        QueryBuilder<TimeEntry, String> queryBuilder = queryBuilder();
        queryBuilder.where().eq("user_id", userId)
                .and().eq("date",date);
        PreparedQuery<TimeEntry> preparedQuery = queryBuilder.prepare();
        List<TimeEntry> timeEntryList =query(preparedQuery);
        return timeEntryList;
    }

    public void deleteTimeEntryOfDay(String date)  throws SQLException{
        DeleteBuilder<TimeEntry, String> deleteBuilder = deleteBuilder();
        deleteBuilder.where().eq("date",date);
        deleteBuilder.delete();
    }

    public List<TimeEntry> findTimeEntry(String dateStart, String dateEnd,String userId, String searchString)  throws SQLException{
        QueryBuilder<TimeEntry, String> queryBuilder = queryBuilder();
        Where where = queryBuilder.where();
        where.and(where.like("description",searchString),
                where.eq("user_id", userId),
                where.between("date",dateStart,dateEnd));
        queryBuilder.orderBy("date",false);
        PreparedQuery<TimeEntry> preparedQuery = queryBuilder.prepare();
        List<TimeEntry> timeEntryList =query(preparedQuery);

        return timeEntryList;
    }
}
