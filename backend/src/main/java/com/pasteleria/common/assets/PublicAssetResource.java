package com.pasteleria.common.assets;

import java.nio.file.Path;

public record PublicAssetResource(
    Path path,
    String filename,
    String contentType
) {
}
