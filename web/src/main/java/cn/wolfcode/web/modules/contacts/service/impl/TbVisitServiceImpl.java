package cn.wolfcode.web.modules.contacts.service.impl;

import cn.wolfcode.web.modules.contacts.entity.TbVisit;
import cn.wolfcode.web.modules.contacts.entity.TbVisitDto;
import cn.wolfcode.web.modules.contacts.mapper.TbVisitMapper;
import cn.wolfcode.web.modules.contacts.service.ITbVisitService;
import cn.wolfcode.web.modules.customer.entity.TbCustomer;
import cn.wolfcode.web.modules.customer.mapper.TbCustomerMapper;
import cn.wolfcode.web.modules.linkman.entity.TbCustLinkman;
import cn.wolfcode.web.modules.linkman.mapper.TbCustLinkmanMapper;
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
 * 拜访信息表 服务实现类
 * </p>
 *
 * @author FUCK
 * @since 2023-05-19
 */
@Service
public class TbVisitServiceImpl extends ServiceImpl<TbVisitMapper, TbVisit> implements ITbVisitService {

    @Autowired
    private TbVisitMapper tbVisitMapper;
    @Autowired
    private TbCustomerMapper tbCustomerMapper;
    @Autowired
    private TbCustLinkmanMapper tbCustLinkmanMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Override
    public Page<TbVisitDto> selectByPageByVisitReason(Page<TbVisitDto> vistPage,String parameterName, String visitReason, String visitType) {
        Page<TbVisitDto> tbVisitPage = tbVisitMapper.selectByPageByVisitReason(vistPage, parameterName,visitReason, visitType);
        /**
         * 修改拜访客户及联系人客户名称
         */
        List<TbVisitDto> records = tbVisitPage.getRecords();
        Page<TbVisitDto> page = new Page<>();
        BeanUtils.copyProperties(tbVisitPage,page,"records");
        List<TbVisitDto> tbVisitList = records.stream().map((item) -> {
            TbVisitDto tbVisit = new TbVisitDto();
            BeanUtils.copyProperties(item,tbVisit);
            //获取客户id并返回其名字
            String custId = item.getCustId();
            System.out.println(custId);
            TbCustomer tbCustomer = tbCustomerMapper.selectById(custId);
            tbVisit.setCustomerName(tbCustomer.getCustomerName());

            //获取联系人id并返回其名字
            String linkmanId = item.getLinkmanId();
            TbCustLinkman tbCustLinkman = tbCustLinkmanMapper.selectById(linkmanId);
            tbVisit.setLinkmanName(tbCustLinkman.getLinkman());
            System.out.println(tbCustLinkman.getLinkman());

            //获取录入人id并返回其名字
            SysUser sysUser = sysUserMapper.selectById(item.getInputUser());
            tbVisit.setInputUserName(sysUser.getUsername());

            return tbVisit;

        }).collect(Collectors.toList());


        page.setRecords(tbVisitList);

        return page;
    }
}
