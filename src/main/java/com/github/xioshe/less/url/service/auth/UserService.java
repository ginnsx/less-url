package com.github.xioshe.less.url.service.auth;

import com.github.xioshe.less.url.api.dto.CountLinkResponse;
import com.github.xioshe.less.url.entity.auth.User;
import com.github.xioshe.less.url.repository.auth.UserRepository;
import com.github.xioshe.less.url.security.SecurityUser;
import com.github.xioshe.less.url.service.link.LinkService;
import com.github.xioshe.less.url.util.constants.RoleNames;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final LinkService linkService;
    private final RoleService roleService;

    public void update(User user) {
        if (StringUtils.hasText(user.getPassword())) {
            user.setPassword(null);
        }
        if (!userRepository.updateById(user)) {
            throw new IllegalArgumentException("更新用户信息失败");
        }
    }

    public CountLinkResponse migrate(String guestId, SecurityUser user) {
        if (user.isGuest()) {
            throw new AuthorizationServiceException("游客不允许迁移数据，请注册正式用户");
        }
        return linkService.migrate(guestId, user.getUserId());
    }

    @Transactional(rollbackFor = Exception.class)
    public void creatUser(User user) {
        userRepository.save(user);
        roleService.assignRoleToUser(user.getId(), RoleNames.ROLE_USER);
    }
}
