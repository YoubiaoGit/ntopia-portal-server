package com.hcycom.sso.OperationLog;

import ch.qos.logback.classic.spi.CallerData;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.db.DBAppenderBase;
import ch.qos.logback.core.db.DBHelper;
import tk.mybatis.mapper.util.StringUtil;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

/**
 * @Path：
 * @Classname：
 * @Description：
 * @Author：yandi
 * @CreateTime：2018/9/18 17:21
 * @ModifyUser：yandi
 * @ModifyRemark：
 * @ModifyTime：2018/9/18 17:21
 */
public class MyDBAppender extends DBAppenderBase<ILoggingEvent> {

    protected static final Method GET_GENERATED_KEYS_METHOD;

    //插入sql
    protected String insertSQL;
    //id
    static final int ID_INDEX = 1;
    //日志级别
    static final int TYPE_INDEX = 2;
    //日志标题
    static final int TITLE_INDEX = 3;
    //业务主键
    static final int BIZKEY_INDEX = 4;
    //操作地址
    static final int URL_INDEX = 5;
    //请求服务器地址
    static final int REQUESTIP_INDEX = 6;
    //用户名
    static final int USERNAME_INDEX = 7;


    static final StackTraceElement EMPTY_CALLER_DATA = CallerData.naInstance();


    static {
        Method getGeneratedKeysMethod;
        try {
            getGeneratedKeysMethod = PreparedStatement.class.getMethod("getGeneratedKeys", (Class[]) null);
        } catch (Exception ex) {
            getGeneratedKeysMethod = null;
        }
        GET_GENERATED_KEYS_METHOD = getGeneratedKeysMethod;
    }

    @Override
    public void start() {
        insertSQL = MySQLBuilder.buildInsertSQL();
        System.out.println("insertSQL:"+insertSQL);
        super.start();
    }

    @Override
    protected Method getGeneratedKeysMethod() {
        return GET_GENERATED_KEYS_METHOD;
    }

    @Override
    protected String getInsertSQL() {
        return insertSQL;
    }

    @Override
    protected void subAppend(ILoggingEvent event, Connection connection, PreparedStatement insertStatement) throws Throwable {
        String msg = bindLoggingEventWithInsertStatement(insertStatement, event);
        int updateCount = 0;
        if (msg.contains("operation_log_data_remoteAddr_userid_serverAddr:")){
            String[] s = msg.split("operation_log_data_remoteAddr_userid_serverAddr:");
            msg = s[1];
            msg = msg.substring(1,msg.length()-1);
            String data[] = msg.split(",");
            String remoteAddr = data[0];
            String username = data[1];
            String serverAddr= data[2];
            String title = data[3];
            insertStatement.setString(URL_INDEX,remoteAddr);
            insertStatement.setString(REQUESTIP_INDEX,serverAddr);
            insertStatement.setString(USERNAME_INDEX, username);
            insertStatement.setString(TITLE_INDEX,title);
            if (!StringUtil.isEmpty(msg)){
                bindLoggingMyInfoWithPreparedStatement(insertStatement, event);
                bindCallerDataWithPreparedStatement(insertStatement, event.getCallerData());//类名加方法名

                System.out.println("insertSQL:"+insertSQL);
                updateCount= insertStatement.executeUpdate();//执行sql
            }
        }
        if (updateCount != 1) {
            addWarn("Failed to insert loggingEvent");
        }

    }

    @Override
    protected void secondarySubAppend(ILoggingEvent eventObject, Connection connection, long eventId) throws Throwable {

    }

    //安全验证及个性化的数据
    void bindLoggingMyInfoWithPreparedStatement(PreparedStatement stmt, ILoggingEvent event)throws SQLException {
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        stmt.setString(ID_INDEX, String.valueOf(UUID.randomUUID()));
        stmt.setString(TYPE_INDEX, event.getLevel().toString());
        stmt.setString(BIZKEY_INDEX, "id");

    }

    String bindLoggingEventWithInsertStatement(PreparedStatement stmt, ILoggingEvent event) throws SQLException {
        String msg = event.getFormattedMessage();
        return msg;
    }

    void bindCallerDataWithPreparedStatement(PreparedStatement stmt, StackTraceElement[] callerDataArray) throws SQLException {
        StackTraceElement caller = extractFirstCaller(callerDataArray);
        caller.getClassName();//路径
        String titl = caller.getFileName()+"-"+caller.getMethodName(); //类名+方法名
    }

    private StackTraceElement extractFirstCaller(StackTraceElement[] callerDataArray) {
        StackTraceElement caller = EMPTY_CALLER_DATA;
        if (hasAtLeastOneNonNullElement(callerDataArray))
            caller = callerDataArray[0];
        return caller;
    }

    private boolean hasAtLeastOneNonNullElement(StackTraceElement[] callerDataArray) {
        return callerDataArray != null && callerDataArray.length > 0 && callerDataArray[0] != null;
    }

    /* (non-Javadoc)
     * @see ch.qos.logback.core.db.DBAppenderBase#append(java.lang.Object)
     */
    @Override
    public void append(ILoggingEvent eventObject) {
        Connection connection = null;
        try {
            connection = connectionSource.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement insertStatement;
            insertStatement = connection.prepareStatement(getInsertSQL());
            // inserting an event and getting the result must be exclusive
            synchronized (this) {
                subAppend(eventObject, connection, insertStatement);
            }

            // we no longer need the insertStatement
            if (insertStatement != null) {
                insertStatement.close();
            }
            connection.commit();
        } catch (Throwable sqle) {
            addError("problem appending event", sqle);
        } finally {
            DBHelper.closeConnection(connection);
        }
    }
}
