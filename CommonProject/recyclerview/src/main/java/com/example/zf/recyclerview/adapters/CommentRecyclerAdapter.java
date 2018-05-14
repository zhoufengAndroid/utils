package com.example.zf.recyclerview.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by zf on 2017/8/3.
 */

public abstract class CommentRecyclerAdapter<T, H extends CommentViewHolder> extends RecyclerView.Adapter<H> {

    public Context mContext;
    protected List<T> mList;
    private int mLayoutId;

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private MultiTypeSupport<T> mMultiTypeSupport;//多布局支持接口

    public CommentRecyclerAdapter(List<T> mList, int layoutId) {
        this.mList = mList;
        this.mLayoutId = layoutId;
    }

    /**
     * 多布局支持
     */
    public CommentRecyclerAdapter(List<T> data, MultiTypeSupport<T> multiTypeSupport) {
        this(data, -1);
        this.mMultiTypeSupport = multiTypeSupport;
    }

    public void notifyData(List<T> mDatas) {
        if (mDatas != null) {
            mList = mDatas;
            notifyDataSetChanged();
        }
    }

    public void deleteItem(int position) {
        if (mList != null && mList.size() > position) {
            mList.remove(position);
            notifyDataSetChanged();
        }

    }

    public void loadMoreData(List<T> mDatas) {
        if (mList != null && mDatas != null) {
            mList.addAll(mDatas);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mMultiTypeSupport != null) {
            return mMultiTypeSupport.getLayoutId(mList.get(position), position);
        }
        return super.getItemViewType(position);
    }

    @Override
    public H onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        //多布局支持
        if (mMultiTypeSupport != null) {
            mLayoutId = viewType;
        }

        View view = LayoutInflater.from(mContext).inflate(mLayoutId, parent, false);
        H viewHolder =createBaseViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(H holder, final int position) {
        if (mList != null && mList.size() != 0) {
            convert(holder, mList.get(position));
        }

        if (mOnItemClickListener != null) {
            holder.setOnItemClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick((RecyclerView) v.getParent(), v, position);
                }
            });
        }

        if (mOnItemLongClickListener != null) {
            holder.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemLongClickListener.onItemLongClick((RecyclerView) v.getParent(), v, position);
                    return false;
                }
            });
        }
    }

    @SuppressWarnings("unchecked")
    protected H createBaseViewHolder(View view) {
        Class temp = getClass();
        Class z = null;
        while (z == null && null != temp) {
            z = getInstancedGenericKClass(temp);
            temp = temp.getSuperclass();
        }
        H h;
        // 泛型擦除会导致z为null
        if (z == null) {
            h = (H) new CommentViewHolder(view);
        } else {
            h = createGenericKInstance(z, view);
        }
        return h != null ? h : (H) new CommentViewHolder(view);
    }

    /**
     * get generic parameter H
     *
     * @param z
     * @return
     */
    private Class getInstancedGenericKClass(Class z) {
        Type type = z.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            for (Type temp : types) {
                if (temp instanceof Class) {
                    Class tempClass = (Class) temp;
                    if (CommentViewHolder.class.isAssignableFrom(tempClass)) {
                        return tempClass;
                    }
                }
            }
        }
        return null;
    }

    /**
     * try to create Generic H instance
     *
     * @param z
     * @param view
     * @return
     */
    @SuppressWarnings("unchecked")
    private H createGenericKInstance(Class z, View view) {
        try {
            Constructor constructor;
            // inner and unstatic class
            if (z.isMemberClass() && !Modifier.isStatic(z.getModifiers())) {
                constructor = z.getDeclaredConstructor(getClass(), View.class);
                constructor.setAccessible(true);
                return (H) constructor.newInstance(this, view);
            } else {
                constructor = z.getDeclaredConstructor(View.class);
                constructor.setAccessible(true);
                return (H) constructor.newInstance(view);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    protected abstract void convert(H holder, T item);

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }


    public interface OnItemClickListener {
        void onItemClick(RecyclerView parent, View view, int position);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(RecyclerView parent, View view, int position);
    }
}
