export type IncidentType = 'cancha' | 'gradas' | 'redes_sociales';

export interface Incident {
  type: IncidentType;
  description: string;
  location: string;
  reportedAt: string;
  reporterName?: string;
}

export interface Artwork {
  id: string;
  gradient?: string;
  imagen_base64?: string | null;
  imagen_url?: string | null;
  resumen_publico?: string;
  prompt: string;
  createdAt: string;
  incidentType: IncidentType | 'sin_clasificar';
  location: string;
}
