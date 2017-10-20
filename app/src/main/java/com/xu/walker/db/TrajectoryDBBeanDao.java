package com.xu.walker.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.xu.walker.db.helper.LocationInfoListConverter;
import java.util.List;

import com.xu.walker.db.bean.TrajectoryDBBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "TRAJECTORY_DBBEAN".
*/
public class TrajectoryDBBeanDao extends AbstractDao<TrajectoryDBBean, Long> {

    public static final String TABLENAME = "TRAJECTORY_DBBEAN";

    /**
     * Properties of entity TrajectoryDBBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property IsSportsComplete = new Property(1, boolean.class, "isSportsComplete", false, "IS_SPORTS_COMPLETE");
        public final static Property SportsBeginTime = new Property(2, int.class, "sportsBeginTime", false, "SPORTS_BEGIN_TIME");
        public final static Property SportsEndTime = new Property(3, int.class, "sportsEndTime", false, "SPORTS_END_TIME");
        public final static Property SportsTime = new Property(4, int.class, "sportsTime", false, "SPORTS_TIME");
        public final static Property LocationInfoBeans = new Property(5, String.class, "locationInfoBeans", false, "LOCATION_INFO_BEANS");
    }

    private final LocationInfoListConverter locationInfoBeansConverter = new LocationInfoListConverter();

    public TrajectoryDBBeanDao(DaoConfig config) {
        super(config);
    }
    
    public TrajectoryDBBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"TRAJECTORY_DBBEAN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"IS_SPORTS_COMPLETE\" INTEGER NOT NULL ," + // 1: isSportsComplete
                "\"SPORTS_BEGIN_TIME\" INTEGER NOT NULL ," + // 2: sportsBeginTime
                "\"SPORTS_END_TIME\" INTEGER NOT NULL ," + // 3: sportsEndTime
                "\"SPORTS_TIME\" INTEGER NOT NULL ," + // 4: sportsTime
                "\"LOCATION_INFO_BEANS\" TEXT);"); // 5: locationInfoBeans
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"TRAJECTORY_DBBEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, TrajectoryDBBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getIsSportsComplete() ? 1L: 0L);
        stmt.bindLong(3, entity.getSportsBeginTime());
        stmt.bindLong(4, entity.getSportsEndTime());
        stmt.bindLong(5, entity.getSportsTime());
 
        List locationInfoBeans = entity.getLocationInfoBeans();
        if (locationInfoBeans != null) {
            stmt.bindString(6, locationInfoBeansConverter.convertToDatabaseValue(locationInfoBeans));
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, TrajectoryDBBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getIsSportsComplete() ? 1L: 0L);
        stmt.bindLong(3, entity.getSportsBeginTime());
        stmt.bindLong(4, entity.getSportsEndTime());
        stmt.bindLong(5, entity.getSportsTime());
 
        List locationInfoBeans = entity.getLocationInfoBeans();
        if (locationInfoBeans != null) {
            stmt.bindString(6, locationInfoBeansConverter.convertToDatabaseValue(locationInfoBeans));
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public TrajectoryDBBean readEntity(Cursor cursor, int offset) {
        TrajectoryDBBean entity = new TrajectoryDBBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getShort(offset + 1) != 0, // isSportsComplete
            cursor.getInt(offset + 2), // sportsBeginTime
            cursor.getInt(offset + 3), // sportsEndTime
            cursor.getInt(offset + 4), // sportsTime
            cursor.isNull(offset + 5) ? null : locationInfoBeansConverter.convertToEntityProperty(cursor.getString(offset + 5)) // locationInfoBeans
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, TrajectoryDBBean entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setIsSportsComplete(cursor.getShort(offset + 1) != 0);
        entity.setSportsBeginTime(cursor.getInt(offset + 2));
        entity.setSportsEndTime(cursor.getInt(offset + 3));
        entity.setSportsTime(cursor.getInt(offset + 4));
        entity.setLocationInfoBeans(cursor.isNull(offset + 5) ? null : locationInfoBeansConverter.convertToEntityProperty(cursor.getString(offset + 5)));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(TrajectoryDBBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(TrajectoryDBBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(TrajectoryDBBean entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
