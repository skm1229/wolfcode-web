package cn.wolfcode.web.modules.linkman.mapper;

import cn.wolfcode.web.modules.linkman.entity.TbCustLinkman;
import cn.wolfcode.web.modules.linkman.entity.TbCustLinkmanVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.lettuce.core.dynamic.annotation.Param;

/**
 * <p>
 * 客户联系人 Mapper 接口
 * </p>
 *
 * @author FUCK
 * @since 2023-05-17
 */
public interface TbCustLinkmanMapper extends BaseMapper<TbCustLinkman> {

    /**
     * selectByPageByparmByCustId 通过关键字和企业id查询，
     * @param custLinkmanPage 分页参数
     * @param parameterName 输入框关键字
     * @param custId 企业客户id
     * @return 返回分页对象
     */
    Page<TbCustLinkmanVo> selectByPageByparmByCustId (Page<TbCustLinkmanVo> custLinkmanPage, @Param("parameterName") String parameterName, @Param("custId") String custId);


}
