package com.zjh.server.utils;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.LinkedList;
import java.util.logging.Logger;

/**
 * @author Mono
 */
public class MyDataSource implements DataSource {
    private final LinkedList<Connection> pool = new LinkedList<>();

    public MyDataSource(String driver, String url, String name, String pwd, int poolSize){

        try{

            Class.forName(driver);

            if(poolSize<=0){
                throw new RuntimeException("不支持的池大小"+poolSize);
            }
            for(int i=0;i<poolSize;i++){
                Connection conn = DriverManager.getConnection(url,name,pwd);
                conn = ConnectionProxy.getProxyedConnection(conn,pool);
                //获取被代理的对象
                pool.add(conn);
                //添加被代理的对象
            }
        }catch(Exception e){
            throw new RuntimeException(e.getMessage(),e);
        }

    }

    @Override
    public Connection getConnection() {
        synchronized (pool) {
            if(pool.size()==0){
                try {
                    pool.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e.getMessage(),e);
                }
                return getConnection();
            }else{
                return pool.removeFirst();
            }
        }
    }


    @Override
    public Connection getConnection(String username, String password) {
        throw new RuntimeException("不支持接收用户名和密码的操作");
    }


    @Override
    public PrintWriter getLogWriter() {
        throw new RuntimeException("不支持此操作");
    }


    @Override
    public void setLogWriter(PrintWriter out) {
        throw new RuntimeException("不支持此操作");
    }


    @Override
    public void setLoginTimeout(int seconds) {
        throw new RuntimeException("不支持此操作");
    }


    @Override
    public int getLoginTimeout() {

        return 0;
    }


    @Override
    public Logger getParentLogger() {

        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) {

        return DataSource.class.equals(iface);
    }

    static class ConnectionProxy implements InvocationHandler {

        private final Object o;

        private final LinkedList<Connection> pool;

        private ConnectionProxy(Object o, LinkedList<Connection> pool){

            this.o=o;

            this.pool=pool;

        }

        public static Connection getProxyedConnection(Object o,LinkedList<Connection> pool){

            Object proxed = Proxy.newProxyInstance(o.getClass().getClassLoader(),

                    new Class[]{Connection.class},new ConnectionProxy(o,pool));

            return (Connection) proxed;

        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args)

                throws Throwable {

            if("close".equals(method.getName())){
                synchronized (pool) {
                    pool.add((Connection) proxy);
                    //将被代理的对象放回池中
                    pool.notify();
                    //通知等待线程去获取一个连接吧
                }
                return null;

            }else{

                return method.invoke(o, args);

            }

        }
    }

}

