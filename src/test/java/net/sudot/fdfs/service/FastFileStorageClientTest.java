package net.sudot.fdfs.service;

import net.sudot.fdfs.FastdfsTestBase;
import net.sudot.fdfs.TestConstants;
import net.sudot.fdfs.TestUtils;
import net.sudot.fdfs.ThreadExecuteUtil;
import net.sudot.fdfs.domain.MateData;
import net.sudot.fdfs.domain.RandomTextFile;
import net.sudot.fdfs.domain.StorePath;
import net.sudot.fdfs.proto.storage.DownloadByteArray;
import net.sudot.fdfs.proto.storage.DownloadCallback;
import net.sudot.fdfs.util.FdfsUtil;
import net.sudot.fdfs.util.IOUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * FastFileStorageClient客户端
 * @author tobato
 */
public class FastFileStorageClientTest extends FastdfsTestBase {

    /** 日志 */
    protected static Logger LOGGER = LoggerFactory.getLogger(FastFileStorageClientTest.class);
    protected StorageClient storageClient = DefaultFdfsOptionsFactory.getStorageClient();

    /**
     * 上传文件，并且设置MateData
     */
    @Test
    public void testUploadFileAndMateData() {

        LOGGER.debug("##上传文件..##");
        RandomTextFile file = new RandomTextFile();
        // Metadata
        Set<MateData> metaDataSet = createMateData();
        // 上传文件和Metadata
        StorePath path = storageClient.uploadFile(file.getInputStream(), file.getFileSize(), file.getFileExtName(),
                metaDataSet);
        LOGGER.info("返回文件path={}", path);
        assertNotNull(path);
        LOGGER.debug("上传文件路径{}", path);

        // 验证获取MataData
        LOGGER.debug("##获取Metadata##");
        Set<MateData> fetchMateData = storageClient.getMetadata(path.getGroup(), path.getPath());
        assertEquals(fetchMateData, metaDataSet);

        LOGGER.debug("##删除文件..##");
        storageClient.deleteFile(path.getGroup(), path.getPath());
    }

    /**
     * 不带MateData也应该能上传成功
     */
    @Test
    public void testUploadFileWithoutMateData() {

        LOGGER.debug("##上传文件..##");
        RandomTextFile file = new RandomTextFile();
        // 上传文件和Metadata
        StorePath path = storageClient.uploadFile(file.getInputStream(), file.getFileSize(), file.getFileExtName(),
                null);
        assertNotNull(path);

        LOGGER.debug("##删除文件..##");
        storageClient.deleteFile(path.getFullPath());
    }

    /**
     * 上传图片，并且生成缩略图
     */
    @Test
    public void testUploadImageAndCrtThumbImage() throws IOException {
        LOGGER.debug("##上传文件..##");
        Set<MateData> metaDataSet = createMateData();
        StorePath path = uploadImageAndCrtThumbImage(TestConstants.PERFORM_FILE_PATH, metaDataSet);
        LOGGER.debug("上传文件路径{}", path);

        // 验证获取MataData
        LOGGER.debug("##获取Metadata##");
        Set<MateData> fetchMateData = storageClient.getMetadata(path.getGroup(), path.getPath());
        assertEquals(fetchMateData, metaDataSet);

        LOGGER.debug("##下载文件##");
        byte[] bytes = storageClient.downloadFile(path.getGroup(), path.getPath(), new DownloadByteArray());
//        IOUtils.write(bytes, new FileOutputStream("D:\\sss.jpg"));
        LOGGER.debug("##获取Metadata##");
        // 这里需要一个获取从文件名的能力，所以从文件名配置以后就最好不要改了
//        String slavePath = thumbImageConfig.getThumbImagePath(path.getPath());
//        // 或者由客户端再记录一下从文件的前缀
//        FileInfo slaveFile = storageClient.queryFileInfo(path.getGroup(), slavePath);
//        assertNotNull(slaveFile);
//        LOGGER.debug("##获取到从文件##{}", slaveFile);

    }

    /**
     * 下载图片
     */
    @Test
    public void testDownload() {
        LOGGER.debug("##下载文件..##");
        Object downloadFile = storageClient.downloadFile(TestConstants.DEFAULT_GROUP, "M00/00/00/wKgKgFi_oNuAIptWAAHYvQQn-YE828.jpg", new DownloadCallback<Object>() {
            @Override
            public Object recv(InputStream ins) throws IOException {

                LOGGER.debug("##返回结果##");
                return IOUtils.copy(ins, new FileOutputStream("d://sss.jpg"));
            }
        });

        LOGGER.debug("##是否为异步下载##" + downloadFile);
    }
    /**
     * 下载图片
     */
    @Test
    public void testDelete() {
        LOGGER.debug("##删除文件..##");
        storageClient.deleteFile("group1/M00/00/00/wKgKgFjcj1aAR2JyAAHYvQQn-YE163_150x150.jpg");
    }

    /**
     * 上传文件
     * @param filePath
     * @return
     */
    private StorePath uploadImageAndCrtThumbImage(String filePath, Set<MateData> metaDataSet) {
        InputStream in = null;
        File file = TestUtils.getFile(filePath);
        String fileExtName = FdfsUtil.getExtension(file.getName());
        long fileSize = file.length();
        try {
            in = TestUtils.getFileInputStream(filePath);
//            return storageClient.uploadImageAndCrtThumbImage(in, fileSize, fileExtName, metaDataSet);
            return null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;

    }

    /**
     * 并发下载
     * @return
     */
    @Test
    public void downloadThread() {
        final AtomicInteger failCount = new AtomicInteger(0);
        final AtomicInteger count = new AtomicInteger(0);
        int total = 100;
        for (int i = 0; i < total; i ++) {
            ThreadExecuteUtil.execute(new Runnable() {
                @Override
                public void run() {
                    for (int n = 20; n > 0; n --) {
                        try {
                            storageClient.downloadFile("group1/M00/00/00/wKgKgFjcn-uAN0KsAAh0KA_A1Y0095.pdf", new DownloadByteArray());
                        } catch (Exception e) {
                            failCount.getAndDecrement();
                            e.printStackTrace();
                        } finally {
                            count.getAndDecrement();
                        }
                    }
                }
            });
        }
        while (count.get() < total) {
            try {
                Thread.sleep(1000L * 10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        ThreadExecuteUtil.destroy();
        System.out.println("success count: " + count.get());
        System.out.println("fail count: " + failCount.get());
    }

    private Set<MateData> createMateData() {
        Set<MateData> metaDataSet = new HashSet<MateData>();
        metaDataSet.add(new MateData("Author", "wyf"));
        metaDataSet.add(new MateData("CreateDate", "2016-01-05"));
        return metaDataSet;
    }

}
