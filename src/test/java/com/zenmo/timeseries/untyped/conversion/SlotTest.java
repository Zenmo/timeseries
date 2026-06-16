package com.zenmo.timeseries.untyped.conversion;

import com.zenmo.timeseries.untyped.TestUtil;
import com.zenmo.timeseries.untyped.conversion.DestinationSlot;
import com.zenmo.timeseries.untyped.conversion.SourceSlot;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SlotTest {
    @Test
    public void testHourlySourceToQuarterHourlyReadSpecification() {
        var start = TestUtil.startOf2025;

        var sourceStep = TestUtil.hour;
        var destinationStep = TestUtil.quarterHour;

        var sourceSlot = new SourceSlot(start, start.plus(sourceStep), 0);
        var destinationSlot = new DestinationSlot(start, start.plus(destinationStep));

        var readSpecification = sourceSlot.toReadSpecification(destinationSlot);

        assertEquals(0.0, readSpecification.getStartIndex());
        assertEquals(0.25, readSpecification.getEndIndex());

        destinationSlot = destinationSlot.next(destinationStep);
        readSpecification = sourceSlot.toReadSpecification(destinationSlot);

        assertEquals(0.25, readSpecification.getStartIndex());
        assertEquals(0.5, readSpecification.getEndIndex());

        destinationSlot = destinationSlot.next(destinationStep).next(destinationStep);
        readSpecification = sourceSlot.toReadSpecification(destinationSlot);

        assertEquals(0.75, readSpecification.getStartIndex());
        assertEquals(1.0, readSpecification.getEndIndex());

        destinationSlot = destinationSlot.next(destinationStep);
        sourceSlot = sourceSlot.next(sourceStep);
        readSpecification = sourceSlot.toReadSpecification(destinationSlot);

        assertEquals(1.0, readSpecification.getStartIndex());
        assertEquals(1.25, readSpecification.getEndIndex());
    }
}
