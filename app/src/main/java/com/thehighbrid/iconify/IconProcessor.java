package com.thehighbrid.iconify;

import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

public final class IconProcessor {
    private IconProcessor() {}

    public static Bitmap render(Bitmap input) {
        final int size = 1024;
        Bitmap icon = normalize(input, size);
        Bitmap result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawColor(Color.rgb(246, 241, 232));

        Paint shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        shadowPaint.setColor(Color.argb(120, 0, 0, 0));
        shadowPaint.setMaskFilter(new BlurMaskFilter(34, BlurMaskFilter.Blur.NORMAL));
        int[] offset = new int[2];
        Bitmap alpha = icon.extractAlpha(shadowPaint, offset);
        canvas.drawBitmap(alpha, offset[0] + 22, offset[1] + 38, shadowPaint);
        alpha.recycle();

        Paint softEdge = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG);
        RectF bigger = new RectF(-18, -10, size + 18, size + 18);
        softEdge.setAlpha(150);
        Bitmap edge = tint(icon, 0.82f);
        canvas.drawBitmap(edge, null, bigger, softEdge);
        edge.recycle();

        Paint main = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG);
        canvas.drawBitmap(icon, 0, 0, main);

        Bitmap lighting = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas lc = new Canvas(lighting);
        lc.drawBitmap(icon, 0, 0, null);
        Paint gloss = new Paint(Paint.ANTI_ALIAS_FLAG);
        gloss.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        gloss.setColor(Color.argb(92, 255, 255, 255));
        lc.drawOval(new RectF(size * 0.22f, size * 0.14f, size * 0.65f, size * 0.44f), gloss);
        gloss.setColor(Color.argb(44, 255, 255, 255));
        lc.drawOval(new RectF(size * 0.18f, size * 0.12f, size * 0.82f, size * 0.68f), gloss);
        Paint shade = new Paint(Paint.ANTI_ALIAS_FLAG);
        shade.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        shade.setColor(Color.argb(72, 0, 0, 0));
        lc.drawOval(new RectF(size * 0.30f, size * 0.61f, size * 0.90f, size * 1.02f), shade);
        canvas.drawBitmap(lighting, 0, 0, null);
        lighting.recycle();

        addTexture(result);
        icon.recycle();
        return result;
    }

    private static Bitmap normalize(Bitmap input, int size) {
        Bitmap src = input.copy(Bitmap.Config.ARGB_8888, false);
        Rect b = bounds(src);
        Bitmap out = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(out);
        c.drawColor(Color.TRANSPARENT);
        float scale = Math.min(size * 0.70f / Math.max(1, b.width()), size * 0.70f / Math.max(1, b.height()));
        float w = b.width() * scale;
        float h = b.height() * scale;
        RectF dst = new RectF((size - w) / 2f, (size - h) / 2f, (size + w) / 2f, (size + h) / 2f);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG);
        c.drawBitmap(src, b, dst, p);
        src.recycle();
        return out;
    }

    private static Rect bounds(Bitmap bm) {
        int w = bm.getWidth();
        int h = bm.getHeight();
        int[] px = new int[w * h];
        bm.getPixels(px, 0, w, 0, 0, w, h);
        boolean alpha = false;
        for (int v : px) if (Color.alpha(v) < 250) { alpha = true; break; }
        int minX = w, minY = h, maxX = -1, maxY = -1;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int v = px[y * w + x];
                boolean keep = alpha ? Color.alpha(v) > 24 : !(Color.red(v) > 244 && Color.green(v) > 244 && Color.blue(v) > 244);
                if (keep) {
                    if (x < minX) minX = x;
                    if (y < minY) minY = y;
                    if (x > maxX) maxX = x;
                    if (y > maxY) maxY = y;
                }
            }
        }
        if (maxX < minX) return new Rect(0, 0, w, h);
        return new Rect(minX, minY, maxX + 1, maxY + 1);
    }

    private static Bitmap tint(Bitmap src, float amount) {
        Bitmap out = src.copy(Bitmap.Config.ARGB_8888, true);
        int w = out.getWidth();
        int h = out.getHeight();
        int[] px = new int[w * h];
        out.getPixels(px, 0, w, 0, 0, w, h);
        for (int i = 0; i < px.length; i++) {
            int a = Color.alpha(px[i]);
            if (a == 0) continue;
            px[i] = Color.argb(a, Math.round(Color.red(px[i]) * amount), Math.round(Color.green(px[i]) * amount), Math.round(Color.blue(px[i]) * amount));
        }
        out.setPixels(px, 0, w, 0, 0, w, h);
        return out;
    }

    private static void addTexture(Bitmap bm) {
        int w = bm.getWidth();
        int h = bm.getHeight();
        int[] px = new int[w * h];
        bm.getPixels(px, 0, w, 0, 0, w, h);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int i = y * w + x;
                int v = px[i];
                int n = ((x * 73856093) ^ (y * 19349663)) & 7;
                int delta = n - 3;
                px[i] = Color.rgb(clamp(Color.red(v) + delta), clamp(Color.green(v) + delta), clamp(Color.blue(v) + delta));
            }
        }
        bm.setPixels(px, 0, w, 0, 0, w, h);
    }

    private static int clamp(int v) {
        return Math.max(0, Math.min(255, v));
    }
}
