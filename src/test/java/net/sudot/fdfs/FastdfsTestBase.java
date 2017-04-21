package net.sudot.fdfs;

import net.sudot.fdfs.conn.ConnectionManager;
import net.sudot.fdfs.conn.TrackerConnectionManager;
import net.sudot.fdfs.service.DefaultFdfsOptionsFactory;
import net.sudot.fdfs.service.FdfsOptionsFactory;
import net.sudot.fdfs.service.StorageClient;
import net.sudot.fdfs.service.TrackerClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 测试基类
 * @author wenchukai1
 * @author sudot on 2017-04-18 0018.
 */
public class FastdfsTestBase {
    /** 日志 */
    protected Logger logger = LoggerFactory.getLogger(getClass());

    public static final FdfsOptionsFactory FDFS_OPTIONS_FACTORY = new DefaultFdfsOptionsFactory();
    protected TrackerClient trackerClient = FDFS_OPTIONS_FACTORY.getTrackerClient();
    protected StorageClient storageClient = FDFS_OPTIONS_FACTORY.getStorageClient();
    protected ConnectionManager storageConnectionManager = FDFS_OPTIONS_FACTORY.getStorageConnectionManager();
    protected TrackerConnectionManager trackerConnectionManager = (TrackerConnectionManager) FDFS_OPTIONS_FACTORY.getTrackerConnectionManager();
}
