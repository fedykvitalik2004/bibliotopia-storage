package org.vitalii.fedyk;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.vitalii.fedyk.minio.model.MinIoObjectInfo;
import org.vitalii.fedyk.minio.repository.MinIoObjectInfoRepository;
import org.vitalii.fedyk.minio.usecase.MinIoInteractionUseCase;
import org.vitalii.fedyk.minio.usecase.MinIoObjectInfoUseCase;

@Service
public class MinIoObjectInfoUseCaseImpl implements MinIoObjectInfoUseCase {
  private final MinIoInteractionUseCase minIoInteractionUseCase;
  private final MinIoObjectInfoRepository repository;

  @Autowired
  public MinIoObjectInfoUseCaseImpl(MinIoInteractionUseCase minIoInteractionUseCase, MinIoObjectInfoRepository repository) {
    this.minIoInteractionUseCase = minIoInteractionUseCase;
    this.repository = repository;
  }

  @Override
  public MinIoObjectInfo save(final String appName, final MultipartFile file) {
    final String fileName = UUID.randomUUID().toString();
    final MinIoObjectInfo minIoResponse = minIoInteractionUseCase.uploadFile(appName, fileName, file);

    return repository.save(minIoResponse);
  }

  @Override
  public MinIoObjectInfo findById(final UUID id) {
    final MinIoObjectInfo minIoObjectInfo = repository.findById(id)
            .orElseThrow();
    final String url = minIoInteractionUseCase.getUrl(minIoObjectInfo.getBucketName(), minIoObjectInfo.getObjectName());
    return minIoObjectInfo.toBuilder()
            .url(url)
            .build();
  }
}
