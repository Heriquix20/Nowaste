package A3.project.noWaste.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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

    @Test
    void shouldReturnUnknownWhenExpirationDateIsNull() {
        Batch batch = new Batch();

        assertEquals("UNKNOWN", batch.getStatus());
    }

    @Test
    void shouldReturnNullDaysToExpireWhenExpirationDateIsNull() {
        Batch batch = new Batch();

        assertNull(batch.getDaysToExpire());
    }

    @Test
    void shouldCalculateDaysToExpireWhenExpirationDateExists() {
        Batch batch = new Batch();
        batch.setExpirationDate(LocalDate.now().plusDays(4));

        assertEquals(4, batch.getDaysToExpire());
    }

    @Test
    void shouldCalculateTotalWeightFromProductWeightAndQuantity() {
        Product product = new Product();
        product.setWeightInGrams(500.0);

        Batch batch = new Batch();
        batch.setProduct(product);
        batch.setQuantity(4);

        assertEquals(2000.0, batch.getTotalWeight());
    }

    @Test
    void shouldReturnZeroTotalWeightWhenProductIsMissing() {
        Batch batch = new Batch();
        batch.setQuantity(4);

        assertEquals(0.0, batch.getTotalWeight());
    }
}
