export type IconifyPromptInput = {
  brandName: string;
  styleNotes?: string;
};

export function buildIconifyPrompt({ brandName, styleNotes }: IconifyPromptInput) {
  const normalizedBrandName = brandName.trim() || "the provided brand";
  const normalizedStyleNotes = styleNotes?.trim();

  return `Transform the provided ${normalizedBrandName} logo into a standalone, front-facing, inflated 3D app icon.

Preserve the original logo's core silhouette, color palette, symbol, and visual identity.

Render it as a premium soft air-filled icon with rounded volume, subtle highlights, realistic material depth, clean edges, and elegant shadows.

The result should feel cohesive, high-end, modern, and suitable for an app icon.

Do not create a collage.
Do not add extra text.
Do not distort the logo identity.
Do not change the brand colors unless required for lighting realism.${
    normalizedStyleNotes
      ? `\n\nAdditional style notes: ${normalizedStyleNotes}`
      : ""
  }`;
}
