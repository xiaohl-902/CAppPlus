package com.cpigeon.app.utils;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by Zhu TingYu on 2018/1/30.
 */

public class VideoProvider extends ContentProvider {
    public static final Uri CONTENT_URI_BASE =
            Uri.parse("content://com.myexampleapp.video.VideoProvider.files.files/");

    private static final String VIDEO_MIME_TYPE = "video/mp4";

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public String getType(final Uri uri) {
        return VIDEO_MIME_TYPE;
    }

    @Override
    public ParcelFileDescriptor openFile(final Uri uri, final String mode)
            throws FileNotFoundException {
        File f = new File(uri.getPath());

        if (f.exists())
            return (ParcelFileDescriptor.open(f,
                    ParcelFileDescriptor.MODE_READ_ONLY));

        throw new FileNotFoundException(uri.getPath());
    }

    @Override
    public int delete(final Uri uri, final String selection, final String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Uri insert(final Uri uri, final ContentValues values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Cursor query(final Uri uri, final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int update(final Uri uri, final ContentValues values, final String selection, final String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }
}
