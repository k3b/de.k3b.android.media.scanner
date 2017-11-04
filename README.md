Enhanced jpg/exif/iptc/xmp media scaner to update android-4.0-s media database

Goal: extract mediascanner functionality (parse exif/iptc/xmp from jpg/xmp) out of [APhotoManager](https://github.com/k3b/APhotoManager/) into an independant service/package of it-s own
so that android-s media-db can be kept up to date even if APhotoManager is not installed or running.

## Features

* MediaScanTraceActivity : While not destroyed collect and display media-scanner-related broadcasts
  * ?? android.intent.action_MEDIA_SCANNER_STARTED + ??
  * ?? android.intent.action.MEDIA_SCANNER_FINISHED + ??
  * android.intent.action.MEDIA_SCANNER_SCAN_FILE + file:///path/to/file.jpg
  * android.hardware.action.NEW_PICTURE + content://media/external/...
  * com.android.camera.NEW_PICTURE + content://media/external/...

## Current Project Status

* MediaScanTraceActivity: early analysis to find out what is happening inside android.
* no scan logic implemented yet

