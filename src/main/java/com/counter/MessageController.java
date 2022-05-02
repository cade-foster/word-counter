package com.counter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.StringTokenizer;

/**
 * Provides a REST API for processing {@link Message} objects.
 */
@RestController
public class MessageController {

    private Logger log = LoggerFactory.getLogger(MessageController.class);
    private long totalWordCount = 0;
    private final IdCountRepository repo;

    MessageController(IdCountRepository repo) {
        this.repo = repo;
    }

    /**
     * Accepts a {@link Message} and returns a {@link Count} that contains the total number of words contained in all
     * the messages received by the service at this point.
     *
     * @param message - The {@link Message} to count the words in.
     * @return A {@link ResponseEntity} either signifying a bad request or containing a {@link Count} of the total
     *         number of words contained in all the messages received by the service at this point.
     */
    @PostMapping("/count")
    ResponseEntity<?> count(@RequestBody Message message) {
        log.info("processing message: {}", message);

        if (message == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                    .body(Problem.create()
                            .withTitle("Message must not be null.")
                            .withDetail("Unable to process null message."));
        } else if (message.getId() == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                    .body(Problem.create()
                            .withTitle("Message id must not be null.")
                            .withDetail("Unable to process message with text: \'" + message.getMessage() +
                                    "\' due to the message id being null."));
        }

        final long wordCount = countWords(message.getMessage());
        incrementAndStore(wordCount, message.getId());
        return ResponseEntity.ok(new Count(totalWordCount));
    }

    /**
     * Counts the words in the given text.
     *
     * @param text - The text of words to be counted.
     * @return The number of words in the given text (0 for blank or null texts).
     */
    private long countWords(String text) {
        if (text == null || text.isBlank()) {
            return 0;
        }

        StringTokenizer tokenizer = new StringTokenizer(text);
        return tokenizer.countTokens();
    }

    /**
     * Synchronized method that increments the count for the total number of words in all the messages received by this
     * service up to this point and stores a new {@link IdCount} in the database.
     *
     * @param count - The number to increment the counter by.
     * @param id - The id of the {@link Message} to store in the database to prevent duplicate processing.
     */
    private synchronized void incrementAndStore(long count, String id) {
        if (!repo.existsById(id)) {
            repo.save(new IdCount(id, count));
            this.totalWordCount += count;
            log.info("Incremented count for message id: \'{}\' by {}. New count: {}", id, count, totalWordCount);
        } else {
            log.info("Message with id: \'{}\' has been previously processed. Count remaining at {}.", id, totalWordCount);
        }
    }
}
