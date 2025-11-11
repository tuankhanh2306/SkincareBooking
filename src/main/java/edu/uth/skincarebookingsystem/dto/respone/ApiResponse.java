package edu.uth.skincarebookingsystem.dto.respone;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class ApiResponse<T> {
    private int code;
    private String message;
    private T result;
}