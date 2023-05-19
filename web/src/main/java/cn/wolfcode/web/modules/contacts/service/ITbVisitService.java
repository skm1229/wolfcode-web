package cn.wolfcode.web.modules.contacts.service;

import cn.wolfcode.web.modules.contacts.entity.TbVisit;
import cn.wolfcode.web.modules.contacts.entity.TbVisitDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 拜访信息表 服务类
 * </p>
 *
 * @author FUCK
 * @since 2023-05-19
 */
public interface ITbVisitService extends IService<TbVisit> {

    Page<TbVisitDto> selectByPageByVisitReason (Page<TbVisitDto> vistPage,String parameterName, String visitReason, String visitType);

}
