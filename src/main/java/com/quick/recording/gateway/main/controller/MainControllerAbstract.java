package com.quick.recording.gateway.main.controller;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.nimbusds.jose.util.ArrayUtils;
import com.quick.recording.gateway.config.error.exeption.BuildClassException;
import com.quick.recording.gateway.dto.BaseDto;
import com.quick.recording.gateway.dto.SmartDto;
import com.quick.recording.gateway.dto.util.*;
import com.quick.recording.gateway.entity.SmartEntity;
import com.quick.recording.gateway.enumerated.Delete;
import com.quick.recording.gateway.main.service.local.MainService;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public abstract class MainControllerAbstract<Dto extends SmartDto, Entity extends SmartEntity,
        Service extends MainService<Entity, Dto>>
        implements MainController<Dto> {

    private final String PERMISSION_TEMPLATE = "%s_%s";
    private final String PERMISSION_POSTFIX_READ = "READ";
    private final String PERMISSION_POSTFIX_CREATE = "CREATE";
    private final String PERMISSION_POSTFIX_PUT = "PUT";
    private final String PERMISSION_POSTFIX_PATCH = "PATCH";
    private final String PERMISSION_POSTFIX_DELETE = "DELETE";
    private final String PERMISSION_POSTFIX_ALL = "ALL";
    protected final String PERMISSION_ALL_READ = "ALL_READ";
    protected final String PERMISSION_ALL_CREATE = "ALL_CREATE";
    protected final String PERMISSION_ALL_PUT = "ALL_PUT";
    protected final String PERMISSION_ALL_PATCH = "ALL_PATCH";
    protected final String PERMISSION_ALL_DELETE = "ALL_DELETE";
    protected final String permissionRead;
    protected final String permissionCreate;
    protected final String permissionPut;
    protected final String permissionPatch;
    protected final String permissionDelete;
    protected final String permissionAll;
    protected final String[] permissionReadDefaultArray;
    protected final String[] permissionCreateDefaultArray;
    protected final String[] permissionPutDefaultArray;
    protected final String[] permissionPatchDefaultArray;
    protected final String[] permissionDeleteDefaultArray;
    protected final String roleName;
    protected final Service service;

    public MainControllerAbstract() {
        throw new BuildClassException("Call empty constructor in class MainControllerAbstract");
    }

    public MainControllerAbstract(Service service) {
        this.service = service;
        this.roleName = getRoleName();
        this.permissionRead = String.format(PERMISSION_TEMPLATE, roleName, PERMISSION_POSTFIX_READ);
        this.permissionCreate = String.format(PERMISSION_TEMPLATE, roleName, PERMISSION_POSTFIX_CREATE);
        this.permissionPut = String.format(PERMISSION_TEMPLATE, roleName, PERMISSION_POSTFIX_PUT);
        this.permissionPatch = String.format(PERMISSION_TEMPLATE, roleName, PERMISSION_POSTFIX_PATCH);
        this.permissionDelete = String.format(PERMISSION_TEMPLATE, roleName, PERMISSION_POSTFIX_DELETE);
        this.permissionAll = String.format(PERMISSION_TEMPLATE, roleName, PERMISSION_POSTFIX_ALL);
        this.permissionReadDefaultArray = new String[]{permissionRead, PERMISSION_ALL_READ, permissionAll};
        this.permissionCreateDefaultArray = new String[]{permissionCreate, PERMISSION_ALL_CREATE, permissionAll};
        this.permissionPutDefaultArray = new String[]{permissionPut, PERMISSION_ALL_PUT, permissionAll};
        this.permissionPatchDefaultArray = new String[]{permissionPatch, permissionPut, PERMISSION_ALL_PUT,
                PERMISSION_ALL_PATCH, permissionAll};
        this.permissionDeleteDefaultArray = new String[]{permissionDelete, PERMISSION_ALL_DELETE, permissionAll};
    }

    @Override
    @GetMapping({"/{uuid}"})
    @PreAuthorize("hasAnyAuthority(#root.this.byUuidAuthority())")
    public ResponseEntity<Dto> byUuid(@PathVariable @NotNull(message = "{validation.uuid}") UUID uuid) {
        return ResponseEntity.ok(getService().byUuid(uuid));
    }

    @Override
    @GetMapping
    @PreAuthorize("hasAnyAuthority(#root.this.searchAuthority())")
    public Page<Dto> list(@SpringQueryMap @Validated({List.class}) Dto search, Pageable pageable) {
        return getService().list(search, pageable);
    }

    @Override
    @PostMapping("/search")
    @PreAuthorize("hasAnyAuthority(#root.this.searchAuthority())")
    public ResponseEntity<Collection<Dto>> search(@RequestBody @Validated({Search.class}) Dto dto) {
        return ResponseEntity.ok(getService().search(dto));
    }

    @Override
    @PostMapping
    @PreAuthorize("hasAnyAuthority(#root.this.postAuthority())")
    public ResponseEntity<Dto> post(@RequestBody @Validated({Post.class}) Dto dto) {
        return ResponseEntity.ok(getService().post(dto));
    }

    @Override
    @PutMapping({"/patch"})
    @PreAuthorize("hasAnyAuthority(#root.this.patchAuthority())")
    public ResponseEntity<Dto> patch(@RequestBody @Validated({Patch.class}) Dto dto) {
        return ResponseEntity.ok(getService().patch(dto));
    }

    @Override
    @PutMapping
    @PreAuthorize("hasAnyAuthority(#root.this.putAuthority())")
    public ResponseEntity<Dto> put(@RequestBody @Validated({Put.class}) Dto dto) {
        return ResponseEntity.ok(getService().put(dto));
    }

    @Override
    @DeleteMapping({"/{uuid}"})
    @PreAuthorize("hasAnyAuthority(#root.this.deleteAuthority())")
    public ResponseEntity<Dto> delete(@PathVariable @NotNull(message = "{validation.uuid}") UUID uuid,
                                      @RequestParam(name = "delete")
                                      @NotNull(message = "{validation.description}") Delete delete) {
        return ResponseEntity.ok(getService().delete(uuid, delete));
    }

    @Override
    @PutMapping({"/{uuid}"})
    @PreAuthorize("hasAnyAuthority(#root.this.restoreAuthority())")
    public ResponseEntity<Dto> restore(@PathVariable @NotNull(message = "{validation.uuid}") UUID uuid) {
        return ResponseEntity.ok(getService().restore(uuid));
    }

    /*
         Override these methods if you need
         different authorization settings.
     */

    public final String[] byUuidAuthority() {
        return arrayConcat(getPermissionReadDefaultArray(), additionalByUuidAuthority());
    }

    public final String[] searchAuthority() {
        return arrayConcat(getPermissionReadDefaultArray(), additionalSearchAuthority());
    }

    public final String[] postAuthority() {
        return arrayConcat(getPermissionCreateDefaultArray(), additionalPostAuthority());
    }

    public final String[] patchAuthority() {
        return arrayConcat(getPermissionPatchDefaultArray(), additionalPatchAuthority());
    }

    public final String[] putAuthority() {
        return arrayConcat(getPermissionPutDefaultArray(), additionalPutAuthority());
    }

    public final String[] deleteAuthority() {
        return arrayConcat(getPermissionDeleteDefaultArray(), additionalDeleteAuthority());
    }

    public final String[] restoreAuthority() {
        return arrayConcat(getPermissionDeleteDefaultArray(), additionalRestoreAuthority());
    }

    public String[] additionalByUuidAuthority() {
        return new String[0];
    }

    public String[] additionalSearchAuthority() {
        return new String[0];
    }

    public String[] additionalPostAuthority() {
        return new String[0];
    }

    public String[] additionalPatchAuthority() {
        return new String[0];
    }

    public String[] additionalPutAuthority() {
        return new String[0];
    }

    public String[] additionalDeleteAuthority() {
        return new String[0];
    }

    public String[] additionalRestoreAuthority() {
        return new String[0];
    }

    public Service getService() {
        return service;
    }

    public String getPermissionRead() {
        return permissionRead;
    }

    public String getPermissionCreate() {
        return permissionCreate;
    }

    public String getPermissionPut() {
        return permissionPut;
    }

    public String getPermissionPatch() {
        return permissionPatch;
    }

    public String getPermissionDelete() {
        return permissionDelete;
    }

    public String getPermissionAll() {
        return permissionAll;
    }

    public String[] getPermissionReadDefaultArray() {
        return permissionReadDefaultArray;
    }

    public String[] getPermissionCreateDefaultArray() {
        return permissionCreateDefaultArray;
    }

    public String[] getPermissionPutDefaultArray() {
        return permissionPutDefaultArray;
    }

    public String[] getPermissionPatchDefaultArray() {
        return permissionPatchDefaultArray;
    }

    public String[] getPermissionDeleteDefaultArray() {
        return permissionDeleteDefaultArray;
    }


    private String[] arrayConcat(String[] defaultAuthority, String[] additionalAuthority) {
        if (Objects.nonNull(additionalAuthority) && additionalAuthority.length > 0) {
            return ArrayUtils.concat(defaultAuthority, additionalAuthority);
        }
        return defaultAuthority;
    }

    private String getRoleName() {
        try {
            Class<? extends BaseDto> dtoClass = getType();
            String[] splitDtoClass = dtoClass.getName().split("\\.");
            String className = splitDtoClass[splitDtoClass.length - 1];
            if (className.endsWith("Dto")) {
                className = className.substring(0, className.length() - 3);
                className = PropertyNamingStrategies.SnakeCaseStrategy.INSTANCE.translate(className);
                return className.toUpperCase(Locale.ROOT);
            }
            return "MAIN";
        } catch (Exception e) {
            return "MAIN";
        }
    }
}
