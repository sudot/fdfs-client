package net.sudot.fdfs.proto.storage;


import net.sudot.fdfs.util.IOUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * 下载为byte流
 * @author tobato
 */
public class DownloadByteArray implements DownloadCallback<byte[]> {

    @Override
    public byte[] recv(InputStream ins) throws IOException {
        byte[] bytes = IOUtils.toByteArray(ins);
        IOUtils.closeQuietly(ins);
        return bytes;
    }
}
