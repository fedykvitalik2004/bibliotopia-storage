package org.vitalii.fedyk.compression;

import java.io.IOException;
import java.io.InputStream;

/** Port interface for image compression. */
public interface ImageCompressorPort extends CompressorPort {
  byte[] compress(final InputStream input) throws IOException;
}
