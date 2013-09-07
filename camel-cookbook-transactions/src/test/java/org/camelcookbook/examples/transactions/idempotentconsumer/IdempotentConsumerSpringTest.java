/*
 * Copyright (C) Scott Cranton and Jakub Korab
 * https://github.com/CamelCookbook
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.camelcookbook.examples.transactions.idempotentconsumer;

import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.apache.camel.test.spring.CamelSpringTestSupport;

/**
 * Tests that demonstrate the behavior of idempotent consumption.
 */
public class IdempotentConsumerSpringTest extends CamelSpringTestSupport {

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/idempotentConsumer-context.xml");
    }

    @Test
    public void testReplayOfSameMessageWillNotTriggerCall() throws InterruptedException {
        MockEndpoint mockWs = getMockEndpoint("mock:ws");
        mockWs.setExpectedMessageCount(1);

        MockEndpoint mockOut = getMockEndpoint("mock:out");
        mockOut.setExpectedMessageCount(2);

        template.sendBodyAndHeader("direct:in", "Insert", "messageId", 1);
        template.sendBodyAndHeader("direct:in", "Insert", "messageId", 1); // again

        assertMockEndpointsSatisfied();
    }

}
