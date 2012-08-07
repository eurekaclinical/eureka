package edu.emory.cci.aiw.cvrg.eureka.etl;

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
        if (method.getName().equals("close")) {
            return false;
        }
        return method.invoke(this.proxied, args);
    }
}
