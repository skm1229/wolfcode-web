package cn.wolfcode.web.modules.customer.service.impl;

import cn.wolfcode.web.modules.customer.entity.TbCustomer;
import cn.wolfcode.web.modules.customer.mapper.TbCustomerMapper;
import cn.wolfcode.web.modules.customer.service.ITbCustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 客户信息 服务实现类
 * </p>
 *
 * @author FUCK
 * @since 2023-05-15
 */
@Service
public class TbCustomerServiceImpl extends ServiceImpl<TbCustomerMapper, TbCustomer> implements ITbCustomerService {

}
