package net.sudot.fdfs.exception;

/**
 * 命令执行异常
 * Created by tangjialin on 2017-04-17 0017.
 */
public class FdfsExecuteException extends FdfsException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param message
     */
    public FdfsExecuteException(String message, Throwable t) {
        super(message, t);
    }

}
