package com.quick.recording.gateway.dto.employee;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class ProfessionDto {

  private UUID uuid;
  @NotNull(
      message = "Name is required"
  )
  private String name;
  @NotNull(
      message = "Description is required"
  )
  private String description;
  private Boolean isActive = true;

}
