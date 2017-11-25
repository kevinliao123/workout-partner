package com.fruitguy.workoutpartner.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.test.InstrumentationRegistry;

import com.fruitguy.workoutpartner.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by heliao on 11/25/17.
 */
public class BitmapUtilsTest {
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getImageBytes() throws Exception {
        Bitmap image = BitmapFactory.decodeResource(InstrumentationRegistry.getTargetContext().getResources(), R.mipmap.ic_launcher);
        assertNotNull(image);
        byte[] imageBytes = BitmapUtils.getImageBytes(image);
        assertNotNull(imageBytes);
        assertNotEquals(0, imageBytes.length);
    }

    @Test
    public void getImage() throws Exception {
    }

    @Test
    public void getBytes() throws Exception {
    }

    @Test
    public void readBitmap() throws Exception {
    }

}