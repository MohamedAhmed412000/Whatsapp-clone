package com.project.commons.rest.outbound;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.project.commons.rest.outbound.dto.Header;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"header", "body"})
public class RestResponse {
    @JsonProperty("header")
    private Header header;
    @JsonProperty("body")
    private Object body;
}
