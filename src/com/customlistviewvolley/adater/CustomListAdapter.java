package com.customlistviewvolley.adater;

import com.pvm.R;
import com.customlistviewvolley.app.AppController;
import com.customlistviewvolley.model.Negocios;
 
import java.util.List;
 
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
 
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
 
public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Negocios> negItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
 
    public CustomListAdapter(Activity activity, List<Negocios> negItems) {
        this.activity = activity;
        this.negItems = negItems;
    }
 
    @Override
    public int getCount() {
        return negItems.size();
    }
 
    @Override
    public Object getItem(int location) {
        return negItems.get(location);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
 
        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);
 
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView.findViewById(R.id.vistaImg);
        TextView nomNeg = (TextView) convertView.findViewById(R.id.vistaNom);
        TextView slogan = (TextView) convertView.findViewById(R.id.slogan);
        TextView tags = (TextView) convertView.findViewById(R.id.tags);
 
        // getting market data for the row
        Negocios n = negItems.get(position);
 
        // thumbnail image
        thumbNail.setImageUrl(n.getThumbnailUrl(), imageLoader);

        //nombre negocio
        nomNeg.setText(n.getNombreNegocio());
        
        //tags
        String ts[] = n.getTagsNegocio().split(","); String tag="";
        for(int i =0;i<ts.length;i++)
        {
        	if(i==0)
        		tag =ts[i];
        	else
        		tag = tag +", "+ts[i];
        }
        tags.setText("Qué ofrece: "+tag);
        
        // Slogan
        slogan.setText(n.getEslogan());


        return convertView;
    }
 
}