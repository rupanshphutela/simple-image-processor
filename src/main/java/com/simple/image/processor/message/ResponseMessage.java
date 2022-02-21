package com.simple.image.processor.message;

import java.util.Map;

public class ResponseMessage {
  private Map<String, Object> message;
  public ResponseMessage(Map<String, Object> result) {
    this.message = result;
  }
  public Map<String, Object> getMessage() {
    return message;
  }
  public void setMessage(Map<String, Object> message) {
    this.message = message;
  }
}