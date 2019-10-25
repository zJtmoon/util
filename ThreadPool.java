package com.creditease.honeybot.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class ThreadPool {

	public  static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(20, 100, 10, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(1000));
	
	public  static ExecutorService threadPoolAudio = Executors.newFixedThreadPool(30);
	public  static ExecutorService cachePoolCase = Executors.newCachedThreadPool();
}
