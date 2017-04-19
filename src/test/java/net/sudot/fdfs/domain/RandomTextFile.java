package net.sudot.fdfs.domain;

import net.sudot.fdfs.TestConstants;
import net.sudot.fdfs.TestUtils;

import java.io.InputStream;
import java.util.Random;

/**
 * 测试用随机字符文件
 * @author tobato
 */
public class RandomTextFile {

    private String text;

    private InputStream inputStream;

    private long fileSize;

    private String fileExtName = "text";

    public RandomTextFile() {
        String temple = "762830abdcefghijklmnopqrstuvwxyz0991822-";
        int len = temple.length();
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 30; i ++) {
            int nextInt = 0;
            while ((nextInt = random.nextInt()) < 0);
            builder.append(temple.charAt(nextInt % len));
        }
        this.text = builder.toString();
        this.fileSize = TestUtils.getTextLength(text);
    }

    public RandomTextFile(String text) {
        this.text = text;
        this.fileSize = TestUtils.getTextLength(text);
    }

    public String getText() {
        return text;
    }

    public InputStream getInputStream() {
        this.inputStream = TestUtils.getTextInputStream(text);
        return inputStream;
    }

    public long getFileSize() {
        return fileSize;
    }

    public String getFileExtName() {
        return fileExtName;
    }

    public byte[] toByte() {
        return this.text.getBytes(TestConstants.DEFAULT_CHARSET);
    }

}
