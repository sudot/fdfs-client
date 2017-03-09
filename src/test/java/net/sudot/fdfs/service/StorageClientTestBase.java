package net.sudot.fdfs.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * StorageClient测试基类
 * 
 * @author tobato
 *
 */
public class StorageClientTestBase {

    @Autowired
    protected AppendFileStorageClient storageClient;

    /** 日志 */
    protected static Logger LOGGER = LoggerFactory.getLogger(StorageClientTestBase.class);

}
