package com.ebiz.connection;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.tomcat.util.http.fileupload.IOUtils;

public class MyBatisConnection {
	private static final String resource = "/mybatis-config.xml";
    private static SqlSessionFactory sqlSessionFactory;

    static {
          Reader reader = null;
          try {
        	    System.out.println("JDBC connection");
                 reader = Resources.getResourceAsReader(resource);
                 sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
              System.out.println("JDBC connected");
          } catch (IOException e) {
                 System.err.println(e);
          } finally {
                 IOUtils.closeQuietly(reader);
          }
    }

    public static SqlSession getSession() {
          return sqlSessionFactory.openSession();
    }

}
