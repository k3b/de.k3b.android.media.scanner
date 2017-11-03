package de.k3b.android.extimagemediascanner;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

/**
 * Created by EVE on 03.11.2017.
 */

public class MediaScannerTools {
    public static String getText(Context context, Intent intent) {
        String message = "null";
        if (intent != null) {
            final Uri uri = intent.getData();

            if (uri != null) {
                message = intent.getAction() + "\t" + uri + "\n\t";

                String translatedPath = getFilePath(context, uri);

                File file = new File(translatedPath == null ? uri.getPath() : translatedPath);

                if (file.exists()) message += "\texisting ";
                if (file.isFile()) message += "file ";
                if (file.isDirectory()) message += "dir ";

                if (translatedPath != null) {
                    message += translatedPath;
                }
            }
        }
        message += "\n\n";

        return message;
    }

    private static String getFilePath(Context context, Uri uri) {
        // Get image file name
        Cursor cursor = null;
        String image_filename = null;
        try {
            cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
            if ((cursor != null) && cursor.moveToFirst()) {
                image_filename = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return image_filename;
    }

}
