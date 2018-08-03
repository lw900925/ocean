package org.matrixstudio.ocean.restful.config.security;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.matrixstudio.ocean.core.model.entity.User;
import org.matrixstudio.ocean.core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.security.Principal;

@RestController
@Api(description = "用户信息", tags = "UserInfo")
public class UserInfoEndpoint {

    @Autowired
    private UserRepository userRepository;

    @ApiOperation(value = "获取登陆用户信息")
    @GetMapping("/oauth/user_info")
    public User getPrincipal(Principal principal) {
        return userRepository.findById(principal.getName()).orElseThrow(EntityNotFoundException::new);
    }
}
