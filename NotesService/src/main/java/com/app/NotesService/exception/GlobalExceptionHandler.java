package com.app.NotesService.exception;

import com.app.NotesService.model.ApiError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NoteNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiError> handleNoteNotFoundException(NoteNotFoundException exception){

        logger.error(exception.getMessage());

        ApiError errorResponse = new ApiError();

        String message = exception.getMessage();
        int statusCode = HttpStatus.NOT_FOUND.value();

        errorResponse.setMessage(message);
        errorResponse.setStatusCode(statusCode);

        return new ResponseEntity<ApiError>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmptyContentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> handleEmptyContentException(EmptyContentException exception){

        logger.error(exception.getMessage());

        ApiError errorResponse = new ApiError();

        String message = exception.getMessage();
        int statusCode = HttpStatus.BAD_REQUEST.value();

        errorResponse.setMessage(message);
        errorResponse.setStatusCode(statusCode);

        return new ResponseEntity<ApiError>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {

        logger.error(exception.getMessage());

        ApiError errorResponse = new ApiError();

        String argumentName = exception.getName();
        String type = exception.getRequiredType().getSimpleName();

        String message = "Incorrect data type provided for " + argumentName + ". It should be of type: " + type;
        int statusCode = HttpStatus.BAD_REQUEST.value();

        errorResponse.setMessage(message);
        errorResponse.setStatusCode(statusCode);

        return new ResponseEntity<ApiError>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
