package cn.wolfcode.web.modules.linkman.service.impl;

import cn.wolfcode.web.modules.linkman.entity.TbCustLinkman;
import cn.wolfcode.web.modules.linkman.entity.TbCustLinkmanVo;
import cn.wolfcode.web.modules.linkman.mapper.TbCustLinkmanMapper;
import cn.wolfcode.web.modules.linkman.service.ITbCustLinkmanService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    @Override
    public Page<TbCustLinkmanVo>
    selectByPageByparmByCustId(Page<TbCustLinkmanVo> custLinkmanPage, String
            parameterName, String custId) {
        Page<TbCustLinkmanVo> tbCustLinkmanVoPage = tbCustLinkmanMapper.selectByPageByparmByCustId(custLinkmanPage, parameterName,
                        custId);
        return tbCustLinkmanVoPage;
    }


}
