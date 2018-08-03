package org.matrixstudio.ocean.restful.controller;

import io.swagger.annotations.*;
import org.matrixstudio.ocean.core.model.entity.Role;
import org.matrixstudio.ocean.restful.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
@Api(description = "角色", tags = "Role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @ApiOperation(value = "角色列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "search", value = "搜索关键字", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "页码", dataType = "int", defaultValue = "0", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "每页大小", dataType = "int", defaultValue = "20", paramType = "query"),
            @ApiImplicitParam(name = "sort", value = "排序", dataType = "string", defaultValue = "authority,ASC", paramType = "query")
    })
    @GetMapping
    public ResponseEntity<Page<Role>> list(@RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(roleService.list(search, pageable));
    }

    @ApiOperation(value = "获取角色")
    @GetMapping("/{authority}")
    public ResponseEntity<Role> get(@ApiParam(value = "角色名称", required = true) @PathVariable String authority) {
        return ResponseEntity.ok(roleService.get(authority));
    }

    @ApiOperation(value = "新建角色")
    @PostMapping
    public ResponseEntity<Role> create(@RequestBody @Validated Role role) {
        return ResponseEntity.ok(roleService.create(role));
    }

    @ApiOperation(value = "更新角色")
    @PutMapping
    public ResponseEntity<Role> update(@RequestBody @Validated Role role) {
        return ResponseEntity.ok(roleService.update(role));
    }

    @ApiOperation(value = "删除角色")
    @DeleteMapping("/{authority}")
    public ResponseEntity<Role> delete(@ApiParam(value = "角色名称", required = true) @PathVariable String authority) {
        return ResponseEntity.ok(roleService.delete(authority));
    }
}
