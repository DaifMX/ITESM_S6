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
  gradient: string;
  prompt: string;
  createdAt: string;
  incidentType: IncidentType;
  location: string;
}
