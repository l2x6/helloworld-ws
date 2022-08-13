/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.wshelloworld.it;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceException;

import org.assertj.core.api.Assertions;
import org.jboss.as.quickstarts.wshelloworld.HelloWorldService;
import org.junit.Test;

/**
 * Simple set of tests for the HelloWorld Web Service to demonstrate accessing the web service using a client
 */
public class ClientIT {

    private static final String BASE_URI = "http://localhost:8088";

    @Test
    public void calculate() throws MalformedURLException {
        QName serviceName = new QName(HelloWorldService.TARGET_NS, HelloWorldService.class.getSimpleName());

        final long deadline = System.currentTimeMillis() + 10_000L;

        Service service = null;
        while (true) {
            try {
                service = Service.create(new URL(BASE_URI + "/helloworld-ws/HelloWorldService?wsdl"), serviceName);
                break;
            } catch (WebServiceException e) {
                if (System.currentTimeMillis() < deadline) {
                    /* wait and retry */
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e1) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    throw e;
                }
            }

        }
        final HelloWorldService helloWorldService = service.getPort(HelloWorldService.class);

        Assertions.assertThat(helloWorldService).isNotNull();

        Assertions.assertThat(helloWorldService.sayHello()).isEqualTo("Hello World!");
        Assertions.assertThat(helloWorldService.sayHelloToName("Joe")).isEqualTo("Hello Joe!");
        Assertions.assertThat(helloWorldService.sayHelloToNames(Arrays.asList("Joe", "Mary"))).isEqualTo("Hello Joe & Mary!");

    }

}
