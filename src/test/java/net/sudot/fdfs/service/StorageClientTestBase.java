package net.sudot.fdfs.service;

import net.sudot.fdfs.FastdfsTestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * StorageClient测试基类
 * @author tobato
 */
public class StorageClientTestBase extends FastdfsTestBase {

    /** 日志 */
    protected static Logger LOGGER = LoggerFactory.getLogger(StorageClientTestBase.class);
    @Resource
    protected DefaultStorageClient storageClient;

}
