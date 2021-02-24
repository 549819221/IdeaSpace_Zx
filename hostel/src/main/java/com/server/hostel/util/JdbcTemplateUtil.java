package com.server.hostel.util;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.sql.SQLException;
import java.util.*;

/**
 * JdbcTemplate Util
 *
 *@author wanghb
 *@date 2017-11-06
 *@version V1.0
 */
public class JdbcTemplateUtil {


    /**
     * 用于处理批量插入时将实体类转换成符合jdbc的参数要求 方法
     *
     * @param listBean01 List<T> 类型 要处理的字符串
     * @return String类型
     *
     * @author yucl
     * @date 2017-11-13
     * @version V1.0
     */
    public static <T> SqlParameterSource[] ListBeanPropSource(List<T> listBean01)
    {
        SqlParameterSource[] paramSource = new SqlParameterSource[listBean01.size()];
        try {
            for(int i = 0; i < listBean01.size(); i++)
            {
                paramSource[i] = new BeanPropertySqlParameterSource(listBean01.get(i));
            }
        }catch (Exception e) {

        }
        return paramSource;
    }

    /**
     * @description  将list转化为 in 参数
     * @param  list  数据
     * @param  name  键名称
     * @return  .
     * @date  20/06/04 18:11
     * @author  wanghb
     * @edit  .
     */
    public static  String toInParams(List<Map<String, Object>> list,String name){
        StringBuffer ids = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> temp = list.get(i);
            String id = PowerUtil.getString( temp.get( name ) );
            ids.append( ids.length() == 0 ? "" : "," );
            ids.append("'"+id+"'");
        }
        return PowerUtil.isNull( ids.toString() ) ? "'XXX'" : ids.toString();
    }


    /**
     * @description  将list转化为 in 参数
     * @param  list  数据
     * @param  name  键名称
     * @return  .
     * @date  20/06/04 18:11
     * @author  wanghb
     * @edit  .
     */
    public static  String toInParams(List<Map<String, Object>> list,String name,Integer pageNo,Integer pageNum){
        Set<String> idsSet = new LinkedHashSet<>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> temp = list.get(i);
            idsSet.add( PowerUtil.getString( temp.get( name ) ) );
        }
        StringBuffer ids = new StringBuffer();
        Integer startNum = (pageNo - 1) * pageNum;
        Integer endNum = idsSet.size() <=  pageNo * pageNum ? idsSet.size() : pageNo * pageNum;
        List<String> idsList = new ArrayList<>(idsSet);
        for (int i = startNum; i < endNum; i++) {
            String id = idsList.get( i );
            ids.append( ids.length() == 0 ? "" : "," );
            ids.append("'"+id+"'");
        }
        return PowerUtil.isNull( ids.toString() ) ? "'XXX'" : ids.toString();
    }



    public static String toInParams(List<String> set) {
        StringBuffer ids = new StringBuffer();
        for (int i = 0; i < set.size(); i++) {
            String id = set.get(i);
            ids.append( ids.length() == 0 ? "" : "," );
            ids.append("'"+id+"'");
        }
        return PowerUtil.isNull( ids.toString() ) ? "'XXX'" : ids.toString();
    }


    /**
     * @description  转成按in条件顺序SQL
     * 例:  inParams : '1','2','3' fieldName : 'id'  结果:  case id when '1' then 1 when  '2' then 2 when  '3' then 3 end
     * @param  inParams  参数
     * @param  fieldName  字段名
     * @return  返回值
     * @date  20/06/15 15:06
     * @author  wanghb
     * @edit  .
     */
    public static String toInOrder(String inParams,String fieldName){
        StringBuffer inOrder = new StringBuffer(" case ").append( fieldName );
        String[] inOrderArr = inParams.split( "," );
        for (int i = 0; i < inOrderArr.length; i++) {
            String temp = inOrderArr[i];
            inOrder.append( " when " ).append( temp ).append( " then " ).append( i + 1 );
        }
        inOrder.append( " end " );
        return inOrder.toString();
    }


    /**
     * 用于生成查询条件语句的工具类 方法(模仿jpa的做法)
     *
     * TODO eq等于、  neq不相等,  gt大于,  gte大于等于, lt小于, lte小于等于,like  模糊查询
     *
     * @param SQL String 类型 要处理的SQL  必须包含 where 1 = 1
     * @return String类型
     * @author wanghb
     * @date 2017-11-13
     * @version V1.0
     */
    public static String searchSqlCreate(String SQL, Map<String,Object> searchParams) {
        String prefixStr = "";//标识
        String column_hump = "";//驼峰字段名
        String column = "";//数据库字段名
        for (String key : searchParams.keySet()) {
            if(key.contains("_")){
                prefixStr = key.substring(0,key.indexOf("_"));
            }else{
                continue;
            }
            column_hump = key.substring(key.indexOf("_")+1);
            if("".equals(column_hump)){
                continue;
            }
            column = PowerUtil.toUnderline(column_hump);
            String value = PowerUtil.getString(searchParams.get(key));
            if("".equals(value)){
                continue;
            }

            if("eq".equals(prefixStr)){//等于
                SQL += " AND " +column + " = :"+key;
            }else if("neq".equals(prefixStr)){//不等于
                SQL += " AND " +column + " != :"+key;
            }else if("gt".equals(prefixStr)){//大于
                SQL += " AND " +column + " > :"+key;
            }else if("gte".equals(prefixStr)){ //大于等于
                SQL += " AND " +column + " >= :"+key;
            }else if("lt".equals(prefixStr)){//大于
                SQL += " AND " +column + " < :"+key;
            }else if("lte".equals(prefixStr)){//大于等于
                SQL += " AND " +column + " <= :"+key;
            }else if("like".equals(prefixStr)){//模糊查询
                SQL += " AND " +column + " LIKE '%' :"+key + " '%'";
            }

        }

        return SQL;
    }

    public static String dataBaseType = "";
    /**
     * @description  获取当前数据库类型
     * @date  20/07/29 16:54
     * @author  wanghb
     * @edit
     */
    public static String getDataBaseType(){
        try {
            if(PowerUtil.isNull( dataBaseType )){
                dataBaseType  = SpringContextUtil.getBean( JdbcTemplate.class ).getDataSource().getConnection().getMetaData().getDatabaseProductName();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return dataBaseType;
    }

    /**
     * @description  生成oracle toDate语句
     * @param  nowDate
     * @return  返回结果
     * @date  20/09/12 13:20
     * @author  wanghb
     * @edit
     */
    public static String getOracelToDate(Date nowDate) {
        return new StringBuilder("to_date('" ).append( DateUtil.toString( nowDate,DateUtil.DATE_LONG )  ).append( "','yyyy-mm-dd hh24:mi:ss')").toString();
    }

    /**
     * @description  生成oracle toDate语句
     * @param  nowDate
     * @return  返回结果
     * @date  20/09/12 13:20
     * @author  wanghb
     * @edit
     */
    public static String getOracleUpate(Date nowDate,String userId) {
        return  new StringBuilder("update_user = '" ).append( userId ).append( "',update_time = " ).append( getOracelToDate( nowDate )  ).toString();
    }

    /**
     * @description  生成oracle toDate语句
     * @param  columnName
     * @return  返回结果
     * @date  20/09/12 13:20
     * @author  wanghb
     * @edit
     */
    public static String getOracelToChar(String columnName) {
        //:ss
        return new StringBuilder("to_char(" ).append( columnName  ).append( ",'yyyy-mm-dd hh24:mi') ").toString();
    }

}
