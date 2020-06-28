package com.hcycom.sso.OperationLog;

/**
 * @Path：
 * @Classname：
 * @Description：
 * @Author：yandi
 * @CreateTime：2018/9/18 17:22
 * @ModifyUser：yandi
 * @ModifyRemark：
 * @ModifyTime：2018/9/18 17:22
 */
public class MySQLBuilder {
    //生成插入日志的sql
    static String buildInsertSQL() {
        StringBuilder sqlBuilder = new StringBuilder("INSERT INTO ");
        sqlBuilder.append("sys_operation_log").append(" (");
        sqlBuilder.append("id").append(", ");
        sqlBuilder.append("type").append(", ");
        sqlBuilder.append("title").append(", ");
        sqlBuilder.append("oper_time").append(", ");
        sqlBuilder.append("biz_key").append(", ");
        sqlBuilder.append("remote_addr").append(", ");
        sqlBuilder.append("server_addr").append(", ");
        sqlBuilder.append("sys_user_id").append(") ");
        sqlBuilder.append("VALUES (?, ?, ? ,now(), ?, ?, ?, ?)");
        return sqlBuilder.toString();
    }
}
