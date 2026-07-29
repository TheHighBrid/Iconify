const mockResults = [
  "Soft inflated glyph",
  "Glass-lit app tile",
  "Editorial clay render",
];

export default function IconResultGrid() {
  return (
    <section className="grid gap-4 md:grid-cols-3">
      {mockResults.map((label, index) => (
        <div
          className="group rounded-3xl border border-white/10 bg-gradient-to-br from-white/15 to-white/[0.03] p-4 shadow-2xl shadow-cyan-950/20"
          key={label}
        >
          <div className="mb-4 flex aspect-square items-center justify-center rounded-[2rem] bg-[radial-gradient(circle_at_30%_20%,rgba(255,255,255,0.8),rgba(34,211,238,0.25)_35%,rgba(99,102,241,0.35)_70%,rgba(15,23,42,0.9))] transition duration-300 group-hover:scale-[1.02]">
            <div className="flex size-24 items-center justify-center rounded-[2rem] bg-white/20 text-4xl font-black text-white shadow-2xl shadow-black/30 backdrop-blur-md">
              {index + 1}
            </div>
          </div>
          <h3 className="font-semibold text-white">{label}</h3>
          <p className="mt-1 text-sm text-slate-400">
            Preview placeholder for future image-generation outputs.
          </p>
        </div>
      ))}
    </section>
  );
}
