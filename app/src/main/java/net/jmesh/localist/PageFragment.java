package net.jmesh.localist;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;

import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import net.jmesh.localist.database.ReminderDataBase;
import net.jmesh.localist.database.ReminderDbSchema;
import net.jmesh.localist.MainActivity;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.R.color.primary_text_light;

/**
 * Created by jonas on 4/27/16.
 */
public class PageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;
    private SQLiteDatabase mDatabase;
    private ReminderDataBase rDatabase;
    private LocSingleton curLoc;

    public static PageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main, container, false);
        LinearLayout linlayout = (LinearLayout) view.findViewById(R.id.linLayout);

        LinearLayout titleDateLayout = new LinearLayout(getContext());
        titleDateLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        curLoc = new LocSingleton();
        if (mPage == 1) {
            EditText titletext = new EditText(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.weight = 2f;
            titletext.setLayoutParams(params);
            titletext.setInputType(InputType.TYPE_CLASS_TEXT);
            titletext.setTextColor(ContextCompat.getColor(getContext(), primary_text_light));
            titletext.setHint("TITLE");
            titletext.setId(R.id.titlefieldnote);
            titleDateLayout.addView(titletext);

            TextView datetext = new TextView(getContext());
            long date = System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
            String dateString = sdf.format(date);
            datetext.setText(dateString);
            datetext.setTextColor(ContextCompat.getColor(getContext(), primary_text_light));
            titleDateLayout.addView(datetext);
            linlayout.addView(titleDateLayout);

            EditText bodyText = new EditText(getContext());
            bodyText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            bodyText.setTextColor(Color.BLACK);
            bodyText.setGravity(Gravity.TOP);
            bodyText.setVerticalScrollBarEnabled(true);
            bodyText.setBackground(null);
            bodyText.setHint("notes here");
            bodyText.setId(R.id.bodytextfield);
            linlayout.addView(bodyText);

            //Peerapat put stuff here
            List<ReminderNote> dbEntries = new ArrayList<ReminderNote>();
            rDatabase = new ReminderDataBase();
            mDatabase = rDatabase.getDB(getContext());
            String[] columns = new String[] { "uuid", "title",
                    "content", "latitude", "longitude", "date"};
            Location tmpLoc = curLoc.getLocation();
            if (tmpLoc != null) {
                double curlat = tmpLoc.getLatitude();
                double curlong = tmpLoc.getLongitude();
            }
            Cursor cursor = mDatabase.rawQuery("select * from notes", null);
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                ReminderNote newNote = new ReminderNote();
                newNote.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                newNote.setContent(cursor.getString(cursor.getColumnIndex("content")));
                Location newLoc = new Location("dummyprovider");
                newLoc.setLatitude(cursor.getDouble(cursor.getColumnIndex("latitude")));
                newLoc.setLongitude(cursor.getDouble(cursor.getColumnIndex("longitude")));
                newNote.setLocation(newLoc);
                newNote.setDate(new Date(cursor.getLong(cursor.getColumnIndex("date"))));
                dbEntries.add(newNote);
                cursor.moveToNext();
            }
            if (dbEntries.size() > 0) {
                titletext.setText(dbEntries.get(0).getTitle());
                bodyText.setText(dbEntries.get(0).getContent());
            }
        } else if (mPage == 2) {
            EditText titletext = new EditText(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.weight = 2f;
            titletext.setLayoutParams(params);
            titletext.setInputType(InputType.TYPE_CLASS_TEXT);
            titletext.setTextColor(ContextCompat.getColor(getContext(), primary_text_light));
            titletext.setHint("TITLE");
            titletext.setId(R.id.titlefieldlist);
            titleDateLayout.addView(titletext);

            final ImageView reminderbutton = new ImageView(getContext());
            reminderbutton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_star_border_black_36dp));
            reminderbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popup = new PopupMenu(getContext(), reminderbutton);
                    popup.getMenuInflater().inflate(R.menu.reminder_popup, popup.getMenu());

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            MainActivity theActivity = (MainActivity)getActivity();
                            theActivity.setActivityField(item.getTitle().toString());
                            reminderbutton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_star_black_36dp));
                            Toast.makeText(getContext(),
                                    "You Clicked : " + item.getTitle(),
                                    Toast.LENGTH_SHORT
                            ).show();
                            return true;
                        }
                    });

                    popup.show();
                }
            });
            titleDateLayout.addView(reminderbutton);
            linlayout.addView(titleDateLayout);

            LinearLayout listitem = new LinearLayout(getContext());
            CheckBox listbox = new CheckBox(getContext());
            EditText listtext = new EditText(getContext());

            listitem.addView(listbox);
            listitem.addView(listtext);
            linlayout.addView(listitem);

            List<ReminderNote> dbEntries = new ArrayList<ReminderNote>();
            rDatabase = new ReminderDataBase();
            mDatabase = rDatabase.getDB(getContext());
            String[] columns = new String[] { "uuid", "title",
                    "content", "activity"};
            Cursor cursor = mDatabase.rawQuery("select * from lists", null);
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                ReminderNote newNote = new ReminderNote();
                newNote.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                newNote.setContent(cursor.getString(cursor.getColumnIndex("content")));
                newNote.setDate(new Date(cursor.getLong(cursor.getColumnIndex("activity"))));
                dbEntries.add(newNote);
                cursor.moveToNext();
            }
            if (dbEntries.size() > 0) {
                titletext.setText(dbEntries.get(0).getTitle());
            }
        }
        return view;
    }

}

class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Notes", "Lists" };
    private Context context;

    public SampleFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return PageFragment.newInstance(position + 1);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}