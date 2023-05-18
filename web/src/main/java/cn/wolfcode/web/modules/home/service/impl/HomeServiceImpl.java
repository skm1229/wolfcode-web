package cn.wolfcode.web.modules.home.service.impl;

import cn.wolfcode.web.modules.customer.service.ITbCustomerService;
import cn.wolfcode.web.modules.home.entity.Home;
import cn.wolfcode.web.modules.home.mapper.HomeMapper;
import cn.wolfcode.web.modules.home.service.IHomeService;
import cn.wolfcode.web.modules.sys.service.SysUserLoginLogService;
import cn.wolfcode.web.modules.sys.service.SysUserOperationLogService;
import cn.wolfcode.web.modules.sys.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class HomeServiceImpl extends ServiceImpl<HomeMapper, Home> implements IHomeService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ValueOperations<String, String> valueOperations;

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserLoginLogService sysUserLoginLogService;
    @Autowired
    private SysUserOperationLogService sysUserOperationLogService;
    @Autowired
    private ITbCustomerService iTbCustomerService;

    String home = "Home:";

    @Override
    public int getLoginCount() {
        //设置 key 名称
        String key = home.concat("getLoginCount");
        //去缓存数据库读取是否有缓存
        String value = valueOperations.get(key);
        if (value != null) {
            return Integer.valueOf(value);
        }
        //没有,查数据库
        int dbCount = sysUserLoginLogService.count();
        //设置到缓存
        valueOperations.set(key, String.valueOf(dbCount));
        //设置缓存时间
        redisTemplate.expire(key, 10, TimeUnit.SECONDS);
        //返回数据
        return dbCount;
    }

    @Override
    public int getUserCount() {
        return sysUserService.count();
    }

    @Override
    public int getOperationLogCount() {
        return sysUserOperationLogService.count();
    }

    @Override
    public int getCustInfoCount() {
        return iTbCustomerService.count();
    }
}

