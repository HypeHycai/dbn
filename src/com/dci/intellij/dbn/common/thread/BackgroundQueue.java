package com.dci.intellij.dbn.common.thread;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.jetbrains.annotations.NotNull;

import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;

public abstract class BackgroundQueue<T extends Queueable> {

    private String title;
    private Project project;
    private boolean executing = false;
    private final Queue<T> elements = new ConcurrentLinkedQueue<T>();

    BackgroundQueue(Project project, String title) {
        this.project = project;
        this.title = title;
    }

    public void queue(T element) {
        if (!this.elements.contains(element)) {
            element.setQueued(true);
            this.elements.add(element);
            execute();
        }
    }

    private void execute() {
        if (!executing) {
            executing = true;
            new BackgroundTask(project, title, true, true) {
                @Override
                protected void execute(@NotNull ProgressIndicator progressIndicator) {
                    try {
                        T element = elements.poll();
                        while (element != null) {
                            try {
                                BackgroundQueue.this.process(element);
                            } catch (ProcessCanceledException ignore) {}

                            if (progressIndicator.isCanceled()) {
                                cancel();
                            }
                            element = elements.poll();
                        }
                    } finally {
                        executing = false;
                        if (progressIndicator.isCanceled()) {
                            cancel();
                        }
                    }
                }
            }.start();
        }
    }

    protected abstract void process(T element);

    protected abstract void onCompletion(T element);
    protected abstract void onCancel(T element);
    protected abstract void onError(T element);

    public void cancel() {
        // cleanup queue for "untouched" elements
        T element = elements.poll();
        while(element != null) {
            //element.getExecutionStatus().reset();
            element = elements.poll();
        }
    }

    public boolean contains(T element) {
        return elements.contains(element);
    }

    public void cancel(T element) {
        element.setQueued(false);
        elements.remove(element);
        if (elements.size() == 0) {
            executing = false;
        }
    }
}