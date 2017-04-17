package net.sudot.fdfs.proto;

import net.sudot.fdfs.proto.mapper.FdfsParamMapper;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.nio.charset.Charset;

/**
 * Fdfs交易应答基类
 * @author tobato
 */
public abstract class FdfsResponse<T> {
    /** 返回值类型 */
    protected final Class<T> genericType;
    /** 报文头 */
    protected ProtoHead head;

    /**
     * 构造函数
     */
    @SuppressWarnings("unchecked")
    public FdfsResponse() {
        super();
        Type type = getClass().getGenericSuperclass();
        while (type.getClass() != Class.class) {
            System.out.println(type.getClass());
            if (type instanceof ParameterizedType) {
                type = ((ParameterizedType) type).getActualTypeArguments()[0];
            } else if (type instanceof TypeVariable) {
                type = Object.class;
            } else if (type instanceof WildcardType) {
                throw new IllegalArgumentException("未实现的反射类型(WildcardType):" + getClass().getName());
            } else if (type instanceof GenericArrayType) {
                throw new IllegalArgumentException("未实现的反射类型(GenericArrayType):" + getClass().getName());
            }
        }
        this.genericType = (Class<T>) type;
    }

    /** 获取报文长度 */
    protected long getContentLength() {
        return head.getContentLength();
    }

    /**
     * 解析反馈结果,head已经被解析过
     * @param head
     * @param in
     * @param charset
     * @return
     * @throws IOException
     */
    public T decode(ProtoHead head, InputStream in, Charset charset) throws IOException {
        this.head = head;
        return decodeContent(in, charset);
    }

    /**
     * 解析反馈内容
     * @param in
     * @param charset
     * @return
     * @throws IOException
     */
    public T decodeContent(InputStream in, Charset charset) throws IOException {
        // 如果有内容
        if (getContentLength() > 0) {
            byte[] bytes = new byte[(int) getContentLength()];
            int contentSize = in.read(bytes);
            // 获取数据
            if (contentSize != getContentLength()) {
                throw new IOException("读取到的数据长度与协议长度不符");
            }
            return FdfsParamMapper.map(bytes, genericType, charset);
        }
        return null;
    }

}
