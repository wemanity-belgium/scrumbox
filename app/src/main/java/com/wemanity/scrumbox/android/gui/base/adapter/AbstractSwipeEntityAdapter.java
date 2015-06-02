package com.wemanity.scrumbox.android.gui.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.wemanity.scrumbox.android.db.entity.Entity;
import com.wemanity.scrumbox.android.gui.base.OnEntityChangeListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public abstract class AbstractSwipeEntityAdapter<T extends Entity> extends BaseSwipeAdapter implements View.OnClickListener  {

    List<ActionViewId> listOfActionView;

    private OnEntityChangeListener<T> onEntityChangeListener;
    /**
     * Contains the list of objects that represent the data of this ArrayAdapter.
     * The content of this list is referred to as "the array" in the documentation.
     */
    private List<T> mObjects;

    /**
     * Lock used to modify the content of {@link #mObjects}. Any write operation
     * performed on the array should be synchronized on this lock. This lock is also
     * used by the filter (see {@link #getFilter()} to make a synchronized copy of
     * the original array of data.
     */
    private final Object mLock = new Object();

    /**
     * The resource indicating what views to inflate to display the content of this
     * array adapter.
     */
    private int mResource;

    /**
     * The resource indicating what views to inflate to display the content of this
     * array adapter in a drop down widget.
     */
    private int mDropDownResource;

    /**
     * If the inflated resource is not a TextView, {@link #mFieldId} is used to find
     * a TextView inside the inflated views hierarchy. This field must contain the
     * identifier that matches the one defined in the resource file.
     */
    private int mFieldId = 0;

    /**
     * Indicates whether or not {@link #notifyDataSetChanged()} must be called whenever
     * {@link #mObjects} is modified.
     */
    private boolean mNotifyOnChange = true;

    private Context mContext;

    private List<T> mOriginalValues;

    public AbstractSwipeEntityAdapter(Context mContext,int resource, List<T> objects) {
        this.mContext = mContext;
        this.mObjects = objects;
        mResource = resource;
    }
    public AbstractSwipeEntityAdapter(Context mContext,int resource) {
        this.mContext = mContext;
        this.mObjects = new ArrayList<>();
        mResource = resource;
        listOfActionView = getActionBottomViewIds();
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        View v = LayoutInflater.from(mContext).inflate(mResource, null);
        SwipeLayout swipeLayout = (SwipeLayout) v.findViewById(getSwipeLayoutResourceId(position));
        View bottomLayout = swipeLayout.getBottomView();
        for (ActionViewId action : listOfActionView){
            View actionView = swipeLayout.findViewById(action.getId());
            actionView.setOnClickListener(this);
        }
        return v;
    }

    @Override
    public void fillValues(int i, View view) {
        SwipeLayout swipeLayout = (SwipeLayout) view.findViewById(getSwipeLayoutResourceId(i));
        swipeLayout.open(false);
        fillFrontValues(i, view);
        fillBackValues(i, view);
    }

    private final void fillBackValues(int i, View view){
        for (ActionViewId action : listOfActionView){
            View actionView = view.findViewById(action.getId());
            actionView.setTag(getItem(i));
        }
    }

    protected abstract void fillFrontValues(int i, View view);

    protected abstract List<ActionViewId> getActionBottomViewIds();

    @Override
    public int getCount() {
        return mObjects.size();
    }

    @Override
    public T getItem(int position) {
        return mObjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mObjects.get(position).getId();
    }

    /**
     * Adds the specified object at the end of the array.
     *
     * @param object The object to add at the end of the array.
     */
    public void add(T object) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.add(object);
            } else {
                mObjects.add(object);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * Adds the specified Collection at the end of the array.
     *
     * @param collection The Collection to add at the end of the array.
     */
    public void addAll(Collection<? extends T> collection) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.addAll(collection);
            } else {
                mObjects.addAll(collection);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * Adds the specified items at the end of the array.
     *
     * @param items The items to add at the end of the array.
     */
    @SuppressWarnings("unchecked")
    public void addAll(T ... items) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                Collections.addAll(mOriginalValues, items);
            } else {
                Collections.addAll(mObjects, items);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * Inserts the specified object at the specified index in the array.
     *
     * @param object The object to insert into the array.
     * @param index The index at which the object must be inserted.
     */
    public void insert(T object, int index) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.add(index, object);
            } else {
                mObjects.add(index, object);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * Removes the specified object from the array.
     *
     * @param object The object to remove.
     */
    public void remove(T object) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.remove(object);
            } else {
                mObjects.remove(object);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void switchObjectById(T newEntity){
        synchronized (mLock) {
            List<T> values;
            if (mOriginalValues != null) {
                values = mOriginalValues;
            } else {
                values = mObjects;
            }
            boolean switched = false;
            for (T entity : values){
                if (entity.getId() == newEntity.getId()){
                    values.remove(entity);
                    values.add(newEntity);
                    switched = true;
                }
            }
            if (!switched){
                values.add(newEntity);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * Remove all elements from the list.
     */
    public void clear() {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.clear();
            } else {
                mObjects.clear();
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * Sorts the content of this adapter using the specified comparator.
     *
     * @param comparator The comparator used to sort the objects contained
     *        in this adapter.
     */
    public void sort(Comparator<? super T> comparator) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                Collections.sort(mOriginalValues, comparator);
            } else {
                Collections.sort(mObjects, comparator);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        mNotifyOnChange = true;
    }

    /**
     * Control whether methods that change the list ({@link #add},
     * {@link #insert}, {@link #remove}, {@link #clear}) automatically call
     * {@link #notifyDataSetChanged}.  If set to false, caller must
     * manually call notifyDataSetChanged() to have the changes
     * reflected in the attached view.
     *
     * The default is true, and calling notifyDataSetChanged()
     * resets the flag to true.
     *
     * @param notifyOnChange if true, modifications to the list will
     *                       automatically call {@link
     *                       #notifyDataSetChanged}
     */
    public void setNotifyOnChange(boolean notifyOnChange) {
        mNotifyOnChange = notifyOnChange;
    }


    @Override
    @SuppressWarnings("unchecked")
    public void onClick(View v) {
        ActionViewId action = findActionView(v.getId());
        if (onEntityChangeListener != null && action != null){
            onEntityChangeListener.onEntityChange(action.getAction(), (T)v.getTag());
        }
    }

    private ActionViewId findActionView(int id){
        for (ActionViewId action : listOfActionView){
            if (action.getId() == id){
                return action;
            }
        }
        return null;
    }

    public OnEntityChangeListener<T> getOnEntityChangeListener() {
        return onEntityChangeListener;
    }

    public void setOnEntityChangeListener(OnEntityChangeListener<T> onEntityChangeListener) {
        this.onEntityChangeListener = onEntityChangeListener;
    }
}
