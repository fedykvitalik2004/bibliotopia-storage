package org.vitalii.fedyk;

/** Represents a file with its content and extension. */
public record FileInfo(String extension, byte[] content) {}
