import IconResultGrid from "@/components/IconResultGrid";
import UploadBox from "@/components/UploadBox";

export default function Home() {
  return (
    <main className="min-h-screen overflow-hidden bg-slate-950 px-5 py-8 text-white md:px-10 lg:px-16">
      <div className="pointer-events-none fixed inset-0 bg-[radial-gradient(circle_at_top_left,rgba(34,211,238,0.24),transparent_32%),radial-gradient(circle_at_80%_20%,rgba(168,85,247,0.22),transparent_28%),linear-gradient(135deg,rgba(15,23,42,0),rgba(15,23,42,0.9))]" />
      <div className="relative mx-auto max-w-7xl space-y-8">
        <UploadBox />
        <IconResultGrid />
      </div>
    </main>
  );
}
