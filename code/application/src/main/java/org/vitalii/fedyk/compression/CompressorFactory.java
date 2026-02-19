package org.vitalii.fedyk.compression;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/** Factory for obtaining an {@link ImageCompressorPort} implementation based on the file format. */
@Component
public class CompressorFactory {
  private final Map<String, ImageCompressorPort> compressors;

  /**
   * Constructs the factory with a list of available image compressors.
   *
   * @param strategyList the list of {@link ImageCompressorPort} implementations to register
   */
  public CompressorFactory(final List<ImageCompressorPort> strategyList) {
    this.compressors =
        strategyList.stream()
            .flatMap(
                strategy ->
                    strategy.getSupportedNames().stream().map(name -> Map.entry(name, strategy)))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  /** Returns the appropriate compressor for the given format. */
  public ImageCompressorPort getCompressorFor(final String fileFormat) {
    final ImageCompressorPort compressor = this.compressors.get(fileFormat.toLowerCase());
    if (compressor == null) {
      throw new IllegalArgumentException("Unsupported file format: " + fileFormat);
    }
    return compressor;
  }

  public boolean isAvailableFor(final String fileFormat) {
    return this.compressors.containsKey(fileFormat);
  }
}
