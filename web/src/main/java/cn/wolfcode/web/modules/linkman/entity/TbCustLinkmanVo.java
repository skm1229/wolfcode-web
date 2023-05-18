package cn.wolfcode.web.modules.linkman.entity;

import lombok.Data;

@Data
public class TbCustLinkmanVo extends TbCustLinkman{
    // 企业名称
    private String customerName;
    // 录入人名称
    private String inputUserName;
}
