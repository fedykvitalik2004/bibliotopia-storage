package org.vitalii.fedyk.apirest.storage;

import org.openapitools.api.StorageApi;
import org.openapitools.model.UploadFile200Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;


// violation below 'The name of the outer type and the file do not match.'
@interface MyAnnotation2 {
  String name();

  int version();
}


@Controller
@MyAnnotation2(name = "F", version = 1)
public class StorageController implements StorageApi {
  @Override
  public ResponseEntity<UploadFile200Response> uploadFile(MultipartFile file, String storageType, String duration) {
               int tab4 = 1;
      return new ResponseEntity<>(HttpStatus.ACCEPTED);
  }
}
