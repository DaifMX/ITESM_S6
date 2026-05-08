import type { Artwork } from '../types';

const typeLabels: Record<string, string> = {
  cancha: 'En la cancha',
  gradas: 'En las gradas',
  redes_sociales: 'Redes sociales',
  sin_clasificar: 'Sin clasificar',
};

const typeColors: Record<string, string> = {
  cancha: 'bg-red-500/20 text-red-400 border-red-500/30',
  gradas: 'bg-orange-500/20 text-orange-400 border-orange-500/30',
  redes_sociales: 'bg-blue-500/20 text-blue-400 border-blue-500/30',
  sin_clasificar: 'bg-zinc-500/20 text-zinc-400 border-zinc-500/30',
};

interface Props {
  artwork: Artwork;
}

export default function ArtCard({ artwork }: Props) {
  const label = typeLabels[artwork.incidentType] ?? 'Sin clasificar';
  const colorClass = typeColors[artwork.incidentType] ?? typeColors.sin_clasificar;

  return (
    <article className="group overflow-hidden rounded-xl border border-zinc-800 bg-zinc-900 transition-all duration-300 hover:border-zinc-600 hover:shadow-xl hover:shadow-black/50">
      {artwork.imagen_base64 ? (
        <img
          src={`data:image/png;base64,${artwork.imagen_base64}`}
          alt={artwork.resumen_publico || artwork.prompt}
          className="h-52 w-full object-cover"
          loading="lazy"
          decoding="async"
        />
      ) : artwork.imagen_url ? (
        <img
          src={artwork.imagen_url}
          alt={artwork.resumen_publico || artwork.prompt}
          className="h-52 w-full object-cover"
          loading="lazy"
          decoding="async"
        />
      ) : (
        <div className={`${artwork.gradient ?? 'art-gradient-1'} h-52 w-full`} />
      )}

      <div className="p-4 space-y-3">
        <div className="flex items-center justify-between">
          <span className={`rounded-full border px-2.5 py-0.5 text-xs font-medium ${colorClass}`}>
            {label}
          </span>
          <time className="text-xs text-zinc-500">{artwork.createdAt}</time>
        </div>

        <p className="text-sm text-zinc-300 leading-relaxed line-clamp-3 italic">
          "{artwork.resumen_publico || artwork.prompt}"
        </p>

        <p className="text-xs text-zinc-500 flex items-center gap-1">
          <span>📍</span>
          {artwork.location}
        </p>
      </div>
    </article>
  );
}
