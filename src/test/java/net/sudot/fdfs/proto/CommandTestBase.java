package net.sudot.fdfs.proto;

import net.sudot.fdfs.FastdfsTestBase;
import net.sudot.fdfs.TestConstants;

/**
 * command测试基类
 * @author tobato
 * @author sudot on 2017-04-21 0021.
 */
public class CommandTestBase extends FastdfsTestBase {

    /**
     * 执行Tracker交易命令
     * @param command
     * @return
     */
    protected <T> T executeTrackerCmd(FdfsCommand<T> command) {
        return trackerClient.getConnectionManager().executeFdfsCmd(TestConstants.TRACKER_ADDRESS, command);
    }

    /**
     * 执行存储交易命令
     * @param command
     * @return
     */
    protected <T> T executeStoreCmd(FdfsCommand<T> command) {
        return storageClient.getConnectionManager().executeFdfsCmd(TestConstants.STORE_ADDRESS, command);
    }

}
