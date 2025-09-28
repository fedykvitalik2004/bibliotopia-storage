package org.vitalii.fedyk.minio.usecase;

import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;
import org.vitalii.fedyk.minio.model.MinIoObjectInfo;

/** A use case interface for handling file storage operations. */
public interface MinIoObjectInfoUseCase {

  /**
   * Saves a file associated with the specified application name.
   *
   * @param appName the name of the application the file belongs to
   * @param data the file data to be saved, as a {@link MultipartFile}
   * @return a {@link MinIoObjectInfo} object containing information about the stored file
   */
  MinIoObjectInfo save(String appName, MultipartFile data);

  /**
   * Retrieves information about a stored file by its unique identifier.
   *
   * @param id the unique identifier of the stored file
   * @return a {@link MinIoObjectInfo} object containing information about the file, or {@code null}
   *     if no file with the given ID exists
   */
  MinIoObjectInfo findById(UUID id);
}
