package net.sudot.fdfs.proto.storage;

import net.sudot.fdfs.TestConstants;
import net.sudot.fdfs.domain.StorePath;
import net.sudot.fdfs.exception.FdfsServerException;
import net.sudot.fdfs.proto.StorageCommandTestBase;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * 文件上传命令测试
 * @author tobato
 * @author sudot on 2017-04-18 0018.
 */
public class StorageUploadFileCommandTest extends StorageCommandTestBase {

    /**
     * 文件上传测试
     */
    @Test
    public void testStorageUploadFileCommand() {
        String text = "非append模式";
        // 非append模式
        StorePath storePath = execStorageUploadFileCommand(TestConstants.CAT_IMAGE_FILE, false);
        // 文件续传
        ByteArrayInputStream stream = new ByteArrayInputStream(text.getBytes(TestConstants.DEFAULT_CHARSET));
        try {
            executeStoreCmd(new StorageAppendFileCommand(stream, stream.available(), storePath.getPath()));
        } catch (Exception e) {
            logger.info(e.getLocalizedMessage());
            Assert.assertTrue(e.getClass().getName(), e instanceof FdfsServerException);
            Assert.assertEquals(e.getLocalizedMessage(), e.getLocalizedMessage(), "错误码：22，错误信息：无效的参数");
        }
        executeStoreCmd(new StorageDeleteFileCommand(storePath.getGroup(), storePath.getPath()));
    }

    @Test
    public void testStorageUploadFileCommandByAppend() throws IOException {
        String text = "非append模式";
        // 非append模式
        StorePath storePath = execStorageUploadFileCommand(TestConstants.CAT_IMAGE_FILE, true);
        // 文件续传
        ByteArrayInputStream stream = new ByteArrayInputStream(text.getBytes(TestConstants.DEFAULT_CHARSET));
        executeStoreCmd(new StorageAppendFileCommand(stream, stream.available(), storePath.getPath()));
        executeStoreCmd(new StorageDeleteFileCommand(storePath.getGroup(), storePath.getPath()));
    }

}
