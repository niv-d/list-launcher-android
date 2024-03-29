package com.dmratcliffe.list_launcher;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.ViewHolder> {
    private AppList appList;

    boolean debug = true;
    String TAG = "list-launcher";

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textView;
        public ImageView img;


        //This is the subclass ViewHolder which simply
        //'holds the views' for us to show on each row
        public ViewHolder(View itemView) {
            super(itemView);

            //Finds the views from our row.xml
            textView = (TextView) itemView.findViewById(R.id.text);
            img = (ImageView) itemView.findViewById(R.id.img);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = getAdapterPosition();
//                    if(debug)
//                        Log.i(TAG, "onLongClick: Long clicked on" + appsList.get(pos));
//                    MainActivity.appsList.add(appsList.get(pos));
                    return true;
                }
            });
        }

        @Override
        public void onClick (View v) {
            int pos = getAdapterPosition();
            Context context = v.getContext();

            Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(MainActivity.appsList.getPackageName(pos).toString());
            context.startActivity(launchIntent);
            Toast.makeText(v.getContext(), MainActivity.appsList.getLabel(pos).toString(), Toast.LENGTH_LONG).show();

        }
    }



    public AppListAdapter(Context c, AppList appsList) {

        ArrayList<AppInfo> sorted = new ArrayList<AppInfo>();
        for (int j = 0; j < appsList.size(); j++) {
            for (int g = j + 1; g  < appsList.size(); g++) {
                // comparing strings
                if (appsList.get(g).label.toString().compareToIgnoreCase(appsList.get(j).label.toString()) < 0) {
                    AppInfo temp = appsList.get(j);
                    appsList.set(j, appsList.get(g));
                    appsList.set(g, temp);
                }
            }
            sorted.add(appsList.get(j));
        }
        this.appsList = appList;
    }

    public class AppCompare implements Comparator<AppInfo>{
        @Override
        public int compare(AppInfo app1, AppInfo app2){
            return app1.label.toString().compareToIgnoreCase(app2.label.toString());
        }
    }

    @Override
    public void onBindViewHolder(AppListAdapter.ViewHolder viewHolder, int i) {

        //Here we use the information in the list we created to define the views

        String appLabel = appsList.get(i).label.toString();
        String appPackage = appsList.get(i).packageName.toString();
        Drawable appIcon = appsList.get(i).icon;

        TextView textView = viewHolder.textView;
        textView.setText(appLabel);
        ImageView imageView = viewHolder.img;
        imageView.setImageDrawable(appIcon);
    }


    @Override
    public int getItemCount() {

        //This method needs to be overridden so that Androids knows how many items
        //will be making it into the list

        return appsList.size();
    }


    @Override
    public AppListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //This is what adds the code we've written in here to our target view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.app_row, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }
}