package cn.wolfcode.web.modules.customer.controller;

import cn.wolfcode.web.commons.entity.LayuiPage;
import cn.wolfcode.web.commons.utils.CityUtils;
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
import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import link.ahsj.core.annotations.AddGroup;
import link.ahsj.core.annotations.SameUrlData;
import link.ahsj.core.annotations.SysLog;
import link.ahsj.core.annotations.UpdateGroup;
import link.ahsj.core.entitys.ApiModel;
import link.ahsj.core.exception.AppServerException;
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
 * @since 2023-05-15
 */
@Controller
@RequestMapping("customer")
public class TbCustomerController extends BaseController {

    @Autowired
    private ITbCustomerService entityService;
    @Autowired
    private ITbCustLinkmanService custLinkmanService;

    private static final String LogModule = "TbCustomer";

    @GetMapping("/list.html")
    public ModelAndView list(ModelAndView mv) {
        // 获取省份数据,返回给页面
        mv.addObject("citys",CityUtils.citys);
        mv.setViewName("cust/customer/list");
        return mv;

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
    public ResponseEntity page(LayuiPage layuiPage,String parameterName,String cityId,String openStatus) {
        //System.out.println("条件：" + parameterName);
        SystemCheckUtils.getInstance().checkMaxPage(layuiPage);
        // 1，分页条件：页码：1，条数：10
        IPage page = new Page<>(layuiPage.getPage(), layuiPage.getLimit());
        // 2，搜索关键字条件
        // entityService继承了IService
        // tbCustomerLambdaQueryChainWrapper条件构造器对象
        LambdaQueryChainWrapper<TbCustomer> tbCustomerLambdaQueryChainWrapper = entityService.lambdaQuery();
        // 查询条件：parameterName非空的时候，模糊查询企业名称或者法定代表人
        tbCustomerLambdaQueryChainWrapper.
                //省份查询
                eq(!StringUtils.isEmpty(cityId),TbCustomer::getProvince,cityId).
                // 经营状态
                eq(!StringUtils.isEmpty(openStatus),TbCustomer::getOpenStatus,openStatus).
                and(!StringUtils.isEmpty(parameterName),q -> q.like(TbCustomer::getCustomerName,parameterName).
                or().
                like(!StringUtils.isEmpty(parameterName), TbCustomer::getCustomerName, parameterName));
        // 3，在搜索条件的基础上，添加分页条件
        page = tbCustomerLambdaQueryChainWrapper.page(page);
        // 4，展示列表的时候，处理省份数据
        List<TbCustomer> records = page.getRecords();

//        Page<TbCustomer> newPage = new Page();
//        BeanUtils.copyProperties(records,newPage,"records");
//        // Stream流拷贝
//       List<TbCustomer> records2 = records.stream().map((item) -> {
//           TbCustomer tbCustomer = new TbCustomer();
//           // 拿到省份id进行校验，返回省份id所对应的省份名称
//           String cityValue = CityUtils.getCityValue(item.getProvince());
//           // 列表中每条记录添加ProvinceName属性
//           tbCustomer.setProvinceName(cityValue);
//           return tbCustomer;
//       }).collect(Collectors.toList());
//
//       newPage.setRecords(records2);

        for (TbCustomer record:records ) {
            // 拿到省份id进行校验，返回省份id所对应的省份名称
            String cityValue = CityUtils.getCityValue(record.getProvince());
            // 列表中每条记录添加ProvinceName属性
            record.setProvinceName(cityValue);
        }

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

        // 判断数据库中是否和当前添加的对象重名
        Integer count = entityService.lambdaQuery().eq(TbCustomer::getCustomerName, entity.getCustomerName()).count();
        // 个数大于0说明有重名
        if (count > 0){
            throw new AppServerException("这个客户已经存在了!");
        }
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
        // 修改的时候判断客户名称是否已经存在
        Integer count = entityService.lambdaQuery().
                eq(TbCustomer::getCustomerName, entity.getCustomerName()). //等于
        ne(TbCustomer::getId, entity.getId()).count(); //不等于
        if (count > 0){
            throw new AppServerException("这个客户已经存在！");
        }
        entityService.updateById(entity);
        return ResponseEntity.ok(ApiModel.ok());
    }

    @SysLog(value = LogModules.DELETE, module = LogModule)
    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasAuthority('cust:customer:delete')")
    public ResponseEntity<ApiModel> delete(@PathVariable("id") String id) {
        // 删除企业客户
        entityService.removeById(id);
        // 删除企业客户的联系人 -- 删除的企业的id和客户联系人的企业id相同的删除
        custLinkmanService.lambdaUpdate().eq(TbCustLinkman::getCustId,id).remove();
        return ResponseEntity.ok(ApiModel.ok());

    }

}
