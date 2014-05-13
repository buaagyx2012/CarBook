package com.Doric.CarBook;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.*;
import android.widget.*;
import com.Doric.CarBook.car.CarShow;
import com.Doric.CarBook.car.HotCarShow;
import com.Doric.CarBook.member.Login;
import com.Doric.CarBook.member.PersonalCenter;
import com.Doric.CarBook.member.UserCollection;
import com.Doric.CarBook.search.SearchMain;

public class MainActivity extends Activity {

    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;

    private CharSequence drawerTitle;
    private CharSequence title;
    private String[] leftDrawerTitles;
    private int[] leftDrawerIcons;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        title = drawerTitle = getTitle();
        leftDrawerTitles = getResources().getStringArray(R.array.left_drawer_array);
        leftDrawerIcons = new int[]{
                R.drawable.ic_collection,
                R.drawable.ic_collection,
                R.drawable.ic_search,
                R.drawable.ic_collection,
                R.drawable.ic_collection,
                R.drawable.ic_collection,
                R.drawable.ic_settings
        };
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);

        LeftDrawerListAdapter adapter = new LeftDrawerListAdapter(this, leftDrawerTitles, leftDrawerIcons);
        drawerList.setAdapter(adapter);
        drawerList.setOnItemClickListener(new DrawerItemSelectedListener());

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_navigation_drawer,
                R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setTitle(drawerTitle);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                setTitle(title);
                invalidateOptionsMenu();
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);
        if (savedInstanceState == null) {
            selectItem(0);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = drawerLayout.isDrawerOpen(drawerList);
        //menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void setTitle(CharSequence title) {
        this.title = title;
        getActionBar().setTitle(title);
    }

    private void selectItem(int position) {
        Fragment fragment;
        FragmentManager fragmentManager = getFragmentManager();
        Bundle args;
        switch (position) {
            case 0:  //若用户已登录，则为「个人中心」，否则为「注册登录」
                fragment = new PersonalCenter();
                args = new Bundle();
                args.putString("name", "Vincent");
                fragment.setArguments(args);
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                break;
            case 1:  //「热门汽车排行」模块
                fragment = new HotCarShow();
                args = new Bundle();
                args.putString("tag", "HotCarShow");
                fragment.setArguments(args);
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                break;
            case 2: //「找车」模块
                startActivity(new Intent(getApplicationContext(), SearchMain.class));
                break;
            case 3: // [我的收藏]模块
                fragment = new UserCollection();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                break;
            case 4:
                startActivity(new Intent(getApplicationContext(), Login.class));
                break;
            case 5:
                Intent intent = new Intent();
                intent.putExtra("carID", "123");
                intent.setClass(MainActivity.this, CarShow.class);
                startActivity(intent);
                break;
            default:
                fragment = new Fragment();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                break;
        }

        drawerList.setItemChecked(position, true);
        setTitle(leftDrawerTitles[position]);
        drawerLayout.closeDrawer(drawerList);
    }

    private class DrawerItemSelectedListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            selectItem(i);
        }
    }

    public class LeftDrawerListAdapter extends ArrayAdapter<String> {

        private Context context;
        private String[] values;
        private int[] icons;

        public LeftDrawerListAdapter(Context context, String[] values, int[] icons) {
            super(context, R.layout.drawer_list_item, values);
            this.context = context;
            this.values = values;
            this.icons = icons;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = convertView;
            if (rowView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(R.layout.drawer_list_item, parent, false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.textView = (TextView) rowView.findViewById(R.id.left_drawer_text);
                viewHolder.imageView = (ImageView) rowView.findViewById(R.id.left_drawer_image);
                rowView.setTag(viewHolder);
            }

            ViewHolder viewHolder = (ViewHolder) rowView.getTag();
            viewHolder.textView.setText(values[position]);
            viewHolder.imageView.setImageResource(icons[position]);
            return rowView;
        }
    }

    static class ViewHolder {
        TextView textView;
        ImageView imageView;
    }
}
