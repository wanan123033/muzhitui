package com.nevermore.muzhitui;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by Administrator on 2018/1/30.
 */

public class VideoFilter implements FilenameFilter {
    @Override
    public boolean accept(File dir, String name) {
        return name.endsWith(".mp4");
    }
}
