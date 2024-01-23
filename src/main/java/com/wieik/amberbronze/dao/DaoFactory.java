package com.wieik.amberbronze.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * The DaoFactory class is responsible for providing instances of Dao objects.
 * It maintains a map of Dao objects for each entity class, ensuring that only one instance is created per class.
 */
public class DaoFactory {

    private static final Map<Class<?>, Dao<?, ?>> daoMap = new HashMap<>();

    /**
     * Private constructor to prevent instantiation.
     */
    private DaoFactory() {
    }

    /**
     * Retrieves the Dao object for the specified entity class.
     * If the Dao object does not exist, it is created and stored in the map.
     *
     * @param clazz the entity class for which the Dao object is requested
     * @param <T>   the type of the entity class
     * @param <ID>  the type of the entity's ID
     * @return the Dao object for the specified entity class
     * @throws SQLException if the Dao object cannot be created
     */
    public static <T, ID> Dao<T, ID> getDao(Class<T> clazz) {
        if (!daoMap.containsKey(clazz)) {
            try {
                String databaseUrl = "jdbc:sqlite:./db.sqlite";
                ConnectionSource connectionSource = new JdbcConnectionSource(databaseUrl);

                TableUtils.createTableIfNotExists(connectionSource, clazz);

                Dao<T, ID> dao = DaoManager.createDao(connectionSource, clazz);
                daoMap.put(clazz, dao);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return (Dao<T, ID>) daoMap.get(clazz);
    }
}
