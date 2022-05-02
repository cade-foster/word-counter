package com.counter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

/**
 * Database model representing a {@link Message} ID and the count of the words that message contains.
 */
@Entity
public class IdCount {
    @Id
    private String id;

    private Long count;

    public IdCount() {}

    public IdCount(String id, Long count) {
        this.id = id;
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IdCount idCount = (IdCount) o;
        return id.equals(idCount.id) && count.equals(idCount.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, count);
    }

    @Override
    public String toString() {
        return "IdCount{" +
                "id='" + id + '\'' +
                ", count=" + count +
                '}';
    }
}
