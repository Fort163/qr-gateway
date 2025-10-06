package com.quick.recording.gateway.main.controller;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.nimbusds.jose.util.ArrayUtils;
import com.quick.recording.gateway.config.error.exeption.BuildClassException;
import com.quick.recording.gateway.dto.BaseDto;
import com.quick.recording.gateway.dto.util.Patch;
import com.quick.recording.gateway.dto.util.Post;
import com.quick.recording.gateway.dto.util.Put;
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

import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public abstract class MainControllerAbstract<Dto extends BaseDto, Entity extends SmartEntity,
        Service extends MainService<Entity, Dto>>
        implements MainController<Dto> {

    private final String PERMISSION_TEMPLATE = "%s_%s";
    private final String PERMISSION_POSTFIX_READ = "READ";
    private final String PERMISSION_POSTFIX_CREATE = "CREATE";
    private final String PERMISSION_POSTFIX_EDIT = "EDIT";
    private final String PERMISSION_POSTFIX_DELETE = "DELETE";
    private final String PERMISSION_POSTFIX_ALL = "ALL";
    protected final String PERMISSION_ALL_READ = "ALL_READ";
    protected final String PERMISSION_ALL_CREATE = "ALL_CREATE";
    protected final String PERMISSION_ALL_EDIT = "ALL_EDIT";
    protected final String PERMISSION_ALL_DELETE = "ALL_DELETE";
    protected final String permissionRead;
    protected final String permissionCreate;
    protected final String permissionEdit;
    protected final String permissionDelete;
    protected final String permissionAll;
    protected final String[] permissionReadDefaultArray;
    protected final String[] permissionCreateDefaultArray;
    protected final String[] permissionEditDefaultArray;
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
        this.permissionEdit = String.format(PERMISSION_TEMPLATE, roleName, PERMISSION_POSTFIX_EDIT);
        this.permissionDelete = String.format(PERMISSION_TEMPLATE, roleName, PERMISSION_POSTFIX_DELETE);
        this.permissionAll = String.format(PERMISSION_TEMPLATE, roleName, PERMISSION_POSTFIX_ALL);
        this.permissionReadDefaultArray = new String[]{permissionRead, PERMISSION_ALL_READ, permissionAll};
        this.permissionCreateDefaultArray = new String[]{permissionCreate, PERMISSION_ALL_CREATE, permissionAll};
        this.permissionEditDefaultArray = new String[]{permissionEdit, PERMISSION_ALL_EDIT, permissionAll};
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
    public Page<Dto> search(@SpringQueryMap Dto search, Pageable pageable) {
        return getService().search(search, pageable);
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
    public ResponseEntity<Boolean> delete(@PathVariable @NotNull(message = "{validation.uuid}") UUID uuid,
                                          @RequestParam(name = "delete")
                                          @NotNull(message = "{validation.description}") Delete delete) {
        return ResponseEntity.ok(getService().delete(uuid, delete));
    }

    @Override
    @PutMapping({"/{uuid}"})
    @PreAuthorize("hasAnyAuthority(#root.this.restoreAuthority())")
    public ResponseEntity<Boolean> restore(@PathVariable @NotNull(message = "{validation.uuid}") UUID uuid) {
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
        return arrayConcat(getPermissionEditDefaultArray(), additionalPatchAuthority());
    }

    public final String[] putAuthority() {
        return arrayConcat(getPermissionEditDefaultArray(), additionalPutAuthority());
    }

    public final String[] deleteAuthority() {
        return arrayConcat(getPermissionEditDefaultArray(), additionalDeleteAuthority());
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

    public String getPermissionEdit() {
        return permissionEdit;
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

    public String[] getPermissionEditDefaultArray() {
        return permissionEditDefaultArray;
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
