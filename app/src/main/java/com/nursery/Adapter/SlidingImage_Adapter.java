package com.nursery.Adapter;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.nursery.Model.Viewpager;

import com.nursery.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class SlidingImage_Adapter extends PagerAdapter {

    private ArrayList<Viewpager> arrayList;
    private LayoutInflater inflater;
    private Context context;

    public SlidingImage_Adapter(Context context, ArrayList<Viewpager> arr) {
        Log.e("arr======",""+arr);
        this.context = context;
        this.arrayList= arr;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {

        View itemview = inflater.inflate(R.layout.custom_pager, view, false);
        // assert imageLayout != null;
        ImageView imageView = itemview.findViewById(R.id.imageView);
        String imag1 = "http://www.waghnursery.com/nursery/assets/img/"+arrayList.get(position).getImage();
        Log.e("imag1==",""+imag1);
        Log.e("arraylist==",""+arrayList);
        try {
            Picasso.with(context)
                    .load("http://www.waghnursery.com/nursery/assets/img/"+arrayList.get(position).getImage())
                    .placeholder(R.drawable.defaultimage)
                    .into(imageView);
            view.addView(itemview);
        }catch (Exception e){
            e.printStackTrace();
        }
        return itemview;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return view==(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
