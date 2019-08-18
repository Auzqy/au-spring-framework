package top.auzqy.spring.beans.factory;

import com.sun.istack.internal.Nullable;

/**
 * description:  自定义 bean 存储的异常
 * createTime: 2019-08-17 12:32
 *
 * @author au
 */
public class AuBeanDefinitionStoreException extends RuntimeException {
    public AuBeanDefinitionStoreException(String msg) {
        super(msg);
    }

    public AuBeanDefinitionStoreException(@Nullable Throwable cause) {
        super(cause);
    }

    public AuBeanDefinitionStoreException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }

    @Override
    @Nullable
    public String getMessage() {
        return super.getMessage();
    }
}
