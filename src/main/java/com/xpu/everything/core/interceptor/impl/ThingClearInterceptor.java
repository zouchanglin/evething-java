package com.xpu.everything.core.interceptor.impl;

import com.xpu.everything.core.dao.FileIndexDao;
import com.xpu.everything.core.interceptor.ThingInterceptor;
import com.xpu.everything.core.model.Thing;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 数据清理过滤器，实现ThingInterceptor接口
 */
public class ThingClearInterceptor implements ThingInterceptor, Runnable {
    /**
     * 清理文件Thing对象缓冲区，默认值为1024个
     */
    private Queue<Thing> queue = new ArrayBlockingQueue<>(1024);

    /**
     * 文件Dao操作的对象
     */
    private final FileIndexDao fileIndexDao;

    /**
     * 构造方法传入Dao层操作对象
     * @param fileIndexDao Dao层操作对象：index操作对象
     */
    public ThingClearInterceptor(FileIndexDao fileIndexDao) {
        this.fileIndexDao = fileIndexDao;
    }


    /**
     * 拦截目的的实现方法：将Thing对象加入到回收缓冲区
     * @param thing 要删除的Thing对象
     */
    @Override
    public void apply(Thing thing) {
        queue.add(thing);
    }

    /**
     * 不断从缓冲区拿出对象，通过Dao层对象删除对应数据库表中的记录
     */
    @Override
    public void run() {
        while(true){
            Thing poll = this.queue.poll();
            if(poll != null){
                fileIndexDao.delete(poll);
            }
        }
    }
}
