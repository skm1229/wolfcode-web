package cn.wolfcode.web.modules.home.service;

public interface IHomeService {
    /**
     * 获取系统登录次数
     * @return
     */
    int getLoginCount();

    /**
     * 获取系统用户数量
     * @return
     */
    int getUserCount();

    /**
     * 获取系统操作日志数量
     * @return
     */
    int getOperationLogCount();

    /**
     * 获取系统大客户数量
     * @return
     */
    int getCustInfoCount();

}
