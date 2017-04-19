package net.sudot.fdfs.proto.storage;

import net.sudot.fdfs.TestConstants;
import net.sudot.fdfs.domain.StorePath;
import net.sudot.fdfs.exception.FdfsIOException;
import net.sudot.fdfs.proto.StorageCommandTestBase;
import org.junit.Test;

import java.io.InputStream;

/**
 * 文件修改命令
 * @author sudot on 2017-04-18 0018.
 */
public class StorageModifyCommandTest extends StorageCommandTestBase {

    @Test
    public void testStorageModifyCommand() throws Exception {
        String original = "这是一段测试内容";
        String modify = "修改文字";
        // 测试修改内容和上传内容一致,结果:修改文字测试内容
        String result1 = modify(original, modify, modify.getBytes(TestConstants.DEFAULT_CHARSET).length, 0);
        // 测试修改起始位置变化,结果:这是一段修改文字
        String result2 = modify(original, modify, modify.getBytes(TestConstants.DEFAULT_CHARSET).length, "这是一段".getBytes(TestConstants.DEFAULT_CHARSET).length);
        // 测试修改内容比上传文件小.结果:这是修改测试内容
        String result3 = modify(original, modify, "一段".getBytes(TestConstants.DEFAULT_CHARSET).length, "这是".getBytes(TestConstants.DEFAULT_CHARSET).length);
        // 测试修改内容和起始位置都为0,结果:异常
        String result4 = modify(original, modify, 0, 0);
        // 测试修改内容比上传文件大,结果:异常
        String result5 = modify(original, modify, original.getBytes(TestConstants.DEFAULT_CHARSET).length, 0);
        logger.debug("测试结果:\n原始内容:{}\n修改内容:{}\n{}\n{}\n{}\n{}\n{}", original, modify, result1, result2, result3, result4, result5);
    }

    /**
     * 返回修改后的文件内容
     * @param original   文件初始化内容
     * @param modify     修改的内容
     * @param modifySize 修改的范围
     * @param modifyOffset 修改的起始位置
     * @return
     */
    private String modify(String original, String modify, long modifySize, long modifyOffset) throws Exception {
        InputStream firstIn = getTextInputStream(original);
        int fileSize = firstIn.available();
        // 上载文字
        logger.debug("上传文字长度:{}", fileSize);
        StorePath path = uploadInputStream(firstIn, "txt", fileSize, true);
        // 文件修改
        InputStream modifyIn = getTextInputStream(modify);
        logger.debug("修改文字长度:{}", modifySize);
        try {
            executeStoreCmd(new StorageModifyCommand(path.getPath(), modifyIn, modifySize, modifyOffset));
            logger.debug("--文件修改处理成功--");
            byte[] bytes = executeStoreCmd(new StorageDownloadCommand<byte[]>(path.getGroup(), path.getPath(), new DownloadByteArray()));
            String context = new String(bytes);
            return context;
        } catch (FdfsIOException | IllegalArgumentException e) {
            return e.getLocalizedMessage();
        } catch (Exception e) {
            throw e;
        } finally {
            executeStoreCmd(new StorageDeleteFileCommand(path.getGroup(), path.getPath()));
        }
    }
}
