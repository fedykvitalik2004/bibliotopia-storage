package org.vitalii.fedyk.compression;

import java.util.Set;

/** Generic port interface for a data or file compressor. */
public interface CompressorPort {
  Set<String> getSupportedNames();
}
