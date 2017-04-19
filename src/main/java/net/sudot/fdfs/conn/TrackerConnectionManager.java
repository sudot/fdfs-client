package net.sudot.fdfs.conn;

import net.sudot.fdfs.domain.TrackerLocator;
import net.sudot.fdfs.exception.FdfsConnectException;
import net.sudot.fdfs.exception.FdfsException;
import net.sudot.fdfs.exception.FdfsUnavailableException;
import net.sudot.fdfs.proto.FdfsCommand;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;

/**
 * 管理TrackerClient连接池分配
 * @author tobato
 */
public class TrackerConnectionManager extends ConnectionManager {
    public static final String SPLIT_REGEX = ",|;| |\t|\r\n|\n";

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
     * @param command
     * @return
     */
    public <T> T executeFdfsTrackerCmd(FdfsCommand<T> command) {
        Connection conn = null;
        InetSocketAddress address = null;
        // 获取连接
        try {
            address = trackerLocator.getTrackerAddress();
            logger.debug("获取到Tracker连接地址{}", address);
            conn = getConnection(address);
            trackerLocator.setActive(address);
        } catch (FdfsConnectException e) {
            returnObject(address, conn);
            trackerLocator.setInActive(address);
            throw e;
        } catch (FdfsUnavailableException e) {
            returnObject(address, conn);
            throw e;
        } catch (Exception e) {
            returnObject(address, conn);
            throw new FdfsException("Unable to borrow buffer from pool", e);
        }
        // 执行交易
        return execute(address, conn, command);
    }

    public TrackerLocator getTrackerLocator() {
        return trackerLocator;
    }

    public TrackerConnectionManager setTrackerLocator(TrackerLocator trackerLocator) {
        this.trackerLocator = trackerLocator;
        return this;
    }

    public TrackerConnectionManager setTrackerList(List<String> trackerList) {
        setTrackerLocator(new TrackerLocator(trackerList));
        return this;
    }

    public TrackerConnectionManager setTrackerListFromString(String trackerListStr) {
        setTrackerList(Arrays.asList(trackerListStr.split(SPLIT_REGEX)));
        return this;
    }
}
