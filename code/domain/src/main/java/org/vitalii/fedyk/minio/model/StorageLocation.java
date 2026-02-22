package org.vitalii.fedyk.minio.model;

import java.util.Objects;
import lombok.Builder;

/**
 * A record representing the storage location of an object in a bucket.
 *
 * @param bucketName the name of the bucket where the object is stored
 * @param objectKey the name of the object within the bucket
 */
@Builder
public record StorageLocation(String bucketName, String objectKey) {
  /**
   * The compact constructor for {@code StorageLocation}. This constructor is implicitly called to
   * validate the state of the record fields upon creation. It ensures that both {@code bucketName}
   * and {@code objectKey} are not null and not empty after trimming whitespace.
   *
   * @throws NullPointerException if {@code bucketName} or {@code objectKey} is null
   * @throws IllegalArgumentException if {@code bucketName} or {@code objectKey} is empty or blank
   */
  public StorageLocation {
    Objects.requireNonNull(bucketName, "Bucket name cannot be null");
    Objects.requireNonNull(objectKey, "Object key cannot be null");

    if (bucketName.trim().isEmpty()) {
      throw new IllegalArgumentException("Bucket name cannot be empty");
    }
    if (objectKey.trim().isEmpty()) {
      throw new IllegalArgumentException("Object key cannot be empty");
    }
  }
}
