package top.happing.mybatis.localism;

public interface Localism {

    String buildPaginationSql(String originalSql, int offset, int limit);
}
