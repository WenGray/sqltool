package xin.ryven.sqltool.generator;

import xin.ryven.sqltool.bean.TableProperties;
import xin.ryven.sqltool.tool.ArrayUtils;
import xin.ryven.sqltool.tool.CaseUtils;
import xin.ryven.sqltool.tool.FileUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 自动生成Mybatis文件
 *
 * @author Gray
 */
public class MyBatisGenerator {

    public static void generate(Class<?> clz) {
        TableProperties properties = tablePropertiesFromClass(clz);
        String modelXml = FileUtils.readResourceFile("g_base_mapper.xml");
        String allColumns = columnFromArray(properties.getColumns());
        String insertAll = generateInsertWithAll(properties);
        String selectOne = generateSelectOne(properties);
        String selectList = generateSelectList(properties);
        String updateNotNull = generateUpdateNotNull(properties);
        String deleteById = generateDeleteById(properties);
        String resultStr = modelXml.replace("JAVA_CLASS", properties.getClassFullName())
                .replace("ALL_COLUMNS", commonIndent(allColumns))
                .replace("SELECT_ONE", commonIndent(selectOne))
                .replace("SELECT_LIST", commonIndent(selectList))
                .replace("INSERT_ALL", commonIndent(insertAll))
                .replace("UPDATE_NOT_NULL", commonIndent(updateNotNull))
                .replace("DELETE_BY_ID", commonIndent(deleteById));
        FileUtils.writeToFile(null,
                properties.getClassName() + "Mapper.xml",
                resultStr);
    }

    /**
     * 通过反射获取表的信息
     *
     * @param tableClz class
     * @return properties 对象
     */
    private static TableProperties tablePropertiesFromClass(Class<?> tableClz) {
        if (tableClz == null) {
            throw new RuntimeException("class 不存在");
        }
        TableProperties properties = new TableProperties();
        String clzName = tableClz.getName();
        String className = clzName.substring(clzName.lastIndexOf(".") + 1, clzName.length());
        properties.setTableName(CaseUtils.upper2Underline(className));
        List<String> fieldList = new ArrayList<>();
        Class<?> clazz = tableClz;
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            //对class处理
            Method[] declaredMethods = clazz.getDeclaredMethods();
            for (Method method : declaredMethods) {
                //获取getter的属性
                String methodName = method.getName();
                if (methodName.startsWith("get")) {
                    //找到get属性，去掉get然后将第一个字母小写
                    fieldList.add(CaseUtils.extractGet(method.getName()));
                }
            }
        }
        String[] fields = new String[fieldList.size()];
        String[] columns = new String[fieldList.size()];
        for (int i = 0; i < fieldList.size(); i++) {
            fields[i] = fieldList.get(i);
            columns[i] = CaseUtils.upper2Underline(fieldList.get(i));
        }
        properties.setClassFullName(clzName);
        properties.setClassName(className);
        properties.setColumns(columns);
        properties.setFields(fields);
        return properties;
    }

    private static String generateInsertWithAll(TableProperties properties) {
        return String.format("insert into \n\t%s \n(%s) \nvalues \n(%s)",
                properties.getTableName(),
                columnFromArray(properties.getColumns(), "update_time"),
                fieldFromArray(properties.getFields(), "updateTime"));

    }

    private static String generateSelectOne(TableProperties properties) {
        return String.format("select \n <include refid=\"all_columns\"/> \nfrom %s\n where id = #{id}",
                properties.getTableName());
    }

    private static String generateSelectList(TableProperties properties) {
        return String.format("select \n <include refid=\"all_columns\"/> \nfrom %s\n where %s",
                properties.getTableName(),
                fieldSelectNotNull(properties));
    }

    private static String generateUpdateNotNull(TableProperties properties) {
        return String.format("update \n\t%s\n set \n %s \nwhere id = #{id}",
                properties.getTableName(),
                fieldUpdateNotNull(properties));
    }

    private static String generateDeleteById(TableProperties properties) {
        return String.format("delete from \n\t%s\n where id = #{id}",
                properties.getTableName());
    }

    private static String columnFromArray(String[] columns, String... excludes) {
        StringBuilder builder = new StringBuilder();
        for (String col : columns) {
            if (ArrayUtils.inStringArray(col, excludes)) {
                continue;
            }
            builder.append(builder.length() != 0 ? ", " : "")
                    .append(col);
        }
        return builder.toString();
    }

    private static String fieldFromArray(String[] fields, String... excludes) {
        StringBuilder builder = new StringBuilder();
        for (String field : fields) {
            if (ArrayUtils.inStringArray(field, excludes)) {
                continue;
            }
            builder.append(builder.length() != 0 ? ", " : "")
                    .append("#{")
                    .append(field)
                    .append("}");
        }
        return builder.toString();
    }

    private static String fieldSelectNotNull(TableProperties tableProperties) {
        StringBuilder sb = new StringBuilder();
        sb.append("1 = 1");
        for (int i = 0; i < tableProperties.getColumns().length; i++) {
            String col = tableProperties.getColumns()[i];
            String field = tableProperties.getFields()[i];
            sb.append(String.format("\n<if test=\"%s != null\">\n\tand %s\n</if>",
                    field,
                    col + " = #{" + field + "}"));
        }
        return sb.toString();
    }

    private static String fieldUpdateNotNull(TableProperties tableProperties) {
        StringBuilder sb = new StringBuilder();
        sb.append("id = id");
        for (int i = 0; i < tableProperties.getColumns().length; i++) {
            String col = tableProperties.getColumns()[i];
            String field = tableProperties.getFields()[i];
            //更新排除的属性
            if ("createTime".equals(field) || "id".equals(field)) {
                continue;
            }
            sb.append(String.format("\n<if test=\"%s != null\">\n\t, %s\n</if>",
                    field,
                    col + " = #{" + field + "}"));
        }
        return sb.toString();
    }

    private static String commonIndent(String src) {
        String indent = "\t\t";
        return indent + src.replace("\n", "\n" + indent);
    }
}
