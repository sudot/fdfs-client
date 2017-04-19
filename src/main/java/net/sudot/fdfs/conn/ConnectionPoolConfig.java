package net.sudot.fdfs.conn;

import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;

/**
 * 连接池配置
 * @author tobato
 * @author sudot on 2017-04-17 0017.
 */
public class ConnectionPoolConfig extends GenericKeyedObjectPoolConfig {

    public ConnectionPoolConfig() {
        /*
        资源回收线程执行一次回收操作，回收资源的数量
        当 设置为0时，不回收资源。
        设置为 小于0时，回收资源的个数为 (int)Math.ceil( 池中空闲资源个数 / Math.abs(numTestsPerEvictionRun) );
        设置为 大于0时，回收资源的个数为 Math.min( numTestsPerEvictionRun,池中空闲的资源个数 );
        */
        setNumTestsPerEvictionRun(-1);
    }
}