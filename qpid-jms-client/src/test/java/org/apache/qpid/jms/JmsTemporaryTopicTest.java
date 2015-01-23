/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.qpid.jms;

import static org.junit.Assert.*;
import static org.apache.qpid.jms.SerializationTestSupport.roundTripSerializeDestination;
import static org.apache.qpid.jms.SerializationTestSupport.serializeDestination;

import javax.jms.Destination;

import org.apache.qpid.jms.test.QpidJmsTestCase;
import org.junit.Test;

public class JmsTemporaryTopicTest extends QpidJmsTestCase {

    @Test
    public void testIsQueue() {
        JmsTemporaryTopic topic = new JmsTemporaryTopic("myTopic");
        assertFalse("should not be a queue", topic.isQueue());
    }

    @Test
    public void testIsTopic() {
        JmsTemporaryTopic topic = new JmsTemporaryTopic("myTopic");
        assertTrue("should be a topic", topic.isTopic());
    }

    @Test
    public void testIsTemporary() {
        JmsTemporaryTopic topic = new JmsTemporaryTopic("myTopic");
        assertTrue("should be temporary", topic.isTemporary());
    }

    @Test
    public void testIsDeleted() throws Exception {
        JmsTemporaryTopic topic = new JmsTemporaryTopic("myTopic");
        assertFalse("should not be deleted", topic.isDeleted());
        topic.delete();
        assertTrue("should be deleted", topic.isDeleted());
    }

    @Test
    public void testEqualsWithNull() {
        JmsTemporaryTopic topic = new JmsTemporaryTopic("myTopic");
        assertFalse("should not be equal", topic.equals(null));
    }

    @Test
    public void testEqualsWithDifferentObjectType() {
        JmsTemporaryTopic topic = new JmsTemporaryTopic("name");
        JmsQueue otherObject = new JmsQueue("name");
        assertFalse("should not be equal", topic.equals(otherObject));
    }

    @Test
    public void testEqualsWithSameObject() {
        JmsTemporaryTopic topic = new JmsTemporaryTopic("name");
        assertTrue("should be equal to itself", topic.equals(topic));
    }

    @Test
    public void testEqualsWithDifferentObject() {
        JmsTemporaryTopic topic1 = new JmsTemporaryTopic("name");
        JmsTemporaryTopic topic2 = new JmsTemporaryTopic("name");
        assertTrue("should be equal", topic1.equals(topic2));
        assertTrue("should still be equal", topic2.equals(topic1));
    }

    @Test
    public void testHashcodeWithEqualNamedObjects() {
        JmsTemporaryTopic topic1 = new JmsTemporaryTopic("name");
        JmsTemporaryTopic topic2 = new JmsTemporaryTopic("name");
        assertEquals("should have same hashcode", topic1.hashCode(), topic2.hashCode());
    }

    @Test
    public void testHashcodeWithDifferentNamedObjects() {
        JmsTemporaryTopic topic1 = new JmsTemporaryTopic("name1");
        JmsTemporaryTopic topic2 = new JmsTemporaryTopic("name2");

        // Not strictly a requirement, but expected in this case
        assertNotEquals("should not have same hashcode", topic1.hashCode(), topic2.hashCode());
    }

    @Test
    public void testSerializeThenDeserialize() throws Exception {
        String name = "myTopic";
        JmsTemporaryTopic topic = new JmsTemporaryTopic(name);

        Destination roundTripped = roundTripSerializeDestination(topic);

        assertNotNull("Null destination returned", roundTripped);
        assertEquals("Unexpected type", JmsTemporaryTopic.class, roundTripped.getClass());
        assertEquals("Unexpected name", name, ((JmsTemporaryTopic)roundTripped).getTopicName());
        assertEquals("Objects were not equal", topic, roundTripped);
    }

    @Test
    public void testSerializeTwoEqualDestinations() throws Exception {
        JmsTemporaryTopic topic1 = new JmsTemporaryTopic("myTopic");
        JmsTemporaryTopic topic2 = new JmsTemporaryTopic("myTopic");

        assertEquals("Destinations were not equal", topic1, topic2);

        byte[] bytes1 = serializeDestination(topic1);
        byte[] bytes2 = serializeDestination(topic2);

        assertArrayEquals("Serialized bytes were not equal", bytes1, bytes2);
    }

    @Test
    public void testSerializeTwoDifferentDestinations() throws Exception {
        JmsTemporaryTopic topic1 = new JmsTemporaryTopic("myTopic1");
        JmsTemporaryTopic topic2 = new JmsTemporaryTopic("myTopic2");

        assertNotEquals("Destinations were not expected to be equal", topic1, topic2);

        byte[] bytes1 = serializeDestination(topic1);
        byte[] bytes2 = serializeDestination(topic2);

        try {
            assertArrayEquals(bytes1, bytes2);
            fail("Expected arrays to differ");
        } catch (AssertionError ae) {
            // Expected, pass
        }
    }
}
