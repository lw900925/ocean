package org.matrixstudio.ocean.restful.service;

import org.matrixstudio.ocean.core.model.entity.Role;
import org.matrixstudio.ocean.core.model.entity.User;
import org.matrixstudio.ocean.core.repository.jpa.RoleRepository;
import org.matrixstudio.ocean.core.repository.jpa.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
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
    public Page<User> list(String search, Pageable pageable) {
        if (StringUtils.hasText(search)) {
            return userRepository.findAllByUsernameStartingWith(search, pageable);
        } else {
            return userRepository.findAll(pageable);
        }
    }

    @Transactional(readOnly = true)
    public User get(String username) {
        return userRepository.findById(username).orElseThrow(EntityNotFoundException::new);
}

    public User create(User user) {
        // 判断用户是否存在
        User newUser = userRepository.findById(user.getUsername()).orElse(null);
        if (newUser != null) {
            throw new EntityExistsException(String.format("Username %s has already exists.", user.getUsername()));
        }

        newUser = new User();
        BeanUtils.copyProperties(user, newUser);
        newUser.setClientCapability(User.ClientCapability.CUSTOMER);

        if (StringUtils.hasText(newUser.getPassword())) {
            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        }

        // 保存角色
        setUserRoles(user.getAuthorities(), newUser);
        return userRepository.save(newUser);
    }

    public User update(User user) {
        // 根据username查询用户
        User newUser = userRepository.findById(user.getUsername()).orElseThrow(EntityNotFoundException::new);
        BeanUtils.copyProperties(user, newUser, "username", "password");

        if (StringUtils.hasText(user.getPassword())) {
            newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        // 保存角色
        setUserRoles(user.getAuthorities(), newUser);
        return userRepository.save(newUser);
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

    public User register(String email, String password, String username) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);

        user = create(user);
        user.setEnabled(false);
        user = userRepository.save(user);

        return user;
    }

    public User updatePassword(String username, String originPassword, String newPassword) {
        User user = userRepository.findById(username).orElseThrow(EntityNotFoundException::new);
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
        User user = userRepository.findById(username).orElseThrow(EntityNotFoundException::new);
        userRepository.delete(user);
        // 删除用户角色的关联，需要设置一个空的集合，否则JSON序列化会报错
        user.setAuthorities(new HashSet<>());
        return user;
    }
}
