package net.sudot.fdfs.domain;

import net.sudot.fdfs.FastdfsTestBase;
import net.sudot.fdfs.exception.FdfsUnsupportStorePathException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * 文件路径对象
 * @author tobato
 */
public class StorePathTest extends FastdfsTestBase {

    /** 日志 */
    protected static Logger LOGGER = LoggerFactory.getLogger(StorePathTest.class);

    /**
     * 可以从url解析文件路径
     */
    @Test
    public void testPraseFromUrl() {
        String filePath = "group1/huex_sjuej/3hjshf.jpg";
        StorePath path = StorePath.praseFromUrl(filePath);
        assertNotNull(path);
        assertEquals(path.getGroup(), "group1");
        assertEquals(path.getPath(), "huex_sjuej/3hjshf.jpg");
    }

    /**
     * 不支持错误路径
     */
    @Test
    public void testPraseFromUrlWithErr() {
        String filePath = "group1jshf.jpg";
        try {
            StorePath.praseFromUrl(filePath);
            fail("No exception thrown.");
        } catch (Exception e) {
            assertTrue(e instanceof FdfsUnsupportStorePathException);
            LOGGER.debug(((FdfsUnsupportStorePathException) e).getMessage());
        }
    }

    /**
     * 路径地址必须包含group
     */
    @Test
    public void testPraseFromUrlWithFullPathErr() {
        String filePath = "http://192.1.1.2/group1/jshf.jpg";
        StorePath path = StorePath.praseFromUrl(filePath);
        assertNotNull(path);
        assertEquals(path.getGroup(), "group1");
        assertEquals(path.getPath(), "jshf.jpg");
    }

}
