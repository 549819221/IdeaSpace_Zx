package com.server.hostel.util;

import com.alibaba.fastjson.JSON;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @Title: Map的工具类
 * @author wanghb
 * @date 2019-07-18
 */
public class MapUtil {
    /**
     * 通过键获取String类型
     * @param key  键
     * @return value  值
     * @author wanghb
     * @date 2019-06-25
     */
    public static String getString(Map map,Object key) {
        return PowerUtil.getString(map.get(key));
    }

    /**
     * 通过键获取String类型  如果获取不到返回默认值
     * @explain  跟getOrDefault有所不同的是  getOrDefault如果存在这个键  它也还会返回空 这个则不会 省去了多余的判断
     * @param key  键
     * @return value  值
     * @author wanghb
     * @date 2019-06-25
     */
    public static String getStrOrDefaultStr(Map map,Object key, String defaultValue) {
        Object value = map.get(key);
        if(value == null){
            return defaultValue;
        }
        return PowerUtil.getString(value);
    }

    /**
     * map模糊取值 方法
     * @param keyLike String
     * @param map Map<String,Integer>
     * @return String类型
     * @author wanghb
     * @date 2017-11-13
     */
    public static List<Integer> likeString(String keyLike, Map<String,Integer> map) {
        List<Integer> list_integer_01 = new ArrayList<>();
        Iterator it = map.entrySet().iterator();
        while(it.hasNext()) {

            Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>)it.next();
            String key = entry.getKey();
            Integer value = entry.getValue();
            if (key.indexOf(keyLike) != -1) {
                list_integer_01.add(value);
            }
        }
        return list_integer_01;
    }


    /**
     * 合并map 方法
     * @param maps Map<K, V>
     * @return String类型
     * @author wanghb
     * @date 2017-11-13

     */
    public static <K, V> Map mergeMaps(Map<K, V>... maps) {
        Class clazz = maps[0].getClass(); // 获取传入map的类型
        Map<K, V> map = null;
        try {
            map = (Map) clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0, len = maps.length; i < len; i++) {
            map.putAll(maps[i]);
        }
        return map;

    }

    /**
     * 实体类转Map 方法
     *
     * @param bean Object
     * @return String类型
     * @author wanghb
     * @date 2017-11-13

     */
    public static Map<String, Object> toMap(Object bean){
        if(bean == null){
            return new HashMap<>();
        }
        Class type = bean.getClass();
        Map<String,Object> returnMap = new HashMap<String, Object>();
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(type);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (int i = 0; i < propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();
                if (!propertyName.equals("class")) {
                    Method readMethod = descriptor.getReadMethod();
                    Object result = readMethod.invoke(bean, new Object[0]);
                    if (result != null) {
                        returnMap.put(propertyName, result);
                    } else {
                        returnMap.put(propertyName, "");
                    }
                }
            }
        }catch (IntrospectionException e){
        }catch (InvocationTargetException e){
        }catch (IllegalAccessException e){}

        return  returnMap;
    }


    /**
     * List实体类转ListMap 方法
     *
     * @param ListBean List<T>
     * @return String类型
     * @author wanghb
     * @date 2017-11-13

     */
    public static <T> List<Map<String, Object>> toListMap(List<T> ListBean)  {
        if(ListBean == null){
            return null;
        }
        List<Map<String, Object>> list_map_01 = new ArrayList<>();
        try {

            for (T bean : ListBean) {
                Class type = bean.getClass();
                Map<String,Object> returnMap = new HashMap<>();
                BeanInfo beanInfo = Introspector.getBeanInfo(type);
                PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
                for (int i = 0; i < propertyDescriptors.length; i++) {
                    PropertyDescriptor descriptor = propertyDescriptors[i];
                    String propertyName = descriptor.getName();
                    if (!propertyName.equals("class")) {
                        Method readMethod = descriptor.getReadMethod();
                        Object result = readMethod.invoke(bean, new Object[0]);
                        if (result != null) {
                            returnMap.put(propertyName, result);
                        } else {
                            returnMap.put(propertyName, "");
                        }
                    }
                }
                list_map_01.add(returnMap);
            }
        }catch (Exception e){
            return null;
        }
        return list_map_01;
    }

    /**
     * 将List<Map<String,Object>>  转成 List<been>的工具类
     * @param listMap List<Map<String,Object>> 类型
     * @param T Class 类型
     * @return List<T>类型
     * @author wanghb
     * @date 2018-05-28
     */
    public static <T> List<T> toListBean(List<Map<String,Object>> listMap, Class T){
        List<T> beanList = new ArrayList<T>();
        for(int i=0, n= listMap.size(); i<n; i++){
            Map<String,Object> map = listMap.get(i);
            String json = JSON.toJSONString(map);
            T bean = (T)JSON.parseObject(json, T);
            beanList.add(bean);
        }
        return beanList;
    }

    /**
     * 将Map<String,Object>  转成 been>的工具类
     * @param map <Map<String,Object> 类型
     * @param T Class 类型
     * @return T类型
     * @author wanghb
     * @date 2018-05-28
     */
    public static <T>T toBean(Map<String,Object> map, Class T) {
        T bean = (T)JSON.parseObject(JSON.toJSONString(map), T);
        return bean;
    }

    /**
     * 将命名改为驼峰的方法
     * @param listMap List<T>
     * @return String类型
     * @author wanghb
     * @date 2017-11-13

     */
    public static  void toHump(List<Map<String, Object>> listMap) {
        int listMapSize = listMap.size();
        for (int i = 0; i < listMapSize; i++) {
            Map<String,Object> map = listMap.get(i);
            Map<String,Object> humpMap = new HashMap<>();
            Set<Map.Entry<String, Object>> entrySet = map.entrySet();
            Iterator<Map.Entry<String, Object>> iter = entrySet.iterator();
            while (iter.hasNext())
            {
                Map.Entry<String, Object> entry = iter.next();
                String keyStr = entry.getKey();
                Object valueStr = entry.getValue();
                humpMap.put(PowerUtil.toHump(keyStr),valueStr);
            }
            listMap.set(i,humpMap);
        }
    }

    /**
     * 将命名改为驼峰的方法
     * @param map List<T>
     * @return String类型
     * @author wanghb
     * @date 2017-11-13

     */
    public static void toHump(Map<String, Object> map) {
        Map<String,Object> humpMap = new HashMap<>();
        Iterator<Map.Entry<String, Object>> iter = map.entrySet().iterator();
        while (iter.hasNext())
        {
            Map.Entry<String, Object> entry = iter.next();
            String keyStr = entry.getKey();
            Object valueStr = entry.getValue();
            humpMap.put(PowerUtil.toHump(keyStr),valueStr);
        }
        map.clear();
        Iterator<Map.Entry<String, Object>> iterHump = humpMap.entrySet().iterator();
        while (iterHump.hasNext())
        {
            Map.Entry<String, Object> entry = iterHump.next();
            String keyStr = entry.getKey();
            Object valueStr = entry.getValue();
            map.put(keyStr,valueStr);
        }
    }

    public static void main(String[] args) {
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("c_d","1");
        map.put("c_d1","1");

        Map<String,Object> map1 = new HashMap<>();
        map1.put("c1_d","1");
        list.add(map);
        list.add(map1);
        toHump(map);
        System.out.println(1);
    }
}
