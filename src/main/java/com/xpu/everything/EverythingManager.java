package com.xpu.everything;

import com.xpu.everything.config.EverythingConfig;
import com.xpu.everything.core.common.HandlePath;
import com.xpu.everything.core.dao.DataSourceFactory;
import com.xpu.everything.core.dao.FileIndexDao;
import com.xpu.everything.core.dao.impl.FileIndexImpl;
import com.xpu.everything.core.index.FileScan;
import com.xpu.everything.core.index.impl.FileScanImpl;
import com.xpu.everything.core.interceptor.FileIndexInterceptor;
import com.xpu.everything.core.interceptor.impl.ThingClearInterceptor;
import com.xpu.everything.core.model.Condition;
import com.xpu.everything.core.model.Thing;
import com.xpu.everything.core.monitor.FileWatch;
import com.xpu.everything.core.monitor.impl.FileWatchImpl;
import com.xpu.everything.core.search.FileSearch;
import com.xpu.everything.core.search.impl.FileSearchImpl;

import javax.sql.DataSource;
import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 调度器类
 */
public class EverythingManager {
    /**
     * 单例自身实例对象
     */
    private static volatile EverythingManager manager;

    /**
     * 文件搜索实现类对象
     */
    private FileSearch fileSearch;

    /**
     * 文件扫描实现类对象
     */
    private FileScan fileScan;

    /**
     * 清理删除的文件
     */
    private ThingClearInterceptor thingClearInterceptor;

    /**
     * 后台清理线程实例对象
     */
    private Thread backgroundClearThread;

    /**
     * 线程池对象
     */
    private ExecutorService executorService;

    //标识后台线程的状态
    private AtomicBoolean backgroundClearThreadStatus =
            new AtomicBoolean(false);


    /**
     * 文件监控
     */
    private FileWatch fileWatch;

    /**
     * 检索
     */
    public List<Thing> search(Condition condition){
        //JDK8流的特性
        return this.fileSearch.search(condition)
                .stream().filter(thing -> {
           String path = thing.getPath();
           File f = new File(path);
           boolean flag = f.exists();

           if(!flag){
               //删除
               thingClearInterceptor.apply(thing);
           }
           return flag;
        }).collect(Collectors.toList());
    }

    /**
     * 构建索引
     */
    public void buildIndex(){
        initOrResetDataBases();
        Set<String> includePath = EverythingConfig.getInstance().getIncludePath();
        if(this.executorService == null){
            this.executorService = Executors.
                    newFixedThreadPool(includePath.size(), new ThreadFactory() {
                        private final AtomicInteger threadId = new AtomicInteger(0);
                        @Override
                        public Thread newThread(Runnable r) {
                            Thread thread = new Thread(r);
                            thread.setName("Thead-Scan-"+threadId);
                            return thread;
                        }
                    });
        }

        //监视线程的状态
        final CountDownLatch countDownLatch = new CountDownLatch(includePath.size());
        System.out.println("开始构建索引...");

        for (String path: includePath) {
            this.executorService.submit(()-> {
                    EverythingManager.this.fileScan.index(path);
                    //当前任务完成,值-1
                    countDownLatch.countDown();
            });
        }
        //阻塞，直到任务完成，值为0
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("构建索引完成!!!");
        System.out.print("everything >>");
    }

    /**
     * 初始化给调度器的准备
     */
    private void initComponent() {
        //准备输数据源对象
        DataSource dataSource = DataSourceFactory.getDataSource();
        initOrResetDataBases();

        //数据库层得准备工作
        FileIndexDao fileIndexDao = new FileIndexImpl(dataSource);
        //业务层的对象
        this.fileScan = new FileScanImpl();
        this.fileSearch = new FileSearchImpl(fileIndexDao);
        //this.fileScan.interceptor(new FilePrintInterceptor());
        this.fileScan.interceptor(new FileIndexInterceptor(fileIndexDao));

        this.thingClearInterceptor = new ThingClearInterceptor(fileIndexDao);

        this.backgroundClearThread = new Thread(this.thingClearInterceptor);
        this.backgroundClearThread.setName("Thing Background Clear Thread");
        this.backgroundClearThread.setDaemon(true);

        this.fileWatch = new FileWatchImpl(fileIndexDao);

    }

    private void initOrResetDataBases(){
        String path = EverythingConfig.getInstance().getH2IndexPath();
        if(!new File(path+".mv.db").exists()){
            DataSourceFactory.initDatabase();
            System.out.println("初始化数据库");
        }
    }

    /**
     * Manager单例对象得获取
     */
    public static EverythingManager getInstance() {
        if (manager == null) {
            synchronized (EverythingManager.class) {
                if (manager == null) {
                    manager = new EverythingManager();
                    manager.initComponent();
                }
            }
        }
        return manager;
    }


    /**
     * 启动清理线程
     */
    public void startBackgroundClearThread(){
        if(this.backgroundClearThreadStatus.compareAndSet(false, true)){
            this.backgroundClearThread.start();
            this.backgroundClearThreadStatus.set(true);
        }else{
            System.out.println("后台清理线程已经启动,不能重复启动backgroundClearThreadstatus");
        }
    }

    /**
     * 启动文件系统监听
     */
    public void startFileSystemMonitor(){
        EverythingConfig config = EverythingConfig.getInstance();
        HandlePath handlePath = new HandlePath();
        handlePath.setIncludePath(config.getIncludePath());
        handlePath.setExcludePath(config.getExcludePath());
        this.fileWatch.monitor(handlePath);
        new Thread(() -> {
            try {
                fileWatch.getFileAlterationMonitor().start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * 停止文件系统监听
     */
    public void stopFileSystemMonitor(){
        try {
            this.fileWatch.getFileAlterationMonitor().stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
