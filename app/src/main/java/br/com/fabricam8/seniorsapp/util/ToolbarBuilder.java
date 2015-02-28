package br.com.fabricam8.seniorsapp.util;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import br.com.fabricam8.seniorsapp.R;

/**
 * Created by Aercio on 2/15/15.
 */
public class ToolbarBuilder {

    public static Toolbar build(ActionBarActivity context, boolean navigateToParent) {
        Toolbar toolbar = (Toolbar) context.findViewById(R.id.seniors_toolbar);
        context.setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_toolbar_seniors);
        if(navigateToParent) {
            toolbar.setNavigationIcon(R.drawable.ic_action_back);
        }

        return toolbar;
    }

}
