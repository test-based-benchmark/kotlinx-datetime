/*
 * Copyright 2016-2020 JetBrains s.r.o.
 * Use of this source code is governed by the Apache 2.0 License that can be found in the LICENSE.txt file.
 */
package kotlinx.datetime

import kotlin.test.*

class ThreeTenBpLocalDateTimeTest {
    @Test
    fun toSecondsAfterEpoch() {
        for (i in -5..4) {
            val iHours = i * 3600
            val offset = ZoneOffset(iHours)
            for (j in 0..99999L) {
                val a = LocalDateTime(1970, 1, 1, 0, 0, 0, 0).plusSeconds(j)
                assertEquals(j - iHours, a.toEpochSecond(offset), "$i, $j")
            }
        }
    }

    @Test
    fun toSecondsBeforeEpoch() {
        for (i in 0..99999L) {
            val a: LocalDateTime = LocalDateTime(1970, 1, 1, 0, 0, 0, 0).plusSeconds(-i)
            assertEquals(-i, a.toEpochSecond(ZoneOffset.UTC), "$i")
        }
    }

    @Test
    fun plusSeconds() {
        var t = LocalDateTime(2007, 7, 15, 0, 0, 0, 0)
        val d: LocalDate = t.date
        var hour = 0
        var min = 0
        var sec = 0
        for (i in 0..3699) {
            t = t.plusSeconds(1)
            sec++
            if (sec == 60) {
                min++
                sec = 0
            }
            if (min == 60) {
                hour++
                min = 0
            }
            assertEquals(d, t.date)
            assertEquals(hour, t.hour)
            assertEquals(min, t.minute)
            assertEquals(sec, t.second)
        }
    }

    @Test
    fun until() {
        val data = arrayOf(
            Pair(Pair("2012-06-15T00:00", "2012-06-15T00:00"), Pair(CalendarUnit.NANOSECOND, 0L)),
            Pair(Pair("2012-06-15T00:00", "2012-06-15T00:00"), Pair(CalendarUnit.SECOND, 0L)),
            Pair(Pair("2012-06-15T00:00", "2012-06-15T00:00"), Pair(CalendarUnit.MINUTE, 0L)),
            Pair(Pair("2012-06-15T00:00", "2012-06-15T00:00"), Pair(CalendarUnit.HOUR, 0L)),
            Pair(Pair("2012-06-15T00:00", "2012-06-15T00:00:01"), Pair(CalendarUnit.NANOSECOND, 1000000000L)),
            Pair(Pair("2012-06-15T00:00", "2012-06-15T00:00:01"), Pair(CalendarUnit.SECOND, 1L)),
            Pair(Pair("2012-06-15T00:00", "2012-06-15T00:00:01"), Pair(CalendarUnit.MINUTE, 0L)),
            Pair(Pair("2012-06-15T00:00", "2012-06-15T00:00:01"), Pair(CalendarUnit.HOUR, 0L)),
            Pair(Pair("2012-06-15T00:00", "2012-06-15T00:01"), Pair(CalendarUnit.NANOSECOND, 60000000000L)),
            Pair(Pair("2012-06-15T00:00", "2012-06-15T00:01"), Pair(CalendarUnit.SECOND, 60L)),
            Pair(Pair("2012-06-15T00:00", "2012-06-15T00:01"), Pair(CalendarUnit.MINUTE, 1L)),
            Pair(Pair("2012-06-15T00:00", "2012-06-15T00:01"), Pair(CalendarUnit.HOUR, 0L)),
            Pair(Pair("2012-06-15T12:30:40.500", "2012-06-15T12:30:39.499"), Pair(CalendarUnit.SECOND, -1L)),
            Pair(Pair("2012-06-15T12:30:40.500", "2012-06-15T12:30:39.500"), Pair(CalendarUnit.SECOND, -1L)),
            Pair(Pair("2012-06-15T12:30:40.500", "2012-06-15T12:30:39.501"), Pair(CalendarUnit.SECOND, 0L)),
            Pair(Pair("2012-06-15T12:30:40.500", "2012-06-15T12:30:40.499"), Pair(CalendarUnit.SECOND, 0L)),
            Pair(Pair("2012-06-15T12:30:40.500", "2012-06-15T12:30:40.500"), Pair(CalendarUnit.SECOND, 0L)),
            Pair(Pair("2012-06-15T12:30:40.500", "2012-06-15T12:30:40.501"), Pair(CalendarUnit.SECOND, 0L)),
            Pair(Pair("2012-06-15T12:30:40.500", "2012-06-15T12:30:41.499"), Pair(CalendarUnit.SECOND, 0L)),
            Pair(Pair("2012-06-15T12:30:40.500", "2012-06-15T12:30:41.500"), Pair(CalendarUnit.SECOND, 1L)),
            Pair(Pair("2012-06-15T12:30:40.500", "2012-06-15T12:30:41.501"), Pair(CalendarUnit.SECOND, 1L)),
            Pair(Pair("2012-06-15T12:30:40.500", "2012-06-16T12:30:39.499"), Pair(CalendarUnit.SECOND, 86400L - 2)),
            Pair(Pair("2012-06-15T12:30:40.500", "2012-06-16T12:30:39.500"), Pair(CalendarUnit.SECOND, 86400L - 1)),
            Pair(Pair("2012-06-15T12:30:40.500", "2012-06-16T12:30:39.501"), Pair(CalendarUnit.SECOND, 86400L - 1)),
            Pair(Pair("2012-06-15T12:30:40.500", "2012-06-16T12:30:40.499"), Pair(CalendarUnit.SECOND, 86400L - 1)),
            Pair(Pair("2012-06-15T12:30:40.500", "2012-06-16T12:30:40.500"), Pair(CalendarUnit.SECOND, 86400L + 0)),
            Pair(Pair("2012-06-15T12:30:40.500", "2012-06-16T12:30:40.501"), Pair(CalendarUnit.SECOND, 86400L + 0)),
            Pair(Pair("2012-06-15T12:30:40.500", "2012-06-16T12:30:41.499"), Pair(CalendarUnit.SECOND, 86400L + 0)),
            Pair(Pair("2012-06-15T12:30:40.500", "2012-06-16T12:30:41.500"), Pair(CalendarUnit.SECOND, 86400L + 1)),
            Pair(Pair("2012-06-15T12:30:40.500", "2012-06-16T12:30:41.501"), Pair(CalendarUnit.SECOND, 86400L + 1)))
        for ((values, interval) in data) {
            val (v1, v2) = values
            val dt1 = v1.toLocalDateTime()
            val dt2 = v2.toLocalDateTime()
            val (unit, length) = interval
            assertEquals(length, dt1.until(dt2, unit))
            assertEquals(-length, dt2.until(dt1, unit))
        }
    }
}