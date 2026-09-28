package com.ait.pase.orden_api.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ValidationErrror {
    private Map<String, String> errors;
}
