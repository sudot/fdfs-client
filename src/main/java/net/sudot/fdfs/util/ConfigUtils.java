package net.sudot.fdfs.util;

import net.sudot.fdfs.exception.FdfsIOException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * 系统配置参数读取工具
 * @author tangjialin on 2016-04-05 0005.
 */
public class ConfigUtils {
	private static Properties properties;

	/**
	 * 初始化接口地址
	 */
	static {
		URL url = ConfigUtils.class.getClassLoader().getResource("");
		File resourceDir = new File(url.getPath());
		FilenameFilter filenameFilter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				// 只加载以此字符串结尾的文件
				return name.endsWith(".properties") && name.contains("fastdfs");
			}
		};
		File[] properties = resourceDir.listFiles(filenameFilter);
		ConfigUtils.properties = new Properties();
		for (File file : properties) {
			loadProperties(ConfigUtils.properties, file);
		}
	}

	/**
	 * 获取配置
	 * @param key 配置键值
	 * @return
	 */
	public static String getConfigValue(String key) {
		return properties.getProperty(key);
	}

	/**
	 * 获取配置
	 * @param key 配置键值
	 * @return
	 */
	public static String getConfigValue(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}

	/**
	 * 加载Properties
	 * @param file 需要加载的Properties文件
	 * @return 返回加载后的Properties文件, 加载失败返回null
	 */
	public static Properties loadProperties(File file) {
		InputStream io = null;
		try {
			io = new FileInputStream(file);
			Properties properties = new Properties();
			properties.load(io);
			return properties;
		} catch (IOException e) {
			throw new FdfsIOException(e);
		} finally {
			IOUtils.closeQuietly(io);
		}
	}

	/**
	 * 加载Properties内容到指定Properties实例
	 * @param properties Properties实例,用于存放加载内容
	 * @param file       需要加载的Properties文件
	 */
	public static void loadProperties(Properties properties, File file) {
		InputStream io = null;
		try {
			io = new FileInputStream(file);
			properties.load(io);
		} catch (IOException e) {
			throw new FdfsIOException(e);
		} finally {
			IOUtils.closeQuietly(io);
		}
	}

}