package io.sdb.common.utils;

import com.alibaba.fastjson.JSON;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RecordUtils {

    private static final Logger logger = LoggerFactory.getLogger(RecordUtils.class);

    /**
     * Record 转 任意实例类
     *
     * @param record
     * @return
     */
    public static <T> T converModel(Record record, Class<T> clazz) {
        String str = JSON.toJSONString(record.getColumns());
        T obj = JSON.parseObject(str, clazz);
        return obj;
    }

    /**
     * Record 转 任意实例类
     *
     * @param recordList
     * @return
     */
    public static <T> List<T> converModel(List<Record> recordList, Class<T> clazz) {

        List<Map<String, Object>> list = recordList.stream().map(item -> {
            return item.getColumns();
        }).collect(Collectors.toList());

        String str = JSON.toJSONString(list);
        List<T> arr = JSON.parseArray(str, clazz);
        return arr;
    }
}