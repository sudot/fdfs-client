package net.sudot.fdfs.proto.storage;

///**
// * 下载为输入流
// * 多线程并发下会导致异常:
// * socket io exception occured while receive content
// * Caused by: java.io.IOException: recv cmd: [num] is not correct, expect cmd: [num]
// * @author sudot on 2017-03-17 0017.
// */
//public class DownloadInputStream implements DownloadCallback<InputStream> {
//
//    @Override
//    public InputStream recv(InputStream ins) throws IOException {
//        return ins;
//    }
//}
