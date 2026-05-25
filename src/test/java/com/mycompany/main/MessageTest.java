package com.mycompany.main;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    @BeforeEach
    public void setup() {
        Message.resetMessagesForTesting();
    }

    @Test
    public void testMessageLengthSuccess() {
        Message message = new Message("0012345678", 0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message ready to send.", message.checkMessageLength());
    }

    @Test
    public void testMessageLengthFailure() {
        String longMessage = "A".repeat(255);
        Message message = new Message("0012345678", 0, "+27718693002", longMessage);
        assertEquals("Message exceeds 250 characters by 5; please reduce the size.", message.checkMessageLength());
    }

    @Test
    public void testRecipientNumberCorrectlyFormatted() {
        Message message = new Message("0012345678", 0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Cell phone number successfully captured.", message.checkRecipientCell());
    }

    @Test
    public void testRecipientNumberIncorrectlyFormatted() {
        Message message = new Message("0012345678", 1, "08575975889", "Hi Keegan, did you receive the payment?");
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.", message.checkRecipientCell());
    }

    @Test
    public void testMessageHashIsCorrect() {
        Message message = new Message("0012345678", 0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("00:0:HITONIGHT", message.createMessageHash());
    }

    @Test
    public void testMessageHashesInLoop() {
        Message[] messages = {
            new Message("0012345678", 0, "+27718693002", "Hi Mike, can you join us for dinner tonight?"),
            new Message("1112345678", 1, "+27838968976", "Hello John")
        };

        String[] expectedHashes = {
            "00:0:HITONIGHT",
            "11:1:HELLOJOHN"
        };

        for (int i = 0; i < messages.length; i++) {
            assertEquals(expectedHashes[i], messages[i].createMessageHash());
        }
    }

    @Test
    public void testMessageIDIsCreated() {
        Message message = new Message();
        assertTrue(message.checkMessageID());
        assertEquals(10, message.getMessageID().length());
        assertTrue(message.messageIDCreatedMessage().startsWith("Message ID generated: "));
    }

    @Test
    public void testMessageSuccessfullySent() {
        Message message = new Message("0012345678", 0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message successfully sent.", message.SentMessage(1));
        assertEquals(1, message.returnTotalMessagess());
    }

    @Test
    public void testMessageDisregarded() {
        Message message = new Message("0012345678", 1, "08575975889", "Hi Keegan, did you receive the payment?");
        assertEquals("Press 0 to delete the message.", message.SentMessage(2));
    }

    @Test
    public void testMessageStored() {
        Message message = new Message("0012345678", 0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message successfully stored.", message.SentMessage(3));
    }
}
