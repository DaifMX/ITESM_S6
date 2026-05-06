import { Link, useLocation } from 'react-router-dom';

const links = [
  { to: '/', label: 'Inicio' },
  { to: '/galeria', label: 'Galería' },
  { to: '/reportar', label: 'Reportar' },
];

export default function Navbar() {
  const { pathname } = useLocation();

  return (
    <nav className="sticky top-0 z-50 border-b border-zinc-800 bg-zinc-950/90 backdrop-blur-sm">
      <div className="mx-auto flex max-w-6xl items-center justify-between px-6 py-4">
        <Link to="/" className="flex items-center gap-2">
          <span className="text-2xl">⚽</span>
          <span className="font-display text-xl font-bold tracking-tight text-white">
            CANCHA<span className="text-green-400">VIVA</span>
          </span>
        </Link>

        <div className="flex items-center gap-1">
          {links.map(({ to, label }) => (
            <Link
              key={to}
              to={to}
              className={`rounded-lg px-4 py-2 text-sm font-medium transition-colors ${
                pathname === to
                  ? 'bg-green-500/10 text-green-400'
                  : 'text-zinc-400 hover:bg-zinc-800 hover:text-white'
              }`}
            >
              {label}
            </Link>
          ))}
          <Link
            to="/reportar"
            className="ml-3 rounded-lg bg-green-500 px-4 py-2 text-sm font-semibold text-black transition-colors hover:bg-green-400"
          >
            + Reportar incidente
          </Link>
        </div>
      </div>
    </nav>
  );
}
