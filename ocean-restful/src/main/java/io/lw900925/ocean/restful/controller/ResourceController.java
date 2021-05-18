package io.lw900925.ocean.restful.controller;

import io.lw900925.ocean.core.model.entity.Resource;
import io.swagger.annotations.*;
import io.lw900925.ocean.restful.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/resources")
@Api(tags = "Resource", description = "资源")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @ApiOperation(value = "资源列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "search", value = "搜索关键字", dataType = "string", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "page", value = "页码", dataType = "int", dataTypeClass = Integer.class, defaultValue = "0", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "每页大小", dataType = "int", dataTypeClass = Integer.class, defaultValue = "20", paramType = "query"),
            @ApiImplicitParam(name = "sort", value = "排序", dataType = "string", dataTypeClass = String.class, defaultValue = "oid,ASC", paramType = "query")
    })
    @GetMapping
    public ResponseEntity<Page<Resource>> page(@RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(resourceService.page(search, pageable));
    }

    @ApiOperation(value = "获取资源")
    @GetMapping("/{resourceId}")
    public ResponseEntity<Resource> get(@PathVariable("resourceId") Long resourceId) {
        return ResponseEntity.ok(resourceService.get(resourceId));
    }

    @ApiOperation(value = "新建资源")
    @PostMapping
    public ResponseEntity<Resource> save(@RequestBody @Validated Resource resource) {
        return ResponseEntity.ok(resourceService.save(resource));
    }

    @ApiOperation(value = "更新资源")
    @PutMapping
    public ResponseEntity<Resource> update(@RequestBody @Validated Resource resource) {
        return ResponseEntity.ok(resourceService.update(resource));
    }

    @ApiOperation(value = "删除资源")
    @DeleteMapping("/{resourceId}")
    public ResponseEntity<Void> delete(@PathVariable("resourceId") Long resourceId) {
        resourceService.delete(resourceId);
        return ResponseEntity.ok(null);
    }

    @ApiOperation(value = "资源授权")
    @PutMapping("/{resourceId}/grant")
    public ResponseEntity<Resource> grant(@ApiParam(name = "resourceId", value = "资源ID", required = true) @PathVariable("resourceId") Long resourceId,
                                          @ApiParam(name = "authority", value = "角色名", required = true) @RequestParam String authority) {
        return ResponseEntity.ok(resourceService.grant(resourceId, authority));
    }

    @ApiOperation(value = "回收权限")
    @PutMapping("/{resourceId}/revoke")
    public ResponseEntity<Resource> revoke(@ApiParam(name = "resourceId", value = "资源ID", required = true) @PathVariable("resourceId") Long resourceId,
                                          @ApiParam(name = "authority", value = "角色名", required = true) @RequestParam String authority) {
        return ResponseEntity.ok(resourceService.revoke(resourceId, authority));
    }
}
