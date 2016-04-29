package net.jmesh.localist;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;

import static android.R.color.primary_text_light;

/**
 * Created by jonas on 4/27/16.
 */
public class PageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

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

        EditText titletext = new EditText(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 2f;
        titletext.setLayoutParams(params);
        titletext.setInputType(InputType.TYPE_CLASS_TEXT);
        titletext.setTextColor(ContextCompat.getColor(getContext(), primary_text_light));
        titletext.setHint("TITLE");
        titleDateLayout.addView(titletext);

        if (mPage == 1) {
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
            linlayout.addView(bodyText);
        } else if (mPage == 2) {
            ImageView reminderbutton = new ImageView(getContext());
            reminderbutton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_star_black_36dp));
            titleDateLayout.addView(reminderbutton);
            linlayout.addView(titleDateLayout);
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