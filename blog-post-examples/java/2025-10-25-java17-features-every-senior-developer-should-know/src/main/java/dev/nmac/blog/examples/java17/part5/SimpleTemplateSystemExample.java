package dev.nmac.blog.examples.java17.part5;

/**
 * Demonstrates a simple template system using text blocks and formatted().
 *
 * Text blocks combined with formatted() enable building flexible template systems
 * for emails, HTML pages, documents, and other structured text without external libraries.
 */
public class SimpleTemplateSystemExample {

    // Email template with variable interpolation
    public static String renderEmailTemplate(String recipientName, String activationUrl, String expirationHours) {
        return """
            <html>
              <body style="font-family: Arial, sans-serif;">
                <h2>Welcome, %s!</h2>
                <p>Thank you for signing up. Please activate your account within %s hours.</p>
                <p><a href="%s" style="background-color: #007bff; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;">Activate Account</a></p>
                <p>If you didn't sign up, please ignore this email.</p>
                <p>Best regards,<br>The Team</p>
              </body>
            </html>""".formatted(recipientName, expirationHours, activationUrl);
    }

    // Invoice template with multiple variables
    public static String renderInvoiceTemplate(String invoiceNumber, String customerName, String amount, String dueDate) {
        return """
            ╔════════════════════════════════════════╗
            ║           INVOICE                      ║
            ╚════════════════════════════════════════╝

            Invoice #: %s
            Customer:  %s

            Amount Due: $%s
            Due Date:   %s

            ────────────────────────────────────────
            Thank you for your business!
            """.formatted(invoiceNumber, customerName, amount, dueDate);
    }

    // API response template
    public static String renderAPIResponseTemplate(String status, String requestId, String message) {
        return """
            {
              "status": "%s",
              "requestId": "%s",
              "message": "%s",
              "timestamp": "%s"
            }""".formatted(status, requestId, message, java.time.Instant.now().toString());
    }

    // Configuration file template
    public static String renderConfigTemplate(String appName, String environment, String port) {
        return """
            # Configuration for %s
            # Environment: %s

            app.name=%s
            app.environment=%s
            server.port=%s
            logging.level=INFO
            """.formatted(appName, environment, appName, environment, port);
    }

    public static void main(String[] args) {
        System.out.println("=== Email Template ===");
        System.out.println(renderEmailTemplate("John Smith", "https://app.example.com/activate?token=xyz123", "24"));

        System.out.println("\n=== Invoice Template ===");
        System.out.println(renderInvoiceTemplate("INV-2024-001", "Acme Corp", "1,500.00", "2024-12-31"));

        System.out.println("\n=== API Response Template ===");
        System.out.println(renderAPIResponseTemplate("success", "req-12345", "User created successfully"));

        System.out.println("\n=== Config Template ===");
        System.out.println(renderConfigTemplate("MyApp", "production", "8080"));
    }
}
