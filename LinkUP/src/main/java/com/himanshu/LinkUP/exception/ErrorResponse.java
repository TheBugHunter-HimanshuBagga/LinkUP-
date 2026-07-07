package com.himanshu.LinkUP.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@Builder
public class ErrorResponse {
     private Boolean success;
     private int status;
     private String message;
     private LocalDateTime timestamp;
}

/*
User not found
{
  "success": false,
  "status": ...,
  "message": "...",
  "timestamp": "..."
}
 */
