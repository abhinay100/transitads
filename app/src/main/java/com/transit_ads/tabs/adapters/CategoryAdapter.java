package com.transit_ads.tabs.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.transit_ads.tabs.R;
import com.transit_ads.tabs.model.Categories;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by admin on 19-05-2016.
 */
public class CategoryAdapter extends BaseAdapter {

    private LayoutInflater inflater;
//    int[] thumb;
    List<Categories> img_txt;
 //   ArrayList<String> thumb;

    private Context ctx;

    public CategoryAdapter(Context context,List<Categories> img_txt) {
        ctx=context;
      //  this.thumb = thumb;
        this.img_txt = img_txt;
    }

    @Override
    public int getCount() {
        return img_txt.size();
    }

    @Override
    public Object getItem(int position) {
        return img_txt.get(position);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView =inflater.inflate(R.layout.single_grid_item ,null);
        }
        ImageView imageview = (ImageView)convertView.findViewById(R.id.thumbView);
        TextView txt_disp = (TextView)convertView.findViewById(R.id.list_txt);

       // imageview.setBackgroundResource(thumb.get(position));
        File imageFile = new File(Environment.getExternalStorageDirectory() + "/TransitCategories/" +img_txt.get(position).getIcon());
        if(imageFile.exists()){
            imageview.setImageBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()));
        }

        Typeface face= Typeface.createFromAsset(ctx.getAssets(), "fonts/myriad_pro.ttf");
        txt_disp.setTypeface(face);

        txt_disp.setText(img_txt.get(position).getName());
        txt_disp.setTextColor(Color.parseColor(img_txt.get(position).getColor()));

        return convertView;
    }
}
