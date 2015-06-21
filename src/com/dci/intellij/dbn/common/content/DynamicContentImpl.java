package com.dci.intellij.dbn.common.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.dci.intellij.dbn.common.content.dependency.ContentDependencyAdapter;
import com.dci.intellij.dbn.common.content.dependency.VoidContentDependencyAdapter;
import com.dci.intellij.dbn.common.content.loader.DynamicContentLoadException;
import com.dci.intellij.dbn.common.content.loader.DynamicContentLoader;
import com.dci.intellij.dbn.common.dispose.DisposerUtil;
import com.dci.intellij.dbn.common.dispose.FailsafeUtil;
import com.dci.intellij.dbn.common.filter.Filter;
import com.dci.intellij.dbn.common.list.AbstractFiltrableList;
import com.dci.intellij.dbn.common.list.FiltrableList;
import com.dci.intellij.dbn.common.thread.BackgroundTask;
import com.dci.intellij.dbn.common.util.CollectionUtil;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.GenericDatabaseElement;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.util.Disposer;
import gnu.trove.THashMap;

public abstract class DynamicContentImpl<T extends DynamicContentElement> implements DynamicContent<T> {
    public static final List EMPTY_CONTENT = Collections.unmodifiableList(new ArrayList(0));
    public static final List EMPTY_UNTOUCHED_CONTENT = Collections.unmodifiableList(new ArrayList(0));

    private long changeTimestamp = 0;
    private volatile boolean isLoading = false;
    private volatile boolean isLoadingInBackground = false;
    private volatile boolean isLoaded = false;
    private volatile boolean isDirty = false;
    private volatile boolean disposed = false;

    private GenericDatabaseElement parent;
    protected DynamicContentLoader<T> loader;
    protected ContentDependencyAdapter dependencyAdapter;
    private boolean indexed;
    private Map<String, T> index;

    protected List<T> elements = EMPTY_UNTOUCHED_CONTENT;

    protected DynamicContentImpl(@NotNull GenericDatabaseElement parent, @NotNull DynamicContentLoader<T> loader, ContentDependencyAdapter dependencyAdapter, boolean indexed) {
        this.parent = parent;
        this.loader = loader;
        this.dependencyAdapter = dependencyAdapter;
        this.indexed = indexed;
    }

    @Nullable
    protected abstract Filter<T> getFilter();

    @NotNull
    public GenericDatabaseElement getParentElement() {
        return FailsafeUtil.get(parent);
    }

    @NotNull
    public ConnectionHandler getConnectionHandler() {
        return FailsafeUtil.get(getParentElement().getConnectionHandler());
    }

    public DynamicContentLoader<T> getLoader() {
        return loader;
    }

    public ContentDependencyAdapter getDependencyAdapter() {
        return dependencyAdapter;
    }

    public long getChangeTimestamp() {
        return changeTimestamp;
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    /**
     * The content can load
     */
    public boolean canLoadFast() {
        return dependencyAdapter.canLoadFast();
    }

    @Override
    public boolean isSubContent() {
        return dependencyAdapter.isSubContent();
    }

    public boolean isLoading() {
        return isLoading;
    }

    public boolean isDirty() {
        return isDirty || dependencyAdapter.isDirty();
    }

    public boolean isDisposed() {
        return disposed;
    }

    public void setDirty(boolean dirty) {
        isDirty = dirty;
    }

    public final void load(boolean force) {
        if (shouldLoad(force)) {
            synchronized (this) {
                if (shouldLoad(force)) {
                    isLoading = true;
                    try {
                        performLoad();
                        isLoaded = true;
                    } catch (InterruptedException e) {
                        setElements(EMPTY_CONTENT);
                        isDirty = true;
                    } finally {
                        isLoading = false;
                        updateChangeTimestamp();
                    }
                }
            }
        }
    }

    public final void reload() {
        if (!disposed && !isLoading) {
            synchronized (this) {
                if (!disposed && !isLoading) {
                    isLoading = true;
                    try {
                        performReload();
                        isLoaded = true;
                    } catch (InterruptedException e) {
                        setElements(EMPTY_CONTENT);
                        isDirty = true;
                    } finally {
                        isLoading = false;
                        updateChangeTimestamp();
                    }
                }
            }
        }
    }

    @Override
    public final void loadInBackground(final boolean force) {
        if (!isLoadingInBackground && shouldLoad(force)) {
            synchronized (this) {
                if (!isLoadingInBackground && shouldLoad(force)) {
                    isLoadingInBackground = true;
                    ConnectionHandler connectionHandler = getConnectionHandler();
                    String connectionString = " (" + connectionHandler.getName() + ')';
                    new BackgroundTask(getProject(), "Loading data dictionary" + connectionString, true) {
                        @Override
                        protected void execute(@NotNull ProgressIndicator progressIndicator) {
                            try {
                                DatabaseLoadMonitor.startBackgroundLoad();
                                load(force);
                            } finally {
                                DatabaseLoadMonitor.endBackgroundLoad();
                                isLoadingInBackground = false;
                            }
                        }
                    }.start();
                }
            }
        }
    }

    private void performLoad() throws InterruptedException {
        checkDisposed();
        dependencyAdapter.beforeLoad();
        checkDisposed();
        try {
            // mark first the dirty status since dirty dependencies may
            // become valid due to parallel background load
            isDirty = false;
            loader.loadContent(this, false);
        } catch (DynamicContentLoadException e) {
            isDirty = !e.isModelException();
        }
        checkDisposed();
        dependencyAdapter.afterLoad();
    }

    private void performReload() throws InterruptedException {
        checkDisposed();
        dependencyAdapter.beforeReload(this);
        checkDisposed();
        try {
            checkDisposed();
            loader.reloadContent(this);
        } catch (DynamicContentLoadException e) {
            isDirty = !e.isModelException();
        }
        checkDisposed();
        dependencyAdapter.afterReload(this);
    }

    public void updateChangeTimestamp() {
        changeTimestamp = System.currentTimeMillis();
    }


    /**
     * do whatever is needed after the content is loaded (e.g. refresh browser tree..)
     */
    public abstract void notifyChangeListeners();

    public void setElements(List<T> elements) {
        if (disposed || elements == null || elements.size() == 0) {
            elements = EMPTY_CONTENT;
            index = null;
        } else {
            sortElements(elements);
        }
        List<T> oldElements = this.elements;
        this.elements = new AbstractFiltrableList<T>(elements) {
            @Nullable
            @Override
            public Filter<T> getFilter() {
                return DynamicContentImpl.this.getFilter();
            }
        };
        updateIndex();
        if (oldElements.size() != 0 || elements.size() != 0 ){
            notifyChangeListeners();
        }
        if (!dependencyAdapter.isSubContent() && oldElements.size() > 0 ) {
            DisposerUtil.dispose(oldElements);
        }
    }

    public void sortElements(List<T> elements) {
        Collections.sort(elements);
    }

    @NotNull
    public List<T> getElements() {
        if (isSubContent() || DatabaseLoadMonitor.isEnsureDataLoaded() || DatabaseLoadMonitor.isLoadingInBackground()) {
            load(false);
        } else{
            loadInBackground(false);
        }
        return elements;
    }

    @Override
    public List<T> getAllElements() {
        List<T> elements = getElements();
        if (elements instanceof FiltrableList) {
            FiltrableList<T> filteredElements = (FiltrableList<T>) elements;
            return filteredElements.getFullList();
        }
        return elements;
    }


    protected void updateIndex() {
        if (indexed) {
            if (elements.size() > 30) {
                if (index == null)
                    index = new THashMap<String, T>(); else
                    index.clear();

                for (T element : elements) {
                    String name = element.getName().toUpperCase();
                    index.put(name, element);
                }
            } else {
                index = null;
            }
        }
    }

    public T getElement(String name, int overload) {
        if (name != null) {
            List<T> elements = getAllElements();
            if (indexed && index != null) {
                return index.get(name.toUpperCase());
            } else {
                for (T element : elements) {
                    if (element.getName().equalsIgnoreCase(name)) {
                        if (overload == 0 || overload == element.getOverload()) {
                            return element;
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    @Nullable
    public List<T> getElements(String name) {
        List<T> elements = null;
        for (T element : getAllElements()) {
            if (element.getName().equalsIgnoreCase(name)) {
                if (elements == null) {
                    elements = new ArrayList<T>();
                }
                elements.add(element);
            }
        }
        return elements;
    }

    public int size() {
        return getElements().size();
    }

    public boolean shouldLoad(boolean force) {
        if (isLoading || disposed) {
            return false;
        }

        ConnectionHandler connectionHandler = getConnectionHandler();
        if (force || !isLoaded) {
            return dependencyAdapter.canConnect(connectionHandler);
        }

        if (isDirty()) {
            return dependencyAdapter.canLoad(connectionHandler);
        }

        return false;
    }

    public void checkDisposed() throws InterruptedException {
        if (disposed) throw new InterruptedException();
    }

    public void dispose() {
        if (!disposed) {
            disposed = true;
            if (elements != EMPTY_CONTENT && elements != EMPTY_UNTOUCHED_CONTENT) {
                if (dependencyAdapter.isSubContent())
                    elements.clear(); else
                    DisposerUtil.dispose(elements);
            }
            CollectionUtil.clearMap(index);
            Disposer.dispose(dependencyAdapter);
            dependencyAdapter = VoidContentDependencyAdapter.INSTANCE;
            parent = null;
        }
    }
}
