export default function Footer() {
  return (
    <footer className="border-t border-zinc-800 py-8 mt-16">
      <div className="mx-auto max-w-6xl px-6 flex flex-col sm:flex-row items-center justify-between gap-4 text-zinc-500 text-sm">
        <div className="flex items-center gap-2">
          <span>⚽</span>
          <span className="font-display font-semibold text-white">
            CANCHA<span className="text-green-400">VIVA</span>
          </span>
          <span>— Arte contra el racismo en el fútbol</span>
        </div>
        <div className="flex items-center gap-4 text-xs">
          <span>Impulsado por</span>
          <span className="rounded border border-zinc-700 px-2 py-1 font-mono text-zinc-400">n8n</span>
          <span className="rounded border border-zinc-700 px-2 py-1 font-mono text-zinc-400">Ollama</span>
        </div>
      </div>
    </footer>
  );
}
