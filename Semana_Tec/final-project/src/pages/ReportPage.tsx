import { useState, type FormEvent } from 'react';
import type { Artwork, Incident, IncidentType } from '../types';

const N8N_WEBHOOK = import.meta.env.VITE_N8N_WEBHOOK_URL ?? '';

type Status = 'idle' | 'loading' | 'success' | 'error';

const incidentTypes: { value: IncidentType; label: string; icon: string }[] = [
  { value: 'cancha', label: 'En la cancha', icon: '⚽' },
  { value: 'gradas', label: 'En las gradas', icon: '🏟️' },
  { value: 'redes_sociales', label: 'En redes sociales', icon: '📱' },
];

export default function ReportPage() {
  const [form, setForm] = useState<Incident>({
    type: 'cancha',
    description: '',
    location: '',
    reportedAt: new Date().toISOString().slice(0, 16),
    reporterName: '',
  });
  const [status, setStatus] = useState<Status>('idle');
  const [error, setError] = useState('');

  async function handleSubmit(e: FormEvent) {
    e.preventDefault();
    if (!form.description.trim() || !form.location.trim()) return;

    setStatus('loading');
    setError('');

    try {
      const res = await fetch(N8N_WEBHOOK, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          tipoIncidente: form.type,
          description: form.description,
          location: form.location,
          reportedAt: form.reportedAt,
          reporterName: form.reporterName,
        }),
      });

      if (!res.ok) throw new Error(`Error ${res.status}`);

      try {
        const obra = await res.json();
        const artwork: Artwork = {
          id: obra.id ?? crypto.randomUUID(),
          imagen_base64: obra.imagen_base64 ?? null,
          resumen_publico: obra.resumen_publico ?? '',
          prompt: obra.prompt_artistico ?? '',
          createdAt: (obra.fecha ?? new Date().toISOString()).slice(0, 10),
          incidentType: obra.tipo_incidente ?? 'sin_clasificar',
          location: obra.lugar ?? form.location,
        };
        const stored: Artwork[] = JSON.parse(localStorage.getItem('cancha-viva-obras') ?? '[]');
        localStorage.setItem('cancha-viva-obras', JSON.stringify([artwork, ...stored]));
      } catch {
        // el workflow completó pero la respuesta no incluye la obra (ej. SD offline)
      }

      setStatus('success');
    } catch (err) {
      setStatus('error');
      setError(err instanceof Error ? err.message : 'Error desconocido');
    }
  }

  if (status === 'success') {
    return (
      <div className="mx-auto max-w-xl px-6 py-24 text-center">
        <div className="text-6xl mb-6">🎨</div>
        <h1 className="font-display text-3xl font-bold text-white">¡Reporte recibido!</h1>
        <p className="mt-4 text-zinc-400 leading-relaxed">
          Tu denuncia ya está en camino. Ollama está analizando el incidente y
          en breve generará una obra de arte que aparecerá en la galería.
        </p>
        <button
          onClick={() => {
            setStatus('idle');
            setForm({ type: 'cancha', description: '', location: '', reportedAt: new Date().toISOString().slice(0, 16), reporterName: '' });
          }}
          className="mt-8 rounded-xl border border-zinc-700 px-6 py-3 text-sm font-semibold text-white transition-colors hover:bg-zinc-800"
        >
          Reportar otro incidente
        </button>
      </div>
    );
  }

  return (
    <div className="mx-auto max-w-2xl px-6 py-16">
      <div className="mb-10">
        <h1 className="font-display text-4xl font-bold text-white">Reportar un incidente</h1>
        <p className="mt-3 text-zinc-400">
          Tu reporte activa el flujo de arte generativo. Sé lo más descriptivo posible.
        </p>
      </div>

      <form onSubmit={handleSubmit} className="space-y-6">

        {/* Tipo de incidente */}
        <fieldset>
          <legend className="mb-3 text-sm font-medium text-zinc-300">Tipo de incidente *</legend>
          <div className="grid grid-cols-3 gap-3">
            {incidentTypes.map(({ value, label, icon }) => (
              <label
                key={value}
                className={`flex cursor-pointer flex-col items-center gap-2 rounded-xl border p-4 text-center transition-colors ${
                  form.type === value
                    ? 'border-green-500 bg-green-500/10 text-green-400'
                    : 'border-zinc-700 bg-zinc-900 text-zinc-400 hover:border-zinc-600'
                }`}
              >
                <input
                  type="radio"
                  name="type"
                  value={value}
                  checked={form.type === value}
                  onChange={() => setForm(f => ({ ...f, type: value }))}
                  className="sr-only"
                />
                <span className="text-2xl">{icon}</span>
                <span className="text-xs font-medium">{label}</span>
              </label>
            ))}
          </div>
        </fieldset>

        {/* Descripción */}
        <div>
          <label className="mb-2 block text-sm font-medium text-zinc-300">
            Descripción del incidente *
          </label>
          <textarea
            required
            rows={5}
            value={form.description}
            onChange={e => setForm(f => ({ ...f, description: e.target.value }))}
            placeholder="Describe con detalle qué ocurrió, quiénes estaban involucrados y cómo se manifestó el racismo..."
            className="w-full rounded-xl border border-zinc-700 bg-zinc-900 px-4 py-3 text-sm text-white placeholder-zinc-600 outline-none transition-colors focus:border-green-500 focus:ring-1 focus:ring-green-500/50 resize-none"
          />
        </div>

        {/* Lugar y fecha en paralelo */}
        <div className="grid gap-4 sm:grid-cols-2">
          <div>
            <label className="mb-2 block text-sm font-medium text-zinc-300">
              Lugar / Club / Plataforma *
            </label>
            <input
              type="text"
              required
              value={form.location}
              onChange={e => setForm(f => ({ ...f, location: e.target.value }))}
              placeholder="Ej: Estadio Azteca, Twitter, Liga MX..."
              className="w-full rounded-xl border border-zinc-700 bg-zinc-900 px-4 py-3 text-sm text-white placeholder-zinc-600 outline-none transition-colors focus:border-green-500 focus:ring-1 focus:ring-green-500/50"
            />
          </div>
          <div>
            <label className="mb-2 block text-sm font-medium text-zinc-300">
              Fecha y hora *
            </label>
            <input
              type="datetime-local"
              required
              value={form.reportedAt}
              onChange={e => setForm(f => ({ ...f, reportedAt: e.target.value }))}
              className="w-full rounded-xl border border-zinc-700 bg-zinc-900 px-4 py-3 text-sm text-white outline-none transition-colors focus:border-green-500 focus:ring-1 focus:ring-green-500/50 [color-scheme:dark]"
            />
          </div>
        </div>

        {/* Nombre opcional */}
        <div>
          <label className="mb-2 block text-sm font-medium text-zinc-300">
            Tu nombre <span className="text-zinc-500">(opcional)</span>
          </label>
          <input
            type="text"
            value={form.reporterName}
            onChange={e => setForm(f => ({ ...f, reporterName: e.target.value }))}
            placeholder="Anónimo por defecto"
            className="w-full rounded-xl border border-zinc-700 bg-zinc-900 px-4 py-3 text-sm text-white placeholder-zinc-600 outline-none transition-colors focus:border-green-500 focus:ring-1 focus:ring-green-500/50"
          />
        </div>

        {/* Error */}
        {status === 'error' && (
          <p className="rounded-lg border border-red-500/30 bg-red-500/10 px-4 py-3 text-sm text-red-400">
            No se pudo enviar el reporte: {error}. Verifica la URL del webhook en .env.
          </p>
        )}

        <button
          type="submit"
          disabled={status === 'loading'}
          className="w-full rounded-xl bg-green-500 py-4 text-base font-semibold text-black transition-colors hover:bg-green-400 disabled:cursor-not-allowed disabled:opacity-60"
        >
          {status === 'loading' ? 'Enviando reporte...' : 'Enviar reporte y generar arte'}
        </button>

        <p className="text-center text-xs text-zinc-600">
          Tu reporte activa el flujo n8n → Ollama → imagen generativa → galería pública.
        </p>
      </form>
    </div>
  );
}
