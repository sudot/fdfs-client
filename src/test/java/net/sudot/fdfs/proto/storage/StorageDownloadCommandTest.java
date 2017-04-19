package net.sudot.fdfs.proto.storage;

import net.sudot.fdfs.TestConstants;
import net.sudot.fdfs.domain.StorePath;
import net.sudot.fdfs.exception.FdfsServerException;
import net.sudot.fdfs.proto.StorageCommandTestBase;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件下载
 * @author tobato
 * @author sudot on 2017-04-19 0019.
 */
public class StorageDownloadCommandTest extends StorageCommandTestBase {
    String text_1 = "test";
    String text_2 = "StorageDownloadCommandChunk";
    String text_3 = "一个单元测试";
    String textAll = text_1 + text_2 + text_3;
    byte[] textBytes = textAll.getBytes(TestConstants.DEFAULT_CHARSET);
    InputStream stream = new ByteArrayInputStream(textBytes);
    StorePath storePath;

    @Before
    public void uploadFile () throws IOException {
        storePath = uploadInputStream(stream, "txt", stream.available(), false);
    }
    @After
    public void deleteFile () throws IOException {
        // 删除文件
        executeStoreCmd(new StorageDeleteFileCommand(storePath.getGroup(), storePath.getPath()));
    }

    /**
     * 完整下载
     */
    @Test
    public void testStorageDownloadCommand() throws IOException {
        long downloadOffset = 0;
        long downloadSize = 0;
        byte[] bytes = null;
        String download = null;

        bytes = executeStoreCmd(new StorageDownloadCommand<byte[]>(storePath.getGroup(), storePath.getPath(), new DownloadByteArray()));
        download = new String(bytes);
        logger.debug("----下载内容-----{}", download);
        Assert.assertEquals("完整文件下载错误", textAll, download);

        // 下载全部内容
        bytes = executeStoreCmd(new StorageDownloadCommand<byte[]>(storePath.getGroup(), storePath.getPath(), downloadOffset, downloadSize, new DownloadByteArray()));
        download = new String(bytes);
        logger.debug("----下载内容-----{}", download);
        Assert.assertEquals("完整文件下载错误", textAll, download);
    }

    /**
     * 下载偏移量后的所有内容
     */
    @Test
    public void testStorageDownloadCommandChunk0() throws IOException {
        long downloadOffset = text_1.getBytes(TestConstants.DEFAULT_CHARSET).length;
        long downloadSize = 0;
        byte[] bytes = null;
        String download = null;
        bytes = executeStoreCmd(new StorageDownloadCommand<byte[]>(storePath.getGroup(), storePath.getPath(), downloadOffset, downloadSize, new DownloadByteArray()));
        download = new String(bytes);
        logger.debug("----下载内容-----{}", download);
        Assert.assertEquals("片段文件下载错误", text_2 + text_3, download);
    }

    /**
     * 从文件起始位置下载指定大小的文件
     */
    @Test
    public void testStorageDownloadCommandChunk1() throws IOException {
        long downloadOffset = 0;
        long downloadSize = text_1.getBytes(TestConstants.DEFAULT_CHARSET).length;
        byte[] bytes = null;
        String download = null;
        bytes = executeStoreCmd(new StorageDownloadCommand<byte[]>(storePath.getGroup(), storePath.getPath(), downloadOffset, downloadSize, new DownloadByteArray()));
        download = new String(bytes);
        logger.debug("----下载内容-----{}", download);
        Assert.assertEquals("片段文件下载错误", text_1, download);
    }

    /**
     * 从文件指定位置下载指定大小的文件
     */
    @Test
    public void testStorageDownloadCommandChunk2() throws IOException {
        long downloadOffset = 31;
        long downloadSize = text_3.getBytes(TestConstants.DEFAULT_CHARSET).length;
        byte[] bytes = null;
        String download = null;
        bytes = executeStoreCmd(new StorageDownloadCommand<byte[]>(storePath.getGroup(), storePath.getPath(), downloadOffset, downloadSize, new DownloadByteArray()));
        download = new String(bytes);
        logger.debug("----下载内容-----{}", download);
        Assert.assertEquals("片段文件下载错误", text_3, download);
    }

    /**
     * 从文件指定位置下载指定大小的文件
     * <pre>
     *     观察此单元测试时,需屏蔽构造函数{@link StorageDownloadCommand#StorageDownloadCommand(java.lang.String, java.lang.String, long, long, net.sudot.fdfs.proto.storage.DownloadCallback)}
     *     中对downloadOffset和downloadSize的判断
     * </pre>
     */
    @Test
    public void testStorageDownloadCommandChunkError() throws IOException {
        long downloadOffset = -1;
        long downloadSize = 0;
        try {
            executeStoreCmd(new StorageDownloadCommand<byte[]>(storePath.getGroup(), storePath.getPath(), downloadOffset, downloadSize, new DownloadByteArray()));
        } catch (FdfsServerException e) {
            logger.info(e.getLocalizedMessage());
            Assert.assertEquals(e.getLocalizedMessage(), "错误码：22，错误信息：无效的参数", e.getLocalizedMessage());
        }

        downloadOffset = 0;
        downloadSize = -1;
        try {
            executeStoreCmd(new StorageDownloadCommand<byte[]>(storePath.getGroup(), storePath.getPath(), downloadOffset, downloadSize, new DownloadByteArray()));
        } catch (FdfsServerException e) {
            logger.info(e.getLocalizedMessage());
            Assert.assertEquals(e.getLocalizedMessage(), "错误码：22，错误信息：无效的参数", e.getLocalizedMessage());
        }
    }

    /**
     * 下载超过文件大小的片段
     */
    @Test
    public void testStorageDownloadCommandChunkErrorExceed() throws IOException {
        long downloadOffset = 1;
        long downloadSize = textBytes.length;
        try {
            executeStoreCmd(new StorageDownloadCommand<byte[]>(storePath.getGroup(), storePath.getPath(), downloadOffset, downloadSize, new DownloadByteArray()));
        } catch (FdfsServerException e) {
            logger.info(e.getLocalizedMessage());
            Assert.assertEquals(e.getLocalizedMessage(), "错误码：22，错误信息：无效的参数", e.getLocalizedMessage());
        }

        downloadOffset = textBytes.length + 1;
        downloadSize = textBytes.length;
        try {
            executeStoreCmd(new StorageDownloadCommand<byte[]>(storePath.getGroup(), storePath.getPath(), downloadOffset, downloadSize, new DownloadByteArray()));
        } catch (FdfsServerException e) {
            logger.info(e.getLocalizedMessage());
            Assert.assertEquals(e.getLocalizedMessage(), "错误码：22，错误信息：无效的参数", e.getLocalizedMessage());
        }
    }

}
