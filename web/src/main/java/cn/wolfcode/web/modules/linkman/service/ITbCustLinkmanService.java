package cn.wolfcode.web.modules.linkman.service;

import cn.wolfcode.web.modules.linkman.entity.TbCustLinkman;
import cn.wolfcode.web.modules.linkman.entity.TbCustLinkmanVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 客户联系人 服务类
 * </p>
 *
 * @author FUCK
 * @since 2023-05-17
 */
public interface ITbCustLinkmanService extends IService<TbCustLinkman> {

    /**
     * 根据条件查询
     * @param custLinkmanPage
     * @param parameterName
     * @param custId
     * @return 返回分页对象
     */
    public Page<TbCustLinkmanVo> selectByPageByparmByCustId(Page<TbCustLinkmanVo> custLinkmanPage, String parameterName,String custId);


}
