package com.github.xioshe.less.url.util.constants;

public class RedisKeys {
    //***------------------- Common -------------------***//
    public static final String REDIS_KEY_PREFIX = "lu:";

    //***------------------- Security -------------------***//
    public static final String BLACKLIST_KEY = REDIS_KEY_PREFIX + "blacklist:";
    public static final String ACCESS_TOKEN_BLACKLIST_PREFIX = BLACKLIST_KEY + "at:";
    public static final String REFRESH_TOKEN_BLACKLIST_PREFIX = BLACKLIST_KEY + "rt:";

    public static final String AUTH_PREFIX = REDIS_KEY_PREFIX + "auth:";
    public static final String REFRESH_TOKEN_STORE_PREFIX = AUTH_PREFIX + "rt:";

    public static final String TOKEN_TYPE = "type";

    public static final String GUEST_ID_PREFIX = REDIS_KEY_PREFIX + "guest:";

    //***------------------- VerifyCode -------------------***//
    public final static String VERIFY_CODE_KEY_PREFIX = REDIS_KEY_PREFIX + "verify:";

    //***------------------- Cache -------------------***//
    public static final String CACHE_USERS = "users";
    public static final String CACHE_EMAILS = "emails";
    public static final String CACHE_LINKS = "links";

    public static final String CACHE_USER_PREFIX = REDIS_KEY_PREFIX + CACHE_USERS + ":";
    public static final String CACHE_EMAIL_PREFIX = REDIS_KEY_PREFIX + CACHE_EMAILS + ":";
    public static final String CACHE_LINK_PREFIX = REDIS_KEY_PREFIX + CACHE_LINKS + ":";

    //***------------------- Lock -------------------***//
    public static final String LOCK_PREFIX = REDIS_KEY_PREFIX + "lock:";

    //***------------------- Link -------------------***//
    public static final String VISIT_COUNT_PREFIX = REDIS_KEY_PREFIX + "vc:";

}
