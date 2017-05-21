package com.pensive.android.romplanuib;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.lapism.searchview.SearchAdapter;
import com.lapism.searchview.SearchHistoryTable;
import com.lapism.searchview.SearchItem;
import com.lapism.searchview.SearchView;
import com.pensive.android.romplanuib.models.Building;
import com.pensive.android.romplanuib.models.Room;
import com.pensive.android.romplanuib.util.DataManager;
import com.pensive.android.romplanuib.util.FontController;
import com.pensive.android.romplanuib.util.BuildingComparator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TabActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    FontController fc = new FontController();
    DataManager dataManager;

    private SearchHistoryTable mHistoryDatabase;
    Animation animationFadeOut;
    Animation animationFadeIn;

    AppBarLayout appBarLayout;
    private SearchView mSearchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        dataManager = new DataManager(TabActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //TODO: Create initGUI() method later
        mSearchView = (SearchView) findViewById(R.id.searchView);

        mSearchView.setVisibility(View.INVISIBLE);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), getApplicationContext());

        // Set up the ViewPager with the sections adapter.
        /*
      The {@link ViewPager} that will host the section contents.
     */
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        if (mViewPager != null) { mViewPager.setAdapter(mSectionsPagerAdapter); }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        if (tabLayout != null)
            tabLayout.setupWithViewPager(mViewPager);

        // Iterate over all tabs and set the custom view
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(mSectionsPagerAdapter.getTabView(i));
        }


        setAppBarLayoutNonDrag();
        initAnim();
        setSearchView();

    }

    /**
     * Initializes the fadein anims.
     * TODO: Hvis det blir flere anim; skille de ut i egen klasse.
     */
    private void initAnim() {
        animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
        animationFadeOut = AnimationUtils.loadAnimation(this, R.anim.fadeout);

    }

    //#########################SEARCH#############################//

    /**
     * Initializes the floating SearchView situated in the top of the Activity. Defines what
     * methods to be fired in different use cases of the SearchView.
     */
    protected void setSearchView() {
        mHistoryDatabase = new SearchHistoryTable(this);
        mHistoryDatabase.setHistorySize(5);

        if (mSearchView != null) {
            mSearchView.setHint(getResources().getString(R.string.search_buildings));
            /**
             * Listens to when user inputs text into EditText within the SearchView.
             *
             */
            mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                /**
                 * Defines what to do with the inputted text when submitted by the keyboard "Submit"
                 * @param query the text to search for
                 * @return true TODO: Find out wth this return does. Tip: Check the source code of lib
                 */
                @Override
                public boolean onQueryTextSubmit(String query) {
                    /*Intent intent = getData(query, 0);
                    startActivity(intent);
                    mSearchView.close(true);*/
                    return true;
                }

                /**
                 * Defines what to do when the inputted text in the SearchView is changed.
                 * An occurrence of this is when the text is typed, the string changes and fires this
                 * method.
                 * @param newText text that is being changed, or written
                 * @return false TODO: Find out wth this return does. Tip: Check the source code of lib
                 */
                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
/**
 * Listens to clicks on the Voice icon
 */
            //TODO: Implement the rest of voice search, maybe not necessary though.
            mSearchView.setVoiceText("Set permission on Android 6.0+ !");
            mSearchView.setOnVoiceClickListener(new SearchView.OnVoiceClickListener() {
                @Override
                public void onVoiceClick() {
                    // permission
                }
            });

/**
 * Listens to when the SearchView opens and closes. Can define actions when the the views opens
 * or closes
 */
            mSearchView.setOnOpenCloseListener(new SearchView.OnOpenCloseListener() {
                @Override
                public void onOpen() {

                }
                @Override
                public void onClose() {

                    //Skitten måte på å hide SearchView, men fant null i dokum. til lapism-searchviewet
                    //TODO: Finne ut hvordan i h. man får til standardanim. som i Lapism sitt sample
                    mSearchView.startAnimation(animationFadeOut);
                    mSearchView.setVisibility(View.INVISIBLE);
                }
            });


            List<SearchItem> suggestionsList = getSearchItemList();

            SearchAdapter searchAdapter = new SearchAdapter(this, suggestionsList);

            /**
             * Listens to click on items in the suggestion list that appears when text is inputted.
             * When an item is clicked, onItemClick is fired. Initializes the textview in the
             * item-element. Then gets the string from it and passes it on in the getData()-method.
             * If the query matches a building name, getData() returns an intent. The intent is
             * then started, and will open BuildingActivity with the queried building.
             */
            searchAdapter.addOnItemClickListener(new SearchAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    TextView textView = (TextView) view.findViewById(R.id.textView_item_text);
                    String query = textView.getText().toString();
                    Intent intent = getData(query, position);
                    startActivity(intent);
                    mSearchView.close(true);
                }
            });
            mSearchView.setAdapter(searchAdapter);

        }
    }

    /**
     * Finds a building with a name that matches the query using binarysearch,
     * creates a new Intent for the building and returns it.
     * @param text the query to search for
     * @param position TODO: Find a possible usage for the position of the selected element
     * @return a intent for the building searched for.
     */
    @CallSuper
    protected Intent getData(String text, int position) {
        mHistoryDatabase.addItem(new SearchItem(text));
        List<Building> buildingList = dataManager.getAllBuildings();
        int index = Collections.binarySearch(buildingList, new Building(text,"","", new ArrayList<Room>()), new BuildingComparator());
        Intent intent = new Intent(TabActivity.this, BuildingActivity.class);
        intent.putExtra("building", buildingList.get(index));
        return intent;

    }

    /**
     * Returns a List of SearchItems. It gets the list of the Building objects, then populates
     * the SearchView list with the name of the buildings.
     * @return A list of SearchItems with the building names.
     */
    private List<SearchItem> getSearchItemList(){
        List<SearchItem> suggestionsList = new ArrayList<>();
        List<Building> allBuildings = dataManager.getAllBuildings();

        for (Building build : allBuildings) {
            suggestionsList.add(new SearchItem(build.getName()));
        }
        return suggestionsList;
    }

    /**
     * Set the AppBar, the view that contains the toolbar and the viewpager, to be non-scrollable
     */
    private void setAppBarLayoutNonDrag() {
        //Setting the AppBarLayout to be non-draggable
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        // Disable "Drag" for AppBarLayout (i.e. User can't scroll appBarLayout by directly touching appBarLayout - User can only scroll appBarLayout by only using scrollContent)
        if (appBarLayout.getLayoutParams() != null) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
            AppBarLayout.Behavior appBarLayoutBehaviour = new AppBarLayout.Behavior();
            appBarLayoutBehaviour.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
                @Override
                public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                    return false;
                }
            });
            layoutParams.setBehavior(appBarLayoutBehaviour);
        }

    }

    /**
     * Initializes the menu in the toolbar and inflates it.
     *
     * @param menu See Google's Android documentation for further information on this.
     * @return See Google's Android documentation for further information on this.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        /*SpannableStringBuilder title = new SpannableStringBuilder(getString(R.string.pref_title));
        title.setSpan(tf, 0, title.length(), 0);
        menu.add(Menu.NONE, R.id.action_settings, 0, title); // THIS
        */
        getMenuInflater().inflate(R.menu.menu_tab, menu);

        LayoutInflater layoutInflater = getLayoutInflater();
        final LayoutInflater.Factory existingFactory = layoutInflater.getFactory();
            // use introspection to allow a new Factory to be set
        try {
            Field field = LayoutInflater.class.getDeclaredField("mFactorySet");
            field.setAccessible(true);
            field.setBoolean(layoutInflater, false);
            getLayoutInflater().setFactory(new LayoutInflater.Factory() {
                @Override
                public View onCreateView(String name, final Context context, AttributeSet attrs) {

                    if (name.equalsIgnoreCase(
                            "com.android.internal.view.menu.IconMenuItemView")) {
                        View view = null;
                        // if a factory was already set, we use the returned view
                        if (existingFactory != null) {
                            view = existingFactory.onCreateView(name, context, attrs);
                            Typeface tf = fc.getTypeface(getParent(), "fonts/roboto_thin.ttf");
                            view.setBackgroundColor(getResources().getColor(R.color.primaryDark_blue));
                            ((TextView) view).setTextColor(Color.RED);
                            ((TextView) view).setTypeface(tf);
                        }


                        return view;
                    }
                    return null;
                }
            });
        } catch (NoSuchFieldException e) {
            // ...
        } catch (IllegalArgumentException e) {
            // ...
        } catch (IllegalAccessException e) {
            // ...
        }
        return super.onCreateOptionsMenu(menu);
    }

    /**
     *  What to happen when the options in the top toolbar are selected.
     *  Switch-case is used to determine further actions.
     * @param item the item that has been selected
     * @return the item, see Google's Android documentation for further info on this.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_search: {

                //Skitten måte på å inhide SearchView, men fant null i dokum. til lapism-searchviewet
                //TODO: Finne ut hvordan i h. man får til standardanim. som i Lapism sitt sample
                mSearchView.startAnimation(animationFadeIn);
                mSearchView.open(true, item);
                mSearchView.setVisibility(View.VISIBLE);

                return true;
            }

            case R.id.action_settings: {
                Intent intent = new Intent(TabActivity.this, SettingsActivity.class);
                startActivity(intent);

                return true;
            }
        }
        return super.onOptionsItemSelected(item);

    }

    public void onBackPressed() {
        this.finishAffinity();


    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        Context context;
        public SectionsPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            switch (position) {
                case 0:
                    return AllBuildingsFragment.newInstance(position + 1);
                case 1:
                    return FavoritesFragment.newInstance(position + 1);
                default:
                    return AllBuildingsFragment.newInstance(position + 1);
            }
        }

        /**
         * Determines the number of sections to be shown in ViewPager
         * @return the number of sections.
         */
        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }



        /**
         * Returns the title of the section in the viewpager
         * @param position the position to return the title of
         * @return the title of the section
         */
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Alle bygninger";
                case 1:
                    return "Favoritter";
            }
            return null;
        }

        public View getTabView(int position) {
            String tabTitles[] = new String[] { getResources().getString(R.string.tab_buildings), getResources().getString(R.string.tab_favorites) };
            // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
            View v = LayoutInflater.from(context).inflate(R.layout.custom_viewpager, null);
            TextView tv = (TextView) v.findViewById(R.id.tab_textView);
            tv.setText(tabTitles[position]);
            return v;
        }


    }
}
