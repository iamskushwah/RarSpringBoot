package com.wellsfargo.rarconsumer.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class ErrorMessage {
  private int statusCode;
  private Date timestamp;
  private String message;
  private String description;
}
