package net.sudot.fdfs.domain;

import net.sudot.fdfs.FastdfsTestBase;
import net.sudot.fdfs.exception.FdfsUnavailableException;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * TrackerLocator创建
 * @author tobato
 * @author sudot on 2017-04-19 0019.
 */
public class TrackerLocatorTest extends FastdfsTestBase {
    @Test
    public void testTrackerLocator() {
        // 创建Locator
        TrackerLocator locator = new TrackerLocator(Arrays.asList(new String[]{"127.0.0.1:1234", "127.0.0.1:2234"}));
        assertFalse(locator.getTrackerList().isEmpty());
        // 获取一个连接地址
        InetSocketAddress addressA = getAddress(locator, "获取地址A");
        InetSocketAddress addressB = getAddress(locator, "获取地址B");
        // 地址轮询
        assertFalse(addressA.equals(addressB));
        // IF 连接断开
        locator.setInActive(addressA);
        InetSocketAddress addressC = getAddress(locator, "获取地址C");
        assertFalse(addressA.equals(addressC));
        // 只剩一个地址
        assertEquals(addressB, addressC);
        // 连接恢复
        locator.setActive(addressA);

        // 获取连接
        InetSocketAddress addressD = getAddress(locator, "获取地址D");
        assertEquals(addressA, addressD);
        locator.setInActive(addressA);
        locator.setInActive(addressB);

        try {
            // 无连接可以获取
            getAddress(locator, "获取地址E");
            fail("No exception thrown.");
        } catch (Exception e) {
            assertTrue(e instanceof FdfsUnavailableException);
        }
    }

    /**
     * 获取一个连接地址
     * @param msg
     * @param locator
     * @return
     */
    private InetSocketAddress getAddress(TrackerLocator locator, String msg) {
        InetSocketAddress address = locator.getTrackerAddress();
        assertNotNull(address);
        logger.debug(msg + ":{}", address);
        return address;
    }

}
