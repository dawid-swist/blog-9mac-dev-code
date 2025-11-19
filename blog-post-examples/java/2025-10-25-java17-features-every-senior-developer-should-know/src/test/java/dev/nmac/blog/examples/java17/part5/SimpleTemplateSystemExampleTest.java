package dev.nmac.blog.examples.java17.part5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SimpleTemplateSystemExampleTest {

    @Test
    @DisplayName("Should render email template with variables")
    void shouldRenderEmailTemplate() {
        String email = SimpleTemplateSystemExample.renderEmailTemplate("John", "https://example.com/activate", "24");

        assertTrue(email.contains("Welcome, John!"));
        assertTrue(email.contains("https://example.com/activate"));
        assertTrue(email.contains("24"));
    }

    @Test
    @DisplayName("Should render invoice template with variables")
    void shouldRenderInvoiceTemplate() {
        String invoice = SimpleTemplateSystemExample.renderInvoiceTemplate("INV-001", "ACME Corp", "1000.00", "2024-12-31");

        assertTrue(invoice.contains("INV-001"));
        assertTrue(invoice.contains("ACME Corp"));
        assertTrue(invoice.contains("1000.00"));
        assertTrue(invoice.contains("2024-12-31"));
    }

    @Test
    @DisplayName("Should render API response template with variables")
    void shouldRenderAPIResponseTemplate() {
        String response = SimpleTemplateSystemExample.renderAPIResponseTemplate("success", "req-123", "Created");

        assertTrue(response.contains("success"));
        assertTrue(response.contains("req-123"));
        assertTrue(response.contains("Created"));
        assertTrue(response.contains("timestamp"));
    }

    @Test
    @DisplayName("Should render config template with variables")
    void shouldRenderConfigTemplate() {
        String config = SimpleTemplateSystemExample.renderConfigTemplate("MyApp", "production", "8080");

        assertTrue(config.contains("MyApp"));
        assertTrue(config.contains("production"));
        assertTrue(config.contains("8080"));
        assertTrue(config.contains("app.name=MyApp"));
    }
}
