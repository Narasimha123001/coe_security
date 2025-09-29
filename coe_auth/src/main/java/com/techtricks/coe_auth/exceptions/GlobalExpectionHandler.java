//package com.techtricks.coe_auth.exceptions;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.Map;
//
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    // Handle user already present
//    @ExceptionHandler(UserAlreadyPresentException.class)
//    public ResponseEntity<Map<String, Object>> handleUserAlreadyPresent(UserAlreadyPresentException ex) {
//        Map<String, Object> response = new HashMap<>();
//        response.put("timestamp", LocalDateTime.now());
//        response.put("status", HttpStatus.CONFLICT.value()); // 409
//        response.put("error", "Conflict");
//        response.put("message", ex.getMessage());
//        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
//    }
////
////    // Handle validation or bad request
////    @ExceptionHandler(javax.validation.ConstraintViolationException.class)
////    public ResponseEntity<Map<String, Object>> handleValidationExceptions(javax.validation.ConstraintViolationException ex) {
////        Map<String, Object> response = new HashMap<>();
////        response.put("timestamp", LocalDateTime.now());
////        response.put("status", HttpStatus.BAD_REQUEST.value()); // 400
//        response.put("error", "Bad Request");
//       response.put("message", ex.getMessage());
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//    }
//
//    // Handle other generic exceptions
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Map<String, Object>> handleAllExceptions(Exception ex) {
//        Map<String, Object> response = new HashMap<>();
//        response.put("timestamp", LocalDateTime.now());
//        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value()); // 500
//        response.put("error", "Internal Server Error");
//        response.put("message", ex.getMessage());
//        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//}
