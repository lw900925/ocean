package io.lw900925.ocean.restful.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.lw900925.ocean.core.model.entity.SocialLink;
import io.lw900925.ocean.restful.service.SocialLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{username}/social_links")
@Api(tags = "SocialLink", description = "社交账号连接")
public class SocialLinkController {

    @Autowired
    private SocialLinkService socialLinkService;

    @ApiOperation(value = "社交账号连接列表")
    @GetMapping
    public ResponseEntity<List<SocialLink>> list(@ApiParam(value = "用户名", required = true) @PathVariable String username) {
        return ResponseEntity.ok(socialLinkService.list(username));
    }

    @ApiOperation(value = "获取社交账号连接")
    @GetMapping("/{socialLinkId}")
    public ResponseEntity<SocialLink> get(@ApiParam(value = "用户名", required = true) @PathVariable String username,
                                          @ApiParam(value = "社交账号连接ID", required = true) @PathVariable Long socialLinkId) {
        return ResponseEntity.ok(socialLinkService.get(socialLinkId));
    }

    @ApiOperation(value = "创建/更新社交账号连接")
    @PostMapping
    public ResponseEntity<SocialLink> save(@ApiParam(value = "用户名", required = true) @PathVariable String username,
                                             @RequestBody @Validated SocialLink socialLink) {
        socialLink.setUsername(username);
        return ResponseEntity.ok(socialLinkService.save(socialLink));
    }

    @ApiOperation(value = "删除社交账号连接")
    @DeleteMapping("/{socialLinkId}")
    public ResponseEntity<SocialLink> delete(@ApiParam(value = "用户名", required = true) @PathVariable String username,
                                             @ApiParam(value = "社交账号连接ID", required = true) @PathVariable Long socialLinkId) {
        return ResponseEntity.ok(socialLinkService.delete(socialLinkId));
    }
}
