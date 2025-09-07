package org.vitalii.fedyk.minio.usecase;

import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;
import org.vitalii.fedyk.minio.model.MinIoObjectInfo;

/**
 * A use case interface for handling file storage operations.
 */
public interface MinIoObjectInfoUseCase {
  MinIoObjectInfo save(String appName, MultipartFile data);

  MinIoObjectInfo findById(UUID id);
}
