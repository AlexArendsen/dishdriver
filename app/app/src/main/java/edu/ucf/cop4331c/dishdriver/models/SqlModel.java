package edu.ucf.cop4331c.dishdriver.models;

/**
 * Created by copper on 3/14/17.
 */

public class SqlModel {
    private String sql;
    private String[] args;

    public SqlModel(String sql, String[] args) {
        this.sql = sql;
        this.args = args;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }
}
