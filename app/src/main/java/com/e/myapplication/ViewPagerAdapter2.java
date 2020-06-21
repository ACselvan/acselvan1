package com.e.myapplication;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

public class ViewPagerAdapter2 extends PagerAdapter {
    private String[] urls;
    private LayoutInflater inflater;
    private Context context;

    public ViewPagerAdapter2(Context context, String[] urls) {
        this.context = context;
        this.urls = urls;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return urls.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View imageLayout = inflater.inflate(R.layout.image_item, container, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.imageView);


        Glide.with(context)
                .load(urls[position])
                .into(imageView);

        container.addView(imageLayout, 0);

        return imageLayout;
    }
    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
