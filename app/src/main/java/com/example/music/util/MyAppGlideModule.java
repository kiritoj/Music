package com.example.music.util;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

/**
 * Created by tk on 2019/9/8
 * Glide 4.x 需继承AppGlideModule才能使用GlideApp
 */

@GlideModule
public class MyAppGlideModule extends AppGlideModule {
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
