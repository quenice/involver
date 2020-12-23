package org.quenice.involver.exception;

/**
 * 编码解码异常
 * @author damon.qiu 2020/10/28 7:47 PM
 */
public class CodecException extends InvolverException {
    public CodecException(Throwable e) {
        super(e);
    }

    public CodecException(String msg) {
        super(msg);
    }

    public CodecException(String msg, Throwable e) {
        super(msg, e);
    }
}
