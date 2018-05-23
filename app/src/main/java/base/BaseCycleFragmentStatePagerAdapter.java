package base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wpy on 15/9/1.
 */
public abstract class BaseCycleFragmentStatePagerAdapter<Item> extends FragmentStatePagerAdapter implements BaseCycleAdapterInterface<Item> {
    private List<Item> mItems = new ArrayList<>();

    public BaseCycleFragmentStatePagerAdapter(final FragmentManager fm) {
        super(fm);
    }

    public BaseCycleFragmentStatePagerAdapter(final FragmentManager fm, final List<Item> items) {
        super(fm);
        this.mItems = items;
    }

    /**
     * 根据Item和position，返回Fragment
     */
    protected abstract Fragment getItemFragment(final Item item, final int position);

    @Override
    public Fragment getItem(final int position) {
        final int itemsSize = mItems.size();
        if (position == 0) {
            Log.i("wocao", "itemsSize - 1 = " + (itemsSize - 1));
            return getItemFragment(mItems.get(itemsSize - 1), (itemsSize - 1));
        } else if (position == itemsSize + 1) {
            return getItemFragment(mItems.get(0), 0);
        } else {
            Log.i("wocao", "itemsSize - 1 = " + (position - 1));
            return getItemFragment(mItems.get(position - 1), (position - 1));
        }
    }

    @Override
    public int getCount() {
        final int itemsSize = mItems.size();
        return itemsSize > 1 ? itemsSize + 2 : itemsSize;
    }

    @Override
    public int getItemPosition(Object object) {//简单粗暴的解决刷新问题，感觉效率不是很高
        return POSITION_NONE;
    }

    @Override
    public int getRealCount() {
        return mItems.size();
    }

    @Override
    public void setItems(final List<Item> items) {
        this.mItems = items;
        notifyDataSetChanged();
    }

    @Override
    public Item getItemObject(int position) {
        return mItems.get(position);
    }

}
