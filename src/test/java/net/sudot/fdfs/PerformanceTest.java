package net.sudot.fdfs;

import net.sudot.fdfs.domain.StorePath;
import net.sudot.fdfs.util.IOUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 性能测试
 * @author tobato
 * @author sudot on 2017-04-18 0018.
 */
public class PerformanceTest extends FastdfsTestBase {
    @Test
    public void testPerformance() {
        final AtomicInteger failCount = new AtomicInteger(0);
        final AtomicInteger count = new AtomicInteger(0);
        int totalCount = 5;
        for (int i = 0; i < totalCount; i++) {
            ThreadExecuteUtil.execute(new Runnable() {

                @Override
                public void run() {
                    FileInputStream inputStream = null;
                    try {
                        File file = TestUtils.getFile(TestConstants.BIG_FILE);
                        inputStream = new FileInputStream(file);
                        StorePath storePath = storageClient.uploadFile(null, inputStream, file.length(), TestUtils.getFileExtName(file.getName()));
                        logger.info("{}", storePath);
                        storageClient.deleteFile(storePath);
                    } catch (Exception e) {
                        e.printStackTrace();
                        failCount.incrementAndGet();
                    } finally {
                        IOUtils.closeQuietly(inputStream);
                        count.incrementAndGet();
                    }
                }
            });

        }
        while (count.get() < totalCount) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        ThreadExecuteUtil.destroy();
        System.out.println("success count: " + count.get());
        System.out.println("fail count: " + failCount.get());
    }

    /**
     * 测试服务器停机重启连接重连
     */
    @Test
    public void testReconnect() {
        while (true) {
            FileInputStream inputStream = null;
            try {
                File file = TestUtils.getFile(TestConstants.BIG_FILE);
                inputStream = new FileInputStream(file);
                StorePath storePath = storageClient.uploadFile(null, inputStream, file.length(), TestUtils.getFileExtName(file.getName()));
                logger.info("{}", storePath);
                storageClient.deleteFile(storePath);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(inputStream);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
