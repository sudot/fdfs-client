package net.sudot.fdfs.domain;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * 缩略图配置参数
 * @author tobato
 */
@Component
public class DefaultThumbImageConfig implements ThumbImageConfig {

    private static String cachedPrefixName;
    private int width;
    private int height;

    /**
     * 生成前缀如:_150x150
     */
    @Override
    public String getPrefixName() {
        if (cachedPrefixName == null) {
            StringBuilder buffer = new StringBuilder();
            buffer.append("_").append(width).append("x").append(height);
            cachedPrefixName = new String(buffer);
        }
        return cachedPrefixName;
    }

    /**
     * 根据文件名获取缩略图路径
     */
    @Override
    public String getThumbImagePath(String masterFilename) {
        Validate.notBlank(masterFilename, "主文件不能为空");
        StringBuilder buff = new StringBuilder(masterFilename);
        int index = buff.lastIndexOf(".");
        buff.insert(index, getPrefixName());
        return buff.toString();
    }

    public int getWidth() {
        return width;
    }

    @Value("${fdfs.thumbImage.width}")
    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    @Value("${fdfs.thumbImage.height}")
    public void setHeight(int height) {
        this.height = height;
    }

}
