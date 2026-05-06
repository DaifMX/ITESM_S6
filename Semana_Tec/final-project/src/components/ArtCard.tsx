import type { Artwork, IncidentType } from '../types';

const typeLabels: Record<IncidentType, string> = {
  cancha: 'En la cancha',
  gradas: 'En las gradas',
  redes_sociales: 'Redes sociales',
};

const typeColors: Record<IncidentType, string> = {
  cancha: 'bg-red-500/20 text-red-400 border-red-500/30',
  gradas: 'bg-orange-500/20 text-orange-400 border-orange-500/30',
  redes_sociales: 'bg-blue-500/20 text-blue-400 border-blue-500/30',
};

interface Props {
  artwork: Artwork;
}

export default function ArtCard({ artwork }: Props) {
  return (
    <article className="group overflow-hidden rounded-xl border border-zinc-800 bg-zinc-900 transition-all duration-300 hover:border-zinc-600 hover:shadow-xl hover:shadow-black/50">
      <div className={`${artwork.gradient} h-52 w-full`} />

      <div className="p-4 space-y-3">
        <div className="flex items-center justify-between">
          <span
            className={`rounded-full border px-2.5 py-0.5 text-xs font-medium ${typeColors[artwork.incidentType]}`}
          >
            {typeLabels[artwork.incidentType]}
          </span>
          <time className="text-xs text-zinc-500">{artwork.createdAt}</time>
        </div>

        <p className="text-sm text-zinc-300 leading-relaxed line-clamp-3 italic">
          "{artwork.prompt}"
        </p>

        <p className="text-xs text-zinc-500 flex items-center gap-1">
          <span>📍</span>
          {artwork.location}
        </p>
      </div>
    </article>
  );
}
