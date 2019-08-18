package top.auzqy.spring.beans;

import com.sun.istack.internal.Nullable;

/**
 * description: 这是一个运行时异常，bean 异常通常是致命的，我们没有理由为他们检查
 *
 * <p>Note that this is a runtime (unchecked) exception. Beans exceptions
 * are usually fatal; there is no reason for them to be checked.
 * ———— from spring
 *
 * createTime: 2019-08-16 11:54
 * @author au
 */
public class AuBeansException extends RuntimeException {

    /**
     * Create a new BeansException with the specified message.
     * @param msg the detail message
     */
    public AuBeansException(String msg) {
        super(msg);
    }


    public AuBeansException(Throwable cause) {
        super(cause);
    }

    /**
     * Create a new BeansException with the specified message
     * and root cause.
     * @param msg the detail message
     * @param cause the root cause
     */
    public AuBeansException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }

    @Override
    @Nullable
    public String getMessage() {
        return super.getMessage();
    }
}
