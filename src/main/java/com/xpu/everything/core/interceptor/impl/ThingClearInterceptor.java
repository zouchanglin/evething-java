package com.xpu.everything.core.interceptor.impl;

import com.xpu.everything.core.dao.FileIndexDao;
import com.xpu.everything.core.interceptor.ThingInterceptor;
import com.xpu.everything.core.model.Thing;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class ThingClearInterceptor implements ThingInterceptor, Runnable {
    private Queue<Thing> queue = new ArrayBlockingQueue<>(1024);

    private final FileIndexDao fileIndexDao;

    public ThingClearInterceptor(FileIndexDao fileIndexDao) {
        this.fileIndexDao = fileIndexDao;
    }


    @Override
    public void apply(Thing thing) {
        queue.add(thing);
    }

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
