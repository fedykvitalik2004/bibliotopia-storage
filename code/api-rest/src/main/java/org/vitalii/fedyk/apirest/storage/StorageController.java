package org.vitalii.fedyk.apirest.storage;

import org.openapitools.api.StorageApi;
import org.openapitools.model.UploadFile200Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class StorageController implements StorageApi {
  @Override
  public ResponseEntity<UploadFile200Response> uploadFile(MultipartFile file, String storageType, String duration) {
    return StorageApi.super.uploadFile(file, storageType, duration);
  }
}
