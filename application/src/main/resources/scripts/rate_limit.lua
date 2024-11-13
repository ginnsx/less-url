-- 获取参数
local key = KEYS[1]
local rate = tonumber(ARGV[1]) -- 每秒填充的令牌数
local capacity = tonumber(ARGV[2]) -- 桶的容量
local now = tonumber(ARGV[3])
local fill_time = tonumber(ARGV[4])

-- 获取当前桶中的令牌数和上次更新时间
local bucket = redis.call('hmget', key, 'tokens', 'timestamp')
local tokens = tonumber(bucket[1] or capacity)
local timestamp = tonumber(bucket[2] or now)

-- 计算需要补充的令牌数
local elapsed = (now - timestamp) / 1000.0
local new_tokens = math.min(capacity, tokens + (elapsed * rate))

-- 如果有足够的令牌，则获取成功
if new_tokens >= 1 then
    -- 更新令牌数和时间戳
    redis.call('hset', key, 'tokens', new_tokens - 1, 'timestamp', now)
    -- 设置过期时间
    redis.call('expire', key, fill_time)
    return 1
else
    -- 获取失败
    redis.call('hset', key, 'tokens', new_tokens, 'timestamp', now)
    redis.call('expire', key, fill_time)
    return 0
end
