package net.sudot.fdfs;

import net.sudot.fdfs.domain.StorePath;
import net.sudot.fdfs.service.AppendFileStorageClient;
import net.sudot.fdfs.util.IOUtils;
import org.junit.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 性能测试
 * @author tobato
 */
public class PerformanceTest extends FastdfsTestBase {

    @Resource
    protected AppendFileStorageClient storageClient;

    @Test
    public void testPerformance() {
        final AtomicInteger failCount = new AtomicInteger(0);
        final AtomicInteger count = new AtomicInteger(0);
        int totalCount = 1000;
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(100);
        executor.afterPropertiesSet();
        for (int i = 0; i < totalCount; i++) {
            executor.execute(new Runnable() {

                @Override
                public void run() {
                    FileInputStream inputStream = null;
                    try {
                        File file = new File(TestConstants.PERFORM_FILE_PATH);
                        inputStream = new FileInputStream(file);
                        StorePath storePath = storageClient.uploadFile(null, inputStream, file.length(), "jpg");
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
                return;
            }
        }
        executor.destroy();
        System.out.println("success count: " + count.get());
        System.out.println("fail count: " + failCount.get());
    }

}
