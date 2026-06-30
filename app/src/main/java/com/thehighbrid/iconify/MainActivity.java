package com.thehighbrid.iconify;

import android.app.Activity;
import android.os.Bundle;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private EditText iconInput;
    private TextView output;

    private final int black = Color.rgb(15, 15, 16);
    private final int cream = Color.rgb(232, 220, 200);
    private final int paper = Color.rgb(246, 241, 232);
    private final int muted = Color.rgb(174, 163, 146);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ScrollView scrollView = new ScrollView(this);
        scrollView.setFillViewport(true);
        scrollView.setBackgroundColor(black);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(22), dp(30), dp(22), dp(30));
        scrollView.addView(root);

        TextView eyebrow = text("MELATO LABS", 12, cream, Typeface.BOLD);
        eyebrow.setLetterSpacing(0.18f);
        root.addView(eyebrow);

        TextView title = text("Iconify", 42, paper, Typeface.BOLD);
        title.setPadding(0, dp(8), 0, 0);
        root.addView(title);

        TextView subtitle = text("Turn logo ideas into premium inflated 3D icon prompts.", 16, muted, Typeface.NORMAL);
        subtitle.setPadding(0, dp(4), 0, dp(22));
        root.addView(subtitle);

        TextView intro = text(
            "Paste an app name, logo description, or visual reference note. Iconify will generate a clean prompt you can copy into an image model.",
            15,
            paper,
            Typeface.NORMAL
        );
        intro.setLineSpacing(dp(3), 1.0f);
        intro.setPadding(dp(18), dp(18), dp(18), dp(18));
        intro.setBackground(card(24, Color.rgb(30, 29, 28), Color.rgb(58, 54, 48), 1));
        root.addView(intro, matchWrap());

        iconInput = new EditText(this);
        iconInput.setHint("Example: Instagram camera icon, Nike swoosh, Shopify bag logo...");
        iconInput.setHintTextColor(Color.rgb(125, 116, 102));
        iconInput.setTextColor(black);
        iconInput.setTextSize(16);
        iconInput.setMinLines(4);
        iconInput.setGravity(Gravity.TOP | Gravity.START);
        iconInput.setPadding(dp(16), dp(14), dp(16), dp(14));
        iconInput.setBackground(card(22, paper, Color.TRANSPARENT, 0));
        LinearLayout.LayoutParams inputParams = matchWrap();
        inputParams.setMargins(0, dp(20), 0, dp(14));
        root.addView(iconInput, inputParams);

        Button generate = button("Generate Iconify Prompt");
        root.addView(generate, matchWrap());

        Button copy = button("Copy Prompt");
        LinearLayout.LayoutParams copyParams = matchWrap();
        copyParams.setMargins(0, dp(10), 0, dp(18));
        root.addView(copy, copyParams);

        output = text("Your generated prompt will appear here.", 15, paper, Typeface.NORMAL);
        output.setTextIsSelectable(true);
        output.setLineSpacing(dp(4), 1.0f);
        output.setPadding(dp(18), dp(18), dp(18), dp(18));
        output.setBackground(card(24, Color.rgb(23, 23, 24), Color.rgb(70, 65, 58), 1));
        root.addView(output, matchWrap());

        TextView howTo = text(
            "How to use:\n1. Describe the logo or icon.\n2. Generate and copy the prompt.\n3. Paste it into your image tool.\n4. Ask for separate front-facing icon renders on a clean background.",
            14,
            muted,
            Typeface.NORMAL
        );
        howTo.setLineSpacing(dp(4), 1.0f);
        LinearLayout.LayoutParams howParams = matchWrap();
        howParams.setMargins(0, dp(20), 0, 0);
        root.addView(howTo, howParams);

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                output.setText(buildPrompt(iconInput.getText().toString()));
            }
        });

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String prompt = output.getText().toString();
                if (prompt.trim().length() == 0 || prompt.startsWith("Your generated")) {
                    prompt = buildPrompt(iconInput.getText().toString());
                    output.setText(prompt);
                }
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setPrimaryClip(ClipData.newPlainText("Iconify prompt", prompt));
                Toast.makeText(MainActivity.this, "Iconify prompt copied", Toast.LENGTH_SHORT).show();
            }
        });

        setContentView(scrollView);
    }

    private String buildPrompt(String rawInput) {
        String subject = rawInput == null ? "" : rawInput.trim();
        if (subject.length() == 0) {
            subject = "the provided app logo or icon reference";
        }

        return "Create a standalone, front-facing inflated 3D app icon based on: " + subject + ".\n\n"
            + "Preserve the original logo silhouette, core symbol, color palette, and visual identity. Render it as a soft air-filled object with rounded inflated edges, subtle seams, premium tactile texture, realistic studio lighting, natural shadows, and high-end product-design polish.\n\n"
            + "The result should feel cohesive, expensive, clean, playful, and believable. Use a centered composition on a neutral off-white background. No hands, no phone mockup, no extra text, no collage, no UI screen, no watermark. Create one separate icon image only.";
    }

    private TextView text(String value, int sp, int color, int style) {
        TextView view = new TextView(this);
        view.setText(value);
        view.setTextSize(sp);
        view.setTextColor(color);
        view.setTypeface(Typeface.DEFAULT, style);
        return view;
    }

    private Button button(String value) {
        Button button = new Button(this);
        button.setText(value);
        button.setTextSize(15);
        button.setAllCaps(false);
        button.setTextColor(black);
        button.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        button.setPadding(dp(14), dp(12), dp(14), dp(12));
        button.setBackground(card(18, cream, Color.TRANSPARENT, 0));
        return button;
    }

    private GradientDrawable card(int radiusDp, int fillColor, int strokeColor, int strokeWidthDp) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(dp(radiusDp));
        drawable.setColor(fillColor);
        if (strokeWidthDp > 0) {
            drawable.setStroke(dp(strokeWidthDp), strokeColor);
        }
        return drawable;
    }

    private LinearLayout.LayoutParams matchWrap() {
        return new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
    }

    private int dp(int value) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(value * density);
    }
}
