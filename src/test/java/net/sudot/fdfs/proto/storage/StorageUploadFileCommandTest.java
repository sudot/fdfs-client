package net.sudot.fdfs.proto.storage;

import net.sudot.fdfs.TestConstants;
import net.sudot.fdfs.domain.StorePath;
import net.sudot.fdfs.proto.StorageCommandTestBase;
import org.junit.Test;

/**
 * 文件上传命令测试
 * @author tobato
 * Update by sudot on 2017-04-18 0018.
 */
public class StorageUploadFileCommandTest extends StorageCommandTestBase {

    /**
     * 文件上传测试
     */
    @Test
    public void testStorageUploadFileCommand() {
        // 非append模式
        StorePath storePath = execStorageUploadFileCommand(TestConstants.CAT_IMAGE_FILE, false);
        executeStoreCmd(new StorageDeleteFileCommand(storePath.getGroup(), storePath.getPath()));
    }

    @Test
    public void testStorageUploadFileCommandByAppend() {
        // append模式
        StorePath storePath = execStorageUploadFileCommand(TestConstants.CAT_IMAGE_FILE, true);
        executeStoreCmd(new StorageDeleteFileCommand(storePath.getGroup(), storePath.getPath()));
    }

}
