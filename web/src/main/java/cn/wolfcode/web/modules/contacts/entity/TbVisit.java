package cn.wolfcode.web.modules.contacts.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import link.ahsj.core.annotations.AddGroup;
import link.ahsj.core.annotations.UpdateGroup;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 拜访信息表
 * </p>
 *
 * @author FUCK
 * @since 2023-05-19
 */
@Data
@ToString
public class TbVisit implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一id
     */
    @TableField("id")
    private String id;

    /**
     * 客户id
     */
    @NotBlank(message = "拜访客户不能为空！",groups = {AddGroup.class, UpdateGroup.class})
    @TableField("cust_id")
    private String custId;

    /**
     * 联系人id
     */
    @NotBlank(message = "客户联系人不能为空！",groups = {AddGroup.class, UpdateGroup.class})
    @TableField("linkman_id")
    private String linkmanId;

    /**
     * 拜访方式, 1 上门走访, 2 电话拜访
     */
    @NotNull(message = "拜访方式不能为空！",groups = {AddGroup.class, UpdateGroup.class})
    @TableField("visit_type")
    private Integer visitType;

    /**
     * 拜访原因
     */
    @NotBlank(message = "拜访原因不能为空！",groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 100,message = "拜访原因不能超过100字！",groups = {AddGroup.class,UpdateGroup.class})
    @TableField("visit_reason")
    private String visitReason;

    /**
     * 交流内容
     */
    @NotBlank(message = "交流内容不能为空！",groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 1000,message = "交流内容不能超过1000字！",groups = {AddGroup.class,UpdateGroup.class})
    @TableField("content")
    private String content;

    /**
     * 拜访时间
     */
    @NotNull(message = "拜访时间不能为空！",groups = {AddGroup.class, UpdateGroup.class})
    @TableField("visit_date")
    private LocalDate visitDate;

    /**
     * 录入人
     */
    @TableField("input_user")
    private String inputUser;

    /**
     * 录入时间
     */
    @TableField("input_time")
    private LocalDateTime inputTime;


}
