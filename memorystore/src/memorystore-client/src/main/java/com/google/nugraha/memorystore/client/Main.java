package com.google.nugraha.memorystore.client;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.concurrent.Callable;

import org.redisson.Redisson;
import org.redisson.RedissonNode;
import org.redisson.api.RExecutorService;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.api.annotation.RInject;
import org.redisson.config.Config;
import org.redisson.config.RedissonNodeConfig;
import org.redisson.config.SingleServerConfig;

public class Main {

    public static class RunnableTask implements Runnable, Serializable {

        @RInject
        RedissonClient redisson;

        //@Override
        public void run() {
            RMap<String, String> map = redisson.getMap("myMap");
            map.put("5", "11");
        }
        
    }
    
    public static class CallableTask implements Callable<String>, Serializable {

        @RInject
        RedissonClient redisson;
        
        //@Override
        public String call() throws Exception {
            RMap<String, String> map = redisson.getMap("myMap");
            map.put("1", "2");
            return map.get("3");
        }

    }
    
    public static void main(String[] args) throws IOException {
    	Config config = Config.fromYAML(new File("config-file.yaml"));  

        
        RedissonClient redisson = Redisson.create(config);

        RedissonNodeConfig nodeConfig = new RedissonNodeConfig(config);
        nodeConfig.setExecutorServiceWorkers(Collections.singletonMap("myExecutor", 1));
        RedissonNode node = RedissonNode.create(nodeConfig);
        node.start();

        RExecutorService e = redisson.getExecutorService("myExecutor");
        e.execute(new RunnableTask());
        e.submit(new CallableTask());
        
        e.shutdown();
        node.shutdown();
    }
    
}