package cn.wolfcode.web.modules.customer.controller;

import cn.wolfcode.web.commons.entity.LayuiPage;
import cn.wolfcode.web.commons.utils.CityUtils;
import cn.wolfcode.web.commons.utils.LayuiTools;
import cn.wolfcode.web.commons.utils.SystemCheckUtils;
import cn.wolfcode.web.modules.BaseController;
import cn.wolfcode.web.modules.customer.entity.TbCustomer;
import cn.wolfcode.web.modules.customer.service.ITbCustomerService;
import cn.wolfcode.web.modules.log.LogModules;
import cn.wolfcode.web.modules.sys.entity.SysUser;
import cn.wolfcode.web.modules.sys.form.LoginForm;
import com.alibaba.druid.util.StringUtils;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author FUCK
 * @since 2023-05-15
 */
@Controller
@RequestMapping("customer")
public class TbCustomerController extends BaseController {

    @Autowired
    private ITbCustomerService entityService;

    private static final String LogModule = "TbCustomer";

    @GetMapping("/list.html")
    public String list() {
        return "cust/customer/list";
    }

    @RequestMapping("/add.html")
    @PreAuthorize("hasAuthority('cust:customer:add')")
    public ModelAndView toAdd(ModelAndView mv) {
        // 通过mv对象把CityUtils类的citys对象返回给add页面，add页面可以直接使用
        mv.addObject("citys", CityUtils.citys);
        mv.setViewName("cust/customer/add");
        return mv;
    }


    @GetMapping("/{id}.html")
    @PreAuthorize("hasAuthority('cust:customer:update')")
    public ModelAndView toUpdate(@PathVariable("id") String id, ModelAndView mv) {
        mv.setViewName("cust/customer/update");
        mv.addObject("obj", entityService.getById(id));
        mv.addObject("id", id);
        mv.addObject("citys",CityUtils.citys);
        return mv;
    }

    @RequestMapping("list")
    @PreAuthorize("hasAuthority('cust:customer:list')")
    public ResponseEntity page(LayuiPage layuiPage,String parameterName) {
        //System.out.println("条件：" + parameterName);
        SystemCheckUtils.getInstance().checkMaxPage(layuiPage);
        // 1，分页条件：页码：1，条数：10
        IPage page = new Page<>(layuiPage.getPage(), layuiPage.getLimit());
        // 2，搜索关键字条件
        // entityService继承了IService
        // tbCustomerLambdaQueryChainWrapper条件构造器对象
        LambdaQueryChainWrapper<TbCustomer> tbCustomerLambdaQueryChainWrapper = entityService.lambdaQuery();
        // 查询条件：parameterName非空的时候，模糊查询企业名称或者法定代表人
        tbCustomerLambdaQueryChainWrapper.like(!StringUtils.isEmpty(parameterName), TbCustomer::getCustomerName, parameterName).
                or().
                like(!StringUtils.isEmpty(parameterName), TbCustomer::getLegalLeader, parameterName);
        // 3，在搜索条件的基础上，添加分页条件
        page = tbCustomerLambdaQueryChainWrapper.page(page);
        // 4，展示列表的时候，处理省份数据
        List<TbCustomer> records = page.getRecords();
        // System.out.println(records);
        // Stream流拷贝
       List<TbCustomer> records2 = records.stream().map((item) -> {
           TbCustomer tbCustomer = new TbCustomer();
           // 拿到省份id进行校验，返回省份id所对应的省份名称
           String cityValue = CityUtils.getCityValue(item.getProvince());
           // 列表中每条记录添加ProvinceName属性
           item.setProvinceName(cityValue);
           return item;
       }).collect(Collectors.toList());

        return ResponseEntity.ok(LayuiTools.toLayuiTableModel(page));
    }

    @SameUrlData
    @PostMapping("save")
    @SysLog(value = LogModules.SAVE, module =LogModule)
    @PreAuthorize("hasAuthority('cust:customer:add')")
    public ResponseEntity<ApiModel> save(@Validated({AddGroup.class}) @RequestBody TbCustomer entity,HttpServletRequest request) {
        // 1，获取录入时间LocalDateTime.now()，设置到entity对象的inputTime字段
        entity.setInputTime(LocalDateTime.now());
        // 2，获取用户对象
        System.out.println("HttpServletRequest"+request);
        // SysUser 登录的用户信息 类型强制转换
        SysUser user = (SysUser)request.getSession().getAttribute(LoginForm.LOGIN_USER_KEY);
        // 获取录入人--即登录的用户id
        entity.setInputUserId(user.getUserId());
        // 3，插入数据
        entityService.save(entity);
        return ResponseEntity.ok(ApiModel.ok());

    }

    @SameUrlData
    @SysLog(value = LogModules.UPDATE, module = LogModule)
    @PutMapping("update")
    @PreAuthorize("hasAuthority('cust:customer:update')")
    public ResponseEntity<ApiModel> update(@Validated({UpdateGroup.class}) @RequestBody TbCustomer entity) {
        // 获取当前时间，设置当前时间为修改时间
        entity.setUpdateTime(LocalDateTime.now());
        entityService.updateById(entity);
        return ResponseEntity.ok(ApiModel.ok());
    }

    @SysLog(value = LogModules.DELETE, module = LogModule)
    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasAuthority('cust:customer:delete')")
    public ResponseEntity<ApiModel> delete(@PathVariable("id") String id) {
        entityService.removeById(id);
        return ResponseEntity.ok(ApiModel.ok());
    }

}
