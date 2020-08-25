package br.com.legacy.utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.exifinterface.media.ExifInterface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;

/**
 * Created by appsimples on 1/11/18.
 */

public class ImageUtils {

    private static final String TAG = ImageUtils.class.getSimpleName();

    public static Bitmap getBitmap(final Context context, final Uri uri, final int with, final int height) {
        if (uri != null)
            return decodeUriAsBitmap(context, uri, with, height);
        return null;
    }

    public Bitmap getSmallImage(Context context, Uri uri) {
        return getBitmap(context, uri, 200, 200);
    }

    public static Bitmap getMediumImage(Context context, Uri uri) {
        return getBitmap(context, uri, 600, 600);
    }

    public static Bitmap getLargeImage(Context context, Uri uri) {
        return getBitmap(context, uri, 1800, 1800);
    }

    public static Bitmap decodeUriAsBitmap(Context context, Uri uri, int targetWidth, int targetHeight) {
        Bitmap bitmap = null;
        int rotationInDegrees;
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options);

            if (targetHeight == 0) {
                targetHeight = (int) (options.outHeight * targetWidth / (double) options.outWidth);
            }

            if (targetWidth == 0) {
                targetWidth = (int) (options.outWidth * targetHeight / (double) options.outHeight);
            }

            options.inSampleSize = calculateInSampleSize(options, targetWidth, targetHeight);

            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options);

            ExifInterface exif = getExifFromImageUri(uri, context);

            rotationInDegrees = exifToDegrees(exif);
            if (rotationInDegrees > 0) {
                Matrix matrix = new Matrix();
                matrix.preRotate(rotationInDegrees);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            }
        } catch (Exception e) {
            Log.d(TAG, "Decoding from Uri");
        }
        return bitmap;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static int exifToDegrees(ExifInterface exifInterface) {
        int rotation = 0;
        if (exifInterface == null) return rotation;
        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                rotation = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                rotation = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                rotation = 270;
                break;
        }
        return rotation;
    }

    public static ExifInterface getExifFromImageUri(Uri uri, Context context) {
        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(context.getContentResolver().openInputStream(uri));
        } catch (Exception ex) {
            Log.d(TAG, ex.getMessage(), ex);
        }
        return exifInterface;
    }

    public static void cropImage(Activity activity, Uri uri) {
        CropImage.activity(uri)
                .start(activity);
    }

    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            cursor.close();
            return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    public static void resizeImagesOnScreen(View view, float scale) {

        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                view.getViewTreeObserver().removeOnPreDrawListener(this);
                float height = view.getMeasuredWidth() * scale;

                if (view.getLayoutParams() instanceof ConstraintLayout.LayoutParams) {
                    ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                    layoutParams.height = (int) height;
                    view.setLayoutParams(layoutParams);
                }
                if (view.getLayoutParams() instanceof FrameLayout.LayoutParams) {
                    view.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, (int) height));
                }

                return false;
            }
        });
    }

}
