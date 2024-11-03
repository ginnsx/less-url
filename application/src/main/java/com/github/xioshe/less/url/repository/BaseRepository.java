package com.github.xioshe.less.url.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


/**
 * Repository 父类，为了统一命令
 */
public abstract class BaseRepository<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> {

}
