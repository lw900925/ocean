package io.lw900925.ocean.restful.controller;

import io.lw900925.ocean.core.model.entity.Role;
import io.lw900925.ocean.restful.service.RoleService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
@Api(tags = "Role", description = "角色")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @ApiOperation(value = "角色列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "search", value = "搜索关键字", dataType = "string", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "page", value = "页码", dataType = "int", dataTypeClass = Integer.class, defaultValue = "0", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "每页大小", dataType = "int", dataTypeClass = Integer.class, defaultValue = "20", paramType = "query"),
            @ApiImplicitParam(name = "sort", value = "排序", dataType = "string", dataTypeClass = String.class, defaultValue = "authority,ASC", paramType = "query")
    })
    @GetMapping
    public ResponseEntity<Page<Role>> page(@RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(roleService.page(search, pageable));
    }

    @ApiOperation(value = "获取角色")
    @GetMapping("/{authority}")
    public ResponseEntity<Role> get(@ApiParam(value = "角色名称", required = true) @PathVariable String authority) {
        return ResponseEntity.ok(roleService.get(authority));
    }

    @ApiOperation(value = "新建角色")
    @PostMapping
    public ResponseEntity<Role> create(@RequestBody @Validated Role role) {
        return ResponseEntity.ok(roleService.saveOrUpdate(role));
    }

    @ApiOperation(value = "更新角色")
    @PutMapping
    public ResponseEntity<Role> update(@RequestBody @Validated Role role) {
        return ResponseEntity.ok(roleService.saveOrUpdate(role));
    }

    @ApiOperation(value = "删除角色")
    @DeleteMapping("/{authority}")
    public ResponseEntity<Role> delete(@ApiParam(value = "角色名称", required = true) @PathVariable String authority) {
        return ResponseEntity.ok(roleService.delete(authority));
    }
}
