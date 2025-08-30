package com.project.commons.rest.outbound.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"requestId", "statusCode"})
public class Header {
    @JsonProperty(value = "requestId")
    private String requestId;
    @JsonProperty(value = "statusCode")
    private String applicationCode;
}
