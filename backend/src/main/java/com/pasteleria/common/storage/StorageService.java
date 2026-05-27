package com.pasteleria.common.storage;

import java.nio.file.Path;

public interface StorageService {

  StoredFile store(StoreFileCommand command);

  Path resolveExisting(String relativePath);
}
