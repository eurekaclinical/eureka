package edu.emory.cci.aiw.cvrg.eureka.etl.job;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.protempa.Source;

/**
 * Proxies data source, knowledge source, algorithm source and term source
 * instances. It causes calls to {@link Source#close() } to be a no-op.
 *
 * @author Andrew Post
 */
final class PreventCloseInvocationHandler implements InvocationHandler {
    private Source<?, ?, ?> proxied;

    PreventCloseInvocationHandler(Source<?, ?, ?> proxied) {
        if (proxied == null) {
            throw new IllegalArgumentException("proxied cannot be null");
        }
        this.proxied = proxied;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        String methodName = method.getName();
        if (methodName.equals("close")) {
            return null;
        } else if (methodName.equals("reallyClose")) {
            this.proxied.close();
            return null;
        }
        return method.invoke(this.proxied, args);
    }
}
