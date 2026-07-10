package es.altia.chd.ticket.gestion.portlet;

import com.liferay.portal.kernel.util.Validator;
import es.altia.chd.form.ticket.restapi.dto.TicketInfoDTO;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class to process and prepare ticket data for presentation
 */
public class TicketDataProcessor {

    private static final SimpleDateFormat DATE_FORMAT_INPUT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat DATE_FORMAT_OUTPUT = new SimpleDateFormat("dd-MM-yyyy HH:mm");

    /**
     * Process the ticket description (convert newlines to HTML breaks)
     */
    public static String processDescription(TicketInfoDTO ticket) {
        if (ticket == null) return "No hay datos del ticket.";

        String desc = ticket.description();

        if (Validator.isNull(desc) || desc.trim().equalsIgnoreCase("null") || desc.trim().isEmpty()) {
            return "No hay descripción disponible para este ticket.";
        }

        return desc.replace("\n", "<br/>");
    }

    /**
     * Normalize and map the ticket state from Odoo to a standard state
     */
    public static String normalizeState(TicketInfoDTO ticket) {
        if (ticket == null || ticket.state() == null) {
            return "nuevo";
        }

        String rawState = ticket.state().trim().toLowerCase();

        return switch (rawState) {
            case String s when s.contains("rechazado") -> "rechazado";
            case String s when s.contains("hecho") -> "hecho";
            case String s when s.contains("pendiente contestar") -> "pendiente contestar";
            case String s when s.contains("respondido") -> "respondido";
            case String s when s.contains("en trámite") -> "en trámite";
            default -> "nuevo";
        };
    }

    /**
     * Check if the ticket is closed (no further comments allowed)
     */
    public static boolean isTicketClosed(String state) {
        return "rechazado".equals(state) || "cancelado".equals(state);
    }

    /**
     * Format a date from Odoo format to display format
     */
    public static String formatDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return dateString;
        }

        try {
            Date dateObj = DATE_FORMAT_INPUT.parse(dateString);
            return DATE_FORMAT_OUTPUT.format(dateObj);
        } catch (Exception e) {
            // Return original string if parsing fails
            return dateString;
        }
    }

    /**
     * Determine message bubble styling based on whether it's from client or support
     */
    public static String getMessageBubbleColor(boolean isFromClient) {
        return isFromClient ? "#f8f9fa" : "#e3f2fd";
    }

    /**
     * Determine message bubble border color based on whether it's from client or support
     */
    public static String getMessageBubbleBorderColor(boolean isFromClient) {
        return isFromClient ? "#e9ecef" : "#cde3f5";
    }

    /**
     * Determine author text color based on whether it's from client or support
     */
    public static String getAuthorTextColor(boolean isFromClient) {
        return isFromClient ? "#6c757d" : "#0d6efd";
    }
}