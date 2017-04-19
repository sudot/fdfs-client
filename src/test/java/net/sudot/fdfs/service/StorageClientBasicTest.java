package net.sudot.fdfs.service;

import net.sudot.fdfs.FastdfsTestBase;
import net.sudot.fdfs.TestConstants;
import net.sudot.fdfs.domain.FileInfo;
import net.sudot.fdfs.domain.RandomTextFile;
import net.sudot.fdfs.domain.StorePath;
import net.sudot.fdfs.proto.storage.DownloadByteArray;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

/**
 * 文件基础操作测试演示
 * @author tobato
 * @author sudot on 2017-04-18 0018.
 */
public class StorageClientBasicTest extends FastdfsTestBase {

    /**
     * 基本文件上传操作测试
     * @throws IOException
     */
    @Test
    public void testGenerateStorageClient() throws IOException {
        logger.debug("##上传文件..##");
        RandomTextFile file = new RandomTextFile();
        StorePath path = storageClient.uploadFile(TestConstants.DEFAULT_GROUP, file.getInputStream(),
                file.getFileSize(), file.getFileExtName());
        assertNotNull(path);
        logger.debug("上传文件 result={}", path);

        logger.debug("##查询文件信息..##");
        FileInfo fileInfo = storageClient.queryFileInfo(path.getGroup(), path.getPath());
        logger.debug("查询文件信息 result={}", fileInfo);

        logger.debug("##下载文件..##");
        DownloadByteArray callback = new DownloadByteArray();
        byte[] content = storageClient.downloadFile(path.getGroup(), path.getPath(), callback);
        assertArrayEquals(content, file.toByte());

        logger.debug("##上传从文件..##");
        RandomTextFile slaveFile = new RandomTextFile();
        // TODO 120*120会报错误，看是否可以从客户端截获此错误
        StorePath slavePath = storageClient.uploadSlaveFile(path.getGroup(), path.getPath(), slaveFile.getInputStream(),
                slaveFile.getFileSize(), "120_120", slaveFile.getFileExtName());
        logger.debug("上传从文件 result={}", slavePath);

        logger.debug("##删除主文件..##");
        storageClient.deleteFile(path.getGroup(), path.getPath());
        logger.debug("##删除从文件..##");
        storageClient.deleteFile(slavePath.getGroup(), slavePath.getPath());

    }

    /**
     * 演示上传文件的时候Group可以为空
     * @throws IOException
     */
    @Test
    public void testGenerateStorageClientWithGroupNull() throws IOException {

        logger.debug("##上传文件..##");
        RandomTextFile file = new RandomTextFile();
        StorePath path = storageClient.uploadFile(null, file.getInputStream(), file.getFileSize(),
                file.getFileExtName());
        assertNotNull(path);
        logger.debug("上传文件 result={}", path);

        logger.debug("##删除文件..##");
        storageClient.deleteFile(path.getGroup(), path.getPath());
    }

}
