package cn.wolfcode.web.modules.linkman.controller;

import cn.wolfcode.web.commons.entity.LayuiPage;
import cn.wolfcode.web.commons.utils.LayuiTools;
import cn.wolfcode.web.commons.utils.SystemCheckUtils;
import cn.wolfcode.web.modules.BaseController;
import cn.wolfcode.web.modules.customer.entity.TbCustomer;
import cn.wolfcode.web.modules.customer.service.ITbCustomerService;
import cn.wolfcode.web.modules.linkman.entity.TbCustLinkman;
import cn.wolfcode.web.modules.linkman.service.ITbCustLinkmanService;
import cn.wolfcode.web.modules.log.LogModules;
import cn.wolfcode.web.modules.sys.entity.SysUser;
import cn.wolfcode.web.modules.sys.form.LoginForm;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
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
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author FUCK
 * @since 2023-05-17
 */
@Controller
@RequestMapping("linkman")
public class TbCustLinkmanController extends BaseController {

    @Autowired
    private ITbCustLinkmanService entityService;
    @Autowired
    private ITbCustomerService customerService;


    private static final String LogModule = "TbCustLinkman";

    @GetMapping("/list.html")
    public String list() {
        return "user/linkman/list";
    }

    @RequestMapping("/add.html")
    @PreAuthorize("hasAuthority('user:linkman:add')")
    public ModelAndView toAdd(ModelAndView mv) {
        // 查询所有客户
        List<TbCustomer> custs = customerService.list();
        System.out.println(custs);
        // 把数据返回给页面
        mv.addObject("custs",custs);
        mv.setViewName("user/linkman/add");
        return mv;

    }

    @GetMapping("/{id}.html")
    @PreAuthorize("hasAuthority('user:linkman:update')")
    public ModelAndView toUpdate(@PathVariable("id") String id, ModelAndView mv) {
        // 1，获取所有客户（企业）信息，返回给页面
        List<TbCustomer> list = customerService.list();
        mv.addObject("custs",list);
        mv.setViewName("user/linkman/update");
        // 2，把当前点击的对象的信息返回给页面，页面可以通过obj做数据回显
        mv.addObject("obj", entityService.getById(id));
        mv.addObject("id", id);
        return mv;
    }

    @RequestMapping("list")
    @PreAuthorize("hasAuthority('user:linkman:list')")
    public ResponseEntity page(LayuiPage layuiPage,String parameterName) {
        SystemCheckUtils.getInstance().checkMaxPage(layuiPage);
        // 1，分页查询，page = 1（默认查询第一页） ,limit = 10 （默认查询10条）
        IPage page = new Page<>(layuiPage.getPage(), layuiPage.getLimit());
        // 2，创建条件构造器对象
        LambdaQueryChainWrapper<TbCustLinkman>
                tbCustLinkmanLambdaQueryChainWrapper = entityService.lambdaQuery();
        // 3，根据联系人名称和电话号码查询 模糊查询
        tbCustLinkmanLambdaQueryChainWrapper
                .like(!StringUtils.isEmpty(parameterName),TbCustLinkman::getLinkman,parameterName)
                .or()
                .like(!StringUtils.isEmpty(parameterName),TbCustLinkman::getPhone,parameterName);
        // 4，条件构造器条件加上分页条件
        page = tbCustLinkmanLambdaQueryChainWrapper.page(page);
        return ResponseEntity.ok(LayuiTools.toLayuiTableModel(page));
    }

    @SameUrlData
    @PostMapping("save")
    @SysLog(value = LogModules.SAVE, module =LogModule)
    @PreAuthorize("hasAuthority('user:linkman:add')")
    public ResponseEntity<ApiModel> save(@Validated({AddGroup.class}) @RequestBody TbCustLinkman entity, HttpServletRequest request) {
        // 1，获取当前时间作为录入时间
        entity.setInputTime(LocalDateTime.now());
        // 2，获取当前登陆人信息，作为录入人
        SysUser user = (SysUser) request.getSession().getAttribute(LoginForm.LOGIN_USER_KEY);
        System.out.println(user);
        entity.setInputUser(user.getUserId());
        entityService.save(entity);
        return ResponseEntity.ok(ApiModel.ok());
    }

    @SameUrlData
    @SysLog(value = LogModules.UPDATE, module = LogModule)
    @PutMapping("update")
    @PreAuthorize("hasAuthority('user:linkman:update')")
    public ResponseEntity<ApiModel> update(@Validated({UpdateGroup.class}) @RequestBody TbCustLinkman entity) {
        entityService.updateById(entity);
        return ResponseEntity.ok(ApiModel.ok());
    }

    @SysLog(value = LogModules.DELETE, module = LogModule)
    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasAuthority('user:linkman:delete')")
    public ResponseEntity<ApiModel> delete(@PathVariable("id") String id) {
        entityService.removeById(id);
        return ResponseEntity.ok(ApiModel.ok());
    }

}
