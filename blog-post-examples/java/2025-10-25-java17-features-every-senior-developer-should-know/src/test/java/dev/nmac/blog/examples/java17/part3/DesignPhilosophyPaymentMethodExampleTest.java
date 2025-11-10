package dev.nmac.blog.examples.java17.part3;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Design Philosophy: Payment Method (sealed → final)")
class DesignPhilosophyPaymentMethodExampleTest {

    @Test
    @DisplayName("Should create CardPayment")
    void shouldCreateCardPayment() {
        var payment = new DesignPhilosophyPaymentMethodExample.CardPayment("Visa");

        assertEquals("Visa", payment.cardNetwork());
        assertTrue(payment.description().contains("Card Payment"));
    }

    @Test
    @DisplayName("Should create DomesticTransfer")
    void shouldCreateDomesticTransfer() {
        var payment = new DesignPhilosophyPaymentMethodExample.DomesticTransfer("123456789");

        assertEquals("123456789", payment.routingNumber());
        assertTrue(payment.description().contains("Domestic"));
    }

    @Test
    @DisplayName("Should create InternationalTransfer")
    void shouldCreateInternationalTransfer() {
        var payment = new DesignPhilosophyPaymentMethodExample.InternationalTransfer("DEUTDEFF", "Germany");

        assertEquals("DEUTDEFF", payment.swiftCode());
        assertEquals("Germany", payment.targetCountry());
        assertTrue(payment.description().contains("International"));
    }

    @Test
    @DisplayName("Should create CryptoCurrency")
    void shouldCreateCryptoCurrency() {
        var payment = new DesignPhilosophyPaymentMethodExample.CryptoCurrency("Bitcoin", "1A1z7agoat2wurSrF3S6fDwQoO4XZNXqo");

        assertEquals("Bitcoin", payment.blockchain());
        assertEquals("1A1z7agoat2wurSrF3S6fDwQoO4XZNXqo", payment.walletAddress());
    }

    @Test
    @DisplayName("Should verify PaymentMethod is sealed")
    void shouldVerifyPaymentMethodIsSealed() {
        assertTrue(DesignPhilosophyPaymentMethodExample.PaymentMethod.class.isSealed());
    }

    @Test
    @DisplayName("Should verify BankTransfer is sealed")
    void shouldVerifyBankTransferIsSealed() {
        assertTrue(DesignPhilosophyPaymentMethodExample.BankTransfer.class.isSealed());
    }

    @Test
    @DisplayName("Should verify CardPayment is final (no further extension)")
    void shouldVerifyCardPaymentIsFinal() {
        assertTrue(java.lang.reflect.Modifier.isFinal(
            DesignPhilosophyPaymentMethodExample.CardPayment.class.getModifiers()));
    }

    @Test
    @DisplayName("Should verify CryptoCurrency is final (no further extension)")
    void shouldVerifyCryptoCurrencyIsFinal() {
        assertTrue(java.lang.reflect.Modifier.isFinal(
            DesignPhilosophyPaymentMethodExample.CryptoCurrency.class.getModifiers()));
    }

    @Test
    @DisplayName("Should verify DomesticTransfer and InternationalTransfer are final")
    void shouldVerifyBankTransfersAreFinal() {
        assertTrue(java.lang.reflect.Modifier.isFinal(
            DesignPhilosophyPaymentMethodExample.DomesticTransfer.class.getModifiers()));
        assertTrue(java.lang.reflect.Modifier.isFinal(
            DesignPhilosophyPaymentMethodExample.InternationalTransfer.class.getModifiers()));
    }

    @Test
    @DisplayName("Should verify PaymentMethod permits three branches")
    void shouldVerifyPaymentMethodPermits() {
        var permitted = DesignPhilosophyPaymentMethodExample.PaymentMethod.class.getPermittedSubclasses();
        assertEquals(3, permitted.length);

        var names = java.util.Arrays.stream(permitted)
            .map(Class::getSimpleName)
            .collect(java.util.stream.Collectors.toSet());

        assertTrue(names.contains("CardPayment"));
        assertTrue(names.contains("BankTransfer"));
        assertTrue(names.contains("CryptoCurrency"));
    }

    @Test
    @DisplayName("Should verify BankTransfer permits DomesticTransfer and InternationalTransfer")
    void shouldVerifyBankTransferPermits() {
        var permitted = DesignPhilosophyPaymentMethodExample.BankTransfer.class.getPermittedSubclasses();
        assertEquals(2, permitted.length);

        var names = java.util.Arrays.stream(permitted)
            .map(Class::getSimpleName)
            .collect(java.util.stream.Collectors.toSet());

        assertTrue(names.contains("DomesticTransfer"));
        assertTrue(names.contains("InternationalTransfer"));
    }

    @Test
    @DisplayName("Should demonstrate why final is used for CardPayment (security)")
    void shouldDemonstrateFinalForSecurity() {
        // CardPayment is final - no extensions possible
        var cardPaymentClass = DesignPhilosophyPaymentMethodExample.CardPayment.class;
        assertTrue(java.lang.reflect.Modifier.isFinal(cardPaymentClass.getModifiers()));

        // This is intentional for security/PCI compliance
        // No EnhancedCardPayment or BypassedCardPayment can be created

        // This would NOT compile:
        // public class EnhancedCardPayment extends CardPayment { }
        // ❌ Compile error: CardPayment is final
    }

    @Test
    @DisplayName("Should show mixed hierarchy: some sealed, some final")
    void shouldShowMixedHierarchy() {
        // Top level is sealed - controls 3 branches
        assertTrue(DesignPhilosophyPaymentMethodExample.PaymentMethod.class.isSealed());
        var topPermitted = DesignPhilosophyPaymentMethodExample.PaymentMethod.class.getPermittedSubclasses();
        assertEquals(3, topPermitted.length);

        // Branch 1: CardPayment - final (ends here, no further subdivision)
        assertTrue(java.lang.reflect.Modifier.isFinal(
            DesignPhilosophyPaymentMethodExample.CardPayment.class.getModifiers()));

        // Branch 2: BankTransfer - sealed (allows further subdivision)
        assertTrue(DesignPhilosophyPaymentMethodExample.BankTransfer.class.isSealed());
        var bankPermitted = DesignPhilosophyPaymentMethodExample.BankTransfer.class.getPermittedSubclasses();
        assertEquals(2, bankPermitted.length);

        // Branch 3: CryptoCurrency - final (ends here)
        assertTrue(java.lang.reflect.Modifier.isFinal(
            DesignPhilosophyPaymentMethodExample.CryptoCurrency.class.getModifiers()));

        System.out.println("✓ Top level: sealed (3 branches)");
        System.out.println("✓ CardPayment: final (no extension)");
        System.out.println("✓ BankTransfer: sealed (2 sub-types)");
        System.out.println("✓ CryptoCurrency: final (no extension)");
    }

    @Test
    @DisplayName("Should demonstrate why DomesticTransfer and InternationalTransfer are final")
    void shouldDemonstrateFinalForBankTransfers() {
        // These are final because they represent concrete payment methods
        // No user should create BrazilianTransfer or CustomTransfer

        var domestic = new DesignPhilosophyPaymentMethodExample.DomesticTransfer("987654321");
        var international = new DesignPhilosophyPaymentMethodExample.InternationalTransfer("DEUTDEFF", "Germany");

        assertTrue(domestic instanceof DesignPhilosophyPaymentMethodExample.BankTransfer);
        assertTrue(international instanceof DesignPhilosophyPaymentMethodExample.BankTransfer);

        // But they cannot be extended
        assertTrue(java.lang.reflect.Modifier.isFinal(domestic.getClass().getModifiers()));
        assertTrue(java.lang.reflect.Modifier.isFinal(international.getClass().getModifiers()));
    }
}
