/*
 * #%L
 * Eureka Protempa ETL
 * %%
 * Copyright (C) 2012 Emory University
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
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
