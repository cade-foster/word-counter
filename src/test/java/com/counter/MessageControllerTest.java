package com.counter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * JUnit tests for {@link MessageController}.
 */
@RunWith(MockitoJUnitRunner.class)
public class MessageControllerTest {
    @Mock
    IdCountRepository repo;

    @InjectMocks
    private MessageController messageController;

    /**
     * When: {@link MessageController#count} is called with a {@code null} {@link Message}
     *
     * Then: ensure it returns a "bad request" response.
     */
    @Test
    public void test_count_nullMessage_badRequest() {
        ResponseEntity actual = messageController.count(null);

        ResponseEntity expected = ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create()
                        .withTitle("Message must not be null.")
                        .withDetail("Unable to process null message."));

        assertEquals(expected, actual);
    }

    /**
     * When: {@link MessageController#count} is called with a {@link Message} containing a {@code null} id
     *
     * Then: ensure it returns a "bad request" response.
     */
    @Test
    public void test_count_nullMessageId_badRequest() {
        ResponseEntity actual = messageController.count(new Message(null, ""));

        ResponseEntity expected = ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create()
                        .withTitle("Message id must not be null.")
                        .withDetail("Unable to process message with text: \'\' due to the message id being null."));

        assertEquals(expected, actual);
    }

    /**
     * When: {@link MessageController#count} is called with {@link Message} requests containing 0 words (blank, {@code
     * null}, or empty messages)
     *
     * Then: ensure the returned count is not incremented.
     */
    @Test
    public void test_count_blankOrNullMessage_countNotIncremented() {
        Message emptyMessage = new Message("id1", "");
        Message blankMessage = new Message("id2", "            ");
        Message nullMessage = new Message("id3", null);

        when(repo.existsById(anyString())).thenReturn(false);

        messageController.count(emptyMessage);
        messageController.count(blankMessage);
        ResponseEntity responseEntity = messageController.count(nullMessage);
        Count response = (Count) responseEntity.getBody();

        assertEquals(Long.valueOf(0), response.getCount());
    }

    /**
     * When: {@link MessageController#count} is called with a previously processed {@link Message}
     *
     * Then: ensure the returned count is not incremented.
     */
    @Test
    public void test_count_previouslyProcessedMessage_countNotIncremented() {
        Message message = new Message("duplicate", "test message");

        when(repo.existsById(message.getId())).thenReturn(false, true);

        messageController.count(message);
        ResponseEntity responseEntity = messageController.count(message);
        Count response = (Count) responseEntity.getBody();

        assertEquals(Long.valueOf(2), response.getCount());
        verify(repo, times(2)).existsById(message.getId());
        verify(repo, times(1)).save(any());
    }

    /**
     * When: {@link MessageController#count} is called with unique {@link Message} requests
     *
     * Then: ensure the returned count is incremented each time.
     */
    @Test
    public void test_count_uniqueMessageIds_countIncremented() {
        Message message1 = new Message("unique 1", "test message");
        Message message2 = new Message("unique 2", "the second test message");

        when(repo.existsById(anyString())).thenReturn(false);

        ResponseEntity responseEntity1 = messageController.count(message1);
        Count response1 = (Count) responseEntity1.getBody();

        assertEquals(Long.valueOf(2), response1.getCount());

        ResponseEntity responseEntity2 = messageController.count(message2);
        Count response2 = (Count) responseEntity2.getBody();

        assertEquals(Long.valueOf(6), response2.getCount());

        verify(repo).existsById(message1.getId());
        verify(repo).existsById(message2.getId());
        verify(repo).save(eq(new IdCount(message1.getId(), 2L)));
        verify(repo).save(eq(new IdCount(message2.getId(), 4L)));
    }
}
