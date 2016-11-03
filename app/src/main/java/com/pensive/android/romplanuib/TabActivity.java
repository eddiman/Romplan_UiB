package com.pensive.android.romplanuib;

import android.content.Intent;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.lapism.searchview.SearchAdapter;
import com.lapism.searchview.SearchHistoryTable;
import com.lapism.searchview.SearchItem;
import com.lapism.searchview.SearchView;
import com.pensive.android.romplanuib.models.UIBbuilding;
import com.pensive.android.romplanuib.util.DownloadAndStoreData;
import com.pensive.android.romplanuib.util.FontController;

import java.util.ArrayList;
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
    DownloadAndStoreData dl = new DownloadAndStoreData();

    private SearchHistoryTable mHistoryDatabase;
    Animation animationFadeOut;
    Animation animationFadeIn;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    AppBarLayout appBarLayout;
    private SearchView mSearchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //TODO: Create initGUI() method later
        mSearchView = (SearchView) findViewById(R.id.searchView);

        mSearchView.setVisibility(View.INVISIBLE);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        if (mViewPager != null) { mViewPager.setAdapter(mSectionsPagerAdapter); }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        if (tabLayout != null)
            tabLayout.setupWithViewPager(mViewPager);

        fc.setTitleFont(this, getResources().getString(R.string.splash_title), toolbar, "roboto_thin.ttf", 0);

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

    protected void setSearchView() {
        mHistoryDatabase = new SearchHistoryTable(this);


        if (mSearchView != null) {
            mSearchView.setHint(getResources().getString(R.string.search_buildings));
            mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    getData(query, 0);
                    mSearchView.close(true);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });

            mSearchView.setVoiceText("Set permission on Android 6.0+ !");
            mSearchView.setOnVoiceClickListener(new SearchView.OnVoiceClickListener() {
                @Override
                public void onVoiceClick() {
                    // permission
                }
            });


            mSearchView.setOnOpenCloseListener(new SearchView.OnOpenCloseListener() {
                @Override
                public void onOpen() {

                }
                @Override
                public void onClose() {

                    //Skitten måte på å hide SearchView, men fant null i dokum. til lapism-searchviewet
                    //TODO: Finne ut hvordan i h. man får til standardanim. som i Lapism sitt sample
                    mSearchView.close(true);
                    mSearchView.startAnimation(animationFadeOut);
                    mSearchView.setVisibility(View.INVISIBLE);
                }
            });


            List<SearchItem> suggestionsList = getSearchItemList();

            SearchAdapter searchAdapter = new SearchAdapter(this, suggestionsList);
            searchAdapter.addOnItemClickListener(new SearchAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    TextView textView = (TextView) view.findViewById(R.id.textView_item_text);
                    String query = textView.getText().toString();
                    getData(query, position);
                    mSearchView.close(true);
                }
            });
            mSearchView.setAdapter(searchAdapter);

        }
    }

    @CallSuper
    protected void getData(String text, int position) {
        mHistoryDatabase.addItem(new SearchItem(text));

        //TODO: Bedre søkealgoritme må nok til her as
        for (UIBbuilding build : dl.getStoredDataAllBuildings(TabActivity.this)){
            if (text.equals(build.getName())){
                Intent i = new Intent(TabActivity.this, RoomActivity.class);
                i.putExtra("building", build);
                this.startActivity(i);
                return;
            }

        }
    }



    private List<SearchItem> getSearchItemList(){
        List<SearchItem> suggestionsList = new ArrayList<>();
        List<UIBbuilding> allBuildings = dl.getStoredDataAllBuildings(TabActivity.this);

        for (UIBbuilding build : allBuildings) {
            suggestionsList.add(new SearchItem(build.getName()));
        }
        return suggestionsList;
    }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tab, menu);
        return true;
    }

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
                return true;
            }
        }
        return super.onOptionsItemSelected(item);

    }



    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
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
                case 2:
                    return RecentFragment.newInstance(position + 1);
                default:
                    return AllBuildingsFragment.newInstance(position + 1);
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Alle bygninger";
                case 1:
                    return "Favoritter";
                case 2:
                    return "Nylige";
            }
            return null;
        }
    }
}
