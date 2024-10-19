package com.github.xioshe.less.url.util.constants;

public class RoleNames {

    public static final String ROLE_NAME_PREFIX = "ROLE_";

    public static final String ROLE_ANONYMOUS = "ANONYMOUS";
    public static final String ROLE_GUEST = "GUEST";
    public static final String ROLE_USER = "USER";
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_ANONYMOUS_NAME = ROLE_NAME_PREFIX + ROLE_ANONYMOUS;
    public static final String ROLE_GUEST_NAME = ROLE_NAME_PREFIX + ROLE_GUEST;
    public static final String ROLE_USER_NAME = ROLE_NAME_PREFIX + ROLE_USER;
    public static final String ROLE_ADMIN_NAME = ROLE_NAME_PREFIX + ROLE_ADMIN;

    public static final String[] ALL_ROLES = new String[]{
            ROLE_ANONYMOUS, ROLE_GUEST, ROLE_USER, ROLE_ADMIN
    };

    public static final String[] ALL_AUTHENTICATED_ROLES = new String[]{
            ROLE_GUEST, ROLE_USER, ROLE_ADMIN
    };

}
