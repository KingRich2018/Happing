package top.happing.mybatis.sqlsupplier;

import org.apache.ibatis.mapping.SqlCommandType;

public enum SqlMethod {
    // insert
    insert(SqlCommandType.INSERT, "insert", "<script>INSERT INTO %s %s VALUES %s</script>", "插入一条数据"),

    // delete
    deleteById(SqlCommandType.DELETE, "deleteById", "DELETE FROM %s WHERE %s=#{%s}", "根据ID 删除一条数据"),
    deleteByMap(SqlCommandType.DELETE, "deleteByMap", "<script>DELETE FROM %s %s</script>", "根据columnMap 条件删除记录"),
    delete(SqlCommandType.DELETE, "delete", "<script>DELETE FROM %s %s</script>", "根据 entity 条件删除记录"),
    deleteByIdList(SqlCommandType.DELETE, "deleteBatchIds", "<script>DELETE FROM %s WHERE %s IN (%s)</script>", "根据ID集合，批量删除数据"),

    // update
    updateById(SqlCommandType.UPDATE, "updateById", "<script>UPDATE %s %s WHERE %s=#{%s}</script>", "根据ID 修改数据"),
    update(SqlCommandType.UPDATE, "update", "<script>UPDATE %s %s %s</script>", "根据 whereEntity 条件，更新记录"),

    // query
    selectById(SqlCommandType.SELECT, "selectById", "SELECT %s FROM %s WHERE %s=#{%s}", "根据ID 查询一条数据"),
    selectByIdList(SqlCommandType.SELECT, "selectBatchIds", "<script>SELECT %s FROM %s WHERE %s IN (%s)</script>", "根据ID集合，批量查询数据"),
    selectByMap(SqlCommandType.SELECT, "selectByMap", "<script>SELECT %s FROM %s %s</script>", "根据columnMap 查询一条数据"),
    selectOne(SqlCommandType.SELECT, "selectOne", "<script>SELECT %s FROM %s %s</script>", "查询满足条件一条数据"),
    selectCount(SqlCommandType.SELECT, "selectCount", "<script>SELECT COUNT(1) FROM %s %s</script>", "查询满足条件总记录数"),
    selectList(SqlCommandType.SELECT, "selectList", "<script>SELECT %s FROM %s %s</script>", "查询满足条件所有数据"),
    selectOneByCriteria(SqlCommandType.SELECT, "selectOneByCriteria", "<script>SELECT %s FROM %s %s</script>", "根据QueryCriteria查询满足条件一条数据"),
    selectCountByCriteria(SqlCommandType.SELECT, "selectCountByCriteria", "<script>SELECT COUNT(1) FROM %s %s</script>", "根据QueryCriteria查询满足条件总记录数"),
    selectListByCriteria(SqlCommandType.SELECT, "selectListByCriteria", "<script>SELECT %s FROM %s %s</script>", "根据QueryCriteria查询满足条件所有数据");

    private final SqlCommandType type;
    private final String method;
    private final String script;
    private final String desc;

    SqlMethod(SqlCommandType type, String method, String script, String desc) {
        this.type = type;
        this.method = method;
        this.script = script;
        this.desc = desc;
    }

    public SqlCommandType getType() {
        return type;
    }

    public String getMethod() {
        return method;
    }

    public String getScript() {
        return script;
    }

    public String getDesc() {
        return desc;
    }
}
