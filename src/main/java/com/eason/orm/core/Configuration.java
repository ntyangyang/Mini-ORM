package com.eason.orm.core;

import com.eason.orm.utils.AnnotationUtil;
import com.eason.orm.utils.Dom4jUtil;
import org.dom4j.Document;

import java.io.File;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Eason
 * @createTime 2019-01-14 21:41
 * @description 该类用来解析并封装框架的核心配置文件中的数据
 */
public class Configuration {
    /**
     * classpath路径
     */
    private static String classpath;
    /**
     * 核心配置文件
     */
    private static File cfgFile;
    /**
     * <property>标签中的数据
     */
    private static Map<String, String> propConfig;
    /**
     * 映射配置文件路径
     */
    private static Set<String> mappingSet;
    /**
     * 实体类
     */
    private static Set<String> entitySet;
    /**
     * 映射信息
     */
    public static List<Mapper> mapperList;

    static {
        //得到的classpath路径
        classpath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        //针对中文路径进行转码
        try {
            classpath = URLDecoder.decode(classpath, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //得到核心配置文件
        System.out.println(classpath);
        cfgFile = new File(classpath + "miniORM.cfg.xml");
        if (cfgFile.exists()) {
            // 解析核心配置文件中的数据
            Document document = Dom4jUtil.getXMLByFilePath(cfgFile.getPath());
            propConfig = Dom4jUtil.elements2Map(document, "property", "name");
            mappingSet = Dom4jUtil.elements2Set(document, "mapping", "resource");
            entitySet = Dom4jUtil.elements2Set(document, "entity", "package");
        } else {
            cfgFile = null;
            System.out.println("未找到核心配置文件miniORM.cfg.xml");
        }

    }

    /**
     * @return 从propConfig集合中获取数据并连接数据库
     * @throws Exception
     */
    private Connection getConnection() throws Exception {
        String url = propConfig.get("connection.url");
        String driverClass = propConfig.get("connection.driverClass");
        String username = propConfig.get("connection.username");
        String password = propConfig.get("connection.password");

        Class.forName(driverClass);
        Connection connection = DriverManager.getConnection(url, username, password);
        connection.setAutoCommit(true);
        return connection;
    }

    /**
     * @throws Exception
     */
    private void getMapping() throws Exception {
        mapperList = new ArrayList<>();
        //1. 解析xxx.mapper.xml文件拿到映射数据
        for (String xmlPath : mappingSet) {
            Document document = Dom4jUtil.getXMLByFilePath(classpath + xmlPath);
            String className = Dom4jUtil.getPropValue(document, "class", "name");
            String tableName = Dom4jUtil.getPropValue(document, "class", "table");
            Map<String, String> nameValue = Dom4jUtil.elementsID2Map(document);
            Map<String, String> mapping = Dom4jUtil.elements2Map(document);

            Mapper mapper = new Mapper();
            mapper.setTableName(tableName);
            mapper.setClassName(className);
            mapper.setIdMapper(nameValue);
            mapper.setPropMapper(mapping);

            mapperList.add(mapper);
        }

        //2. 解析实体类中的注解拿到映射数据
        for (String packagePath : entitySet) {
            Set<String> nameSet = AnnotationUtil.getClassNameByPackage(packagePath);
            for (String name : nameSet) {
                Class clz = Class.forName(name);
                String className = AnnotationUtil.getClassName(clz);
                String tableName = AnnotationUtil.getTableName(clz);
                Map<String, String> nameValue = AnnotationUtil.getIdMapper(clz);
                Map<String, String> mapping = AnnotationUtil.getPropMapping(clz);

                Mapper mapper = new Mapper();
                mapper.setTableName(tableName);
                mapper.setClassName(className);
                mapper.setIdMapper(nameValue);
                mapper.setPropMapper(mapping);

                mapperList.add(mapper);
            }
        }
    }

    /**
     * 创建Session对象
     *
     * @return
     * @throws Exception
     */
    public Session buildSession() throws Exception {
        //1. 连接数据库
        Connection connection = getConnection();

        //2. 得到映射数据
        getMapping();

        //3. 创建ORMSession对象
        return new Session(connection);

    }
}
