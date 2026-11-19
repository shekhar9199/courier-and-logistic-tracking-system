export const API_ROOT = import.meta.env.VITE_API_URL || '/api';
export const STATUSES = ['BOOKED', 'PICKED_UP', 'IN_TRANSIT', 'ARRIVED_AT_WAREHOUSE', 'OUT_FOR_DELIVERY', 'DELIVERED', 'CANCELLED', 'FAILED_DELIVERY'];
export const METHODS = ['CASH', 'UPI', 'CREDIT_CARD', 'DEBIT_CARD', 'NET_BANKING'];
export const TYPES = ['DOCUMENT', 'ELECTRONICS', 'CLOTHING', 'FOOD', 'FRAGILE', 'OTHER'];

export async function api(path, options = {}) {
  const response = await fetch(`${API_ROOT}${path}`, { ...options, headers: { ...(options.body ? { 'Content-Type': 'application/json' } : {}), ...options.headers } });
  const text = await response.text();
  let body;
  try { body = text ? JSON.parse(text) : null; } catch { body = text; }
  if (!response.ok) throw new Error(body?.message || body?.data || (typeof body === 'string' ? body : `Request failed (${response.status})`));
  return body;
}
export const formatDate = (value) => {
  if (!value) return '—';
  const date = new Date(value);
  return Number.isNaN(date.getTime()) ? value : new Intl.DateTimeFormat('en', { month: 'short', day: 'numeric', year: 'numeric' }).format(date);
};
export const statusLabel = (value = 'UNKNOWN') => value.replaceAll('_', ' ').toLowerCase().replace(/\b\w/g, (letter) => letter.toUpperCase());
export const initials = (value = '?') => value.split(/\s+/).filter(Boolean).slice(0, 2).map((part) => part[0]).join('').toUpperCase();
