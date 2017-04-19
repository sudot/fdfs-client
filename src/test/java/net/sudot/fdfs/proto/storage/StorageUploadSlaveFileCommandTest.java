package net.sudot.fdfs.proto.storage;

import net.sudot.fdfs.TestConstants;
import net.sudot.fdfs.TestUtils;
import net.sudot.fdfs.domain.StorePath;
import net.sudot.fdfs.proto.StorageCommandTestBase;
import net.sudot.fdfs.util.FdfsUtil;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件上传命令测试
 * @author tobato
 * @author sudot on 2017-04-18 0018.
 */
public class StorageUploadSlaveFileCommandTest extends StorageCommandTestBase {

    /**
     * 文件上传测试
     */
    @Test
    public void testStorageSlaveUploadFileCommand() {
        // 上传主文件
        StorePath path = execStorageUploadFileCommand(TestConstants.CAT_IMAGE_FILE, false);
        // 生成从文件
        StorePath storePath = null;
        try {
            String prefixName = "_120x120";
            File file = TestUtils.getFile(TestConstants.CAT_IMAGE_FILE);
            String fileExtName = FdfsUtil.getExtension(file.getName());
            InputStream in = getThumbImageStream(file, true);
            long fileSize = in.available();
            storePath = executeStoreCmd(new StorageUploadSlaveFileCommand(in, fileSize, path.getPath(), prefixName, fileExtName));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 清理文件
        executeStoreCmd(new StorageDeleteFileCommand(path.getGroup(), path.getPath()));
        executeStoreCmd(new StorageDeleteFileCommand(storePath.getGroup(), storePath.getPath()));
    }

    /**
     * 根据图片路径生成缩略图
     * @param imgFile 原图片路径
     * @param force   是否强制按照宽高生成缩略图(如果为false，则生成最佳比例缩略图)
     */
    public InputStream getThumbImageStream(File imgFile, boolean force) throws IOException {
        int w = 120;
        int h = 120;
        // ImageIO 支持的图片类型 : [BMP, bmp, jpg, JPG, wbmp, jpeg, png, PNG, JPEG, WBMP, GIF, gif]
        // 获取图片后缀
        String fileExtName = FdfsUtil.getExtension(imgFile.getName());
        logger.debug("target image's size, width:{}, height:{}.",w,h);
        Image img = ImageIO.read(imgFile);
        if (!force) {
            // 根据原图与要求的缩略图比例，找到最合适的缩略图比例
            int width = img.getWidth(null);
            int height = img.getHeight(null);
            if ((width * 1.0) / w < (height * 1.0) / h) {
                if (width > w) {
                    h = Integer.parseInt(new java.text.DecimalFormat("0").format(height * w / (width * 1.0)));
                    logger.debug("change image's height, width:{}, height:{}.", w, h);
                }
            } else {
                if (height > h) {
                    w = Integer.parseInt(new java.text.DecimalFormat("0").format(width * h / (height * 1.0)));
                    logger.debug("change image's width, width:{}, height:{}.", w, h);
                }
            }
        }
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics g = bi.getGraphics();
        g.drawImage(img, 0, 0, w, h, Color.LIGHT_GRAY, null);
        g.dispose();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(bi, fileExtName, out);
        return new ByteArrayInputStream(out.toByteArray());
    }
}
