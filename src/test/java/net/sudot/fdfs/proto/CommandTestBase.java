package net.sudot.fdfs.proto;

import net.sudot.fdfs.TestConstants;
import net.sudot.fdfs.conn.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * command测试基类
 * @author tobato
 */
public class CommandTestBase {

    /** 日志 */
    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 连接池
     */
    protected ConnectionManager manager = TestConstants.FDFS_OPTIONS_FACTORY.getTrackerConnectionManager();

    /**
     * 执行Tracker交易命令
     * @param command
     * @return
     */
    protected <T> T executeTrackerCmd(FdfsCommand<T> command) {
        return manager.executeFdfsCmd(TestConstants.TRACKER_ADDRESS, command);
    }

    /**
     * 执行存储交易命令
     * @param command
     * @return
     */
    protected <T> T executeStoreCmd(FdfsCommand<T> command) {
        return manager.executeFdfsCmd(TestConstants.STORE_ADDRESS, command);
    }

}
