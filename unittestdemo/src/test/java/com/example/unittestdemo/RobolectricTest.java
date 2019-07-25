package com.example.unittestdemo;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertNull;

@RunWith(RobolectricTestRunner.class)
public class RobolectricTest {

    @Test
    public void testClickBtn(){
        assertNull(new Object());
//        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
//        mainActivity.findViewById(R.id.button).performClick();

//        Intent expectedIntent = new Intent(mainActivity, LoginActivity.class);
//        Intent actual = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();
//        assertEquals(expectedIntent.getComponent(), actual.getComponent());

    }
}
