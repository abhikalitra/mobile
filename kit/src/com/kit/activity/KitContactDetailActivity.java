package com.kit.activity;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created with IntelliJ IDEA.
 * User: sushil
 * Date: 12/3/12
 * Time: 8:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class KitContactDetailActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        long id = getIntent().getLongExtra("com.kit.cid", -1);
    }
}
