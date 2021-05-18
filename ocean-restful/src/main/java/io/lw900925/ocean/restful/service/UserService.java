package io.lw900925.ocean.restful.service;

import io.lw900925.ocean.core.model.entity.Role;
import io.lw900925.ocean.core.model.entity.User;
import io.lw900925.ocean.core.model.entity.UserProfile;
import io.lw900925.ocean.core.repository.jpa.RoleRepository;
import io.lw900925.ocean.core.repository.jpa.UserRepository;
import io.lw900925.ocean.support.spring.web.exception.AppException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Page<User> page(String search, Pageable pageable) {
        if (StringUtils.hasText(search)) {
            return userRepository.findAllByUsernameStartingWith(search, pageable);
        } else {
            return userRepository.findAll(pageable);
        }
    }

    @Transactional(readOnly = true)
    public User get(String username) {
        return userRepository.findById(username).orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "E000001", username));
    }

    public User create(User user) {
        // 判断用户是否存在
        User dbUser = userRepository.findById(user.getUsername()).orElse(null);
        if (dbUser != null) {
            throw new AppException(HttpStatus.NOT_FOUND, "E000002", user.getUsername());
        }

        dbUser = new User();
        BeanUtils.copyProperties(user, dbUser);
        dbUser.setClientCapability(User.ClientCapability.CUSTOMER);

        if (StringUtils.hasText(dbUser.getPassword())) {
            dbUser.setPassword(passwordEncoder.encode(dbUser.getPassword()));
        }

        // 保存角色
        setUserRoles(user.getAuthorities(), dbUser);
        return userRepository.save(dbUser);
    }

    public User update(User user) {
        // 根据username查询用户
        User origUser = userRepository.findById(user.getUsername()).orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "E000001", user.getUsername()));
        BeanUtils.copyProperties(user, origUser, "username", "password", "profile");

        if (StringUtils.hasText(user.getPassword())) {
            origUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        // 保存角色
        setUserRoles(user.getAuthorities(), origUser);

        // 保存profile
        setUserProfile(user.getProfile(), origUser);
        return userRepository.save(origUser);
    }

    /**
     * <p>绑定用户和角色</p>
     * @param authorities Collection of {@link GrantedAuthority}
     * @param user {@link User} Entity
     */
    private void setUserRoles(Collection<? extends GrantedAuthority> authorities, User user) {
        List<String> roleIds = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        Set<Role> roles = new HashSet<>(roleRepository.findAllById(roleIds));
        user.setAuthorities(roles);
    }

    private void setUserProfile(UserProfile profile, User user) {
        UserProfile origUserProfile = user.getProfile();
        BeanUtils.copyProperties(profile, origUserProfile, "oid", "user");
        user.setProfile(origUserProfile);
    }

    public User updatePassword(String username, String originPassword, String newPassword) {
        User user = userRepository.findById(username).orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "E000001", username));
        // 验证原始密码是否正确
        if (!passwordEncoder.matches(originPassword, user.getPassword())) {
            throw new BadCredentialsException("原始密码不正确");
        }

        // 更新密码
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    public User delete(String username) {
        User user = userRepository.findById(username).orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "E000001", username));
        userRepository.delete(user);
        // 删除用户角色的关联，需要设置一个空的集合，否则JSON序列化会报错
        user.setAuthorities(new HashSet<>());
        return user;
    }
}
