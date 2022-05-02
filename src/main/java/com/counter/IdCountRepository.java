package com.counter;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * {@link JpaRepository} for {@link IdCount} objects.
 */
public interface IdCountRepository extends JpaRepository<IdCount, String> {
}
