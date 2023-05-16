package cn.wolfcode.web.modules.customer.entity;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;

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

    /**
     * 企业名称
     */
    private String customerName;

    /**
     * 法定代表人
     */
    private String legalLeader;

    /**
     * 成立时间
     * S
     */
    private LocalDate registerDate;

    /**
     * 经营状态, 0 开业、1 注销、2 破产
     */
    private Integer openStatus;

    /**
     * 所属地区省份
     */
    private String province;

    /**
     * 注册资本,(万元)
     */
    private String regCapital;

    /**
     * 所属行业
     */
    private String industry;

    /**
     * 经营范围
     */
    private String scope;

    /**
     * 注册地址
     */
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

}
