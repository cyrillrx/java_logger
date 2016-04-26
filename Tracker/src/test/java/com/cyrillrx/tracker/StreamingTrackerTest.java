package com.cyrillrx.tracker;

import com.cyrillrx.tracker.event.TrackEvent;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayDeque;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Cyril Leroux
 *         Created on 20/04/16.
 */
public class StreamingTrackerTest {

    private static final int EVENT_COUNT = 10;

    @Test
    public void testConsistency() {

        final BasicTracker nestedTracker = new BasicTracker();
        final StreamingTracker tracker = new StreamingTracker.Builder()
                .setNestedTracker(nestedTracker)
                .setCapacity(10)
                .build();

        Assert.assertTrue("Should be empty", nestedTracker.isEmpty());

        // Track one event
        tracker.track(TestUtils.createFakeEvent(TestUtils.EVENT_CATEGORY));
        TestUtils.wait100Millis();

        Assert.assertEquals("Should be 1", 1, nestedTracker.getEventCount());

        final List<TrackEvent> trackedEvents = nestedTracker.getEvents();
        for (TrackEvent event : trackedEvents) {
            Assert.assertTrue("Should contain category " + TestUtils.EVENT_CATEGORY, TestUtils.EVENT_CATEGORY.equals(event.getCategory()));
        }
    }

    @Test
    public void testRetry() {

        final RetryTracker nestedTracker = new RetryTracker();
        final StreamingTracker tracker = new StreamingTracker.Builder()
                .setNestedTracker(nestedTracker)
                .setCapacity(10)
                .setRetry(new ArrayDeque<TrackEvent>(), TimeUnit.MILLISECONDS, 200)
                .build();

        Assert.assertTrue("Should be empty", nestedTracker.isEmpty());

        // First track should fail
        tracker.track(TestUtils.createFakeEvent(TestUtils.EVENT_CATEGORY));
        TestUtils.wait100Millis();
        Assert.assertTrue("Should be empty", nestedTracker.isEmpty());

        // Wait for retry to be performed
        TestUtils.wait100Millis();
        TestUtils.wait100Millis();
        Assert.assertEquals("Should be 1", 1, nestedTracker.getEventCount());
    }

    @Test
    public void testOneByOne() {

        final BasicTracker nestedTracker = new BasicTracker();
        final StreamingTracker tracker = new StreamingTracker.Builder()
                .setNestedTracker(nestedTracker)
                .setCapacity(100)
                .build();

        Assert.assertTrue("Should be empty", nestedTracker.isEmpty());

        // Track EVENT_COUNT events
        TestUtils.trackEventsOneByOne(tracker, EVENT_COUNT, TestUtils.EVENT_CATEGORY);
        TestUtils.wait100Millis();
        Assert.assertEquals("Should be " + EVENT_COUNT, EVENT_COUNT, nestedTracker.getEventCount());

        // Track EVENT_COUNT events
        TestUtils.trackEventsOneByOne(tracker, EVENT_COUNT, TestUtils.EVENT_CATEGORY);
        TestUtils.wait100Millis();
        int totalEventCount = EVENT_COUNT * 2;
        Assert.assertEquals("Should be " + totalEventCount, totalEventCount, nestedTracker.getEventCount());
    }

    @Test
    public void testBatch() {

        System.out.println("Test started");

        final BasicTracker nestedTracker = new BasicTracker();
        final StreamingTracker tracker = new StreamingTracker.Builder()
                .setNestedTracker(nestedTracker)
                .setCapacity(100)
                .build();

        Assert.assertTrue("Should be empty", nestedTracker.isEmpty());

        TestUtils.trackEventsBatch(tracker, EVENT_COUNT, TestUtils.EVENT_CATEGORY);
        TestUtils.wait100Millis();
        Assert.assertEquals("Should be " + EVENT_COUNT, EVENT_COUNT, nestedTracker.getEventCount());

        TestUtils.trackEventsBatch(tracker, EVENT_COUNT, TestUtils.EVENT_CATEGORY);
        TestUtils.wait100Millis();
        int totalEventCount = EVENT_COUNT * 2;
        Assert.assertEquals("Should be " + totalEventCount, totalEventCount, nestedTracker.getEventCount());
    }

}
