package io.sdb.config;

import com.jfinal.kit.LogKit;
import com.jfinal.plugin.activerecord.ActiveRecordException;
import com.jfinal.plugin.activerecord.Config;
import com.jfinal.plugin.activerecord.DbKit;
import com.jfinal.plugin.activerecord.NestedTransactionHelpException;
import com.jfinal.plugin.activerecord.tx.TxConfig;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by chunmeng.lu
 * Date: 2016-30-03 19:30
 */
@Aspect
@Component
public class JFinalTxAop {

    @Pointcut(value = "@annotation(io.sdb.common.annotation.JFinalTx)")
    private void jFinalTx() {}

    @Around(value = "jFinalTx()", argNames = "pjp")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        Object retVal = null;
        Config config = getConfigWithTxConfig(pjp);
        if (config == null)
            config = DbKit.getConfig();

        Connection conn = config.getThreadLocalConnection();
        // Nested transaction support
        if (conn != null) {
            try {
                if (conn.getTransactionIsolation() < getTransactionLevel(config))
                    conn.setTransactionIsolation(getTransactionLevel(config));
                retVal = pjp.proceed();
                return retVal;
            } catch (SQLException e) {
                throw new ActiveRecordException(e);
            }
        }

        Boolean autoCommit = null;
        try {
            conn = config.getConnection();
            autoCommit = conn.getAutoCommit();
            config.setThreadLocalConnection(conn);
            // conn.setTransactionIsolation(transactionLevel);
            conn.setTransactionIsolation(getTransactionLevel(config));

            conn.setAutoCommit(false);
            retVal = pjp.proceed();
            conn.commit();
        } catch (NestedTransactionHelpException e) {
            if (conn != null) try {conn.rollback();} catch (Exception e1) {LogKit.error(e1.getMessage(), e1);}
            LogKit.logNothing(e);
        } catch (Throwable t) {
            if (conn != null) try {conn.rollback();} catch (Exception e1) {LogKit.error(e1.getMessage(), e1);}
            throw t instanceof RuntimeException ? (RuntimeException)t : new ActiveRecordException(t);
        }
        finally {
            try {
                if (conn != null) {
                    if (autoCommit != null)
                        conn.setAutoCommit(autoCommit);
                    conn.close();
                }
            } catch (Throwable t) {
                // can not throw exception here, otherwise the more important exception in previous catch block can not be thrown
                LogKit.error(t.getMessage(), t);
            }
            finally {
                // prevent memory leak
                config.removeThreadLocalConnection();
            }
        }
        return retVal;
    }

    /**
     * 获取配置的事务级别
     * @param config
     * @return
     */
    protected int getTransactionLevel(Config config) {
        return config.getTransactionLevel();
    }

    /**
     * 获取配置的TxConfig，可注解到class或者方法上
     * @param pjp
     * @return Config
     */
    public static Config getConfigWithTxConfig(ProceedingJoinPoint pjp) {
        MethodSignature ms = (MethodSignature) pjp.getSignature();
        Method method = ms.getMethod();
        TxConfig txConfig = method.getAnnotation(TxConfig.class);
        if (txConfig == null)
            txConfig = pjp.getTarget().getClass().getAnnotation(TxConfig.class);

        if (txConfig != null) {
            Config config = DbKit.getConfig(txConfig.value());
            if (config == null)
                throw new RuntimeException("Config not found with TxConfig: " + txConfig.value());
            return config;
        }
        return null;
    }
}