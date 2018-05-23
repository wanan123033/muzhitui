package base.helper;

import android.app.Activity;
import android.content.Intent;

import base.ClipImagActivity;


public class PhotoActionHelper {
    private static final String EXTRA_OUTPUT = "output";
    private static final String EXTRA_HEIGHT = "height";
    private static final String EXTRA_INPUT = "input";
    private static final String EXTRA_OUTPUT_MAX_WIDTH = "output-max-width";
    private static final String EXTRA_POSITION = "position";

    private final Intent mIntent;
    private final Activity mFrom;
    private int mRequestCode;


    private PhotoActionHelper(Activity from, Class to) {
        mFrom = from;
        mIntent = new Intent(from, to);

    }

    public static PhotoActionHelper clipImage(Activity from) {
        return new PhotoActionHelper(from, ClipImagActivity.class);
    }

    public PhotoActionHelper setPosition(int mPosition) {
        mIntent.putExtra(EXTRA_POSITION, mPosition);
        return this;
    }

    public PhotoActionHelper setExtraHeight(int height) {
        mIntent.putExtra(EXTRA_HEIGHT, height);
        return this;
    }

    public PhotoActionHelper output(String path) {
        mIntent.putExtra(EXTRA_OUTPUT, path);
        return this;
    }

    public PhotoActionHelper input(String path) {
        mIntent.putExtra(EXTRA_INPUT, path);
        return this;
    }

    public PhotoActionHelper maxOutputWidth(int width) {
        mIntent.putExtra(EXTRA_OUTPUT_MAX_WIDTH, width);
        return this;
    }

    public PhotoActionHelper extra(Intent intent) {
        mIntent.putExtras(intent);
        return this;
    }

    public PhotoActionHelper requestCode(int code) {
        mRequestCode = code;
        return this;
    }

    public void start() {
        mFrom.startActivityForResult(mIntent, mRequestCode);
    }

    public static String getOutputPath(Intent data) {
        return data == null ? null : data.getStringExtra(EXTRA_OUTPUT);
    }

    public static int getExtraHeight(Intent data) {
        return data == null ? null : data.getIntExtra(EXTRA_HEIGHT, 0);
    }

    public static String getInputPath(Intent data) {
        return data == null ? null : data.getStringExtra(EXTRA_INPUT);
    }

    public static int getMaxOutputWidth(Intent data) {
        return data.getIntExtra(EXTRA_OUTPUT_MAX_WIDTH, 0);
    }
    public static int getPosition(Intent data) {
        return data == null ? null : data.getIntExtra(EXTRA_POSITION,0);
    }

}