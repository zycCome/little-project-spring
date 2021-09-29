package com.ruyuan.little.project.spring.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:little@163.com">little</a>
 * version: 1.0
 * Description:spring实战
 **/
public class CloseableUtils {
    /**
     * 日志管理组件
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CloseableUtils.class);

    /**
     * 关闭资源
     * @param closeable 需要关闭的资源
     */
    public static void close(AutoCloseable closeable){
        if (closeable != null){
            try {
                closeable.close();
            } catch (Exception e) {
                LOGGER.error("资源关闭异常",e);
            }
        }
    }
}
