package net.sudot.fdfs.service;

import net.sudot.fdfs.FastdfsTestBase;
import net.sudot.fdfs.TestConstants;
import net.sudot.fdfs.domain.FileInfo;
import net.sudot.fdfs.domain.MetaData;
import net.sudot.fdfs.domain.StorePath;
import net.sudot.fdfs.proto.storage.DownloadByteArray;
import org.junit.After;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by tangjialin on 2017-04-19 0019.
 */
public class StorageClientTest extends FastdfsTestBase {
    String storageClientText = "StorageClient";
    byte[] bytes = (storageClientText + "单元测试").getBytes(TestConstants.DEFAULT_CHARSET);
    InputStream stream = new ByteArrayInputStream(bytes);
    StorePath storePath = null;

    @After
    public void afterClear() {
        if (storePath == null) { return; }
        // 删除文件
        storageClient.deleteFile(storePath.getGroup(), storePath.getPath());
    }

    /**
     * 上传文件(不支持断点续传)
     * @throws Exception
     * @see StorageClient#uploadFile(java.io.InputStream, long, java.lang.String)
     */
    @Test
    public void uploadFile() throws Exception {
        storePath = storageClient.uploadFile(stream, stream.available(), "txt");
        logger.info("文件上传路径:{}", storePath.getFullPath());
    }

    /**
     * 上传文件(不支持断点续传)
     * @throws Exception
     * @see StorageClient#uploadFile(java.lang.String, java.io.InputStream, long, java.lang.String)
     */
    @Test
    public void uploadFile1() throws Exception {
        storePath = storageClient.uploadFile(TestConstants.DEFAULT_GROUP, stream, stream.available(), "txt");
        logger.info("文件上传路径:{}", storePath.getFullPath());
    }

    /**
     * 上传包含元信息的文件(不支持断点续传)
     * @throws Exception
     * @see StorageClient#uploadFile(java.io.InputStream, long, java.lang.String, java.util.Set)
     */
    @Test
    public void uploadFile2() throws Exception {
        Set<MetaData> metaData = new HashSet<>();
        metaData.add(new MetaData("测试键", "测试值"));
        metaData.add(new MetaData("fileSize", String.valueOf(stream.available())));
        storePath = storageClient.uploadFile(stream, stream.available(), "txt", metaData);
        logger.info("文件上传路径:{}", storePath.getFullPath());
        Set<MetaData> metaData1 = storageClient.getMetaData(storePath);
        logger.info("文件信息:{}", metaData1);
    }

    /**
     * 上传包含元信息的文件(不支持断点续传)
     * @throws Exception
     * @see StorageClient#uploadFile(java.lang.String, java.io.InputStream, long, java.lang.String, java.util.Set)
     */
    @Test
    public void uploadFile3() throws Exception {
        Set<MetaData> metaData = new HashSet<>();
        metaData.add(new MetaData("测试键", "测试值"));
        metaData.add(new MetaData("fileSize", String.valueOf(stream.available())));
        storePath = storageClient.uploadFile(TestConstants.DEFAULT_GROUP, stream, stream.available(), "txt", metaData);
        logger.info("文件上传路径:{}", storePath.getFullPath());
        Set<MetaData> metaData1 = storageClient.getMetaData(storePath);
        logger.info("文件信息:{}", metaData1);
    }

    /**
     * 上传支持断点续传的文件,和断点续传
     * @throws Exception
     * @see StorageClient#uploadAppenderFile(java.io.InputStream, long, java.lang.String)
     */
    @Test
    public void uploadAppenderFile() throws Exception {
        storePath = storageClient.uploadAppenderFile(stream, stream.available(), "txt");
        byte[] bytes1 = storageClient.downloadFile(storePath, new DownloadByteArray());
        logger.info("文件上传路径:{} 文件内容:{}", storePath.getFullPath(), new String(bytes1));

        storageClient.appendFile(storePath, new ByteArrayInputStream(this.bytes), this.bytes.length);
        byte[] bytes2 = storageClient.downloadFile(storePath, new DownloadByteArray());
        logger.info("续传后的文件内容:{}", new String(bytes2));
    }

    /**
     * 上传支持断点续传的文件
     * @throws Exception
     * @see StorageClient#uploadAppenderFile(java.lang.String, java.io.InputStream, long, java.lang.String)
     */
    @Test
    public void uploadAppenderFile1() throws Exception {
        storePath = storageClient.uploadAppenderFile(TestConstants.DEFAULT_GROUP, stream, stream.available(), "txt");
        byte[] bytes1 = storageClient.downloadFile(storePath, new DownloadByteArray());
        logger.info("文件上传路径:{} 文件内容:{}", storePath.getFullPath(), new String(bytes1));

        storageClient.appendFile(storePath, new ByteArrayInputStream(this.bytes), this.bytes.length);
        byte[] bytes2 = storageClient.downloadFile(storePath, new DownloadByteArray());
        logger.info("续传后的文件内容:{}", new String(bytes2));
    }

    /**
     * 上传支持断点续传的文件
     * @see StorageClient#uploadAppenderFile(java.lang.String, java.io.InputStream, long, java.lang.String, java.util.Set)
     * @throws IOException
     */
    @Test
    public void uploadAppenderFile2() throws IOException {
        Set<MetaData> metaData = new HashSet<>();
        metaData.add(new MetaData("测试键", "测试值"));
        metaData.add(new MetaData("fileSize", String.valueOf(stream.available())));

        storePath = storageClient.uploadAppenderFile(stream, stream.available(), "txt", metaData);
        byte[] bytes1 = storageClient.downloadFile(storePath, new DownloadByteArray());
        Set<MetaData> metaData1 = storageClient.getMetaData(storePath);
        logger.info("文件上传路径:{} 文件内容:{}\n文件信息:{}", storePath.getFullPath(), new String(bytes1), metaData1);

        storageClient.appendFile(storePath, new ByteArrayInputStream(this.bytes), this.bytes.length);
        byte[] bytes2 = storageClient.downloadFile(storePath, new DownloadByteArray());

        Set<MetaData> metaData2 = storageClient.getMetaData(storePath);
        logger.info("续传后的文件内容:{}\n文件信息:{}", new String(bytes2), metaData2);
    }

    /**
     * 上传支持断点续传的文件
     * @see StorageClient#uploadAppenderFile(java.lang.String, java.io.InputStream, long, java.lang.String, java.util.Set)
     * @throws IOException
     */
    @Test
    public void uploadAppenderFile3() throws IOException {
        Set<MetaData> metaData = new HashSet<>();
        metaData.add(new MetaData("测试键", "测试值"));
        metaData.add(new MetaData("fileSize", String.valueOf(stream.available())));

        storePath = storageClient.uploadAppenderFile(TestConstants.DEFAULT_GROUP, stream, stream.available(), "txt", metaData);
        byte[] bytes1 = storageClient.downloadFile(storePath, new DownloadByteArray());
        Set<MetaData> metaData1 = storageClient.getMetaData(storePath);
        logger.info("文件上传路径:{} 文件内容:{}\n文件信息:{}", storePath.getFullPath(), new String(bytes1), metaData1);

        storageClient.appendFile(storePath, new ByteArrayInputStream(this.bytes), this.bytes.length);
        byte[] bytes2 = storageClient.downloadFile(storePath, new DownloadByteArray());

        Set<MetaData> metaData2 = storageClient.getMetaData(storePath);
        logger.info("续传后的文件内容:{}\n文件信息:{}", new String(bytes2), metaData2);
    }

    /**
     * 断点续传文件
     * @throws Exception
     * @see StorageClient#appendFile(java.lang.String, java.lang.String, java.io.InputStream, long)
     */
    @Test
    public void appendFile() throws Exception {
        storePath = storageClient.uploadAppenderFile(stream, stream.available(), "txt");
        byte[] bytes1 = storageClient.downloadFile(storePath, new DownloadByteArray());
        logger.info("文件上传路径:{} 文件内容:{}", storePath.getFullPath(), new String(bytes1));

        storageClient.appendFile(storePath.getGroup(), storePath.getPath(), new ByteArrayInputStream(this.bytes), this.bytes.length);
        byte[] bytes2 = storageClient.downloadFile(storePath, new DownloadByteArray());
        logger.info("续传后的文件内容:{}", new String(bytes2));
    }

    /**
     * 断点续传文件
     * @throws Exception
     * @see StorageClient#appendFile(java.lang.String, java.io.InputStream, long)
     * @see StorageClientTest#uploadAppenderFile()
     */
    @Test
    public void appendFile1() throws Exception {
        storePath = storageClient.uploadAppenderFile(stream, stream.available(), "txt");
        byte[] bytes1 = storageClient.downloadFile(storePath, new DownloadByteArray());
        logger.info("文件上传路径:{} 文件内容:{}", storePath.getFullPath(), new String(bytes1));

        storageClient.appendFile(storePath.getFullPath(), new ByteArrayInputStream(this.bytes), this.bytes.length);
        byte[] bytes2 = storageClient.downloadFile(storePath, new DownloadByteArray());
        logger.info("续传后的文件内容:{}", new String(bytes2));
    }

    /**
     * 断点续传文件
     * @throws Exception
     * @see StorageClient#appendFile(net.sudot.fdfs.domain.StorePath, java.io.InputStream, long)
     */
    @Test
    public void appendFile2() throws Exception {
        storePath = storageClient.uploadAppenderFile(TestConstants.DEFAULT_GROUP, stream, stream.available(), "txt");
        byte[] bytes1 = storageClient.downloadFile(storePath, new DownloadByteArray());
        logger.info("文件上传路径:{} 文件内容:{}", storePath.getFullPath(), new String(bytes1));

        storageClient.appendFile(storePath, new ByteArrayInputStream(this.bytes), this.bytes.length);
        byte[] bytes2 = storageClient.downloadFile(storePath, new DownloadByteArray());
        logger.info("续传后的文件内容:{}", new String(bytes2));
    }

    /**
     * 上传从文件
     * @throws Exception
     * @see StorageClient#uploadSlaveFile(java.lang.String, java.lang.String, java.io.InputStream, long, java.lang.String, java.lang.String)
     */
    @Test
    public void uploadSlaveFile() throws Exception {
        storePath = storageClient.uploadFile(stream, stream.available(), "txt");
        logger.info("文件上传路径:{}", storePath.getFullPath());
        StorePath uploadSlaveFile = storageClient.uploadSlaveFile(storePath.getGroup(), storePath.getPath(), new ByteArrayInputStream(bytes), bytes.length, "_slave", "txt");
        logger.info("从文件上传路径:{}", uploadSlaveFile.getFullPath());
        // 清理从文件
        storageClient.deleteFile(uploadSlaveFile);
    }

    /**
     * 上传从文件
     * @throws Exception
     * @see StorageClient#uploadSlaveFile(java.lang.String, java.io.InputStream, long, java.lang.String, java.lang.String)
     */
    @Test
    public void uploadSlaveFile1() throws Exception {
        storePath = storageClient.uploadFile(stream, stream.available(), "txt");
        logger.info("文件上传路径:{}", storePath.getFullPath());
        StorePath uploadSlaveFile = storageClient.uploadSlaveFile(storePath.getFullPath(), new ByteArrayInputStream(bytes), bytes.length, "_slave", "txt");
        logger.info("从文件上传路径:{}", uploadSlaveFile.getFullPath());
        // 清理从文件
        storageClient.deleteFile(uploadSlaveFile);
    }

    /**
     * 上传从文件
     * @throws Exception
     * @see StorageClient#uploadSlaveFile(net.sudot.fdfs.domain.StorePath, java.io.InputStream, long, java.lang.String, java.lang.String)
     */
    @Test
    public void uploadSlaveFile2() throws Exception {
        storePath = storageClient.uploadFile(stream, stream.available(), "txt");
        logger.info("文件上传路径:{}", storePath.getFullPath());
        StorePath uploadSlaveFile = storageClient.uploadSlaveFile(storePath, new ByteArrayInputStream(bytes), bytes.length, "_slave", "txt");
        logger.info("从文件上传路径:{}", uploadSlaveFile.getFullPath());
        // 清理从文件
        storageClient.deleteFile(uploadSlaveFile);
    }

    /**
     * 修改续传文件的内容
     * @throws Exception
     * @see StorageClient#modifyFile(java.lang.String, java.lang.String, java.io.InputStream, long, long)
     */
    @Test
    public void modifyFile() throws Exception {
        storePath = storageClient.uploadAppenderFile(TestConstants.DEFAULT_GROUP, stream, stream.available(), "txt");
        byte[] bytes1 = storageClient.downloadFile(storePath, new DownloadByteArray());
        logger.info("文件上传路径:{} 文件内容:{}", storePath.getFullPath(), new String(bytes1));

        byte[] modifyTextBytes = "修改续传文件的内容".getBytes(TestConstants.DEFAULT_CHARSET);
        long modifySize = modifyTextBytes.length;
        long modifyOffset = storageClientText.getBytes(TestConstants.DEFAULT_CHARSET).length;
        storageClient.modifyFile(storePath.getGroup(), storePath.getPath(), new ByteArrayInputStream(modifyTextBytes), modifySize, modifyOffset);
        // 下载文件
        byte[] bytes2 = storageClient.downloadFile(storePath, new DownloadByteArray());
        logger.info("修改后文件内容:{}", new String(bytes2));
    }

    /**
     * 修改续传文件的内容
     * @throws Exception
     * @see StorageClient#modifyFile(java.lang.String, java.io.InputStream, long, long)
     */
    @Test
    public void modifyFile1() throws Exception {
        storePath = storageClient.uploadAppenderFile(TestConstants.DEFAULT_GROUP, stream, stream.available(), "txt");
        byte[] bytes1 = storageClient.downloadFile(storePath, new DownloadByteArray());
        logger.info("文件上传路径:{} 文件内容:{}", storePath.getFullPath(), new String(bytes1));

        byte[] modifyTextBytes = "修改续传文件的内容".getBytes(TestConstants.DEFAULT_CHARSET);
        long modifySize = modifyTextBytes.length;
        long modifyOffset = storageClientText.getBytes(TestConstants.DEFAULT_CHARSET).length;
        storageClient.modifyFile(storePath.getFullPath(), new ByteArrayInputStream(modifyTextBytes), modifySize, modifyOffset);
        // 下载文件
        byte[] bytes2 = storageClient.downloadFile(storePath, new DownloadByteArray());
        logger.info("修改后文件内容:{}", new String(bytes2));
    }

    /**
     * 修改续传文件的内容
     * @throws Exception
     * @see StorageClient#modifyFile(net.sudot.fdfs.domain.StorePath, java.io.InputStream, long, long)
     */
    @Test
    public void modifyFile2() throws Exception {
        storePath = storageClient.uploadAppenderFile(TestConstants.DEFAULT_GROUP, stream, stream.available(), "txt");
        byte[] bytes1 = storageClient.downloadFile(storePath, new DownloadByteArray());
        logger.info("文件上传路径:{} 文件内容:{}", storePath.getFullPath(), new String(bytes1));

        byte[] modifyTextBytes = "修改续传文件的内容".getBytes(TestConstants.DEFAULT_CHARSET);
        long modifySize = modifyTextBytes.length;
        long modifyOffset = storageClientText.getBytes(TestConstants.DEFAULT_CHARSET).length;
        storageClient.modifyFile(storePath, new ByteArrayInputStream(modifyTextBytes), modifySize, modifyOffset);
        // 下载文件
        byte[] bytes2 = storageClient.downloadFile(storePath, new DownloadByteArray());
        logger.info("修改后文件内容:{}", new String(bytes2));
    }

    /**
     * 删减续传类型文件的内容(从文件起始部分删减)
     * @throws Exception
     * @see StorageClient#truncateFile(java.lang.String, java.lang.String, long)
     */
    @Test
    public void truncateFile() throws Exception {
        storePath = storageClient.uploadAppenderFile(TestConstants.DEFAULT_GROUP, stream, stream.available(), "txt");
        byte[] bytes1 = storageClient.downloadFile(storePath, new DownloadByteArray());
        logger.info("文件上传路径:{} 文件内容:{}", storePath.getFullPath(), new String(bytes1));

        // 删减文件
        int truncatedFileSize = storageClientText.getBytes(TestConstants.DEFAULT_CHARSET).length;
        storageClient.truncateFile(storePath.getGroup(), storePath.getPath(), truncatedFileSize);
        // 下载文件
        byte[] bytes2 = storageClient.downloadFile(storePath, new DownloadByteArray());
        logger.info("删减后文件内容:{}", new String(bytes2));
    }

    /**
     * 删减续传类型文件的内容(从文件起始部分删减)
     * @throws Exception
     * @see StorageClient#truncateFile(java.lang.String, long)
     */
    @Test
    public void truncateFile1() throws Exception {
        storePath = storageClient.uploadAppenderFile(TestConstants.DEFAULT_GROUP, stream, stream.available(), "txt");
        byte[] bytes1 = storageClient.downloadFile(storePath, new DownloadByteArray());
        logger.info("文件上传路径:{} 文件内容:{}", storePath.getFullPath(), new String(bytes1));

        // 删减文件
        int truncatedFileSize = storageClientText.getBytes(TestConstants.DEFAULT_CHARSET).length;
        storageClient.truncateFile(storePath.getFullPath(), truncatedFileSize);
        // 下载文件
        byte[] bytes2 = storageClient.downloadFile(storePath, new DownloadByteArray());
        logger.info("删减后文件内容:{}", new String(bytes2));
    }

    /**
     * 删减续传类型文件的内容(从文件起始部分删减)
     * @throws Exception
     * @see StorageClient#truncateFile(net.sudot.fdfs.domain.StorePath, long)
     */
    @Test
    public void truncateFile2() throws Exception {
        storePath = storageClient.uploadAppenderFile(TestConstants.DEFAULT_GROUP, stream, stream.available(), "txt");
        byte[] bytes1 = storageClient.downloadFile(storePath, new DownloadByteArray());
        logger.info("文件上传路径:{} 文件内容:{}", storePath.getFullPath(), new String(bytes1));

        // 删减文件
        int truncatedFileSize = storageClientText.getBytes(TestConstants.DEFAULT_CHARSET).length;
        storageClient.truncateFile(storePath, truncatedFileSize);
        // 下载文件
        byte[] bytes2 = storageClient.downloadFile(storePath, new DownloadByteArray());
        logger.info("删减后文件内容:{}", new String(bytes2));
    }

    /**
     * 删减续传类型文件的全部内容
     * @throws Exception
     * @see StorageClient#truncateFile(java.lang.String, java.lang.String)
     */
    @Test
    public void truncateFile3() throws Exception {
        storePath = storageClient.uploadAppenderFile(TestConstants.DEFAULT_GROUP, stream, stream.available(), "txt");
        byte[] bytes1 = storageClient.downloadFile(storePath, new DownloadByteArray());
        logger.info("文件上传路径:{} 文件内容:{}", storePath.getFullPath(), new String(bytes1));

        // 删减文件
        storageClient.truncateFile(storePath.getGroup(), storePath.getPath());
        // 下载文件
        byte[] bytes2 = storageClient.downloadFile(storePath, new DownloadByteArray());
        logger.info("删减后文件内容:{}", new String(bytes2));
    }

    /**
     * 删减续传类型文件的全部内容
     * @throws Exception
     * @see StorageClient#truncateFile(java.lang.String)
     */
    @Test
    public void truncateFile4() throws Exception {
        storePath = storageClient.uploadAppenderFile(TestConstants.DEFAULT_GROUP, stream, stream.available(), "txt");
        byte[] bytes1 = storageClient.downloadFile(storePath, new DownloadByteArray());
        logger.info("文件上传路径:{} 文件内容:{}", storePath.getFullPath(), new String(bytes1));

        // 删减文件
        storageClient.truncateFile(storePath.getFullPath());
        // 下载文件
        byte[] bytes2 = storageClient.downloadFile(storePath, new DownloadByteArray());
        logger.info("删减后文件内容:{}", new String(bytes2));
    }

    /**
     * 删减续传类型文件的全部内容
     * @throws Exception
     * @see StorageClient#truncateFile(net.sudot.fdfs.domain.StorePath)
     */
    @Test
    public void truncateFile5() throws Exception {
        storePath = storageClient.uploadAppenderFile(TestConstants.DEFAULT_GROUP, stream, stream.available(), "txt");
        byte[] bytes1 = storageClient.downloadFile(storePath, new DownloadByteArray());
        logger.info("文件上传路径:{} 文件内容:{}", storePath.getFullPath(), new String(bytes1));

        // 删减文件
        storageClient.truncateFile(storePath);
        // 下载文件
        byte[] bytes2 = storageClient.downloadFile(storePath, new DownloadByteArray());
        logger.info("删减后文件内容:{}", new String(bytes2));
    }

    /**
     * 查看文件的信息
     * @throws Exception
     * @see StorageClient#queryFileInfo(java.lang.String, java.lang.String)
     */
    @Test
    public void queryFileInfo() throws Exception {
        storePath = storageClient.uploadAppenderFile(TestConstants.DEFAULT_GROUP, stream, stream.available(), "txt");

        FileInfo fileInfo = storageClient.queryFileInfo(storePath.getGroup(), storePath.getPath());
        logger.info("文件上传路径:{} 文件信息:{}", storePath.getFullPath(), fileInfo);
    }

    /**
     * 查看文件的信息
     * @throws Exception
     * @see StorageClient#queryFileInfo(java.lang.String)
     */
    @Test
    public void queryFileInfo1() throws Exception {
        storePath = storageClient.uploadAppenderFile(TestConstants.DEFAULT_GROUP, stream, stream.available(), "txt");

        FileInfo fileInfo = storageClient.queryFileInfo(storePath.getFullPath());
        logger.info("文件上传路径:{} 文件信息:{}", storePath.getFullPath(), fileInfo);
    }

    /**
     * 查看文件的信息
     * @throws Exception
     * @see StorageClient#queryFileInfo(net.sudot.fdfs.domain.StorePath)
     */
    @Test
    public void queryFileInfo2() throws Exception {
        storePath = storageClient.uploadAppenderFile(TestConstants.DEFAULT_GROUP, stream, stream.available(), "txt");

        FileInfo fileInfo = storageClient.queryFileInfo(storePath);
        logger.info("文件上传路径:{} 文件信息:{}", storePath.getFullPath(), fileInfo);
    }

    /**
     * 下载整个文件
     * @throws Exception
     * @see StorageClient#downloadFile(java.lang.String, java.lang.String, net.sudot.fdfs.proto.storage.DownloadCallback)
     */
    @Test
    public void downloadFile() throws Exception {
        storePath = storageClient.uploadAppenderFile(TestConstants.DEFAULT_GROUP, stream, stream.available(), "txt");
        byte[] bytes1 = storageClient.downloadFile(storePath.getGroup(), storePath.getPath(), new DownloadByteArray());
        logger.info("文件上传路径:{} 文件内容:{}", storePath.getFullPath(), new String(bytes1));
    }

    /**
     * 下载整个文件
     * @throws Exception
     * @see StorageClient#downloadFile(java.lang.String, net.sudot.fdfs.proto.storage.DownloadCallback)
     */
    @Test
    public void downloadFile1() throws Exception {
        storePath = storageClient.uploadAppenderFile(TestConstants.DEFAULT_GROUP, stream, stream.available(), "txt");
        byte[] bytes1 = storageClient.downloadFile(storePath.getFullPath(), new DownloadByteArray());
        logger.info("文件上传路径:{} 文件内容:{}", storePath.getFullPath(), new String(bytes1));
    }

    /**
     * 下载整个文件
     * @throws Exception
     * @see StorageClient#downloadFile(net.sudot.fdfs.domain.StorePath, net.sudot.fdfs.proto.storage.DownloadCallback)
     */
    @Test
    public void downloadFile2() throws Exception {
        storePath = storageClient.uploadAppenderFile(TestConstants.DEFAULT_GROUP, stream, stream.available(), "txt");
        byte[] bytes1 = storageClient.downloadFile(storePath, new DownloadByteArray());
        logger.info("文件上传路径:{} 文件内容:{}", storePath.getFullPath(), new String(bytes1));
    }

    /**
     * 下载文件片段
     * @throws Exception
     * @see StorageClient#downloadFile(java.lang.String, java.lang.String, long, long, net.sudot.fdfs.proto.storage.DownloadCallback)
     */
    @Test
    public void downloadFile3() throws Exception {
        storePath = storageClient.uploadAppenderFile(TestConstants.DEFAULT_GROUP, stream, stream.available(), "txt");
        long downloadOffset = storageClientText.getBytes(TestConstants.DEFAULT_CHARSET).length;
        long downloadSize = bytes.length - downloadOffset;
        byte[] bytes1 = storageClient.downloadFile(storePath.getGroup(), storePath.getPath(), downloadOffset, downloadSize, new DownloadByteArray());
        logger.info("文件上传路径:{} 文件内容:{}", storePath.getFullPath(), new String(bytes1));
    }

    /**
     * 下载文件片段
     * @throws Exception
     * @see StorageClient#downloadFile(java.lang.String, long, long, net.sudot.fdfs.proto.storage.DownloadCallback)
     */
    @Test
    public void downloadFile4() throws Exception {
        storePath = storageClient.uploadAppenderFile(TestConstants.DEFAULT_GROUP, stream, stream.available(), "txt");
        long downloadOffset = storageClientText.getBytes(TestConstants.DEFAULT_CHARSET).length;
        long downloadSize = bytes.length - downloadOffset;
        byte[] bytes1 = storageClient.downloadFile(storePath.getFullPath(), downloadOffset, downloadSize, new DownloadByteArray());
        logger.info("文件上传路径:{} 文件内容:{}", storePath.getFullPath(), new String(bytes1));
    }

    /**
     * 下载文件片段
     * @throws Exception
     * @see StorageClient#downloadFile(net.sudot.fdfs.domain.StorePath, long, long, net.sudot.fdfs.proto.storage.DownloadCallback)
     */
    @Test
    public void downloadFile5() throws Exception {
        storePath = storageClient.uploadAppenderFile(TestConstants.DEFAULT_GROUP, stream, stream.available(), "txt");
        long downloadOffset = storageClientText.getBytes(TestConstants.DEFAULT_CHARSET).length;
        long downloadSize = bytes.length - downloadOffset;
        byte[] bytes1 = storageClient.downloadFile(storePath, downloadOffset, downloadSize, new DownloadByteArray());
        logger.info("文件上传路径:{} 文件内容:{}", storePath.getFullPath(), new String(bytes1));
    }

    /**
     * 删除文件
     * @see StorageClient#deleteFile(java.lang.String, java.lang.String)
     * @throws Exception
     */
    @Test
    public void deleteFile() throws Exception {
        storePath = storageClient.uploadAppenderFile(TestConstants.DEFAULT_GROUP, stream, stream.available(), "txt");
        logger.info("文件上传路径:{}", storePath.getFullPath());
        storageClient.deleteFile(storePath.getGroup(), storePath.getPath());
        storePath = null;
    }

    /**
     * 删除文件
     * @see StorageClient#deleteFile(java.lang.String)
     * @throws Exception
     */
    @Test
    public void deleteFile1() throws Exception {
        storePath = storageClient.uploadAppenderFile(TestConstants.DEFAULT_GROUP, stream, stream.available(), "txt");
        logger.info("文件上传路径:{}", storePath.getFullPath());
        storageClient.deleteFile(storePath.getFullPath());
        storePath = null;
    }

    /**
     * 删除文件
     * @see StorageClient#deleteFile(net.sudot.fdfs.domain.StorePath)
     * @throws Exception
     */
    @Test
    public void deleteFile2() throws Exception {
        storePath = storageClient.uploadAppenderFile(TestConstants.DEFAULT_GROUP, stream, stream.available(), "txt");
        logger.info("文件上传路径:{}", storePath.getFullPath());
        storageClient.deleteFile(storePath);
        storePath = null;
    }

    /**
     * 修改文件元信息（合并）
     * @see StorageClient#mergeMetaData(java.lang.String, java.lang.String, java.util.Set)
     * @throws Exception
     */
    @Test
    public void mergeMetaData() throws Exception {
        Set<MetaData> metaData = new HashSet<>();
        metaData.add(new MetaData("测试键", "测试值"));
        metaData.add(new MetaData("fileSize", String.valueOf(stream.available())));
        storePath = storageClient.uploadFile(stream, stream.available(), "txt", metaData);

        Set<MetaData> metaData1 = storageClient.getMetaData(storePath);
        logger.info("文件上传路径:{}\n文件信息:{}", storePath.getFullPath(), metaData1);

        Set<MetaData> metaData2 = new HashSet<>();
        metaData2.add(new MetaData("测试键", "测试值覆盖"));
        metaData2.add(new MetaData("新增键", "新增键-新增值"));
        storageClient.mergeMetaData(storePath.getGroup(), storePath.getPath(), metaData2);
        Set<MetaData> metaData3 = storageClient.getMetaData(storePath);
        logger.info("\n合并后的文件信息:{}", metaData3);
    }

    /**
     * 修改文件元信息（合并）
     * @see StorageClient#mergeMetaData(java.lang.String, java.util.Set)
     * @throws Exception
     */
    @Test
    public void mergeMetaData1() throws Exception {
        Set<MetaData> metaData = new HashSet<>();
        metaData.add(new MetaData("测试键", "测试值"));
        metaData.add(new MetaData("fileSize", String.valueOf(stream.available())));
        storePath = storageClient.uploadFile(stream, stream.available(), "txt", metaData);

        Set<MetaData> metaData1 = storageClient.getMetaData(storePath);
        logger.info("文件上传路径:{}\n文件信息:{}", storePath.getFullPath(), metaData1);

        Set<MetaData> metaData2 = new HashSet<>();
        metaData2.add(new MetaData("测试键", "测试值覆盖"));
        metaData2.add(new MetaData("新增键", "新增键-新增值"));
        storageClient.mergeMetaData(storePath.getFullPath(), metaData2);
        Set<MetaData> metaData3 = storageClient.getMetaData(storePath);
        logger.info("\n合并后的文件信息:{}", metaData3);
    }

    /**
     * 修改文件元信息（合并）
     * @see StorageClient#mergeMetaData(net.sudot.fdfs.domain.StorePath, java.util.Set)
     * @throws Exception
     */
    @Test
    public void mergeMetaData2() throws Exception {
        Set<MetaData> metaData = new HashSet<>();
        metaData.add(new MetaData("测试键", "测试值"));
        metaData.add(new MetaData("fileSize", String.valueOf(stream.available())));
        storePath = storageClient.uploadFile(stream, stream.available(), "txt", metaData);

        Set<MetaData> metaData1 = storageClient.getMetaData(storePath);
        logger.info("文件上传路径:{}\n文件信息:{}", storePath.getFullPath(), metaData1);

        Set<MetaData> metaData2 = new HashSet<>();
        metaData2.add(new MetaData("测试键", "测试值覆盖"));
        metaData2.add(new MetaData("新增键", "新增键-新增值"));
        storageClient.mergeMetaData(storePath, metaData2);

        Set<MetaData> metaData3 = storageClient.getMetaData(storePath);
        logger.info("\n合并后的文件信息:{}", metaData3);
    }

    /**
     * 修改文件元信息（覆盖）
     * @see StorageClient#overwriteMetaData(java.lang.String, java.lang.String, java.util.Set)
     * @throws Exception
     */
    @Test
    public void overwriteMetaData() throws Exception {
        Set<MetaData> metaData = new HashSet<>();
        metaData.add(new MetaData("测试键", "测试值"));
        metaData.add(new MetaData("fileSize", String.valueOf(stream.available())));
        storePath = storageClient.uploadFile(stream, stream.available(), "txt", metaData);

        Set<MetaData> metaData1 = storageClient.getMetaData(storePath);
        logger.info("文件上传路径:{}\n文件信息:{}", storePath.getFullPath(), metaData1);

        Set<MetaData> metaData2 = new HashSet<>();
        metaData2.add(new MetaData("测试键", "测试值覆盖"));
        metaData2.add(new MetaData("新增键", "新增键-新增值"));
        storageClient.overwriteMetaData(storePath.getGroup(), storePath.getPath(), metaData2);

        Set<MetaData> metaData3 = storageClient.getMetaData(storePath);
        logger.info("\n覆盖后的文件信息:{}", metaData3);
    }

    /**
     * 修改文件元信息（覆盖）
     * @see StorageClient#overwriteMetaData(java.lang.String, java.util.Set)
     * @throws Exception
     */
    @Test
    public void overwriteMetaData1() throws Exception {
        Set<MetaData> metaData = new HashSet<>();
        metaData.add(new MetaData("测试键", "测试值"));
        metaData.add(new MetaData("fileSize", String.valueOf(stream.available())));
        storePath = storageClient.uploadFile(stream, stream.available(), "txt", metaData);

        Set<MetaData> metaData1 = storageClient.getMetaData(storePath);
        logger.info("文件上传路径:{}\n文件信息:{}", storePath.getFullPath(), metaData1);

        Set<MetaData> metaData2 = new HashSet<>();
        metaData2.add(new MetaData("测试键", "测试值覆盖"));
        metaData2.add(new MetaData("新增键", "新增键-新增值"));
        storageClient.overwriteMetaData(storePath.getFullPath(), metaData2);

        Set<MetaData> metaData3 = storageClient.getMetaData(storePath);
        logger.info("\n覆盖后的文件信息:{}", metaData3);
    }

    /**
     * 修改文件元信息（覆盖）
     * @see StorageClient#overwriteMetaData(net.sudot.fdfs.domain.StorePath, java.util.Set)
     * @throws Exception
     */
    @Test
    public void overwriteMetaData2() throws Exception {
        Set<MetaData> metaData = new HashSet<>();
        metaData.add(new MetaData("测试键", "测试值"));
        metaData.add(new MetaData("fileSize", String.valueOf(stream.available())));
        storePath = storageClient.uploadFile(stream, stream.available(), "txt", metaData);

        Set<MetaData> metaData1 = storageClient.getMetaData(storePath);
        logger.info("文件上传路径:{}\n文件信息:{}", storePath.getFullPath(), metaData1);

        Set<MetaData> metaData2 = new HashSet<>();
        metaData2.add(new MetaData("测试键", "测试值覆盖"));
        metaData2.add(new MetaData("新增键", "新增键-新增值"));
        storageClient.overwriteMetaData(storePath, metaData2);

        Set<MetaData> metaData3 = storageClient.getMetaData(storePath);
        logger.info("\n覆盖后的文件信息:{}", metaData3);
    }

    /**
     * 获取文件元信息
     * @see StorageClient#getMetaData(java.lang.String, java.lang.String)
     * @throws Exception
     */
    @Test
    public void getMetaData() throws Exception {
        Set<MetaData> metaData = new HashSet<>();
        metaData.add(new MetaData("测试键", "测试值"));
        metaData.add(new MetaData("fileSize", String.valueOf(stream.available())));
        storePath = storageClient.uploadFile(stream, stream.available(), "txt", metaData);

        Set<MetaData> metaData1 = storageClient.getMetaData(storePath.getGroup(), storePath.getPath());
        logger.info("文件上传路径:{}\n文件信息:{}", storePath.getFullPath(), metaData1);
    }

    /**
     * 获取文件元信息
     * @see StorageClient#getMetaData(java.lang.String)
     * @throws Exception
     */
    @Test
    public void getMetaData1() throws Exception {
        Set<MetaData> metaData = new HashSet<>();
        metaData.add(new MetaData("测试键", "测试值"));
        metaData.add(new MetaData("fileSize", String.valueOf(stream.available())));
        storePath = storageClient.uploadFile(stream, stream.available(), "txt", metaData);

        Set<MetaData> metaData1 = storageClient.getMetaData(storePath.getFullPath());
        logger.info("文件上传路径:{}\n文件信息:{}", storePath.getFullPath(), metaData1);
    }

    /**
     * 获取文件元信息
     * @see StorageClient#getMetaData(net.sudot.fdfs.domain.StorePath)
     * @throws Exception
     */
    @Test
    public void getMetaData2() throws Exception {
        Set<MetaData> metaData = new HashSet<>();
        metaData.add(new MetaData("测试键", "测试值"));
        metaData.add(new MetaData("fileSize", String.valueOf(stream.available())));
        storePath = storageClient.uploadFile(stream, stream.available(), "txt", metaData);

        Set<MetaData> metaData1 = storageClient.getMetaData(storePath);
        logger.info("文件上传路径:{}\n文件信息:{}", storePath.getFullPath(), metaData1);
    }

}