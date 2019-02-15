package com.xpu.everything.core.dao.impl;

import com.xpu.everything.core.dao.DataSourceFactory;
import com.xpu.everything.core.dao.FileIndexDao;
import com.xpu.everything.core.model.Condition;
import com.xpu.everything.core.model.FileType;
import com.xpu.everything.core.model.Thing;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FileIndexImpl implements FileIndexDao{
    private final DataSource dataSource;

    public FileIndexImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void insert(Thing thing) {
        Connection conn = null;
        PreparedStatement statement = null;

        try{
            conn = dataSource.getConnection();
            String sql = "insert into file_index(name, path, depth, file_type) values (?,?,?,?)";
            statement = conn.prepareStatement(sql);
            statement.setString(1, thing.getName());
            statement.setString(2, thing.getPath());
            statement.setInt(3, thing.getDepth());
            statement.setString(4, thing.getFileType().name());

            statement.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }finally {
//            try {
//                statement.close();
//                conn.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
            releaseResource(null,statement,conn);
        }
    }

    @Override
    public List<Thing> search(Condition condition) {
        List<Thing> things = new ArrayList<>();
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try{
            conn = dataSource.getConnection();
            //简单查询
            String sql = "select name, path, depth, file_type from file_index";

            //查询时优先选择深度最小
            //name : like
            //fileType: =
            //limit: limit offset
            //orderByAsc
            StringBuilder sqlBuilder = new StringBuilder();
            //select name, path, depth, file_type from file_index where  name like '%' condition.getName() %' and file_type = '
            sqlBuilder.append(sql);
            //name匹配
            sqlBuilder.append(" where ")
                    .append(" name like '%")
                    .append(condition.getName())
                    .append("%'");
            //fileType
            if(condition.getFiletype() != null) {
                sqlBuilder.append(" and file_type = '")
                        .append(condition.getFiletype().toUpperCase()).append("'");
            }
            //orderByAsc
            sqlBuilder.append(" order by depth ")
                    .append(condition.getOrderByAsc() ? "asc": "desc");
            //limit
            sqlBuilder.append(" limit ").append(condition.getLimit())
                    .append(" offset 0");

            System.out.println("sqlBuilder:"+sqlBuilder);
            statement = conn.prepareStatement(sqlBuilder.toString());
            resultSet = statement.executeQuery();

            //处理结果
            while(resultSet.next()){
                //数据库中的行记录变成java的Thing对象
                Thing thing = new Thing();
                thing.setName(resultSet.getString("name"));
                thing.setPath(resultSet.getString("path"));
                thing.setDepth(resultSet.getInt("depth"));
                thing.setFileType(FileType.lookupByName(
                        resultSet.getString("file_type")));

                things.add(thing);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            releaseResource(resultSet, statement, conn);
        }
        return things;
    }

    public static void main(String[] args) {
        Thing t = new Thing();
        t.setName("aaa.ppt");
        t.setPath("D:\\a\\test\\aaa.ppt");
        t.setDepth(3);
        t.setFileType(FileType.DOC);

        FileIndexImpl fileIndex = new FileIndexImpl(DataSourceFactory.getDataSource());
        //fileIndex.insert(t);

        Condition condition = new Condition();
        condition.setName("a");
        condition.setLimit(3);
        //condition.setFiletype("PNG");
        condition.setOrderByAsc(true);

        List<Thing> search = fileIndex.search(condition);
        for(Thing s: search){
            System.out.println(s);
        }

    }

    //重构：解决内部重复代码问题
    private void releaseResource(ResultSet resultSet, PreparedStatement statement, Connection conn) {
        if(statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(conn != null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(resultSet != null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
