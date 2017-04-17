package net.sudot.fdfs.conn;

import net.sudot.fdfs.exception.FdfsException;
import net.sudot.fdfs.exception.FdfsExecuteException;
import net.sudot.fdfs.exception.FdfsUnavailableException;
import net.sudot.fdfs.proto.FdfsCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * 连接池管理
 *
 * <pre>
 * 负责借出连接，在连接上执行业务逻辑，然后归还连
 * </pre>
 * @author tobato
 */
public class ConnectionManager {

    /** 日志 */
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    /** 连接池 */
    private FdfsConnectionPool pool;

    /**
     * 构造函数
     */
    public ConnectionManager() {
        super();
    }

    /**
     * 构造函数
     * @param pool
     */
    public ConnectionManager(FdfsConnectionPool pool) {
        super();
        this.pool = pool;
    }

    /**
     * 获取连接并执行交易
     * @param address
     * @param command
     * @return
     */
    public <T> T executeFdfsCmd(InetSocketAddress address, FdfsCommand<T> command) {

        // 获取连接
        Connection conn = getConnection(address);
        // 执行交易
        return execute(address, conn, command);

    }

    /**
     * 执行交易
     * @param conn
     * @param command
     * @return
     */
    protected <T> T execute(InetSocketAddress address, Connection conn, FdfsCommand<T> command) {
        try {
            // 执行交易
            logger.debug("对地址{}发出交易请求{}", address, command.getClass().getSimpleName());
            return command.execute(conn);
        } catch (FdfsException e) {
            throw e;
        } catch (Exception e) {
            throw new FdfsExecuteException("execute fdfs command error", e);
        } finally {
            returnObject(address, conn);
        }
    }

    /**
     * 获取连接
     * @param address
     * @return
     */
    protected Connection getConnection(InetSocketAddress address) {
        Connection conn = null;
        try {
            // 获取连接
            conn = pool.borrowObject(address);
        } catch (FdfsException e) {
            returnObject(address, conn);
            throw e;
        } catch (Exception e) {
            returnObject(address, conn);
            throw new FdfsUnavailableException("Unable to borrow buffer from pool", e);
        }
        return conn;
    }

    protected void  returnObject (InetSocketAddress address, Connection conn) {
        try {
            if (address != null && null != conn) {
                pool.returnObject(address, conn);
            }
        } catch (Exception e) {
            logger.error("return pooled connection error", e);
        }
    }

    public FdfsConnectionPool getPool() {
        return pool;
    }

    public ConnectionManager setPool(FdfsConnectionPool pool) {
        this.pool = pool;
        return this;
    }

}
