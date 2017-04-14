package net.sudot.fdfs.conn;

/**
 * 表示文件Web服务器对象
 *
 * <pre>
 * 由Nginx服务器承担此角色，通常配置以后就不会再改变
 * </pre>
 * @author tobato
 */
public class FdfsWebServer {

    private String webServerUrl;

    public String getWebServerUrl() {
        return webServerUrl;
    }

    public void setWebServerUrl(String webServerUrl) {
        this.webServerUrl = webServerUrl;
    }

}
