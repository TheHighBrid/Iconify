"use client";

import { ChangeEvent, useMemo, useState } from "react";
import { createIconifyGenerationRequest } from "@/lib/imageGeneration";
import PromptPreview from "./PromptPreview";

export default function UploadBox() {
  const [brandName, setBrandName] = useState("");
  const [styleNotes, setStyleNotes] = useState("");
  const [logoPreview, setLogoPreview] = useState<string | null>(null);
  const [logoFileName, setLogoFileName] = useState<string | undefined>();

  const generation = useMemo(
    () => createIconifyGenerationRequest({ brandName, styleNotes, logoFileName }),
    [brandName, styleNotes, logoFileName],
  );

  function handleLogoUpload(event: ChangeEvent<HTMLInputElement>) {
    const file = event.target.files?.[0];

    if (!file) {
      setLogoPreview(null);
      setLogoFileName(undefined);
      return;
    }

    setLogoFileName(file.name);
    setLogoPreview(URL.createObjectURL(file));
  }

  async function copyPrompt() {
    await navigator.clipboard.writeText(generation.prompt);
  }

  return (
    <div className="grid gap-8 lg:grid-cols-[0.95fr_1.05fr]">
      <section className="rounded-[2rem] border border-white/10 bg-white/[0.06] p-6 shadow-2xl shadow-black/30 backdrop-blur-xl md:p-8">
        <div className="mb-8">
          <p className="mb-3 text-sm font-semibold uppercase tracking-[0.4em] text-cyan-200">
            Iconify Studio
          </p>
          <h1 className="text-4xl font-black tracking-tight text-white md:text-6xl">
            Turn logos into premium 3D app icons.
          </h1>
          <p className="mt-5 max-w-xl text-base leading-7 text-slate-300">
            Upload a logo, describe the brand direction, and generate a polished
            image-model prompt that preserves identity while adding inflated,
            high-end depth.
          </p>
        </div>

        <div className="space-y-5">
          <label className="block">
            <span className="mb-2 block text-sm font-medium text-slate-200">
              Logo upload
            </span>
            <div className="flex min-h-44 cursor-pointer flex-col items-center justify-center rounded-3xl border border-dashed border-cyan-200/30 bg-slate-950/40 p-6 text-center transition hover:border-cyan-200/70 hover:bg-cyan-200/5">
              {logoPreview ? (
                // eslint-disable-next-line @next/next/no-img-element
                <img
                  alt="Uploaded logo preview"
                  className="max-h-28 rounded-2xl object-contain shadow-xl"
                  src={logoPreview}
                />
              ) : (
                <div>
                  <div className="mx-auto mb-4 flex size-14 items-center justify-center rounded-2xl bg-cyan-300/10 text-2xl">
                    ✦
                  </div>
                  <p className="font-semibold text-white">Drop in a logo</p>
                  <p className="mt-1 text-sm text-slate-400">
                    PNG, JPG, WEBP, or SVG works for prompt preparation.
                  </p>
                </div>
              )}
              <input
                accept="image/*"
                className="sr-only"
                onChange={handleLogoUpload}
                type="file"
              />
            </div>
          </label>

          <label className="block">
            <span className="mb-2 block text-sm font-medium text-slate-200">
              Brand name
            </span>
            <input
              className="w-full rounded-2xl border border-white/10 bg-slate-950/60 p-4 text-white outline-none ring-cyan-300/30 transition placeholder:text-slate-500 focus:border-cyan-200/70 focus:ring-4"
              onChange={(event) => setBrandName(event.target.value)}
              placeholder="Melato"
              value={brandName}
            />
          </label>

          <label className="block">
            <span className="mb-2 block text-sm font-medium text-slate-200">
              Style notes
            </span>
            <textarea
              className="min-h-32 w-full rounded-2xl border border-white/10 bg-slate-950/60 p-4 text-white outline-none ring-cyan-300/30 transition placeholder:text-slate-500 focus:border-cyan-200/70 focus:ring-4"
              onChange={(event) => setStyleNotes(event.target.value)}
              placeholder="Luxury, soft inflated textile, editorial shadows..."
              value={styleNotes}
            />
          </label>

          <button
            className="w-full rounded-2xl bg-cyan-300 px-5 py-4 font-bold text-slate-950 shadow-xl shadow-cyan-950/30 transition hover:-translate-y-0.5 hover:bg-cyan-200"
            onClick={copyPrompt}
            type="button"
          >
            Copy generated prompt
          </button>
          <p className="text-sm text-slate-400">{generation.message}</p>
        </div>
      </section>

      <PromptPreview prompt={generation.prompt} />
    </div>
  );
}
