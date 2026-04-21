package A3.project.noWaste.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BatchTest {

    @Test
    void shouldReturnExpiredWhenExpirationDateIsInThePast() {
        Batch batch = new Batch();
        batch.setExpirationDate(LocalDate.now().minusDays(1));

        assertEquals("EXPIRED", batch.getStatus());
    }

    @Test
    void shouldReturnWarningWhenExpirationDateIsWithinSevenDays() {
        Batch batch = new Batch();
        batch.setExpirationDate(LocalDate.now().plusDays(7));

        assertEquals("WARNING", batch.getStatus());
    }

    @Test
    void shouldReturnMonthWarningWhenExpirationDateIsBetweenEightAndThirtyDays() {
        Batch batch = new Batch();
        batch.setExpirationDate(LocalDate.now().plusDays(15));

        assertEquals("MONTH_WARNING", batch.getStatus());
    }

    @Test
    void shouldReturnOkWhenExpirationDateIsMoreThanThirtyDaysAway() {
        Batch batch = new Batch();
        batch.setExpirationDate(LocalDate.now().plusDays(31));

        assertEquals("OK", batch.getStatus());
    }
}
