package io.sdb.builder;

import com.jfinal.plugin.activerecord.generator.ColumnMeta;
import com.jfinal.plugin.activerecord.generator.MetaBuilder;
import com.jfinal.plugin.activerecord.generator.TableMeta;

import javax.sql.DataSource;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class MyMetaBuilder extends MetaBuilder {

    public MyMetaBuilder(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected void buildColumnMetas(TableMeta tableMeta) throws SQLException {
        String sql = dialect.forTableBuilderDoBuild(tableMeta.name);
        Statement stm = conn.createStatement();
        ResultSet rs = stm.executeQuery(sql);
        ResultSetMetaData rsmd = rs.getMetaData();

        DatabaseMetaData dbMeta = conn.getMetaData();

        Map<String, ColumnMeta> colmap = new HashMap<>();
        ResultSet colMetaRs = null;
        try {
            colMetaRs = dbMeta.getColumns(null, null, tableMeta.name, null);
            while (colMetaRs.next()) {
                ColumnMeta columnMeta = new ColumnMeta();
                columnMeta.name = colMetaRs.getString("COLUMN_NAME");
                columnMeta.remarks = colMetaRs.getString("REMARKS");
                colmap.put(columnMeta.name, columnMeta);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (colMetaRs != null) {
                colMetaRs.close();
            }
        }

        for (int i=1; i<=rsmd.getColumnCount(); i++) {
            ColumnMeta cm = new ColumnMeta();
            cm.name = rsmd.getColumnName(i);

            String typeStr = null;
            if (dialect.isKeepByteAndShort()) {
                int type = rsmd.getColumnType(i);
                if (type == Types.TINYINT) {
                    typeStr = "java.lang.Byte";
                } else if (type == Types.SMALLINT) {
                    typeStr = "java.lang.Short";
                }
            }

            if (typeStr == null) {
                String colClassName = rsmd.getColumnClassName(i);
                typeStr = typeMapping.getType(colClassName);
            }

            if (typeStr == null) {
                int type = rsmd.getColumnType(i);
                if (type == Types.BINARY || type == Types.VARBINARY || type == Types.LONGVARBINARY || type == Types.BLOB) {
                    typeStr = "byte[]";
                } else if (type == Types.CLOB || type == Types.NCLOB) {
                    typeStr = "java.lang.String";
                }
                // 支持 oracle 的 TIMESTAMP、DATE 字段类型，其中 Types.DATE 值并不会出现
                // 保留对 Types.DATE 的判断，一是为了逻辑上的正确性、完备性，二是其它类型的数据库可能用得着
                else if (type == Types.TIMESTAMP || type == Types.DATE) {
                    typeStr = "java.util.Date";
                }
                // 支持 PostgreSql 的 jsonb json
                else if (type == Types.OTHER) {
                    typeStr = "java.lang.Object";
                } else {
                    typeStr = "java.lang.String";
                }
            }

            typeStr = handleJavaType(typeStr, rsmd, i);

            cm.javaType = typeStr;

            // 构造字段对应的属性名 attrName
            cm.attrName = buildAttrName(cm.name);

            ColumnMeta cm1 = colmap.get(cm.name);
            if (cm1 != null) {
                cm.remarks = cm1.remarks;
            }

            tableMeta.columnMetas.add(cm);
        }

        rs.close();
        stm.close();
    }

}
