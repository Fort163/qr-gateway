package com.quick.recording.gateway.service.company;

import com.quick.recording.gateway.dto.company.CompanyDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "COMPANY-SERVICE",contextId = "company", path = "/company")
public interface CompanyController {

    @GetMapping("/")
    @PreAuthorize("hasAnyAuthority('ROLE_WRITE')")
    ResponseEntity<CompanyDto> getCompany();

    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('ROLE_READ')")
    ResponseEntity<List<CompanyDto>> getCompanyList();

}
