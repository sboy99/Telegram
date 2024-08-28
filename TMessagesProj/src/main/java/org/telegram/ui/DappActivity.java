package org.telegram.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;

public class DappActivity extends BaseFragment {

    @Override
    public View createView(Context context) {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        if (AndroidUtilities.isTablet()) {
            actionBar.setOccupyStatusBar(false);
        }
        actionBar.setAllowOverlayTitle(true);
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == -1) {
                    finishFragment();
                }
            }
        });
        actionBar.setTitle(LocaleController.getString("DappStore", R.string.DappStore));

        ScrollView scrollView = new ScrollView(context);
        scrollView.setFillViewport(true);

        LinearLayout linearLayout = getLayout(context);

        scrollView.addView(linearLayout);
        fragmentView = scrollView;
        return fragmentView;
    }

    private static @NonNull LinearLayout getLayout(Context context) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        linearLayout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

        // ImageView with a top margin of 100
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(R.drawable.dapps_coming_soon);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        imageParams.setMargins(0, 200, 0, 30); // Top margin of 100
        imageView.setLayoutParams(imageParams);
        linearLayout.addView(imageView);

        // Title TextView with top margin of 50 and centered
        TextView title = new TextView(context);
        title.setTextColor(Theme.getColor(Theme.key_actionBarDefaultTitle));
        title.setText("Dapp Store");
        title.setTextSize(24);
        title.setTypeface(null, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        titleParams.setMargins(0, 50, 0, 30); // Top margin of 50
        title.setLayoutParams(titleParams);
        linearLayout.addView(title);

        // Subtitle TextView (kept the same as before)
        TextView subTitle = new TextView(context);
        subTitle.setPadding(0, 0, 0, 30);
        subTitle.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText2));
        subTitle.setText("Lorem ipsum dolor sit amet. Et consequatur omnis et repellendus odit vel rerum nesciunt ut velit earum qui exercitationem mollitia et accusantium pariatur.");
        subTitle.setGravity(Gravity.CENTER);
        subTitle.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        RelativeLayout.LayoutParams subTitleParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        subTitle.setLayoutParams(subTitleParams);
        linearLayout.addView(subTitle);

        // Coming Soon Box with top margin of 50
        LinearLayout comingSoonBox = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 50, 0, 32); // Top margin of 50
        comingSoonBox.setLayoutParams(layoutParams);
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(20f);
        shape.setColor(Theme.getColor(Theme.key_chats_menuTopBackgroundCats));
        comingSoonBox.setBackground(shape);

        TextView comingSoonText = new TextView(context);
        comingSoonText.setText("Coming Soon");
        comingSoonText.setPadding(24, 24, 24, 24);
        comingSoonText.setTypeface(null, Typeface.BOLD);
        comingSoonText.setTextColor(Theme.getColor(Theme.key_actionBarDefaultTitle));
        comingSoonBox.addView(comingSoonText);
        linearLayout.addView(comingSoonBox);

        return linearLayout;
    }
}
