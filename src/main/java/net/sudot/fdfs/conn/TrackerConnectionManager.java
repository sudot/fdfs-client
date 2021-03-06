package net.sudot.fdfs.conn;

import net.sudot.fdfs.domain.TrackerLocator;
import net.sudot.fdfs.exception.FdfsException;
import net.sudot.fdfs.proto.FdfsCommand;

import java.net.InetSocketAddress;

/**
 * 管理TrackerClient连接池分配
 *
 * @author tobato
 * @author sudot on 2017-04-19 0019.
 */
public class TrackerConnectionManager extends ConnectionManager {

    /** Tracker定位 */
    private TrackerLocator trackerLocator;

    /** 构造函数 */
    public TrackerConnectionManager() {
        super();
    }

    /** 构造函数 */
    public TrackerConnectionManager(FdfsConnectionPool pool) {
        super(pool);
    }

    /**
     * 获取连接并执行交易
     *
     * @param command
     * @return
     */
    public <T> T executeFdfsTrackerCmd(FdfsCommand<T> command) {
        InetSocketAddress address = trackerLocator.getTrackerAddress();
        if (logger.isDebugEnabled()) { logger.debug("获取到Tracker连接地址{}", address); }
        trackerLocator.setActive(address);
        try {
            return super.executeFdfsCmd(address, command);
        } catch (FdfsException e) {
            trackerLocator.setInActive(address);
            throw e;
        }
    }

    public TrackerLocator getTrackerLocator() {
        return trackerLocator;
    }

    public TrackerConnectionManager setTrackerLocator(TrackerLocator trackerLocator) {
        this.trackerLocator = trackerLocator;
        return this;
    }
}
