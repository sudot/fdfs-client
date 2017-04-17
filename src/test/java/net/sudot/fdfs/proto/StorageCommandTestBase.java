package net.sudot.fdfs.proto;

import net.sudot.fdfs.TestConstants;
import net.sudot.fdfs.TestUtils;
import net.sudot.fdfs.domain.StorePath;
import net.sudot.fdfs.proto.storage.StorageUploadFileCommand;
import net.sudot.fdfs.util.FdfsUtil;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertNotNull;

/**
 * command测试基类
 * @author tobato
 */
public abstract class StorageCommandTestBase extends CommandTestBase {

    /**
     * 文件上传操作
     * @param isAppenderFile
     */
    public StorePath execStorageUploadFileCommand(String filePath, boolean isAppenderFile) {
        InputStream in = null;
        File file = TestUtils.getFile(filePath);
        String fileExtName = FdfsUtil.getExtension(file.getName());
        long fileSize = file.length();

        try {
            in = TestUtils.getFileInputStream(filePath);
            return uploadInputStream(in, fileExtName, fileSize, isAppenderFile);
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
     * 获取传输文件第一个部分
     * @param text
     * @return
     * @throws IOException
     */
    protected InputStream getTextInputStream(String text) throws IOException {
        // 将String转换为InputStream
        return new ByteArrayInputStream(text.getBytes(TestConstants.DEFAULT_CHARSET));
    }

    /**
     * 上传默认文件
     * @return
     */
    protected StorePath uploadDefaultFile() {
        // 上传文件
        return execStorageUploadFileCommand(TestConstants.CAT_IMAGE_FILE, false);
    }

    /**
     * 文件上传操作
     * @param isAppenderFile
     */
    protected StorePath uploadInputStream(InputStream in, String fileExtName, long fileSize, boolean isAppenderFile) {
        byte storeIndex = 0;
        StorageUploadFileCommand command = new StorageUploadFileCommand(storeIndex, in, fileExtName, fileSize,
                isAppenderFile);
        StorePath path = executeStoreCmd(command);
        assertNotNull(path);
        logger.debug("isAppenderFile={}-----文件上传处理结果-----", false);
        logger.debug(path.toString());
        return path;
    }

}
