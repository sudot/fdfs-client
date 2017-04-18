package net.sudot.fdfs.service;

import net.sudot.fdfs.FastdfsTestBase;
import net.sudot.fdfs.TestConstants;
import net.sudot.fdfs.ThreadExecuteUtil;
import net.sudot.fdfs.domain.MateData;
import net.sudot.fdfs.domain.RandomTextFile;
import net.sudot.fdfs.domain.StorePath;
import net.sudot.fdfs.proto.storage.DownloadByteArray;
import net.sudot.fdfs.proto.storage.DownloadCallback;
import net.sudot.fdfs.util.IOUtils;
import org.junit.Test;

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
 * Update by sudot on 2017-04-18 0018.
 */
public class FastFileStorageClientTest extends FastdfsTestBase {

    /**
     * 上传文件，并且设置MateData
     */
    @Test
    public void testUploadFileAndMateData() {

        logger.debug("##上传文件..##");
        RandomTextFile file = new RandomTextFile();
        // Metadata
        Set<MateData> metaDataSet = createMateData();
        // 上传文件和Metadata
        StorePath path = storageClient.uploadFile(file.getInputStream(), file.getFileSize(), file.getFileExtName(),
                metaDataSet);
        logger.info("返回文件path={}", path);
        assertNotNull(path);
        logger.debug("上传文件路径{}", path);

        // 验证获取MataData
        logger.debug("##获取Metadata##");
        Set<MateData> fetchMateData = storageClient.getMetadata(path.getGroup(), path.getPath());
        assertEquals(fetchMateData, metaDataSet);

        logger.debug("##删除文件..##");
        storageClient.deleteFile(path.getGroup(), path.getPath());
    }

    /**
     * 不带MateData也应该能上传成功
     */
    @Test
    public void testUploadFileWithoutMateData() {

        logger.debug("##上传文件..##");
        RandomTextFile file = new RandomTextFile();
        // 上传文件和Metadata
        StorePath path = storageClient.uploadFile(file.getInputStream(), file.getFileSize(), file.getFileExtName(),
                null);
        assertNotNull(path);

        logger.debug("##删除文件..##");
        storageClient.deleteFile(path.getFullPath());
    }

    /**
     * 下载图片
     */
    @Test
    public void testDownload() {
        logger.debug("##下载文件..##");
        Object downloadFile = storageClient.downloadFile(TestConstants.DEFAULT_GROUP, "M00/00/00/wKgKgFi_oNuAIptWAAHYvQQn-YE828.jpg", new DownloadCallback<Object>() {
            @Override
            public Object recv(InputStream ins) throws IOException {

                logger.debug("##返回结果##");
                return IOUtils.copy(ins, new FileOutputStream("d://sss.jpg"));
            }
        });

        logger.debug("##是否为异步下载##" + downloadFile);
    }
    /**
     * 下载图片
     */
    @Test
    public void testDelete() {
        logger.debug("##删除文件..##");
        storageClient.deleteFile("group1/M00/00/00/wKgKgFjcj1aAR2JyAAHYvQQn-YE163_150x150.jpg");
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
