package cn.wolfcode.web.modules.contacts.mapper;

import cn.wolfcode.web.modules.contacts.entity.TbVisit;
import cn.wolfcode.web.modules.contacts.entity.TbVisitDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.lettuce.core.dynamic.annotation.Param;

/**
 * <p>
 * 拜访信息表 Mapper 接口
 * </p>
 *
 * @author FUCK
 * @since 2023-05-19
 */
public interface TbVisitMapper extends BaseMapper<TbVisit> {
    Page<TbVisitDto> selectByPageByVisitReason (Page<TbVisitDto> vistPage, @Param("parameterName") String parameterName, @Param("visitReason") String visitReason, @Param("visitType") String visitType);
}
