package net.sudot.fdfs.service;

import net.sudot.fdfs.conn.ConnectionPoolConfig;
import net.sudot.fdfs.conn.FdfsConnectionPool;
import net.sudot.fdfs.conn.PooledConnectionFactory;
import net.sudot.fdfs.conn.StorageConnectionManager;
import net.sudot.fdfs.conn.TrackerConnectionManager;
import net.sudot.fdfs.domain.TrackerLocator;
import net.sudot.fdfs.exception.FdfsIOException;
import net.sudot.fdfs.util.IOUtils;
import net.sudot.fdfs.util.Validate;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * 默认FastDFS操作客户端工厂接口
 *
 * @author sudot on 2017-04-14 0014.
 */
public class DefaultFdfsOptionsFactory implements FdfsOptionsFactory {
    private Properties properties = new Properties();
    private TrackerClient trackerClient;
    private StorageClient storageClient;

    /**
     * 使用默认的配置文件初始化
     */
    public DefaultFdfsOptionsFactory() {
        this("fastdfs.properties");
    }

    /**
     * 使用指定的配置文件初始化
     *
     * @param configPath 配置文件路径
     */
    public DefaultFdfsOptionsFactory(String configPath) {
        File file = new File(configPath);
        if (!file.exists()) {
            URL url = this.getClass().getClassLoader().getResource(configPath);
            if (url == null) { throw new FdfsIOException(String.format("找不到文件:%s", configPath)); }
            file = new File(url.getPath());
            if (!file.exists()) { throw new FdfsIOException(String.format("找不到文件:%s", file.getAbsolutePath())); }
        }
        InputStream io = null;
        try {
            io = new FileInputStream(file);
            properties.load(io);
        } catch (IOException e) {
            throw new FdfsIOException(e);
        } finally {
            IOUtils.closeQuietly(io);
        }
        ConnectionPoolInstance poolInstance = new ConnectionPoolInstance();
        // tracker和storage使用单独的连接池
        trackerClient = new TrackerClientInstance(poolInstance.get()).get();
        storageClient = new StorageClientInstance(poolInstance.get()).get();
    }

    @Override
    public TrackerClient getTrackerClient() {
        return trackerClient;
    }

    @Override
    public StorageClient getStorageClient() {
        return storageClient;
    }

    public Properties getProperties() {
        return properties;
    }

    /**
     * 连接池初始化
     *
     * @author sudot on 2017-04-17 0017.
     */
    private class ConnectionPoolInstance {
        private FdfsConnectionPool get() {
            String soTimeout = properties.getProperty("fdfs.soTimeout");
            String connectTimeout = properties.getProperty("fdfs.connectTimeout");
            String charsetName = properties.getProperty("fdfs.charsetName");
            String maxTotal = properties.getProperty("fdfs.pool.maxTotal");
            String blockWhenExhausted = properties.getProperty("fdfs.pool.blockWhenExhausted");
            String maxWaitMillis = properties.getProperty("fdfs.pool.maxWaitMillis");
            String testWhileIdle = properties.getProperty("fdfs.pool.testWhileIdle");
            String minEvictableIdleTimeMillis = properties.getProperty("fdfs.pool.minEvictableIdleTimeMillis");
            String timeBetweenEvictionRunsMillis = properties.getProperty("fdfs.pool.timeBetweenEvictionRunsMillis");

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
            return new FdfsConnectionPool(factory, config);
        }
    }

    /**
     * TrackerClient初始化
     *
     * @author sudot on 2017-04-17 0017.
     */
    private class TrackerClientInstance {
        private FdfsConnectionPool pool;

        private TrackerClientInstance(FdfsConnectionPool pool) {
            this.pool = pool;
        }

        private TrackerClient get() {
            DefaultTrackerClient trackerClient = new DefaultTrackerClient();
            String trackerList = properties.getProperty("fdfs.trackerList");
            Validate.notBlank(trackerList, "Tracker连接不能为空或包含空字符");
            String retryAfterSecend = properties.getProperty("fdfs.pool.retryAfterSecend");

            TrackerLocator trackerLocator = new TrackerLocator(trackerList);
            if (retryAfterSecend != null) {
                trackerLocator.setRetryAfterSecend(Integer.parseInt(retryAfterSecend));
            }
            TrackerConnectionManager trackerConnectionManager = new TrackerConnectionManager(pool);
            trackerConnectionManager.setTrackerLocator(trackerLocator);
            trackerClient.setConnectionManager(trackerConnectionManager);
            return trackerClient;
        }
    }

    /**
     * StorageClient初始化
     *
     * @author sudot on 2017-04-17 0017.
     */
    private class StorageClientInstance {
        private FdfsConnectionPool pool;

        private StorageClientInstance(FdfsConnectionPool pool) {
            this.pool = pool;
        }

        private StorageClient get() {
            DefaultStorageClient storageClient = new DefaultStorageClient();
            StorageConnectionManager storageConnectionManager = new StorageConnectionManager(pool);
            storageClient.setTrackerClient(trackerClient);
            storageClient.setConnectionManager(storageConnectionManager);
            return storageClient;
        }
    }
}
