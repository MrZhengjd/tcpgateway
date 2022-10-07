package com.game.mj.concurrent;

import io.netty.util.concurrent.*;
import io.netty.util.internal.SystemPropertyUtil;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zheng
 */
public class IGameEventExecutorGroup extends MultithreadEventExecutorGroup {
    public static final int DEFAULT_POOL_COUNT = 32;
    static final int DEFAULT_MAX_PENDING_EXECUTOR_TASKS = Math.max(16, SystemPropertyUtil.getInt("io.netty.eventexecutor.maxPendingTasks", 2147483647));
    //    protected MultithreadEventExecutorGroup(int nThreads, ThreadFactory threadFactory, Object... args) {
//        this(nThreads, (Executor)(threadFactory == null ? null : new ThreadPerTaskExecutor(threadFactory)), args);
//    }
    private static class Holder {
        private static IGameEventExecutorGroup INSTANCE = new IGameEventExecutorGroup(DEFAULT_POOL_COUNT, null);
    }


    public static IGameEventExecutorGroup getInstance() {
        return Holder.INSTANCE;
    }

    protected IGameEventExecutorGroup(int nThreads, ThreadFactory threadFactory, Object... args) {
        super(nThreads, threadFactory, args);
    }

    public IGameEventExecutorGroup(int nThreads, ThreadFactory threadFactory) {
        this(nThreads, threadFactory, LocalEventExecutorChoosenFacotry.getInstance(), DEFAULT_MAX_PENDING_EXECUTOR_TASKS, RejectedExecutionHandlers.reject());
    }

    public IGameEventExecutorGroup(int nThreads, ThreadFactory threadFactory, EventExecutorChooserFactory chooserFactory, Object... args) {
        super(nThreads, (Executor) (threadFactory == null ? null : new ThreadPerTaskExecutor(threadFactory)), chooserFactory, args);
    }

    protected IGameEventExecutorGroup(int nThreads, Executor executor, Object... args) {
        super(nThreads, executor, args);
    }

    protected IGameEventExecutorGroup(int nThreads, Executor executor, EventExecutorChooserFactory chooserFactory, Object... args) {
        super(nThreads, executor, chooserFactory, args);
    }

    public EventExecutor selectByHash(Object o) {
        return LocalEventExecutorChoosenFacotry.getInstance().getChooser().chooseByHash(o);

    }

    @Override
    protected EventExecutor newChild(Executor executor, Object... args) throws Exception {
        return new DefaultEventExecutor(this, executor, (Integer) args[0], (RejectedExecutionHandler) args[1]);
    }

    private interface EventExecutorOutterChoosen extends EventExecutorChooserFactory.EventExecutorChooser {
        EventExecutor chooseByHash(Object object);
    }

    private static final class LocalEventExecutorChoosenFacotry implements EventExecutorChooserFactory {
        private LocalPowerOfTwoEventExecutorChooser chooser;

        public LocalPowerOfTwoEventExecutorChooser getChooser() {
            return chooser;
        }

        public void setChooser(LocalPowerOfTwoEventExecutorChooser chooser) {
            this.chooser = chooser;
        }

        private static class Holder {
            private static LocalEventExecutorChoosenFacotry instance = new LocalEventExecutorChoosenFacotry();
        }

        public static LocalEventExecutorChoosenFacotry getInstance() {
            return Holder.instance;
        }

        public LocalEventExecutorChoosenFacotry() {
        }

        @Override
        public EventExecutorChooser newChooser(EventExecutor[] eventExecutors) {
            LocalPowerOfTwoEventExecutorChooser hold = new LocalPowerOfTwoEventExecutorChooser(eventExecutors);
            this.chooser = hold;
            return chooser;
        }

    }

    private static final class LocalPowerOfTwoEventExecutorChooser implements EventExecutorOutterChoosen {
        private final AtomicInteger idx = new AtomicInteger();
        private final EventExecutor[] executors;

        LocalPowerOfTwoEventExecutorChooser(EventExecutor[] executors) {
            this.executors = executors;
        }

        @Override
        public EventExecutor next() {
            return this.executors[this.idx.getAndIncrement() & this.executors.length - 1];
        }


        @Override
        public EventExecutor chooseByHash(Object object) {
            int hashCode = object.hashCode();
            return this.executors[hashCode & this.executors.length -1  ];
        }
    }
}
