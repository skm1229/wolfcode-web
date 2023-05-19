package cn.wolfcode.web.modules.contacts.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TbVisitDto extends TbVisit{
    //客户名称
    private String customerName;
    //联系人名称
    private String linkmanName;
    // 录入人名称
    private String inputUserName;
}
