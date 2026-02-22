package org.vitalii.fedyk.generation.model;

import java.io.InputStream;

/** Represents a file with its content and extension. */
public record FileInfo(String extension, long length, InputStream content) {}
