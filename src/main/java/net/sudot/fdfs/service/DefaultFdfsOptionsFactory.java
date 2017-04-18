package net.sudot.fdfs.service;

import net.sudot.fdfs.conn.ConnectionManager;
import net.sudot.fdfs.conn.ConnectionPoolConfig;
import net.sudot.fdfs.conn.FdfsConnectionPool;
import net.sudot.fdfs.conn.PooledConnectionFactory;
import net.sudot.fdfs.conn.StorageConnectionManager;
import net.sudot.fdfs.conn.TrackerConnectionManager;
import net.sudot.fdfs.util.ConfigUtils;
import net.sudot.fdfs.util.Validate;

/**
 * 默认FastDFS操作客户端工厂接口
 * Created by tangjialin on 2017-04-14 0014.
 */
public class DefaultFdfsOptionsFactory implements FdfsOptionsFactory {

    public ConnectionManager getTrackerConnectionManager() {
        return TrackerConnectionManagerInstance.TRACKER_CONNECTION_MANAGER;
    }

    public ConnectionManager getStorageConnectionManager() {
        return StorageConnectionManagerInstance.STORAGE_CONNECTION_MANAGER;
    }

    public TrackerClient getTrackerClient() {
        return TrackerClientInstance.TRACKER_CLIENT;
    }

    public StorageClient getStorageClient() {
        return StorageClientInstance.STORAGE_CLIENT;
    }

    /**
     * 连接池静态初始化
     * Created by tangjialin on 2017-04-17 0017.
     */
    private static class ConnectionPoolInstance {
        private static final FdfsConnectionPool POOL;

        static {
            String soTimeout = ConfigUtils.getConfigValue("fdfs.soTimeout");
            String connectTimeout = ConfigUtils.getConfigValue("fdfs.connectTimeout");
            String charsetName = ConfigUtils.getConfigValue("fdfs.charsetName");
            String maxTotal = ConfigUtils.getConfigValue("fdfs.pool.maxTotal");
            String blockWhenExhausted = ConfigUtils.getConfigValue("fdfs.pool.blockWhenExhausted");
            String maxWaitMillis = ConfigUtils.getConfigValue("fdfs.pool.maxWaitMillis");
            String testWhileIdle = ConfigUtils.getConfigValue("fdfs.pool.testWhileIdle");
            String minEvictableIdleTimeMillis = ConfigUtils.getConfigValue("fdfs.pool.minEvictableIdleTimeMillis");
            String timeBetweenEvictionRunsMillis = ConfigUtils.getConfigValue("fdfs.pool.timeBetweenEvictionRunsMillis");

            PooledConnectionFactory factory = new PooledConnectionFactory();
            if (soTimeout != null) { factory.setSoTimeout(Integer.parseInt(soTimeout)); }
            if (soTimeout != null) { factory.setConnectTimeout(Integer.parseInt(connectTimeout)); }
            if (charsetName != null) { factory.setCharsetName(charsetName); }

            ConnectionPoolConfig config = new ConnectionPoolConfig();
            if (maxTotal != null) { config.setMaxTotal(Integer.parseInt(maxTotal)); }
            if (blockWhenExhausted != null) { config.setBlockWhenExhausted(Boolean.parseBoolean(blockWhenExhausted)); }
            if (maxWaitMillis != null) { config.setMaxWaitMillis(Long.parseLong(maxWaitMillis)); }
            if (testWhileIdle != null) { config.setTestWhileIdle(Boolean.parseBoolean(testWhileIdle)); }
            if (minEvictableIdleTimeMillis != null) {
                config.setMinEvictableIdleTimeMillis(Long.parseLong(minEvictableIdleTimeMillis));
            }
            if (timeBetweenEvictionRunsMillis != null) {
                config.setTimeBetweenEvictionRunsMillis(Long.parseLong(timeBetweenEvictionRunsMillis));
            }
            POOL = new FdfsConnectionPool(factory, config);
        }
    }

    /**
     * TrackerConnectionManager静态初始化
     * Created by tangjialin on 2017-04-18 0018.
     */
    private static class TrackerConnectionManagerInstance {
        private static final TrackerConnectionManager TRACKER_CONNECTION_MANAGER = new TrackerConnectionManager();

        static {
            String trackerList = ConfigUtils.getConfigValue("fdfs.trackerList");
            Validate.notBlank(trackerList, "Tracker连接不能为空或包含空字符");
            TRACKER_CONNECTION_MANAGER.setTrackerListFromString(trackerList);
            TRACKER_CONNECTION_MANAGER.setPool(ConnectionPoolInstance.POOL);
        }
    }

    /**
     * TrackerClient静态初始化
     * Created by tangjialin on 2017-04-17 0017.
     */
    private static class TrackerClientInstance {
        private static final DefaultTrackerClient TRACKER_CLIENT = new DefaultTrackerClient();

        static {
            TRACKER_CLIENT.setTrackerConnectionManager(TrackerConnectionManagerInstance.TRACKER_CONNECTION_MANAGER);
        }
    }

    /**
     * StorageConnectionManager静态初始化
     * Created by tangjialin on 2017-04-18 0018.
     */
    private static class StorageConnectionManagerInstance {
        private static final StorageConnectionManager STORAGE_CONNECTION_MANAGER = new StorageConnectionManager();

        static {
            STORAGE_CONNECTION_MANAGER.setPool(ConnectionPoolInstance.POOL);
        }
    }

    /**
     * StorageClient静态初始化
     * Created by tangjialin on 2017-04-17 0017.
     */
    private static class StorageClientInstance {
        private static final DefaultStorageClient STORAGE_CLIENT = new DefaultStorageClient();

        static {
            STORAGE_CLIENT.setTrackerClient(TrackerClientInstance.TRACKER_CLIENT);
            STORAGE_CLIENT.setConnectionManager(StorageConnectionManagerInstance.STORAGE_CONNECTION_MANAGER);
        }
    }
}
