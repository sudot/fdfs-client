package net.sudot.fdfs;

import net.sudot.fdfs.service.StorageClient;
import net.sudot.fdfs.service.TrackerClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 测试基类
 * @author wenchukai1
 * Update by sudot on 2017-04-18 0018.
 */
public class FastdfsTestBase {
    /** 日志 */
    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected TrackerClient trackerClient = TestConstants.FDFS_OPTIONS_FACTORY.getTrackerClient();
    protected StorageClient storageClient = TestConstants.FDFS_OPTIONS_FACTORY.getStorageClient();
}
