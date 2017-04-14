package net.sudot.fdfs.domain;

import net.sudot.fdfs.FastdfsTestBase;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * 缩略图配置测试
 * @author tobato
 * Update by sudot on 2017-03-17 0017.
 */
public class DefaultThumbImageConfigTest extends FastdfsTestBase {

    /** 日志 */
    protected static Logger LOGGER = LoggerFactory.getLogger(DefaultThumbImageConfigTest.class);

    @Resource
    private ThumbImageConfig thumbImageConfig;

    @Test
    public void testGetThumbImagePrefixName() {
        assertNotNull(thumbImageConfig.getSuffixName());
    }

    @Test
    public void testGetThumbImagePath() {

        String path = "wKgBaVaNODiAPpVCAAGtJ7UVNRA438.jpg";
        String thumbPath = "wKgBaVaNODiAPpVCAAGtJ7UVNRA438" + thumbImageConfig.getSuffixName() + ".jpg";

        String result = thumbImageConfig.getThumbImagePath(path);
        LOGGER.debug(result);
        assertEquals(thumbPath, result);

    }

}
