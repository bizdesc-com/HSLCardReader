package com.bizdesc.cardapi;

/**
 * Created by birhanu on 12/7/15.
 */
public class CardException extends RuntimeException {
  private boolean isInternalError;

  public boolean isInternalError() {
    return isInternalError;
  }

  public CardException(String detailMessage, boolean isInternalError) {
    this(detailMessage, null, isInternalError);
  }

  public CardException (String detailMessage, Throwable throwable, boolean isInternalError) {
    super(detailMessage, throwable);
    this.isInternalError = isInternalError;
  }
}
