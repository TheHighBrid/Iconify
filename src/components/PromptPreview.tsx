type PromptPreviewProps = {
  prompt: string;
};

export default function PromptPreview({ prompt }: PromptPreviewProps) {
  return (
    <section className="rounded-3xl border border-white/10 bg-white/[0.04] p-5 shadow-2xl shadow-black/20 backdrop-blur">
      <div className="mb-3 flex items-center justify-between gap-3">
        <h2 className="text-sm font-semibold uppercase tracking-[0.3em] text-cyan-200">
          Generated prompt
        </h2>
        <span className="rounded-full border border-emerald-300/30 bg-emerald-300/10 px-3 py-1 text-xs text-emerald-200">
          Ready
        </span>
      </div>
      <pre className="max-h-96 overflow-auto whitespace-pre-wrap rounded-2xl bg-slate-950/80 p-4 text-sm leading-6 text-slate-200">
        {prompt}
      </pre>
    </section>
  );
}
