package top.happing.mybatis.localisms;


import top.happing.mybatis.localism.Localism;

public class MySqlLocalism implements Localism {

    public static final MySqlLocalism instance = new MySqlLocalism();

    public String buildPaginationSql(String originalSql, int offset, int limit) {
        StringBuilder sql = new StringBuilder(originalSql);
        sql.append(" LIMIT ").append(offset).append(",").append(limit);
        return sql.toString();
    }

}
