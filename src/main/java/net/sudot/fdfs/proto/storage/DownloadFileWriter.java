package net.sudot.fdfs.proto.storage;

import net.sudot.fdfs.util.IOUtils;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件下载回调方法
 * @author tobato
 */
public class DownloadFileWriter implements DownloadCallback<String> {

    /**
     * 文件名称
     */
    private String fileName;

    public DownloadFileWriter(String fileName) {
        this.fileName = fileName;
    }

    /**
     * 文件接收处理
     */
    @Override
    public String recv(InputStream ins) throws IOException {
        FileOutputStream out = null;
        InputStream in = null;
        try {
            out = new FileOutputStream(fileName);
            in = new BufferedInputStream(ins);
            // 通过IOUtils对接输入输出流，实现文件下载
            IOUtils.copy(in, out);
            out.flush();
        } catch (IOException e) {
            throw e;
        } finally {
            // 关闭流
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
        return fileName;
    }

}
