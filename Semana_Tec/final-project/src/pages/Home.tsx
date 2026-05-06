import { Link } from 'react-router-dom';

const steps = [
  { icon: '📝', label: 'Reportas', desc: 'Describes el incidente racista que presenciaste o sufriste.' },
  { icon: '⚡', label: 'n8n orquesta', desc: 'El webhook recibe el reporte y activa el flujo automático.' },
  { icon: '🧠', label: 'Ollama analiza', desc: 'La IA local lee el contexto y escribe un prompt artístico.' },
  { icon: '🎨', label: 'Se genera arte', desc: 'Un modelo de imagen crea una obra digital única.' },
  { icon: '🖼️', label: 'Se publica', desc: 'La obra aparece en la galería pública de Cancha Viva.' },
];

const stats = [
  { value: '247', label: 'Incidentes reportados' },
  { value: '247', label: 'Obras de arte creadas' },
  { value: '18', label: 'Países representados' },
];

export default function Home() {
  return (
    <div className="mx-auto max-w-6xl px-6">

      {/* Hero */}
      <section className="py-24 text-center">
        <span className="mb-6 inline-block rounded-full border border-green-500/30 bg-green-500/10 px-4 py-1.5 text-sm font-medium text-green-400">
          Arte generativo · Activismo digital · Fútbol
        </span>
        <h1 className="mx-auto max-w-3xl text-5xl font-bold leading-tight tracking-tight text-white sm:text-6xl">
          Cada incidente racista se convierte en{' '}
          <span className="text-green-400">una obra de arte.</span>
        </h1>
        <p className="mx-auto mt-6 max-w-2xl text-lg text-zinc-400">
          Reporta el racismo que ocurre en el fútbol. Nuestra plataforma transforma
          cada denuncia en arte generativo que visibiliza el problema y responde con cultura.
        </p>
        <div className="mt-10 flex flex-col items-center gap-4 sm:flex-row sm:justify-center">
          <Link
            to="/reportar"
            className="rounded-xl bg-green-500 px-8 py-3.5 text-base font-semibold text-black transition-colors hover:bg-green-400"
          >
            Reportar un incidente
          </Link>
          <Link
            to="/galeria"
            className="rounded-xl border border-zinc-700 px-8 py-3.5 text-base font-semibold text-white transition-colors hover:border-zinc-500 hover:bg-zinc-800"
          >
            Ver la galería →
          </Link>
        </div>
      </section>

      {/* Stats */}
      <section className="grid grid-cols-3 gap-6 rounded-2xl border border-zinc-800 bg-zinc-900 p-8">
        {stats.map(({ value, label }) => (
          <div key={label} className="text-center">
            <p className="font-display text-4xl font-bold text-green-400">{value}</p>
            <p className="mt-1 text-sm text-zinc-400">{label}</p>
          </div>
        ))}
      </section>

      {/* How it works */}
      <section className="py-20">
        <h2 className="text-center text-3xl font-bold text-white">¿Cómo funciona?</h2>
        <p className="mt-3 text-center text-zinc-400">
          Un flujo automatizado convierte tu reporte en resistencia cultural.
        </p>

        <div className="mt-12 relative">
          <div className="absolute left-0 right-0 top-10 hidden h-px bg-gradient-to-r from-transparent via-zinc-700 to-transparent md:block" />
          <div className="grid grid-cols-1 gap-6 md:grid-cols-5">
            {steps.map((step, i) => (
              <div key={i} className="flex flex-col items-center text-center">
                <div className="relative z-10 flex h-20 w-20 items-center justify-center rounded-2xl border border-zinc-700 bg-zinc-900 text-3xl shadow-lg">
                  {step.icon}
                </div>
                <h3 className="mt-4 font-display text-sm font-semibold text-white">{step.label}</h3>
                <p className="mt-2 text-xs text-zinc-500 leading-relaxed">{step.desc}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Tech stack callout */}
      <section className="mb-16 rounded-2xl border border-zinc-800 bg-zinc-900 p-8 md:p-12">
        <div className="grid gap-8 md:grid-cols-2">
          <div>
            <h2 className="text-2xl font-bold text-white">Tecnología al servicio del activismo</h2>
            <p className="mt-4 text-zinc-400 leading-relaxed">
              Usamos <strong className="text-white">n8n</strong> como cerebro de automatización y{' '}
              <strong className="text-white">Ollama</strong> para el análisis con IA — todo ejecutado
              localmente, sin exponer datos sensibles a servicios externos.
            </p>
            <p className="mt-3 text-zinc-400 leading-relaxed">
              Tu reporte llega a nuestro servidor, activa el flujo y en minutos se transforma en una obra
              de arte publicada en esta galería pública.
            </p>
          </div>
          <div className="flex flex-col justify-center gap-3">
            {[
              { name: 'n8n', desc: 'Orquestación del flujo via webhook' },
              { name: 'Ollama', desc: 'IA local para generar el prompt artístico' },
              { name: 'React + Vite', desc: 'Interfaz web rápida y moderna' },
            ].map(({ name, desc }) => (
              <div key={name} className="flex items-center gap-4 rounded-xl border border-zinc-700 p-4">
                <span className="font-mono text-sm font-semibold text-green-400">{name}</span>
                <span className="text-sm text-zinc-400">{desc}</span>
              </div>
            ))}
          </div>
        </div>
      </section>

    </div>
  );
}
