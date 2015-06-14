package com.wemanity.scrumbox.android.gui.base.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Checkable;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wemanity.scrumbox.android.db.entity.Entity;
import com.wemanity.scrumbox.android.gui.base.adapter.builder.AdapterEntitiesBuilder;
import com.wemanity.scrumbox.android.gui.base.adapter.builder.AdapterLayoutIdBuilder;
import com.wemanity.scrumbox.android.gui.base.adapter.builder.AdapterPropertiesBuilder;
import com.wemanity.scrumbox.android.gui.base.adapter.builder.AdapterViewIdsBuilder;
import com.wemanity.scrumbox.android.gui.base.adapter.builder.EntityAbapterBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class EntityAdapter<E extends Entity> extends BaseAdapter {

	public static abstract class Builder<A extends EntityAdapter<E>,E extends Entity, B extends Builder<A, E, B>>
			implements
			EntityAbapterBuilder<E , B>,
			AdapterLayoutIdBuilder<AdapterEntitiesBuilder<E, AdapterViewIdsBuilder<AdapterPropertiesBuilder<B>>>>,
			AdapterEntitiesBuilder<E, AdapterViewIdsBuilder<AdapterPropertiesBuilder<B>>>,
			AdapterViewIdsBuilder<AdapterPropertiesBuilder<B>>,
			AdapterPropertiesBuilder<B> {

		protected Activity activity;
		protected int layoutId;
		protected Collection<E> entities;
		protected int[] viewIds;
		protected String[] properties;

		protected Builder(){
			entities = new ArrayList<>();
		}

		protected abstract B self();

		@Override
		public B activity(Activity activity) {
			this.activity = activity;
			return self();
		}

		@Override
		public B layoutId(int layoutId) {
			this.layoutId = layoutId;
			return self();
		}

		@Override
		public B entities(E[] entities) {
			this.entities = Arrays.asList(entities);
			return self();
		}

		@Override
		public B entities(Collection<E> entities) {
			this.entities = new ArrayList<>(entities);
			return self();
		}

		@Override
		public B viewIds(int[] viewIds) {
			this.viewIds = viewIds;
			return self();
		}

		@Override
		public B properties(String[] properties) {
			this.properties = properties;
			return self();
		}

		public abstract A build();
	}

	public interface PropertyBinder {
		boolean bind(View v, String propertyName, Object propertyValue);
	}

	/**
	 * Contains the list of objects that represent the data of this ArrayAdapter.
	 * The content of this list is referred to as "the array" in the documentation.
	 */
	private List<E> mObjects;

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
	protected int mResource;

	/**
	 * The resource indicating what views to inflate to display the content of this
	 * array adapter in a drop down widget.
	 */
	private int mDropDownResource;

	/**
	 * Indicates whether or not {@link #notifyDataSetChanged()} must be called whenever
	 * {@link #mObjects} is modified.
	 */
	private boolean mNotifyOnChange = true;

	private Context mContext;

	// A copy of the original mObjects array, initialized from and then used instead as soon as
	// the mFilter ArrayFilter is used. mObjects will then only contain the filtered values.
	private ArrayList<E> mOriginalValues;
	private ArrayFilter mFilter;
	protected LayoutInflater mInflater;
	private int[] mTo;
	private String[] mFrom;

	private PropertyBinder propertyBinder = new PropertyBinder() {

		@Override
		public boolean bind(View v, String propertyName, Object propertyValue) {
			return false;
		}
	};


	/**
	 * Constructor
	 *
	 * @param context  The current context.
	 * @param resource The resource ID for a layout file containing a TextView to use when
	 *                 instantiating views.
	 */
	public EntityAdapter(Context context, int resource, int[] viewIds, String[] propetiesName) {
		init(context, resource, new ArrayList<E>(), viewIds, propetiesName);
	}

	/**
	 * Constructor
	 *
	 * @param context  The current context.
	 * @param resource The resource ID for a layout file containing a TextView to use when
	 *                 instantiating views.
	 * @param objects  The objects to represent in the ListView.
	 */
	public EntityAdapter(Context context, int resource, E[] objects, int[] viewIds, String[] propetiesName) {
		init(context, resource, Arrays.asList(objects), viewIds, propetiesName);
	}


	/**
	 * Constructor
	 *
	 * @param context  The current context.
	 * @param resource The resource ID for a layout file containing a TextView to use when
	 *                 instantiating views.
	 * @param objects  The objects to represent in the ListView.
	 */
	public EntityAdapter(Context context, int resource, Collection<E> objects, int[] viewIds, String[] propetiesName) {
		init(context, resource, objects, viewIds, propetiesName);
	}


	/**
	 * Adds the specified object at the end of the array.
	 *
	 * @param object The object to add at the end of the array.
	 */
	public void add(E object) {
		synchronized (mLock) {
			if (mOriginalValues != null) {
				mOriginalValues.add(object);
			} else {
				mObjects.add(object);
			}
		}
		if (mNotifyOnChange) {
			notifyDataSetChanged();
		}
	}

	/**
	 * Adds the specified Collection at the end of the array.
	 *
	 * @param collection The Collection to add at the end of the array.
	 */
	public void addAll(Collection<? extends E> collection) {
		synchronized (mLock) {
			if (mOriginalValues != null) {
				mOriginalValues.addAll(collection);
			} else {
				mObjects.addAll(collection);
			}
		}
		if (mNotifyOnChange) {
			notifyDataSetChanged();
		}
	}

	/**
	 * Adds the specified items at the end of the array.
	 *
	 * @param items The items to add at the end of the array.
	 */
	@SuppressWarnings("unchecked")
	public void addAll(E... items) {
		synchronized (mLock) {
			if (mOriginalValues != null) {
				Collections.addAll(mOriginalValues, items);
			} else {
				Collections.addAll(mObjects, items);
			}
		}
		if (mNotifyOnChange) {
			notifyDataSetChanged();
		}
	}

	/**
	 * Inserts the specified object at the specified index in the array.
	 *
	 * @param object The object to insert into the array.
	 * @param index  The index at which the object must be inserted.
	 */
	public void insert(E object, int index) {
		synchronized (mLock) {
			if (mOriginalValues != null) {
				mOriginalValues.add(index, object);
			} else {
				mObjects.add(index, object);
			}
		}
		if (mNotifyOnChange) {
			notifyDataSetChanged();
		}
	}

	/**
	 * Removes the specified object from the array.
	 *
	 * @param object The object to remove.
	 */
	public void remove(E object) {
		synchronized (mLock) {
			if (mOriginalValues != null) {
				mOriginalValues.remove(object);
			} else {
				mObjects.remove(object);
			}
		}
		if (mNotifyOnChange) {
			notifyDataSetChanged();
		}
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
		if (mNotifyOnChange) {
			notifyDataSetChanged();
		}
	}

	public void switchObjectById(E newEntity){
		synchronized (mLock) {
			List<E> values;
			if (mOriginalValues != null) {
				values = mOriginalValues;
			} else {
				values = mObjects;
			}
			boolean switched = false;
			for (E entity : values){
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
	 * Sorts the content of this adapter using the specified comparator.
	 *
	 * @param comparator The comparator used to sort the objects contained
	 *                   in this adapter.
	 */
	public void sort(Comparator<? super E> comparator) {
		synchronized (mLock) {
			if (mOriginalValues != null) {
				Collections.sort(mOriginalValues, comparator);
			} else {
				Collections.sort(mObjects, comparator);
			}
		}
		if (mNotifyOnChange) {
			notifyDataSetChanged();
		}
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
	 * <p/>
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

	private void init(Context context, int resource, Collection<E> objects, int[] viewIds, String[] propetiesName) {
		mContext = context;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mResource = mDropDownResource = resource;
		mObjects = new ArrayList<>(objects);
		mFrom = propetiesName;
		mTo = viewIds;
	}

	/**
	 * Returns the context associated with this array adapter. The context is used
	 * to create views from the resource passed to the constructor.
	 *
	 * @return The Context associated with this adapter.
	 */
	public Context getContext() {
		return mContext;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getCount() {
		return mObjects.size();
	}

	/**
	 * {@inheritDoc}
	 */
	public E getItem(int position) {
		return mObjects.get(position);
	}

	/**
	 * Returns the position of the specified item in the array.
	 *
	 * @param item The item to retrieve the position of.
	 * @return The position of the specified item.
	 */
	public int getPosition(E item) {
		return mObjects.indexOf(item);
	}

	/**
	 * {@inheritDoc}
	 */
	public long getItemId(int position) {
		return mObjects.get(position).getId();
	}

	/**
	 * {@inheritDoc}
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		return createViewFromResource(position, convertView, parent, mResource);
	}

	private View createViewFromResource(int position, View convertView, ViewGroup parent, int resource) {
		View view;

		if (convertView == null) {
			view = mInflater.inflate(resource, parent, false);
		} else {
			view = convertView;
		}

		E item = getItem(position);
		bindView(item, view);

		return view;
	}

	protected void bindView(E entity, View view) {
		if (entity == null) {
			return;
		}

		final String[] from = mFrom;
		final int[] to = mTo;
		final int count = to.length;

		for (int i = 0; i < count; i++) {
			final View v = view.findViewById(to[i]);
			if (v != null) {
				final Object data = entity.getProperty(from[i]);
				String text = data == null ? "" : data.toString();
				if (text == null) {
					text = "";
				}

				boolean bound = false;
				if (propertyBinder != null) {
					bound = propertyBinder.bind(v, from[i], data);
				}

				if (!bound) {
					if (v instanceof Checkable) {
						if (data instanceof Boolean) {
							((Checkable) v).setChecked((Boolean) data);
						} else if (v instanceof TextView) {
							// Note: keep the instanceof TextView check at the bottom of these
							// ifs since a lot of views are TextViews (e.g. CheckBoxes).
							setViewText((TextView) v, text);
						} else {
							throw new IllegalStateException(v.getClass().getName() +
									                                " should be bound to a Boolean, not a " +
									                                (data == null ? "<unknown type>" : data.getClass()));
						}
					} else if (v instanceof TextView) {
						// Note: keep the instanceof TextView check at the bottom of these
						// ifs since a lot of views are TextViews (e.g. CheckBoxes).
						setViewText((TextView) v, text);
					} else if (v instanceof ImageView) {
						if (data instanceof Integer) {
							setViewImage((ImageView) v, (Integer) data);
						} else {
							setViewImage((ImageView) v, text);
						}
					} else {
						throw new IllegalStateException(v.getClass().getName() + " is not a " +
								                                " view that can be bounds by this SimpleAdapter");
					}
				}
			}
		}
	}

	/**
	 * Called by bindView() to set the image for an ImageView but only if
	 * there is no existing ViewBinder or if the existing ViewBinder cannot
	 * handle binding to an ImageView.
	 * <p/>
	 * This method is called instead of {@link #setViewImage(ImageView, String)}
	 * if the supplied data is an int or Integer.
	 *
	 * @param v     ImageView to receive an image
	 * @param value the value retrieved from the data set
	 * @see #setViewImage(ImageView, String)
	 */
	public void setViewImage(ImageView v, int value) {
		v.setImageResource(value);
	}

	/**
	 * Called by bindView() to set the image for an ImageView but only if
	 * there is no existing ViewBinder or if the existing ViewBinder cannot
	 * handle binding to an ImageView.
	 * <p/>
	 * By default, the value will be treated as an image resource. If the
	 * value cannot be used as an image resource, the value is used as an
	 * image Uri.
	 * <p/>
	 * This method is called instead of {@link #setViewImage(ImageView, int)}
	 * if the supplied data is not an int or Integer.
	 *
	 * @param v     ImageView to receive an image
	 * @param value the value retrieved from the data set
	 * @see #setViewImage(ImageView, int)
	 */
	public void setViewImage(ImageView v, String value) {
		try {
			v.setImageResource(Integer.parseInt(value));
		} catch (NumberFormatException nfe) {
			v.setImageURI(Uri.parse(value));
		}
	}

	/**
	 * Called by bindView() to set the text for a TextView but only if
	 * there is no existing ViewBinder or if the existing ViewBinder cannot
	 * handle binding to a TextView.
	 *
	 * @param v    TextView to receive text
	 * @param text the text to be set for the TextView
	 */
	public void setViewText(TextView v, String text) {
		v.setText(text);
	}

	/**
	 * <p>Sets the layout resource to create the drop down views.</p>
	 *
	 * @param resource the layout resource defining the drop down views
	 * @see #getDropDownView(int, android.view.View, android.view.ViewGroup)
	 */
	public void setDropDownViewResource(int resource) {
		this.mDropDownResource = resource;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return createViewFromResource(position, convertView, parent, mDropDownResource);
	}

	public PropertyBinder getPropertyBinder() {
		return propertyBinder;
	}

	public void setPropertyBinder(PropertyBinder propertyBinder) {
		this.propertyBinder = propertyBinder;
	}

	/**
	 * {@inheritDoc}
	 */
	public Filter getFilter() {
		if (mFilter == null) {
			mFilter = new ArrayFilter();
		}
		return mFilter;
	}

	/**
	 * <p>An array filter constrains the content of the array adapter with
	 * a prefix. Each item that does not start with the supplied prefix
	 * is removed from the list.</p>
	 */
	private class ArrayFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence prefix) {
			FilterResults results = new FilterResults();

			if (mOriginalValues == null) {
				synchronized (mLock) {
					mOriginalValues = new ArrayList<E>(mObjects);
				}
			}

			if (prefix == null || prefix.length() == 0) {
				ArrayList<E> list;
				synchronized (mLock) {
					list = new ArrayList<E>(mOriginalValues);
				}
				results.values = list;
				results.count = list.size();
			} else {
				String prefixString = prefix.toString().toLowerCase();

				ArrayList<E> values;
				synchronized (mLock) {
					values = new ArrayList<E>(mOriginalValues);
				}

				final int count = values.size();
				final ArrayList<E> newValues = new ArrayList<E>();

				for (int i = 0; i < count; i++) {
					final E value = values.get(i);
					final String valueText = value.toString().toLowerCase();

					// First match against the whole, non-splitted value
					if (valueText.startsWith(prefixString)) {
						newValues.add(value);
					} else {
						final String[] words = valueText.split(" ");

						// Start at index 0, in case valueText starts with space(s)
						for (String word : words) {
							if (word.startsWith(prefixString)) {
								newValues.add(value);
								break;
							}
						}
					}
				}

				results.values = newValues;
				results.count = newValues.size();
			}

			return results;
		}

		@Override
		@SuppressWarnings("unchecked")
		protected void publishResults(CharSequence constraint, FilterResults results) {
			//noinspection unchecked
			mObjects = (List<E>) results.values;
			if (results.count > 0) {
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}
		}
	}


}
