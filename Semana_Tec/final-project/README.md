# Cancha Viva

Plataforma de activismo digital que transforma reportes de racismo en el fútbol en obras de arte generativo. Cada denuncia activa un flujo automatizado que produce una pieza única publicada en una galería pública.

## ¿Cómo funciona?

```
Reporte del usuario → Webhook n8n → Ollama analiza y genera prompt → Imagen generativa → Galería pública
```

1. El usuario describe un incidente racista (en la cancha, en las gradas o en redes sociales)
2. El formulario envía el reporte vía webhook a **n8n**
3. **Ollama** (IA local) lee el contexto y escribe un prompt artístico
4. Un modelo de imagen genera una obra digital única
5. La obra aparece en la galería pública de Cancha Viva

## Stack

| Capa | Tecnología |
|---|---|
| Frontend | React 19 + TypeScript + Vite |
| Estilos | Tailwind CSS |
| Ruteo | React Router DOM v7 |
| Automatización | n8n (webhook) |
| IA local | Ollama |

## Páginas

- `/` — Hero, estadísticas y explicación del flujo
- `/reportar` — Formulario para reportar un incidente
- `/galeria` — Galería de obras generadas, con filtros por tipo de incidente

## Configuración

Copia `.env.example` a `.env` y configura la URL del webhook de n8n:

```env
VITE_N8N_WEBHOOK_URL=http://localhost:5678/webhook/cancha-viva
```

## Desarrollo

```bash
npm install
npm run dev
```

```bash
npm run build   # producción
npm run preview # vista previa del build
```
