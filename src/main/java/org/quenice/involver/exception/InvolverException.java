package org.quenice.involver.exception;

/**
 * @author damon.qiu 2020/10/27 3:28 PM
 */
public class InvolverException extends RuntimeException {

    public InvolverException(Throwable e) {
        super(e);
    }
    public InvolverException(String msg) {
        super(msg);
    }

    public InvolverException(String msg, Throwable e) {
        super(msg, e);
    }
}
