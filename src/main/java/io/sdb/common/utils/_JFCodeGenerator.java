package io.sdb.common.utils;

import com.jfinal.kit.Kv;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.activerecord.generator.ColumnMeta;
import com.jfinal.plugin.activerecord.generator.DataDictionaryGenerator;
import com.jfinal.plugin.activerecord.generator.MetaBuilder;
import com.jfinal.plugin.activerecord.generator.TableMeta;
import io.sdb.builder.MyMetaBuilder;
import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class _JFCodeGenerator {
    public static final _JFCodeGenerator me = new _JFCodeGenerator();
    public static final boolean serviceGenerate = true;

    protected String[] excludedControllerClass = new String[]{
            "Area",
            "SysCaptcha",
            "Cart",
            "OrderDetail",
            "OrderMaster",
            "Product",
            "Receiver",
            "SysRoleMenu",
            "ScheduleJob",
            "ScheduleJobLog",
            "Sn",
            "SysUserRole",
            "SysUserToken",
    };

    protected String[] includedVueClass = new String[]{
            "TestTable",
    };

    protected final JfEnjoy jfEngine = new JfEnjoy();

    protected Kv tablemetaMap = null;
    protected String packageBase = "io";
    protected String basePath = "sdb";
    protected String srcFolder = "src/main/java";
    protected String viewFolder = "/Users/apple/WebstormProjects/sdb-backend/src/views/modules/sys/";


    public _JFCodeGenerator setPackageBase(String packageBase) {
        this.packageBase = packageBase;
        return this;
    }

    public _JFCodeGenerator setBasePath(String basePath) {
        this.basePath = basePath;
        return this;
    }

    public _JFCodeGenerator setSrcFolder(String srcFolder) {
        this.srcFolder = srcFolder;
        return this;
    }

    public _JFCodeGenerator setViewFolder(String viewFolder) {
        this.viewFolder = viewFolder;
        return this;
    }

    protected class DataGenerator extends DataDictionaryGenerator {
        public DataGenerator(DataSource dataSource, String dataDictionaryOutputDir) {
            super(dataSource, dataDictionaryOutputDir);
        }

        public void rebuildColumnMetas(List<TableMeta> tableMetas) {
            super.rebuildColumnMetas(tableMetas);
        }
    }

    ;

    public TableMeta getTableMeta(String tableName) {
        if (tablemetaMap == null) {

            DataSource dataSource = _JFinalGenerator.getDataSource();

            MetaBuilder metaBuilder = new MetaBuilder(dataSource);
            metaBuilder.setDialect(new MysqlDialect());
            metaBuilder.addExcludedTable(_JFinalGenerator.excludedTable);
            List<TableMeta> tableMetas = metaBuilder.build();
            new DataGenerator(dataSource, null).rebuildColumnMetas(tableMetas);

            if (tableMetas.size() == 0) {
                System.out.println("TableMeta 数量为 0，不生成任何文件");
                return null;
            }
            Kv kv = Kv.create();
            for (TableMeta tableMeta : tableMetas) {
                kv.set(tableMeta.name, tableMeta);
            }
            tablemetaMap = kv;
        }
        return (TableMeta) tablemetaMap.get(tableName);
    }

    /**
     * 生成手脚架代码
     */
    public _JFCodeGenerator allRender(String className, String tableName) {
        return javaRender(className, tableName).htmlRender(className, tableName);
    }

    /**
     * java 代码生成
     */
    public _JFCodeGenerator javaRender(String className, String tableName) {
        //刷新 映射对象
        _JFinalGenerator.main(null);

        controller(className);
        service(className, tableName);
        return this;
    }

    private String toClassNameSmall(String className) {
        return new StringBuffer(className.substring(0, 1).toLowerCase()).append(className.substring(1)).toString();
    }

    private String toPackages() {
        return new StringBuffer(packageBase).append(".").append(basePath).toString();
    }

    /**
     * 生成Controller
     *
     * @param className 类名称
     */
    public void controller(String className) {
        for (String excluded:excludedControllerClass
             ) {
            if (excluded.equalsIgnoreCase(className)) {
                return;
            }
        }

        String packages = toPackages();

        String classNameSmall = toClassNameSmall(className);

        String controllerPathName = "/controller/";
        if (!StringUtils.startsWith(className.toLowerCase(), "sys")) {
            controllerPathName = "/controller/Sys";
        }

        jfEngine.render("/java/controller.html",
                Kv.by("package", packages)
                        .set("className", className)
                        .set("classNameSmall", classNameSmall)
                        .set("basePath", basePath)
                ,
                new StringBuilder()
                        .append(System.getProperty("user.dir"))
                        .append("/")
                        .append(srcFolder)
                        .append("/")
                        .append(packages.replace(".", "/"))
                        .append(controllerPathName)
                        .append(className)
                        .append("Controller.java")
        );
    }

    /**
     * 生成Service
     *
     * @param className 类名称
     * @param tableName 表名
     */
    public void service(String className, String tableName) {
        String packages = toPackages();

        String classNameSmall = toClassNameSmall(className);

        jfEngine.render("/java/service.html",
                Kv.by("package", packages)
                        .set("className", className)
                        .set("classNameSmall", classNameSmall)
                        .set("tableName", tableName)
                ,
                new StringBuilder()
                        .append(System.getProperty("user.dir"))
                        .append("/")
                        .append(srcFolder)
                        .append("/")
                        .append(packages.replace(".", "/"))
                        .append("/service/")
                        .append(className)
                        .append("Service.java")
        );
    }

    /**
     * 生成ServiceImpl
     *
     * @param className 类名称
     * @param tableName 表名
     */
    public void serviceImpl(String className, String tableName) {
        String packages = toPackages();

        String classNameSmall = toClassNameSmall(className);

        jfEngine.render("/java/serviceImpl.html",
                Kv.by("package", packages)
                        .set("className", className)
                        .set("classNameSmall", classNameSmall)
                        .set("tableName", tableName)
                ,
                new StringBuilder()
                        .append(System.getProperty("user.dir"))
                        .append("/")
                        .append(srcFolder)
                        .append("/")
                        .append(packages.replace(".", "/"))
                        .append("/service/impl/")
                        .append(className)
                        .append("ServiceImpl.java")
        );
    }

    /**
     * 生成Dao
     *
     * @param className 类名称
     * @param tableName 表名
     */
    public void dao(String className, String tableName) {
        String packages = toPackages();

        String classNameSmall = toClassNameSmall(className);

        jfEngine.render("/java/dao.html",
                Kv.by("package", packages)
                        .set("className", className)
                        .set("classNameSmall", classNameSmall)
                        .set("tableName", tableName)
                ,
                new StringBuilder()
                        .append(System.getProperty("user.dir"))
                        .append("/")
                        .append(srcFolder)
                        .append("/")
                        .append(packages.replace(".", "/"))
                        .append("/dao/")
                        .append(className)
                        .append("Dao.java")
        );
    }

    /**
     * @param className
     * @param tableName
     */
    public _JFCodeGenerator htmlRender(String className, String tableName) {
        TableMeta tablemeta = getTableMeta(tableName);

        htmlList(className, tablemeta);

        return this;
    }

    //页面的生成一般定制比较多..就来个简单的吧

    public void htmlList(String className, TableMeta tablemeta) {
        String packages = toPackages();
        String classNameSmall = toClassNameSmall(className);
        String basePathUrl = basePath.replace('.', '/');

        Map<String, String> columnMap = new HashMap<>();

        for (ColumnMeta columnMeta :tablemeta.columnMetas
             ) {

            String desc = StringUtils.substringBetween(columnMeta.remarks, "[", "]");
            columnMap.put(columnMeta.attrName, desc);
        }

        String primaryKey = StrKit.toCamelCase(tablemeta.primaryKey);

        jfEngine.render("/html/index.vue",
                Kv.by("tablemeta", tablemeta)
                        .set("package", packages)
                        .set("className", className)
                        .set("columnMap", columnMap)
                        .set("primaryKey", primaryKey)
                        .set("classNameSmall", classNameSmall)
                        .set("basePath", basePathUrl)
                ,
                new StringBuilder()
                        .append(viewFolder)
                        .append("/")
                        .append(classNameSmall)
                        .append("List.html")
        );
    }

    //页面的生成一般定制比较多..就来个简单的吧

    public void vue(String className, TableMeta tablemeta) {
        boolean includeFlag = false;
        for (String includeClass:includedVueClass
                ) {
            if (includeClass.equalsIgnoreCase(className)) {
                includeFlag = true;
                break;
            }
        }

        if (!includeFlag) {
            return;
        }

        String packages = toPackages();
        String classNameSmall = toClassNameSmall(className);
        String basePathUrl = basePath.replace('.', '/');

        Map<String, String> columnMap = new HashMap<>();

        for (ColumnMeta columnMeta :tablemeta.columnMetas
             ) {

            String desc = StringUtils.substringBetween(columnMeta.remarks, "[", "]");
            columnMap.put(columnMeta.attrName, desc);
        }

        String primaryKey = StrKit.toCamelCase(tablemeta.primaryKey);

        jfEngine.render("/html/index.html",
                Kv.by("tablemeta", tablemeta)
                        .set("package", packages)
                        .set("className", className)
                        .set("columnMap", columnMap)
                        .set("primaryKey", primaryKey)
                        .set("classNameSmall", classNameSmall)
                        .set("basePath", basePathUrl)
                ,
                new StringBuilder()
                        .append(viewFolder)
                        .append("/")
                        .append(classNameSmall)
                        .append(".vue")
        );
    }

    public void vueAddUpdate(String className, TableMeta tablemeta) {
        boolean includeFlag = false;
        for (String includeClass:includedVueClass
                ) {
            if (includeClass.equalsIgnoreCase(className)) {
                includeFlag = true;
                break;
            }
        }

        if (!includeFlag) {
            return;
        }

        String packages = toPackages();
        String classNameSmall = toClassNameSmall(className);
        String basePathUrl = basePath.replace('.', '/');

        Map<String, String> columnMap = new HashMap<>();

        for (ColumnMeta columnMeta :tablemeta.columnMetas
             ) {

            String desc = StringUtils.substringBetween(columnMeta.remarks, "[", "]");
            columnMap.put(columnMeta.attrName, desc);
        }

        String primaryKey = StrKit.toCamelCase(tablemeta.primaryKey);

        jfEngine.render("/html/add-or-update.html",
                Kv.by("tablemeta", tablemeta)
                        .set("package", packages)
                        .set("className", className)
                        .set("columnMap", columnMap)
                        .set("primaryKey", primaryKey)
                        .set("classNameSmall", classNameSmall)
                        .set("basePath", basePathUrl)
                ,
                new StringBuilder()
                        .append(viewFolder)
                        .append("/")
                        .append(classNameSmall)
                        .append("-add-or-update")
                        .append(".vue")
        );
    }

    public void generate(){
        DataSource dataSource = _JFinalGenerator.getDataSource();

        MyMetaBuilder metaBuilder = new MyMetaBuilder(dataSource);
        metaBuilder.setDialect(new MysqlDialect());
        metaBuilder.addExcludedTable(_JFinalGenerator.excludedTable);
        List<TableMeta> tableMetas = metaBuilder.build();
        for (TableMeta tableMeta : tableMetas
                ) {
            if (serviceGenerate) {
                _JFCodeGenerator.me.service(tableMeta.modelName, tableMeta.name);
                _JFCodeGenerator.me.serviceImpl(tableMeta.modelName, tableMeta.name);
                _JFCodeGenerator.me.dao(tableMeta.modelName, tableMeta.name);
                _JFCodeGenerator.me.controller(tableMeta.modelName);
                _JFCodeGenerator.me.vue(tableMeta.modelName, tableMeta);
                _JFCodeGenerator.me.vueAddUpdate(tableMeta.modelName, tableMeta);
            }
        }
    }

    // ... 继续扩展吧~

    public static void main(String[] args) {
        _JFCodeGenerator.me.generate();
    }
}
