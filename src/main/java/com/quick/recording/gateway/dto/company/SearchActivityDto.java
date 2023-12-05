package com.quick.recording.gateway.dto.company;

import lombok.Data;

@Data
public class SearchActivityDto {

    private String name;
    private String description;
    private Boolean isActive = true;

}
