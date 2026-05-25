package com.mycompany.main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Message class for Part 2: Sending Messages.
 */
public class Message {
    private String messageID;
    private int messageNumber;
    private String recipient;
    private String message;
    private String messageHash;
    private String sender;

    private static int totalMessagesSent = 0;
    private static final List<Message> sentMessages = new ArrayList<>();
    private static final List<Message> storedMessages = new ArrayList<>();
    private static final String JSON_FILE_NAME = "stored_messages.json";

    public Message() {
        this.messageID = generateMessageID();
    }

    public Message(String messageID, int messageNumber, String recipient, String message) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.message = message;
        this.messageHash = createMessageHash();
    }

    public String generateMessageID() {
        Random random = new Random();
        long number = 1_000_000_000L + (long) (random.nextDouble() * 9_000_000_000L);
        return String.valueOf(number);
    }

    public String messageIDCreatedMessage() {
        return "Message ID generated: " + messageID;
    }

    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    /**
     * Regex adapted from Oracle Java Pattern documentation:
     * https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/regex/Pattern.html
     */
    public String checkRecipientCell() {
        if (recipient != null && recipient.matches("^\\+27\\d{9}$")) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }

    public boolean isRecipientCellValid() {
        return "Cell phone number successfully captured.".equals(checkRecipientCell());
    }

    public String checkMessageLength() {
        if (message != null && message.length() <= 250) {
            return "Message ready to send.";
        }
        int extra = message == null ? 0 : message.length() - 250;
        return "Message exceeds 250 characters by " + extra + "; please reduce the size.";
    }

    public boolean isMessageLengthValid() {
        return message != null && message.length() <= 250;
    }

    public String createMessageHash() {
        if (messageID == null || messageID.length() < 2 || message == null || message.trim().isEmpty()) {
            messageHash = "";
            return messageHash;
        }

        String cleanMessage = message.trim();
        String[] words = cleanMessage.split("\\s+");
        String firstWord = removePunctuation(words[0]);
        String lastWord = removePunctuation(words[words.length - 1]);
        messageHash = (messageID.substring(0, 2) + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
        return messageHash;
    }

    private String removePunctuation(String word) {
        return word.replaceAll("[^a-zA-Z0-9]", "");
    }

    public String SentMessage(int userChoice) {
        switch (userChoice) {
            case 1:
                createMessageHash();
                sentMessages.add(this);
                totalMessagesSent++;
                return "Message successfully sent.";
            case 2:
                return "Press 0 to delete the message.";
            case 3:
                createMessageHash();
                storedMessages.add(this);
                storeMessage();
                return "Message successfully stored.";
            default:
                return "Invalid option selected.";
        }
    }

    public String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No sent messages to display.";
        }

        StringBuilder output = new StringBuilder();
        for (Message sentMessage : sentMessages) {
            output.append("Message ID: ").append(sentMessage.getMessageID()).append(System.lineSeparator());
            output.append("Message Hash: ").append(sentMessage.getMessageHash()).append(System.lineSeparator());
            output.append("Recipient: ").append(sentMessage.getRecipient()).append(System.lineSeparator());
            output.append("Message: ").append(sentMessage.getMessage()).append(System.lineSeparator());
        }
        return output.toString().trim();
    }

    public int returnTotalMessagess() {
        return totalMessagesSent;
    }

    /**
     * JSON file writing adapted from Oracle FileWriter documentation:
     * https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/io/FileWriter.html
     */
    public void storeMessage() {
        try {
            writeStoredMessagesAsJson();
        } catch (IOException exception) {
            System.out.println("Could not store message: " + exception.getMessage());
        }
    }

    private static void writeStoredMessagesAsJson() throws IOException {
        try (FileWriter writer = new FileWriter(JSON_FILE_NAME)) {
            writer.write("[" + System.lineSeparator());
            for (int i = 0; i < storedMessages.size(); i++) {
                Message current = storedMessages.get(i);
                writer.write("  {" + System.lineSeparator());
                writer.write("    \"messageID\": \"" + escapeJson(current.getMessageID()) + "\"," + System.lineSeparator());
                writer.write("    \"messageHash\": \"" + escapeJson(current.getMessageHash()) + "\"," + System.lineSeparator());
                writer.write("    \"sender\": \"" + escapeJson(current.getSender()) + "\"," + System.lineSeparator());
                writer.write("    \"recipient\": \"" + escapeJson(current.getRecipient()) + "\"," + System.lineSeparator());
                writer.write("    \"message\": \"" + escapeJson(current.getMessage()) + "\"" + System.lineSeparator());
                writer.write("  }");
                if (i < storedMessages.size() - 1) {
                    writer.write(",");
                }
                writer.write(System.lineSeparator());
            }
            writer.write("]" + System.lineSeparator());
        }
    }

    private static String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    public static void resetMessagesForTesting() {
        totalMessagesSent = 0;
        sentMessages.clear();
        storedMessages.clear();
        File file = new File(JSON_FILE_NAME);
        if (file.exists()) {
            file.delete();
        }
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public void setMessageNumber(int messageNumber) {
        this.messageNumber = messageNumber;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageHash() {
        if (messageHash == null || messageHash.isEmpty()) {
            return createMessageHash();
        }
        return messageHash;
    }

    public void setMessageHash(String messageHash) {
        this.messageHash = messageHash;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
