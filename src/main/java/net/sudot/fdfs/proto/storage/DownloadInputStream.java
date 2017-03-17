package net.sudot.fdfs.proto.storage;

import java.io.IOException;
import java.io.InputStream;

/**
 * 下载为输入流
 * Created by tangjialin on 2017-03-17 0017.
 */
public class DownloadInputStream implements DownloadCallback<InputStream> {

    @Override
    public InputStream recv(InputStream ins) throws IOException {
        return ins;
    }
}
