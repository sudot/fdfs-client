package net.sudot.fdfs.conn;

import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 连接池配置
 * @author tobato
 * Update by sudot on 2017-03-17 0017.
 */
@Component
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

    @Value("${fdfs.pool.maxTotal}")
    @Override
    public void setMaxTotal(int maxTotal) {
        super.setMaxTotal(maxTotal);
    }

    @Value("${fdfs.pool.maxWaitMillis}")
    @Override
    public void setMaxWaitMillis(long maxWaitMillis) {
        super.setMaxWaitMillis(maxWaitMillis);
    }

    @Value("${fdfs.pool.minEvictableIdleTimeMillis}")
    @Override
    public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
        super.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
    }

    @Value("${fdfs.pool.testWhileIdle}")
    @Override
    public void setTestWhileIdle(boolean testWhileIdle) {
        super.setTestWhileIdle(testWhileIdle);
    }

    @Value("${fdfs.pool.timeBetweenEvictionRunsMillis}")
    @Override
    public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
        super.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
    }

    @Value("${fdfs.pool.blockWhenExhausted}")
    @Override
    public void setBlockWhenExhausted(boolean blockWhenExhausted) {
        super.setBlockWhenExhausted(blockWhenExhausted);
    }
}