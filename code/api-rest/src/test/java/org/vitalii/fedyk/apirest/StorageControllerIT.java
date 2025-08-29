package org.vitalii.fedyk.apirest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.vitalii.fedyk.apirest.storage.StorageController;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WebMvcTest(StorageController.class)
@ContextConfiguration(classes = StorageController.class)
class StorageControllerIT {
  @Test
  void a () {
    final StorageController storageController = new StorageController();

    System.out.println("1122");
    assertEquals(202, storageController.uploadFile(null, null, null).getStatusCode().value());
  }
}
