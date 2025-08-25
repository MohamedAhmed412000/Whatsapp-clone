package com.project.commons.rest.outbound.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
@JsonPropertyOrder({"message", "details"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorBody {
    @JsonProperty("message")
    private String message;
    @JsonProperty("details")
    private String[] details;
}
