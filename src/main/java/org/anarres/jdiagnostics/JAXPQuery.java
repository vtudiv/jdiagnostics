/**
 * Copyright 2014 Shevek
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.jdiagnostics;

import java.lang.reflect.Method;

/**
 *
 * @author shevek
 */
public class JAXPQuery extends AbstractQuery {

    @Override
    public String getName() {
        return "jaxp";
    }

    public void version(Result result, String prefix) {
        new JarQuery("jaxp.jar").call(result, prefix + "jaxpJar/");
        Class<?> c = new ClassExistsQuery("javax.xml.parsers.DocumentBuilder").findClass(result, prefix);
        if (c == null) {
            result.put(prefix + "version", "none");
            return;
        }
        Method m = new MethodExistsQuery(c, "getDOMImplementation").findMethod(result, prefix);
        if (m == null) {
            result.put(prefix + "version", "1.0.1?");
            return;
        }
        result.put(prefix + "version", ">=1.1");
    }

    public void factory(Result result, String prefix) {
        Object factory = new ClassMethodExistsCallQuery(null, "javax.xml.parsers.DocumentBuilderFactory", "newInstance").invoke(result, prefix);
        if (factory == null)
            return;
        Object instance = new MethodExistsCallQuery(factory, factory.getClass(), "newDocumentBuilder").invoke(result, prefix + "instance/");
        if (instance == null)
            return;
        new ClassQuery(instance.getClass()).call(result, prefix + "type/");
    }

    @Override
    public void call(Result result, String prefix) {
        version(result, prefix + "version/");
        factory(result, prefix + "factory/");
    }

}
