package cn.wolfcode.web.modules.customer.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import link.ahsj.core.annotations.AddGroup;
import link.ahsj.core.annotations.UpdateGroup;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 客户信息
 * </p>
 *
 * @author FUCK
 * @since 2023-05-15
 */
@Data
@ToString
public class TbCustomer implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @NotBlank(message = "企业名称不能为空！",groups = {AddGroup.class, UpdateGroup.class})
    // 长度校验
    @Length(max = 100,message = "企业名称不能超过100字！",groups = {AddGroup.class,UpdateGroup.class})
    private String customerName;


    /**
     * 法定代表人
     */
    @NotBlank(message = "法定代表人不能为空！",groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 30,message = "法定代表人名称不能超过30字！",groups = {AddGroup.class,UpdateGroup.class})
    private String legalLeader;


    /**
     * 成立时间
     */
    @NotNull(message = "成立时间不能为空！",groups = {AddGroup.class, UpdateGroup.class})
    private LocalDate registerDate;


    /**
     * 经营状态, 0 开业、1 注销、2 破产
     */
    private Integer openStatus;


    /**
     * 所属地区省份
     */
    @NotBlank(message = "所属地区省份不能为空！",groups = {AddGroup.class, UpdateGroup.class})
    private String province;


    /**
     * 注册资本,(万元)
     */
    @NotBlank(message = "注册资本不能为空！",groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 20,message = "注册资本不能超过20字！",groups = {AddGroup.class,UpdateGroup.class})
    private String regCapital;


    /**
     * 所属行业
     */
    @NotBlank(message = "所属行业不能为空！",groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 30,message = "所属行业不能超过30字！",groups = {AddGroup.class,UpdateGroup.class})
    private String industry;


    /**
     * 经营范围
     */
    @Length(max = 500,message = "经营范围不能超过500字！",groups =
            {AddGroup.class, UpdateGroup.class})
    private String scope;


    /**
     * 注册地址
     */
    @Length(max = 500,message = "注册地址不能超过500字！",groups =
            {AddGroup.class,UpdateGroup.class})
    private String regAddr;


    /**
     * 录入时间
     */
    private LocalDateTime inputTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 录入人
     */
    private String inputUserId;

    /**
     * 所属地区省份名称
     * 该字段在数据库中不存在
     */
    @TableField(exist = false)
    private String provinceName;



}
