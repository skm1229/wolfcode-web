package cn.wolfcode.web.modules.customer.controller;

import cn.wolfcode.web.commons.entity.LayuiPage;
import cn.wolfcode.web.commons.utils.CityUtils;
import cn.wolfcode.web.commons.utils.LayuiTools;
import cn.wolfcode.web.commons.utils.SystemCheckUtils;
import cn.wolfcode.web.modules.BaseController;
import cn.wolfcode.web.modules.customer.entity.TbCustomer;
import cn.wolfcode.web.modules.customer.service.ITbCustomerService;
import cn.wolfcode.web.modules.log.LogModules;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
    public ResponseEntity page(LayuiPage layuiPage) {
        SystemCheckUtils.getInstance().checkMaxPage(layuiPage);
        IPage page = new Page<>(layuiPage.getPage(), layuiPage.getLimit());
        return ResponseEntity.ok(LayuiTools.toLayuiTableModel(entityService.page(page)));
    }

    @SameUrlData
    @PostMapping("save")
    @SysLog(value = LogModules.SAVE, module =LogModule)
    @PreAuthorize("hasAuthority('cust:customer:add')")
    public ResponseEntity<ApiModel> save(@Validated({AddGroup.class}) @RequestBody TbCustomer entity) {
        entityService.save(entity);
        return ResponseEntity.ok(ApiModel.ok());
    }

    @SameUrlData
    @SysLog(value = LogModules.UPDATE, module = LogModule)
    @PutMapping("update")
    @PreAuthorize("hasAuthority('cust:customer:update')")
    public ResponseEntity<ApiModel> update(@Validated({UpdateGroup.class}) @RequestBody TbCustomer entity) {
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
