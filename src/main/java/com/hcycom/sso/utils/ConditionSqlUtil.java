package com.hcycom.sso.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Field;
import javax.persistence.Transient;

import com.github.pagehelper.util.StringUtil;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

public class ConditionSqlUtil {

    public ConditionSqlUtil() {
    }

    public static Condition keywordQuery(String keyword, Object object) {
        Class<?> clzz = object.getClass();
        Condition condition = new Condition(clzz);
        if (StringUtil.isEmpty(keyword)) {
            return condition;
        } else {
            Field[] fields = clzz.getDeclaredFields();
            Criteria criteria = condition.or();
            Field[] var9 = fields;
            int var8 = fields.length;

            for (int var7 = 0; var7 < var8; ++var7) {
                Field field = var9[var7];
                if (field.getAnnotation(Transient.class) == null) {
                    field.setAccessible(true);
                    String fieldName = field.getName();
                    String type = field.getGenericType().toString();
                    if (type.endsWith("String")) {
                        criteria.orLike(fieldName, "%" + keyword.trim() + "%");
                    }
                }
            }

            return condition;
        }
    }
}
