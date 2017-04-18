package net.sudot.fdfs.proto.storage;

import net.sudot.fdfs.TestConstants;
import net.sudot.fdfs.domain.StorePath;
import net.sudot.fdfs.proto.StorageCommandTestBase;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * 文件修改命令
 * Created by tangjialin on 2017-04-18 0018.
 */
public class StorageModifyCommandTest extends StorageCommandTestBase {

    @Test
    public void testStorageModifyCommand() throws IOException {
        String original = "这是一段测试内容";
        String modify = "修改文字";
        // 结果:修改文字测试内容
        String result1 = modify(original, modify, modify.getBytes(TestConstants.DEFAULT_CHARSET).length, 0);
//        // 结果:异常
//        modify(original, modify, 0, 0);
        // 结果:这是一段修改文字
        String result2 = modify(original, modify, modify.getBytes(TestConstants.DEFAULT_CHARSET).length, "这是一段".getBytes(TestConstants.DEFAULT_CHARSET).length);
        // 结果:这是修改测试内容
        String result3 = modify(original, modify, "一段".getBytes(TestConstants.DEFAULT_CHARSET).length, "这是".getBytes(TestConstants.DEFAULT_CHARSET).length);
//        // 结果:异常
//        modify(original, modify, original.getBytes(TestConstants.DEFAULT_CHARSET).length, 0);
        logger.debug("测试结果:\n原始内容:{}\n修改内容:{}\n{}\n{}\n{}\n{}", original, modify, result1, result2, result3);
    }

    /**
     * 返回修改后的文件内容
     * @param original   文件初始化内容
     * @param modify     修改的内容
     * @param modifySize 修改的范围
     * @param fileOffset 修改的起始位置
     * @return
     */
    private String modify(String original, String modify, long modifySize, long fileOffset) throws IOException {
        InputStream firstIn = getTextInputStream(original);
        int fileSize = firstIn.available();
        // 上载文字
        logger.debug("上传文字长度:{}", fileSize);
        StorePath path = uploadInputStream(firstIn, "txt", fileSize, true);
        // 文件修改
        InputStream modifyIn = getTextInputStream(modify);
        logger.debug("修改文字长度:{}", modifySize);
        executeStoreCmd(new StorageModifyCommand(path.getPath(), modifyIn, modifySize, fileOffset));
        logger.debug("--文件修改处理成功--");
        byte[] bytes = executeStoreCmd(new StorageDownloadCommand<byte[]>(path.getGroup(), path.getPath(), new DownloadByteArray()));
        String context = new String(bytes);
        executeStoreCmd(new StorageDeleteFileCommand(path.getGroup(), path.getPath()));
        return context;
    }
}
