package com.counter;

import java.util.Objects;

/**
 * Represents a count of all the words in all the messages received by the application.
 */
public class Count {

    private final Long count;

    public Count(Long count) {
        this.count = count;
    }

    /**
     * @return The count of all the words in all the messages received by the application.
     */
    public Long getCount() {
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Count count1 = (Count) o;
        return getCount().equals(count1.getCount());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCount());
    }

    @Override
    public String toString() {
        return "Count{" +
                "count=" + count +
                '}';
    }
}
