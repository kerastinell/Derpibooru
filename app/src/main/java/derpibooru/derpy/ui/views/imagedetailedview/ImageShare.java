package derpibooru.derpy.ui.views.imagedetailedview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;

import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import derpibooru.derpy.R;

/**
 * A wrapper for {@link ShareActionProvider} that creates a share intent for the particular image.
 * <p>
 * Since a local file is required for binary data sharing, the class needs an existing image
 * resource to create the intent (see {@link #enableSharing(GlideDrawable, int, String)} method for
 * more information).
 *
 * @author http://stackoverflow.com/a/30172792/1726690
 */
class ImageShare {
    private static final String IMAGE_SHARE_CACHE_DIR = "shared";

    /* note that the filename may be displayed to the user (e.g. in Android's native mail app) */
    private static final String SLASH_TEMP_PNG_FILE_NAME = "/pony.png";
    private static final String SLASH_TEMP_GIF_FILE_NAME = "/pony.gif";

    /* Android's native mail app doesn't allow attachments > 5MiB and there's a good reason for that (mobile networks) */
    private static final int GIF_FILE_SIZE_LIMIT_BYTES = 5242880;

    private final Context mContext;
    private final ShareActionProvider mProvider;

    private boolean mIsSharingEnabled;

    public ImageShare(Context context, ShareActionProvider provider) {
        mContext = context;
        mProvider = provider;
    }

    /**
     * Enables sharing by setting the intent that contains
     * the specified image resource for the {@link ShareActionProvider}.
     *
     * @param imageResource image resource to share
     * @param imageId booru image ID (used to for the accompanying text)
     * @param imageTags names of image tags (used to for the accompanying text)
     * @return {@code true} if sharing was enabled, {@code false} if an error occurred (see the log)
     */
    public boolean enableSharing(GlideDrawable imageResource, int imageId, String imageTags) {
        Intent shareIntent =
                getShareIntentForImage(imageResource, getSharingText(imageId, imageTags));
        if (shareIntent != null) {
            mProvider.setShareIntent(shareIntent);
            mIsSharingEnabled = true;
            return true;
        }
        return false;
    }

    public boolean isSharingEnabled() {
        return mIsSharingEnabled;
    }

    private String getSharingText(int imageId, String imageTags) {
        return String.format(mContext.getString(R.string.share_image), imageId, imageTags);
    }

    @Nullable
    private Intent getShareIntentForImage(GlideDrawable imageResource, String sharingText) {
        File cachedImage = null;
        if (imageResource instanceof GlideBitmapDrawable) {
            cachedImage = getCachedBitmap(((GlideBitmapDrawable) imageResource).getBitmap());
        } else if (imageResource instanceof GifDrawable) {
            cachedImage = getCachedGif((GifDrawable) imageResource);
        }
        if (cachedImage != null) {
            Uri contentUri = FileProvider.getUriForFile(mContext, "derpibooru.derpy.ui.ImageActivity", cachedImage);
            if (contentUri != null) {
                return new Intent()
                        .setAction(Intent.ACTION_SEND)
                        .putExtra(Intent.EXTRA_SUBJECT, sharingText)
                        .putExtra(Intent.EXTRA_STREAM, contentUri)
                        .setDataAndType(contentUri, mContext.getContentResolver().getType(contentUri))
                        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        }
        return null;
    }

    @Nullable
    private File getCachedBitmap(Bitmap bitmap) {
        try {
            File cacheDir = getImageCacheDir();
            if (cacheDir == null) {
                throw new IOException("cache directory does not exist.");
            }
            FileOutputStream stream = new FileOutputStream(cacheDir + SLASH_TEMP_PNG_FILE_NAME);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();
            return new File(cacheDir + SLASH_TEMP_PNG_FILE_NAME);
        } catch (IOException e) {
            Log.e("ImageShare", "getCachedBitmap", e);
        }
        return null;
    }

    @Nullable
    private File getCachedGif(GifDrawable gifDrawable) {
        try {
            File cacheDir = getImageCacheDir();
            if (cacheDir == null) {
                throw new IOException("cache directory does not exist.");
            }
            if (gifDrawable.getData().length >= GIF_FILE_SIZE_LIMIT_BYTES) {
                return getCachedBitmap(gifDrawable.getFirstFrame());
            } else {
                FileOutputStream stream = new FileOutputStream(cacheDir + SLASH_TEMP_GIF_FILE_NAME);
                stream.write(gifDrawable.getData());
                stream.close();
                return new File(cacheDir + SLASH_TEMP_GIF_FILE_NAME);
            }
        } catch (IOException e) {
            Log.e("ImageShare", "getCachedGif", e);
        }
        return null;
    }

    @Nullable
    private File getImageCacheDir() {
        File cache = new File(mContext.getCacheDir(), IMAGE_SHARE_CACHE_DIR);
        return (cache.mkdirs() || cache.isDirectory()) ? cache : null;
    }
}
