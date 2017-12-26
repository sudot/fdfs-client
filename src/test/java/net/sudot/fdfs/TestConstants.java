package net.sudot.fdfs;

import net.sudot.fdfs.service.DefaultFdfsOptionsFactory;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * 测试常量定义
 *
 * @author tobato
 * @author sudot on 2017-04-18 0018.
 */
@SuppressWarnings("unused")
public class TestConstants {
    public static final DefaultFdfsOptionsFactory FDFS_OPTIONS_FACTORY = new DefaultFdfsOptionsFactory();
    public static final String TRACKER_HOST = FDFS_OPTIONS_FACTORY.getProperties().getProperty("fdfs.test.trackerHost");
    public static final int TRACKER_PORT = Integer.parseInt(FDFS_OPTIONS_FACTORY.getProperties().getProperty("fdfs.test.trackerPort"));

    public static final String STORAGE_HOST = FDFS_OPTIONS_FACTORY.getProperties().getProperty("fdfs.test.storageHost");
    public static final int STORAGE_PORT = Integer.parseInt(FDFS_OPTIONS_FACTORY.getProperties().getProperty("fdfs.test.storagePort"));
    public static final InetSocketAddress TRACKER_ADDRESS = new InetSocketAddress(TRACKER_HOST, TRACKER_PORT);
    public static final InetSocketAddress STORE_ADDRESS = new InetSocketAddress(STORAGE_HOST, STORAGE_PORT);

    public static final int SO_TIMEOUT = Integer.parseInt(FDFS_OPTIONS_FACTORY.getProperties().getProperty("fdfs.soTimeout"));
    public static final int CONNECT_TIMEOUT = Integer.parseInt(FDFS_OPTIONS_FACTORY.getProperties().getProperty("fdfs.connectTimeout"));
    public static final Charset DEFAULT_CHARSET = Charset.forName(FDFS_OPTIONS_FACTORY.getProperties().getProperty("fdfs.charsetName"));

    public static final String TRACKER_LIST = FDFS_OPTIONS_FACTORY.getProperties().getProperty("fdfs.trackerList");

    public static final String DEFAULT_GROUP = FDFS_OPTIONS_FACTORY.getProperties().getProperty("fdfs.test.defaultGroup");

    public static final String PERFORM_FILE_PATH = "/images/gs.jpg";
    public static final String CAT_IMAGE_FILE = "/images/cat.jpg";
    public static final String BIG_FILE = "/images/Docker.pdf";

}
