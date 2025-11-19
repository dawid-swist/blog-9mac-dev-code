package dev.nmac.blog.examples.java17.part5;

/**
 * Demonstrates text blocks with HTML and XML templates.
 *
 * Text blocks make HTML/XML templates readable by preserving natural structure
 * without escape sequences or concatenation, perfect for embedded markup.
 */
public class HTMLTextBlocksExample {

    public static String getDashboardHTML() {
        return """
            <html>
              <head>
                <title>User Dashboard</title>
              </head>
              <body>
                <h1>Welcome, User!</h1>
                <p>This is your dashboard.</p>
              </body>
            </html>""";
    }

    public static String getSVGCircle() {
        return """
            <svg width="100" height="100">
              <circle cx="50" cy="50" r="40" fill="blue" />
              <text x="50" y="55" text-anchor="middle" fill="white">SVG</text>
            </svg>""";
    }

    public static void main(String[] args) {
        System.out.println("=== Dashboard HTML ===");
        System.out.println(getDashboardHTML());

        System.out.println("\n=== SVG Circle ===");
        System.out.println(getSVGCircle());
    }
}
