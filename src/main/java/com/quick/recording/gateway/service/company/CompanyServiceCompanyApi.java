package com.quick.recording.gateway.service.company;

import com.quick.recording.gateway.dto.company.CompanyDto;
import com.quick.recording.gateway.dto.company.SearchCompanyDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "COMPANY-SERVICE", contextId = "company", path = "/api/v1/company")
public interface CompanyServiceCompanyApi {

    @GetMapping({"/{uuid}"})
    ResponseEntity<CompanyDto> byUuid(@PathVariable UUID uuid);

    @GetMapping
    Page<CompanyDto> list(@SpringQueryMap SearchCompanyDto searchCompanyDto, @SpringQueryMap Pageable pageable);

    @PostMapping
    ResponseEntity<CompanyDto> post(@RequestBody CompanyDto companyDto);

    @PutMapping({"/patch"})
    ResponseEntity<CompanyDto> patch(@RequestBody CompanyDto companyDto);

    @PutMapping
    ResponseEntity<CompanyDto> put(@RequestBody CompanyDto companyDto);

    @DeleteMapping({"/{uuid}"})
    ResponseEntity<Boolean> delete(@PathVariable UUID uuid);

}
