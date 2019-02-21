package com.xpu.everything.cmd;

import com.xpu.everything.EverythingManager;
import com.xpu.everything.config.EverythingConfig;
import com.xpu.everything.config.PropertiesParse;
import com.xpu.everything.core.model.Condition;
import com.xpu.everything.core.model.Thing;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class EverythingCmdApp {
    private static Scanner scanner = new Scanner(System.in);


    public static void main(String[] args) {
        //解析用户参数
        parseParams(args);

        //创建统一调度器
        EverythingManager manager = EverythingManager.getInstance();

        //启动清理线程
        manager.startBackgroundClearThread();

        //启动监控
        manager.startFileSystemMonitor();

        //交互式
        interactive(manager);
    }

    private static void parseParams(String[] args) {
        EverythingConfig config = EverythingConfig.getInstance();

        //为了避免文件中有空格，重新拼接字符串
        StringBuilder paramStrbuilder = new StringBuilder();
        for (String s : args) {
            paramStrbuilder.append(" ").append(s);
        }
        //重新拼接的字符串
        String paramStr = paramStrbuilder.toString().trim();
        int handindex = paramStr.indexOf("=");
        //如果用户指定的参数不对，不再做解析直接使用默认值
        if (handindex != -1 && handindex < paramStr.length()) {
            //操作参数
            String paramHand = paramStr.substring(0, handindex + 1);
            //赋值参数
            String argsHandle = paramStr.substring(handindex + 1);
            //赋值参数拆分成数组
            //一个一个字符串解析
            String[] paramList = argsHandle.split(";");


            String maxReturnParam = "--maxReturn=";
            if (maxReturnParam.equals(paramHand)) {
                try {
                    config.setMaxReturn(Integer.parseInt(paramList[0]));
                }catch (NumberFormatException e){
                    System.out.println("--maxReturn=参数格式异常");
                }
            }

            String depthOrderAscParam = "--depthOrderByAsc=";
            if (depthOrderAscParam.equals(paramHand)) {
                config.setDeptOrderAsc(Boolean.parseBoolean(paramList[0]));
            }
            String includePathParam = "--includePath=";
            if (includePathParam.equals(paramHand)) {
                if (paramList.length > 0) {
                    config.getIncludePath().clear();
                }
                for (String path : paramList) {
                    if (path != null) {
                        config.getIncludePath().add(path);
                    }
                }
            }
            String excludePathParam = "--excludePath=";
            if (excludePathParam.startsWith(paramHand)) {
                if (paramList.length > 0) {
                    config.getExcludePath().clear();
                }
                for (String path : paramList) {
                    if (path != null) {
                        config.getExcludePath().add(path);
                    }
                }
            }
        }
    }

    private static void interactive(EverythingManager manager){
        do {
            System.out.print("everything >> ");
            String input = scanner.nextLine();
            if(input == null)
                continue;
            History.pushHistory(input);
            //优先处理search
            if (input.startsWith("search")) {
                String[] values = input.split(" ");
                if (values.length >= 2) {
                    if (!values[0].equals("search")) {
                        help();
                        continue;
                    }
                    Condition condition = new Condition();
                    String name = values[1];
                    condition.setName(name);
                    if (values.length >= 3) {
                        String fileType = values[2];
                        condition.setFiletype(fileType.toUpperCase());
                    }
                    search(manager, condition);
                    continue;
                } else {
                    help();
                    continue;
                }
            }

            switch (input) {
                case "help":
                    help();
                    break;
                case "index":
                    index(manager);
                    break;
                case "quit":
                    quit();
                    break;
                case "history":
                    history();
                    break;
                default:
                    help();
            }
        } while (true);

    }

    private static void history() {
        try {
            List<String> strings = IOUtils.readLines(new FileInputStream(
                    new File("history.txt")), "UTF-8");
            for(String s: strings){
                System.out.println(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void index(EverythingManager manager) {
        new Thread(manager::buildIndex).start();
    }

    private static void search(EverythingManager manager, Condition condition){
        EverythingConfig config = EverythingConfig.getInstance();
        condition.setLimit(config.getMaxReturn());
        condition.setOrderByAsc(config.getDeptOrderAsc());
        List<Thing> thingsList = manager.search(condition);
        System.out.println("已经检索到："+thingsList.size()+" 条数据，最大返回条数："+ PropertiesParse.getMaxReturn());
        for(Thing s:thingsList){
            System.out.println(s.getPath());
        }

    }
    private static void help() {
        System.out.println("命令列表:");
        System.out.println("退出: quit");
        System.out.println("帮助: help");
        System.out.println("索引: index");
        System.out.println("搜索: search <name> [<file-Type> img | doc | bin | archieve | other]");
        System.out.println("搜索文件特殊格式: search <\"name contain spacing \"> [<file-Type> img | doc | bin | archieve | other]");
    }

    private static void quit() {
        System.out.println("再见");
        EverythingManager.getInstance().stopFileSystemMonitor();
        System.exit(0);
    }
}
