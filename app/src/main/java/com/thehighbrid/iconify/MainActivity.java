package com.thehighbrid.iconify;

import android.app.Activity;
import android.os.Bundle;
import android.os.Build;
import android.provider.MediaStore;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends Activity {
    private static final int PICK_IMAGE = 701;

    private final int black = Color.rgb(15, 15, 16);
    private final int cream = Color.rgb(232, 220, 200);
    private final int paper = Color.rgb(246, 241, 232);
    private final int muted = Color.rgb(174, 163, 146);

    private ImageView sourcePreview;
    private ImageView resultPreview;
    private TextView status;
    private Bitmap sourceBitmap;
    private Bitmap resultBitmap;
    private Uri savedUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Iconify");

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(black);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(20), dp(28), dp(20), dp(28));
        scroll.addView(root);

        TextView eyebrow = text("ICONIFY LOCAL RENDERER", 12, cream, Typeface.BOLD);
        eyebrow.setLetterSpacing(0.14f);
        root.addView(eyebrow);

        TextView title = text("Upload. Inflate. Save.", 35, paper, Typeface.BOLD);
        title.setPadding(0, dp(8), 0, dp(6));
        root.addView(title);

        TextView intro = text("This APK now processes the icon inside the app. No prompt copying. No external image app as the core flow.", 15, muted, Typeface.NORMAL);
        intro.setPadding(0, 0, 0, dp(18));
        intro.setLineSpacing(dp(3), 1f);
        root.addView(intro);

        sourcePreview = preview("Source icon preview");
        root.addView(sourcePreview, boxParams());

        Button pick = button("Upload icon image");
        root.addView(pick, fullParams(dp(12), 0));

        Button render = button("Generate bubbly 3D inflated icon");
        root.addView(render, fullParams(dp(10), 0));

        resultPreview = preview("Rendered result preview");
        root.addView(resultPreview, fullParams(dp(18), 0));

        Button save = button("Save PNG to gallery");
        root.addView(save, fullParams(dp(12), 0));

        Button share = button("Share PNG");
        root.addView(share, fullParams(dp(10), 0));

        status = text("Status: upload an icon to begin.", 14, paper, Typeface.NORMAL);
        status.setPadding(dp(16), dp(16), dp(16), dp(16));
        status.setLineSpacing(dp(3), 1f);
        status.setBackground(card(22, Color.rgb(26, 25, 24), Color.rgb(58, 54, 48), 1));
        root.addView(status, fullParams(dp(18), 0));

        TextView fixes = text("Issues fixed inside this APK:\n• Removed prompt-generator core.\n• Added image upload.\n• Added local bitmap rendering.\n• Added inflated edge/gloss/shadow/texture treatment.\n• Added save and share output.\n\nBest input: transparent PNG app icon or logo on a clean background.", 14, muted, Typeface.NORMAL);
        fixes.setPadding(dp(16), dp(16), dp(16), dp(16));
        fixes.setLineSpacing(dp(4), 1f);
        fixes.setBackground(card(22, Color.rgb(21, 21, 22), Color.rgb(54, 49, 43), 1));
        root.addView(fixes, fullParams(dp(14), 0));

        pick.setOnClickListener(v -> openPicker());
        render.setOnClickListener(v -> renderNow());
        save.setOnClickListener(v -> saveNow());
        share.setOnClickListener(v -> shareNow());

        setContentView(scroll);
    }

    private void openPicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != PICK_IMAGE || resultCode != RESULT_OK || data == null || data.getData() == null) return;
        try {
            Uri uri = data.getData();
            getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            InputStream input = getContentResolver().openInputStream(uri);
            Bitmap decoded = BitmapFactory.decodeStream(input);
            if (input != null) input.close();
            if (decoded == null) throw new IllegalArgumentException("Could not decode image");
            sourceBitmap = decoded.copy(Bitmap.Config.ARGB_8888, false);
            sourcePreview.setImageBitmap(sourceBitmap);
            resultBitmap = null;
            savedUri = null;
            resultPreview.setImageBitmap(null);
            resultPreview.setBackground(card(24, Color.rgb(31, 30, 29), Color.rgb(60, 56, 50), 1));
            status.setText("Status: icon loaded. Tap generate to render the inflated 3D version locally.");
        } catch (Exception e) {
            status.setText("Issue: image could not be loaded.\nFix: try a PNG or JPG from your gallery.\nDetails: " + e.getMessage());
        }
    }

    private void renderNow() {
        if (sourceBitmap == null) {
            toast("Upload an icon first");
            return;
        }
        try {
            status.setText("Status: rendering inflated icon locally...");
            resultBitmap = IconProcessor.render(sourceBitmap);
            resultPreview.setImageBitmap(resultBitmap);
            savedUri = null;
            status.setText("Done: generated a bubbly inflated 3D-style icon inside the APK. You can save or share the PNG now.");
        } catch (OutOfMemoryError e) {
            status.setText("Issue: image was too large for memory.\nFix: try a smaller icon file or screenshot crop.");
        } catch (Exception e) {
            status.setText("Issue: render failed.\nFix: try a cleaner PNG icon with transparent background.\nDetails: " + e.getMessage());
        }
    }

    private void saveNow() {
        if (resultBitmap == null) {
            toast("Generate the icon first");
            return;
        }
        try {
            savedUri = savePng(resultBitmap);
            status.setText("Saved: Iconify PNG is now in your gallery.\nURI: " + savedUri);
            toast("Saved to gallery");
        } catch (Exception e) {
            status.setText("Issue: save failed.\nFix: check storage permission or try sharing instead.\nDetails: " + e.getMessage());
        }
    }

    private void shareNow() {
        if (resultBitmap == null) {
            toast("Generate the icon first");
            return;
        }
        try {
            if (savedUri == null) savedUri = savePng(resultBitmap);
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/png");
            share.putExtra(Intent.EXTRA_STREAM, savedUri);
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(share, "Share Iconify PNG"));
        } catch (Exception e) {
            status.setText("Issue: share failed.\nFix: save it first, then share from gallery.\nDetails: " + e.getMessage());
        }
    }

    private Uri savePng(Bitmap bitmap) throws Exception {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "iconify_" + System.currentTimeMillis() + ".png");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Iconify");
            values.put(MediaStore.Images.Media.IS_PENDING, 1);
        }
        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        if (uri == null) throw new IllegalStateException("Gallery insert returned null");
        OutputStream out = getContentResolver().openOutputStream(uri);
        if (out == null) throw new IllegalStateException("Could not open gallery output stream");
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        out.flush();
        out.close();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.clear();
            values.put(MediaStore.Images.Media.IS_PENDING, 0);
            getContentResolver().update(uri, values, null, null);
        }
        return uri;
    }

    private ImageView preview(String label) {
        ImageView view = new ImageView(this);
        view.setAdjustViewBounds(true);
        view.setMinimumHeight(dp(230));
        view.setScaleType(ImageView.ScaleType.FIT_CENTER);
        view.setContentDescription(label);
        view.setPadding(dp(12), dp(12), dp(12), dp(12));
        view.setBackground(card(24, Color.rgb(31, 30, 29), Color.rgb(60, 56, 50), 1));
        return view;
    }

    private Button button(String value) {
        Button b = new Button(this);
        b.setText(value);
        b.setAllCaps(false);
        b.setTextColor(black);
        b.setTextSize(15);
        b.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        b.setPadding(dp(12), dp(12), dp(12), dp(12));
        b.setBackground(card(18, cream, Color.TRANSPARENT, 0));
        return b;
    }

    private TextView text(String value, int sp, int color, int style) {
        TextView tv = new TextView(this);
        tv.setText(value);
        tv.setTextSize(sp);
        tv.setTextColor(color);
        tv.setTypeface(Typeface.DEFAULT, style);
        return tv;
    }

    private GradientDrawable card(int radius, int fill, int stroke, int width) {
        GradientDrawable d = new GradientDrawable();
        d.setShape(GradientDrawable.RECTANGLE);
        d.setCornerRadius(dp(radius));
        d.setColor(fill);
        if (width > 0) d.setStroke(dp(width), stroke);
        return d;
    }

    private LinearLayout.LayoutParams boxParams() {
        return new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(280));
    }

    private LinearLayout.LayoutParams fullParams(int top, int bottom) {
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        p.setMargins(0, top, 0, bottom);
        return p;
    }

    private int dp(int v) {
        return Math.round(v * getResources().getDisplayMetrics().density);
    }

    private void toast(String value) {
        Toast.makeText(this, value, Toast.LENGTH_SHORT).show();
    }
}
