package org.matrixstudio.ocean.restful.controller;

import io.swagger.annotations.*;
import org.matrixstudio.ocean.core.model.entity.User;
import org.matrixstudio.ocean.restful.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@Api(description = "用户", tags = "User")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "search", value = "搜索关键字", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "页码", dataType = "int", defaultValue = "0", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "每页大小", dataType = "int", defaultValue = "20", paramType = "query"),
            @ApiImplicitParam(name = "sort", value = "排序", dataType = "string", defaultValue = "username,ASC", paramType = "query")
    })
    @GetMapping
    public ResponseEntity<Page<User>> list(@RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(userService.list(search, pageable));
    }

    @ApiOperation(value = "获取用户")
    @GetMapping("/{username}")
    public ResponseEntity<User> get(@ApiParam(value = "用户名", required = true) @PathVariable String username) {
        return ResponseEntity.ok(userService.get(username));
    }

    @ApiOperation(value = "新建用户")
    @PostMapping
    public ResponseEntity<User> create(@RequestBody @Validated User user) {
        return ResponseEntity.ok(userService.create(user));
    }

    @ApiOperation(value = "更新用户")
    @PutMapping
    public ResponseEntity<User> update(@RequestBody @Valid User user) {
        return ResponseEntity.ok(userService.update(user));
    }

    @ApiOperation(value = "注册")
    @PostMapping("/register")
    public ResponseEntity<User> register(@ApiParam(value = "邮箱", required = true) @RequestParam String email,
                                         @ApiParam(value = "密码", required = true) @RequestParam String password,
                                         @ApiParam(value = "用户名", required = true) @RequestParam String username) {
        return ResponseEntity.ok(userService.register(email, password, username));
    }

    @ApiOperation(value = "修改密码")
    @PatchMapping("/{username}/password")
    public ResponseEntity<User> password(@ApiParam(value = "用户名", required = true) @PathVariable String username,
                                         @ApiParam(value = "原密码", required = true) @RequestParam String originPassword,
                                         @ApiParam(value = "新密码", required = true) @RequestParam String newPassword) {
        return ResponseEntity.ok(userService.updatePassword(username, originPassword, newPassword));
    }

    @ApiOperation(value = "删除用户")
    @DeleteMapping("/{username}")
    public ResponseEntity<User> delete(@ApiParam(value = "用户名", required = true) @PathVariable String username) {
        User user = userService.delete(username);
        return ResponseEntity.ok(user);
    }
}
