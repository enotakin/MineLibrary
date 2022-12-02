package com.enotakin.library.entity.task;

import com.enotakin.library.entity.EntityManager;

public class EntityTickTask implements Runnable {

    private final EntityManager manager;

    public EntityTickTask(EntityManager manager) {
        this.manager = manager;
    }

    @Override
    public void run() {
        manager.onTick();
    }

}
