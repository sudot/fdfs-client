package net.sudot.fdfs.proto.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * param对象与byte映射器
 * @author tobato
 * Update by sudot on 2017-04-18 0018.
 */
public class FdfsParamMapper {

    /** 对象映射缓存 */
    private static Map<String, ObjectMateData> mapCache = new HashMap<String, ObjectMateData>();
    /** 类属性映射缓存 */
    private static Map<String, Map<String, Field>> fieldCache = new HashMap<String, Map<String, Field>>();
    /** 日志 */
    private static Logger LOGGER = LoggerFactory.getLogger(FdfsParamMapper.class);

    private FdfsParamMapper() {
        // hide for utils
    }

    /**
     * 将byte解码为对象
     * @param content
     * @param genericType
     * @param charset
     * @param <T>
     * @return
     */
    public static <T> T map(byte[] content, Class<T> genericType, Charset charset) {
        // 获取映射对象
        ObjectMateData objectMap = getObjectMap(genericType);
        if (LOGGER.isDebugEnabled()) {
            objectMap.dumpObjectMateData();
        }

        try {
            return mapByIndex(content, genericType, objectMap, charset);
        } catch (InstantiationException e) {
            throw new FdfsColumnMapException(e);
        } catch (IllegalAccessException e) {
            throw new FdfsColumnMapException(e);
        } catch (NoSuchFieldException e) {
            throw new FdfsColumnMapException(e);
        }
    }

    /**
     * 获取对象映射定义
     * @param genericType
     * @return
     */
    public static <T> ObjectMateData getObjectMap(Class<T> genericType) {
        ObjectMateData objectMateData = mapCache.get(genericType.getName());
        if (null == objectMateData) {
            // 还未缓存过
            objectMateData = new ObjectMateData(genericType);
            mapCache.put(genericType.getName(), objectMateData);
        }
        return objectMateData;
    }

    /**
     * 获取类属性定义
     * @param genericType 需要获取的属性类
     * @param <T>         类泛型
     * @return 返回该类的属性定义集合
     */
    private static <T> Map<String, Field> getFieldMap(Class<T> genericType) {
        String genericTypeName = genericType.getName();
        Map<String, Field> stringFieldMap = fieldCache.get(genericTypeName);
        if (stringFieldMap == null) {
            Field[] declaredFields = genericType.getDeclaredFields();
            stringFieldMap = new HashMap<>(declaredFields.length);
            for (Field field : declaredFields) {
                field.setAccessible(true);
                stringFieldMap.put(field.getName(), field);
            }
            fieldCache.put(genericTypeName, stringFieldMap);
        }
        return stringFieldMap;
    }

    /**
     * 按列顺序映射
     * @param content
     * @param genericType
     * @param objectMap
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws NoSuchFieldException
     */
    private static <T> T mapByIndex(byte[] content, Class<T> genericType, ObjectMateData objectMap, Charset charset)
            throws IllegalAccessException, InstantiationException, NoSuchFieldException {

        if (genericType == Void.class) { throw new IllegalAccessException("genericType不能为Void"); }
        List<FieldMateData> mappingFields = objectMap.getFieldList();
        Map<String, Field> fieldMap = getFieldMap(genericType);
        T obj = genericType.newInstance();
        for (FieldMateData field : mappingFields) {
            Object value = field.getValue(content, charset);
            // 设置属性值
            if (LOGGER.isDebugEnabled()) { LOGGER.debug("设置值是 {} {}", field, value); }
            Field declaredField = fieldMap.get(field.getFieldName());
            declaredField.set(obj, value);
//            BeanUtils.setProperty(obj, field.getFieldName(), value);
        }
        return obj;
    }

    /**
     * 序列化为Byte
     * @param object
     * @param charset
     * @return
     */
    public static byte[] toByte(Object object, Charset charset) {
        ObjectMateData objectMap = getObjectMap(object.getClass());
        try {
            return convertFieldToByte(objectMap, object, charset);
        } catch (NoSuchMethodException ie) {
            LOGGER.debug("Cannot invoke get methed: ", ie);
            throw new FdfsColumnMapException(ie);
        } catch (IllegalAccessException iae) {
            LOGGER.debug("Illegal access: ", iae);
            throw new FdfsColumnMapException(iae);
        } catch (InvocationTargetException ite) {
            LOGGER.debug("Cannot invoke method: ", ite);
            throw new FdfsColumnMapException(ite);
        }

    }

    /**
     * 将属性转换为byte
     * @param objectMap
     * @param object
     * @param charset
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    private static byte[] convertFieldToByte(ObjectMateData objectMap, Object object, Charset charset)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        List<FieldMateData> mappingFields = objectMap.getFieldList();
        // 获取报文长度 (固定长度+动态长度)
        int size = objectMap.getFieldsSendTotalByteSize(object, charset);
        byte[] result = new byte[size];
        int offsize = 0;
        for (FieldMateData field : mappingFields) {
            byte[] fieldByte = field.toByte(object, charset);
            if (null != fieldByte) {
                System.arraycopy(fieldByte, 0, result, offsize, fieldByte.length);
                offsize += fieldByte.length;
            }
        }
        return result;
    }

}