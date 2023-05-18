package cn.wolfcode.web.modules.linkman.service.impl;

import cn.wolfcode.web.modules.customer.entity.TbCustomer;
import cn.wolfcode.web.modules.customer.mapper.TbCustomerMapper;
import cn.wolfcode.web.modules.linkman.entity.TbCustLinkman;
import cn.wolfcode.web.modules.linkman.entity.TbCustLinkmanVo;
import cn.wolfcode.web.modules.linkman.mapper.TbCustLinkmanMapper;
import cn.wolfcode.web.modules.linkman.service.ITbCustLinkmanService;
import cn.wolfcode.web.modules.sys.entity.SysUser;
import cn.wolfcode.web.modules.sys.mapper.SysUserMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 客户联系人 服务实现类
 * </p>
 *
 * @author FUCK
 * @since 2023-05-17
 */
@Service
public class TbCustLinkmanServiceImpl extends ServiceImpl<TbCustLinkmanMapper, TbCustLinkman> implements ITbCustLinkmanService {
    @Autowired
    private TbCustLinkmanMapper tbCustLinkmanMapper;
    @Autowired
    private TbCustomerMapper tbCustomerMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Override
    public Page<TbCustLinkmanVo>
    selectByPageByparmByCustId(Page<TbCustLinkmanVo> custLinkmanPage, String parameterName, String custId) {
        Page<TbCustLinkmanVo> tbCustLinkmanVoPage = tbCustLinkmanMapper.selectByPageByparmByCustId(custLinkmanPage, parameterName, custId);
        /**
         * 修改企业名称&修改录入人昵称
         */
        List<TbCustLinkmanVo> records = tbCustLinkmanVoPage.getRecords();
        Page<TbCustLinkmanVo> page = new Page<>();
        BeanUtils.copyProperties(tbCustLinkmanVoPage,page,"records");
        List<TbCustLinkmanVo> list = records.stream().map((item) ->{
            TbCustLinkmanVo tbCustLinkmanVo = new TbCustLinkmanVo();
            BeanUtils.copyProperties(item,tbCustLinkmanVo);
            //取出每一个企业的id
            String id = item.getCustId();
            //根据id查询每一个企业的名称
            TbCustomer tbCustomer = tbCustomerMapper.selectById(id);
            //根据录入人的id查询录入人姓名
            SysUser sysUser = sysUserMapper.selectById(item.getInputUser());
            //往每一个TbCustLinkmanVo类企业名称赋值并返回
            tbCustLinkmanVo.setCustomerName(tbCustomer.getCustomerName());
            tbCustLinkmanVo.setInputUserName(sysUser.getUsername());
            return tbCustLinkmanVo;
        }).collect(Collectors.toList());
         page.setRecords(list);
        return page;
    }


}
