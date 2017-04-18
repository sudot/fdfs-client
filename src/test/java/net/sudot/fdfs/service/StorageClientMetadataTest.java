package net.sudot.fdfs.service;

import net.sudot.fdfs.FastdfsTestBase;
import net.sudot.fdfs.TestConstants;
import net.sudot.fdfs.domain.MateData;
import net.sudot.fdfs.domain.RandomTextFile;
import net.sudot.fdfs.domain.StorePath;
import net.sudot.fdfs.exception.FdfsServerException;
import net.sudot.fdfs.proto.ErrorCodeConstants;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Metadata操作演示
 * @author tobato
 * Update by sudot on 2017-04-18 0018.
 */
public class StorageClientMetadataTest extends FastdfsTestBase {

    @Test
    public void testMetadataOperator() {
        logger.debug("##上传文件..##");
        RandomTextFile file = new RandomTextFile();
        StorePath path = storageClient.uploadFile(TestConstants.DEFAULT_GROUP, file.getInputStream(),
                file.getFileSize(), file.getFileExtName());
        assertNotNull(path);
        logger.debug("上传文件 result={}", path);

        logger.debug("##生成Metadata##");
        Set<MateData> firstMateData = new HashSet<MateData>();
        firstMateData.add(new MateData("Author", "wyf"));
        firstMateData.add(new MateData("CreateDate", "2016-01-05"));
        storageClient.overwriteMetadata(path.getGroup(), path.getPath(), firstMateData);

        logger.debug("##获取Metadata##");
        Set<MateData> fetchMateData = storageClient.getMetadata(path.getGroup(), path.getPath());
        assertEquals(fetchMateData, firstMateData);

        logger.debug("##合并Metadata##");
        Set<MateData> secendMateData = new HashSet<MateData>();
        secendMateData.add(new MateData("Author", "tobato"));
        secendMateData.add(new MateData("CreateDate", "2016-01-05"));
        storageClient.mergeMetadata(path.getGroup(), path.getPath(), secendMateData);

        logger.debug("##第二次获取Metadata##");
        fetchMateData = storageClient.getMetadata(path.getGroup(), path.getPath());
        assertEquals(fetchMateData, secendMateData);

        logger.debug("##删除主文件..##");
        storageClient.deleteFile(path.getGroup(), path.getPath());

        logger.debug("##第三次获取Metadata##");
        try {
            fetchMateData = storageClient.getMetadata(path.getGroup(), path.getPath());
            fail("No exception thrown.");
        } catch (Exception e) {
            assertTrue(e instanceof FdfsServerException);
            assertTrue(((FdfsServerException) e).getErrorCode() == ErrorCodeConstants.ERR_NO_ENOENT);
        }
        logger.debug("文件删除以后Metadata会自动删除，第三次就获取不到了");
    }

}
