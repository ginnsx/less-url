package com.github.xioshe.less.url.repository.auth;

import com.github.xioshe.less.url.entity.auth.Role;
import com.github.xioshe.less.url.mapper.RoleMapper;
import com.github.xioshe.less.url.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public class RoleRepository extends BaseRepository<RoleMapper, Role> {

}
