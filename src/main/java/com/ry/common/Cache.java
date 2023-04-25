package com.ry.common;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

public class Cache {

    private static ConcurrentHashMap<String, Semaphore> semaphores = new ConcurrentHashMap<String, Semaphore>();

    public static Semaphore getSemaphore(String key) {
        Semaphore newSemaphore = new Semaphore(0);
        Semaphore semaphore = semaphores.putIfAbsent(key, newSemaphore);
        return semaphore == null ? newSemaphore : semaphore;
    }

    public static void removeSemaphore(String key) {
        semaphores.remove(key);
    }
}
