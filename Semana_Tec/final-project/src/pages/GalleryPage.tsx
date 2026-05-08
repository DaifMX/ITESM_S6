import { useState } from 'react';
import { Link } from 'react-router-dom';
import ArtCard from '../components/ArtCard';
import { mockArtworks } from '../data/mockArtworks';
import type { Artwork, IncidentType } from '../types';

type Filter = 'all' | IncidentType | 'sin_clasificar';

const filters: { value: Filter; label: string }[] = [
  { value: 'all', label: 'Todas' },
  { value: 'cancha', label: 'En la cancha' },
  { value: 'gradas', label: 'En las gradas' },
  { value: 'redes_sociales', label: 'Redes sociales' },
];

function loadRealArtworks(): Artwork[] {
  try {
    return JSON.parse(localStorage.getItem('cancha-viva-obras') ?? '[]');
  } catch {
    return [];
  }
}

export default function GalleryPage() {
  const [active, setActive] = useState<Filter>('all');
  const [realArtworks] = useState<Artwork[]>(loadRealArtworks);

  const allArtworks = [...realArtworks, ...mockArtworks];
  const visible = active === 'all'
    ? allArtworks
    : allArtworks.filter(a => a.incidentType === active);

  return (
    <div className="mx-auto max-w-6xl px-6 py-16">

      <div className="mb-10 flex flex-col gap-4 sm:flex-row sm:items-end sm:justify-between">
        <div>
          <h1 className="font-display text-4xl font-bold text-white">Galería de obras</h1>
          <p className="mt-2 text-zinc-400">
            Cada pieza nació de un incidente real. Arte como acto de resistencia.
          </p>
        </div>
        <Link
          to="/reportar"
          className="self-start rounded-xl bg-green-500 px-5 py-2.5 text-sm font-semibold text-black transition-colors hover:bg-green-400 sm:self-auto"
        >
          + Reportar
        </Link>
      </div>

      {/* Filters */}
      <div className="mb-8 flex flex-wrap gap-2">
        {filters.map(({ value, label }) => (
          <button
            key={value}
            onClick={() => setActive(value)}
            className={`rounded-full px-4 py-1.5 text-sm font-medium transition-colors ${
              active === value
                ? 'bg-green-500 text-black'
                : 'border border-zinc-700 text-zinc-400 hover:border-zinc-500 hover:text-white'
            }`}
          >
            {label}
          </button>
        ))}
        <span className="ml-auto self-center text-sm text-zinc-500">
          {visible.length} {visible.length === 1 ? 'obra' : 'obras'}
        </span>
      </div>

      {visible.length === 0 ? (
        <div className="py-20 text-center text-zinc-500">
          No hay obras para este filtro todavía.
        </div>
      ) : (
        <div className="grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
          {visible.map(artwork => (
            <ArtCard key={artwork.id} artwork={artwork} />
          ))}
        </div>
      )}
    </div>
  );
}
