/*
 * Copyright (c) 2017 by k3b.
 *
 * This file is part of "Extended jpg Media-db Scanner" (media_scan_trace).
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>
 */
package de.k3b.android.media.scanner;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

/**
 * Created by k3b on 03.11.2017.
 */

public class MediaScannerTools {

    public static String formatLogMessage(Context context, Intent intent) {
        String message = "null";
        if (intent != null) {
            final Uri uri = intent.getData();

            if (uri != null) {
                message = intent.getAction() + "\t" + uri;

                String fileInfo = "\n\t";
                String translatedPath = getFilePath(context, uri);

                File file = new File(translatedPath == null ? uri.getPath() : translatedPath);

                if (file.exists()) fileInfo += "existing ";
                if (file.isFile()) fileInfo += "file ";
                if (file.isDirectory()) fileInfo += "dir ";

                if (translatedPath != null) {
                    fileInfo += translatedPath;
                }

                if (fileInfo.length() > 3) {
                    message += fileInfo;
                }
            }
        }
        message += "\n\n";

        return message;
    }

    /** use mediadb to translate content-uri to file-uri
     * @return null if non-content-uri or path is unknown for uri. */
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
