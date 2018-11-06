package io.sdb.common.utils;

import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.generator.Generator;
import com.jfinal.plugin.activerecord.generator.MetaBuilder;
import com.jfinal.plugin.druid.DruidPlugin;

import javax.sql.DataSource;

/**
 * 在数据库表有任何变动时，运行一下 main 方法，极速响应变化进行代码重构
 */
public class _JFinalGenerator {

    final static String DB_NAME = "sdb";
    final static String DB_USER_NAME = "root";
    final static String DB_USER_PWD = "123456";
    final static String PK_NAME = "sdb"; //项目包名称

    final static String[] excludedTable = new String[]{
            "QRTZ_JOB_DETAILS",
            "QRTZ_TRIGGERS",
            "QRTZ_SIMPLE_TRIGGERS",
            "QRTZ_CRON_TRIGGERS",
            "QRTZ_SIMPROP_TRIGGERS",
            "QRTZ_BLOB_TRIGGERS",
            "QRTZ_CALENDARS",
            "QRTZ_PAUSED_TRIGGER_GRPS",
            "QRTZ_FIRED_TRIGGERS",
            "QRTZ_SCHEDULER_STATE",
            "QRTZ_LOCKS"
    };

	public static DataSource getDataSource() {
		String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/"+DB_NAME+"?useUnicode=true&characterEncoding=UTF-8";
		String user = DB_USER_NAME;
		String password = DB_USER_PWD;
		DruidPlugin druidPlugin = new DruidPlugin(jdbcUrl, user, password);
		druidPlugin.start();
		return druidPlugin.getDataSource();
	}

	public static void main(String[] args) {

        String rootPath = PathKit.getWebRootPath();

		// base model 所使用的包名
		String baseModelPackageName = "io."+PK_NAME+".model.base";
		// base model 文件保存路径
		String baseModelOutputDir = rootPath + "/src/main/java/io/"+PK_NAME+"/model/base";


		// model 所使用的包名 (MappingKit 默认使用的包名)
		String modelPackageName = "io."+PK_NAME+".model";
		// model 文件保存路径 (MappingKit 与 DataDictionary 文件默认保存路径)
		String modelOutputDir = baseModelOutputDir + "/..";

		// 创建生成器
		Generator gernerator = new Generator(getDataSource(), baseModelPackageName, baseModelOutputDir, modelPackageName, modelOutputDir);


		MetaBuilder mataBuilder = new MetaBuilder(getDataSource());

		mataBuilder.addExcludedTable(excludedTable);

		gernerator.setMetaBuilder(mataBuilder);

		// 添加不需要生成的表名
//		gernerator.addExcludedTable("order_coupon");
		// 设置是否在 Model 中生成 dao 对象
		gernerator.setGenerateDaoInModel(true);
		// 设置是否生成字典文件
		gernerator.setGenerateDataDictionary(false);
		// 设置需要被移除的表名前缀用于生成modelName。例如表名 "osc_user"，移除前缀 "osc_"后生成的model名为 "User"而非 OscUser
		gernerator.setRemovedTableNamePrefixes("tb_");
		gernerator.generate();

		//代码生成
        _JFCodeGenerator.me.generate();
	}
}




