package net.sudot.fdfs;

import net.sudot.fdfs.util.ConfigUtils;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * 测试常量定义
 * @author tobato
 */
@SuppressWarnings("unused")
public class TestConstants {

    public static final String TRACKER_HOST = ConfigUtils.getConfigValue("fdfs.test.trackerHost");
    public static final int TRACKER_PORT = Integer.parseInt(ConfigUtils.getConfigValue("fdfs.test.trackerPort"));

    public static final String STORAGE_HOST = ConfigUtils.getConfigValue("fdfs.test.storageHost");
    public static final int STORAGE_PORT = Integer.parseInt(ConfigUtils.getConfigValue("fdfs.test.storagePort"));
    public static final InetSocketAddress TRACKER_ADDRESS = new InetSocketAddress(TRACKER_HOST, TRACKER_PORT);
    public static final InetSocketAddress STORE_ADDRESS = new InetSocketAddress(STORAGE_HOST, STORAGE_PORT);

    public static final int SO_TIMEOUT = Integer.parseInt(ConfigUtils.getConfigValue("fdfs.soTimeout"));
    public static final int CONNECT_TIMEOUT = Integer.parseInt(ConfigUtils.getConfigValue("fdfs.connectTimeout"));
    public static final Charset DEFAULT_CHARSET = Charset.forName(ConfigUtils.getConfigValue("fdfs.charsetName"));

    public static final String TRACKER_LIST = ConfigUtils.getConfigValue("fdfs.trackerList");

    public static final String DEFAULT_GROUP = "group1";

    public static final String PERFORM_FILE_PATH = "/images/gs.jpg";
    public static final String CAT_IMAGE_FILE = "/images/cat.jpg";

}
