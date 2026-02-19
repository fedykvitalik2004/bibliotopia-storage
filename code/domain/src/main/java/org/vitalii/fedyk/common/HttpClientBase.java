package org.vitalii.fedyk.common;

import java.net.http.HttpClient;

/**
 * Base class for HTTP clients.
 *
 * <p>Provides common fields and constructor for HTTP communication with a base URL. Subclasses can
 * extend this class to implement specific HTTP requests or integrations.
 */
public abstract class HttpClientBase {

  /** The base URL for all HTTP requests made by subclasses. */
  protected final String baseurl;

  /** The {@link HttpClient} instance used to execute HTTP requests. */
  protected final HttpClient client;

  /**
   * Constructs a new {@code HttpClientBase} with the specified base URL and HTTP client.
   *
   * @param baseurl the base URL to use for all HTTP requests
   * @param client the {@link HttpClient} instance used to perform HTTP calls
   */
  protected HttpClientBase(String baseurl, HttpClient client) {
    this.baseurl = baseurl;
    this.client = client;
  }
}
