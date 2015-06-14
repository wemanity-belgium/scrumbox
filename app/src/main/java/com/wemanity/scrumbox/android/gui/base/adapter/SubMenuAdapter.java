package com.wemanity.scrumbox.android.gui.base.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu.MenuStateChangeListener;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.wemanity.scrumbox.android.R;
import com.wemanity.scrumbox.android.db.entity.Entity;
import com.wemanity.scrumbox.android.gui.base.EntityAction;
import com.wemanity.scrumbox.android.gui.base.OnEntityChangeListener;
import com.wemanity.scrumbox.android.gui.base.adapter.builder.submenu.AdapterSubMenuActionBuilder;
import com.wemanity.scrumbox.android.gui.base.adapter.builder.EntityAbapterBuilder;
import com.wemanity.scrumbox.android.gui.base.adapter.builder.submenu.SubMenuAdapterBuilder;

import java.util.Collection;

public class SubMenuAdapter<E extends Entity> extends EntityAdapter<E> {
	private Activity activy;
	private int[] drawables;
	private EntityAction[] entityActions;
	private MenuStateLastChange menuStateLastChange;
	private OnEntityChangeListener<E> changeListener;

	public static class Builder<E extends Entity> extends EntityAdapter.Builder<SubMenuAdapter<E>, E, Builder<E>>
			implements SubMenuAdapterBuilder<E, Builder<E>>,
			           AdapterSubMenuActionBuilder<EntityAbapterBuilder<E, Builder<E>>> {

		private int[] drawables;
		private EntityAction[] entityActions;
		private OnEntityChangeListener<E> changeListener;
		@Override
		public Builder<E> drawables(int[] drawables) {
			this.drawables = drawables;
			return this;
		}

		@Override
		public Builder<E> entityActions(EntityAction[] entityActions) {
			this.entityActions =entityActions;
			return this;
		}

		@Override
		protected Builder<E> self() {
			return this;
		}

		public  Builder<E> changeListener(OnEntityChangeListener<E> changeListener){
			this.changeListener = changeListener;
			return this;
		}

		@Override
		public SubMenuAdapter<E> build() {
			SubMenuAdapter<E> subMenuAdapter = new SubMenuAdapter<>(activity, layoutId, entities, viewIds, properties);
			subMenuAdapter.activy = this.activity;
			subMenuAdapter.drawables = this.drawables;
			subMenuAdapter.entityActions = this.entityActions;
			subMenuAdapter.changeListener = this.changeListener;
			return subMenuAdapter;
		}
	}

	public static <E extends Entity> SubMenuAdapterBuilder<E, Builder<E>> newBuilder() {
		return new Builder<>();
	}

	private SubMenuAdapter(Context activy, int resource, Collection<E> objects, int[] viewIds, String[] propetiesName) {
		super(activy, resource, objects, viewIds, propetiesName);
		menuStateLastChange = new MenuStateLastChange();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView != null) {
			View v =  super.getView(position, convertView, parent);
			updateMenuPosition(position, v);
			return v;
		}
		View v = super.getView(position, convertView, parent);
		createMenu(position, v);
		return v;
	}

	private void createMenu(int position, View container) {

		FloatingActionMenu.Builder floatingActionMenuBuilder = new FloatingActionMenu.Builder(activy)
				.attachTo(container)
				.setStateChangeListener(menuStateLastChange)
				.enableAnimations()
				.setStartAngle(20)
				.setEndAngle(160);

		for(int i=0; i<drawables.length;i++){
			floatingActionMenuBuilder.addSubActionView(createSubActionButton(position, drawables[i], entityActions[i]), 100,100);
		}
		final FloatingActionMenu floatingActionMenu = floatingActionMenuBuilder.build();
		container.setOnLongClickListener(new OpenMenuOnLongClick(floatingActionMenu));
		container.setOnClickListener(new CloseMenuOnClick(floatingActionMenu));
		container.setTag(R.integer.tag_floating_menu, floatingActionMenu);
		for(FloatingActionMenu.Item item : floatingActionMenu.getSubActionItems()){
			item.view.setOnClickListener(new SubActionButtonOnClickListener(floatingActionMenu));
		}
	}

	private SubActionButton createSubActionButton(int position,int drawableId, EntityAction action){
		SubActionButton.Builder itemBuilder = new SubActionButton.Builder(activy);
		ImageView itemIcon = new ImageView(getContext());
		itemIcon.setImageResource(drawableId);
		SubActionButton subActionButton = itemBuilder.setContentView(itemIcon).build();
		subActionButton.setTag(R.integer.tag_action, action);
		subActionButton.setTag(R.integer.tag_position, position);
		return subActionButton;
	}

	private void updateMenuPosition(int position, View container){
		FloatingActionMenu floatingActionMenu = (FloatingActionMenu) container.getTag(R.integer.tag_floating_menu);
		for(FloatingActionMenu.Item item : floatingActionMenu.getSubActionItems()){
			item.view.setTag(R.integer.tag_position, position);
		}
	}

	public void closeSubMenu() {
		menuStateLastChange.closeMenuOnScreen();
	}

	public void setChangeListener(OnEntityChangeListener<E> changeListener) {
		this.changeListener = changeListener;
	}

	public OnEntityChangeListener<E> getChangeListener() {
		return changeListener;
	}

	private class MenuStateLastChange implements MenuStateChangeListener{
		FloatingActionMenu menuOnScreen;
		@Override
		public void onMenuOpened(FloatingActionMenu floatingActionMenu) {
			if (menuOnScreen != null && menuOnScreen.isOpen()) {
				menuOnScreen.close(true);
			}
			menuOnScreen = floatingActionMenu;
		}

		@Override
		public void onMenuClosed(FloatingActionMenu floatingActionMenu) {
			if (menuOnScreen == floatingActionMenu) {
				menuOnScreen = null;
			}
		}

		public void closeMenuOnScreen(){
			if (menuOnScreen != null && menuOnScreen.isOpen()){
				menuOnScreen.close(true);
			}
		}
	}

	private class CloseMenuOnClick implements View.OnClickListener {

		private FloatingActionMenu floatingActionMenu;

		private CloseMenuOnClick(FloatingActionMenu floatingActionMenu){
			this.floatingActionMenu = floatingActionMenu;
		}

		@Override
		public void onClick(View v) {
			if (floatingActionMenu.isOpen()){
				floatingActionMenu.close(true);
			}
		}
	}

	private class SubActionButtonOnClickListener extends CloseMenuOnClick{

		private SubActionButtonOnClickListener(FloatingActionMenu floatingActionMenu) {
			super(floatingActionMenu);
		}

		@Override
		public void onClick(View v) {
			super.onClick(v);
			if (changeListener != null){
				EntityAction action = (EntityAction) v.getTag(R.integer.tag_action);
				int position = (int) v.getTag(R.integer.tag_position);
				changeListener.onEntityChange(action, getItem(position));
			}
		}
	}

	private class OpenMenuOnLongClick implements View.OnLongClickListener {

		FloatingActionMenu floatingActionMenu;

		private OpenMenuOnLongClick(FloatingActionMenu floatingActionMenu){
			this.floatingActionMenu = floatingActionMenu;
		}

		@Override
		public boolean onLongClick(View v) {
			floatingActionMenu.open(true);
			return true;
		}
	}
}

