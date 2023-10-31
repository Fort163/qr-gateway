package com.quick.recording.gateway.service.company;

import com.quick.recording.gateway.dto.company.CompanyDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "COMPANY-SERVICE",contextId = "company", path = "/company")
public interface CompanyController {

    @GetMapping("/")
    ResponseEntity<CompanyDto> getCompany();

    @GetMapping("/list")
    ResponseEntity<List<CompanyDto>> getCompanyList();

}
