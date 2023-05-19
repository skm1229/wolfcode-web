package cn.wolfcode.web.modules.contacts.controller;

import cn.wolfcode.web.commons.entity.LayuiPage;
import cn.wolfcode.web.commons.utils.LayuiTools;
import cn.wolfcode.web.commons.utils.SystemCheckUtils;
import cn.wolfcode.web.modules.BaseController;
import cn.wolfcode.web.modules.contacts.entity.TbVisit;
import cn.wolfcode.web.modules.contacts.entity.TbVisitDto;
import cn.wolfcode.web.modules.contacts.service.ITbVisitService;
import cn.wolfcode.web.modules.customer.entity.TbCustomer;
import cn.wolfcode.web.modules.customer.service.ITbCustomerService;
import cn.wolfcode.web.modules.linkman.entity.TbCustLinkman;
import cn.wolfcode.web.modules.linkman.service.ITbCustLinkmanService;
import cn.wolfcode.web.modules.log.LogModules;
import cn.wolfcode.web.modules.sys.entity.SysUser;
import cn.wolfcode.web.modules.sys.form.LoginForm;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import link.ahsj.core.annotations.AddGroup;
import link.ahsj.core.annotations.SameUrlData;
import link.ahsj.core.annotations.SysLog;
import link.ahsj.core.annotations.UpdateGroup;
import link.ahsj.core.entitys.ApiModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author FUCK
 * @since 2023-05-19
 */
@Controller
@RequestMapping("visit")
public class TbVisitController extends BaseController {

    @Autowired
    private ITbVisitService entityService;

    @Autowired
    private ITbCustomerService customerService;
    @Autowired
    private ITbCustLinkmanService custLinkmanService;

    private static final String LogModule = "TbVisit";

    @GetMapping("/list.html")
    public String list() {
        return "contacts/visit/list";
    }

    @RequestMapping("/add.html")
    @PreAuthorize("hasAuthority('contacts:visit:add')")
    public ModelAndView toAdd(ModelAndView mv) {
        // 查询所有客户
        List<TbCustomer> custList = customerService.list();
        //查询所有联系人
        List<TbCustLinkman> custLinkmen = custLinkmanService.list();
        mv.addObject("custs",custList);
        mv.addObject("linkmans",custLinkmen);
        mv.setViewName("contacts/visit/add");
        return mv;
    }

    @GetMapping("/{id}.html")
    @PreAuthorize("hasAuthority('contacts:visit:update')")
    public ModelAndView toUpdate(@PathVariable("id") String id, ModelAndView mv) {
        // 1，获取所有客户（企业）信息，返回给页面
        List<TbCustomer> custs = customerService.list();
        mv.addObject("custs",custs);
        // 2. 获取所有联系人的信息，返回给页面
        List<TbCustLinkman> custLinkmen = custLinkmanService.list();
        mv.addObject("linkmans",custLinkmen);
        mv.setViewName("contacts/visit/update");
        mv.addObject("obj", entityService.getById(id));
        mv.addObject("id", id);
        return mv;
    }

    @RequestMapping("list")
    @PreAuthorize("hasAuthority('contacts:visit:list')")
    public ResponseEntity page(LayuiPage layuiPage,String parameterName,String visitReason,String visitType) {
        SystemCheckUtils.getInstance().checkMaxPage(layuiPage);
        Page<TbVisitDto> page = new Page<>(layuiPage.getPage(), layuiPage.getLimit());
//        IPage page = new Page<>(layuiPage.getPage(), layuiPage.getLimit());
//        LambdaQueryChainWrapper<TbVisit> queryChainWrapper = entityService.lambdaQuery();
//        //添加拜访原因条件
//        queryChainWrapper.like(!StringUtils.isEmpty(visitReason),TbVisit::getVisitReason,visitReason);
//        queryChainWrapper.eq(!StringUtils.isEmpty(visitType),TbVisit::getVisitType,visitType);
//
//        page = queryChainWrapper.page(page);
//        List<TbVisit> records = page.getRecords();

        Page<TbVisitDto> tbVisitDtoPage = entityService.selectByPageByVisitReason(page, parameterName,visitReason, visitType);
        return ResponseEntity.ok(LayuiTools.toLayuiTableModel(tbVisitDtoPage));
    }

    @SameUrlData
    @PostMapping("save")
    @SysLog(value = LogModules.SAVE, module =LogModule)
    @PreAuthorize("hasAuthority('contacts:visit:add')")
    public ResponseEntity<ApiModel> save(@Validated({AddGroup.class}) @RequestBody TbVisit entity, HttpServletRequest request) {
        // 1，获取录入时间LocalDateTime.now()，设置到entity对象的inputTime字段
        entity.setInputTime(LocalDateTime.now());
        // 2，获取用户对象
        System.out.println("HttpServletRequest"+request);
        // SysUser 登录的用户信息 类型强制转换
        SysUser user = (SysUser)request.getSession().getAttribute(LoginForm.LOGIN_USER_KEY);
        // 获取录入人--即登录的用户id
        entity.setInputUser(user.getUserId());

        entityService.save(entity);
        return ResponseEntity.ok(ApiModel.ok());
    }

    @SameUrlData
    @SysLog(value = LogModules.UPDATE, module = LogModule)
    @PutMapping("update")
    @PreAuthorize("hasAuthority('contacts:visit:update')")
    public ResponseEntity<ApiModel> update(@Validated({UpdateGroup.class}) @RequestBody TbVisit entity) {
        // 获取当前时间，设置当前时间为修改时间
        entity.setInputTime(LocalDateTime.now());
        entityService.updateById(entity);
        return ResponseEntity.ok(ApiModel.ok());
    }

    @SysLog(value = LogModules.DELETE, module = LogModule)
    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasAuthority('contacts:visit:delete')")
    public ResponseEntity<ApiModel> delete(@PathVariable("id") String id) {
        entityService.removeById(id);
        return ResponseEntity.ok(ApiModel.ok());
    }

}
