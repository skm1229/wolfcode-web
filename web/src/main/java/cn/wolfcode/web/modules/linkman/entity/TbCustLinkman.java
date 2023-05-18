package cn.wolfcode.web.modules.linkman.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import link.ahsj.core.annotations.AddGroup;
import link.ahsj.core.annotations.UpdateGroup;
import lombok.Data;
import org.hibernate.validator.constraints.Length;


import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 客户联系人
 * </p>
 *
 * @author FUCK
 * @since 2023-05-17
 */
@Data
public class TbCustLinkman implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 客户id
     */
    @NotBlank(message = "所属企业不能为空",groups = {AddGroup.class, UpdateGroup.class})
    private String custId;

    /**
     * 客户名称
     */
    @TableField(exist = false)
    private String customerName;

    /**
     * 联系人名字
     */
    @NotBlank(message = "联系人名字不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 30,message = "名字30字内",groups = {AddGroup.class, UpdateGroup.class})
    @Excel(name = "联系人的姓名")
    private String linkman;


    /**
     * 性别 1 男 0 女
     */
    @NotNull(message = "性别不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @Excel(name = "性别")
    private Integer sex;


    /**
     * 年龄
     */
    @Max(value = 100,message = "年龄不能超过100",groups = {AddGroup.class, UpdateGroup.class})
    @Min(value = 0,message = "年龄小于0",groups = {AddGroup.class, UpdateGroup.class})
    private Integer age;


    /**
     * 联系人电话
     */
    @NotBlank(message = "联系人电话不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @Pattern(regexp = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$",message = "手机号码不规范",groups = {AddGroup.class, UpdateGroup.class})
    @Excel(name = "电话")
    private String phone;


    /**
     * 职位
     */
    @Length(max = 20,message = "职位20字内",groups = {AddGroup.class, UpdateGroup.class})
    private String position;



    /**
     * 部门
     */
    @Length(max = 20,message = "部门20字内",groups = {AddGroup.class, UpdateGroup.class})
    private String department;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 录入人
     */
    private String inputUser;

    /**
     * 录入时间
     */
    private LocalDateTime inputTime;

    /**
     * 任职状态
     * @return
     */
    private Integer appointmentStatus;
}
