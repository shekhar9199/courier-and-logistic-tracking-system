import { useCallback, useEffect, useMemo, useState } from 'react';
import { Activity, Box, Building2, CreditCard, LayoutDashboard, MapPin, Menu, PackageCheck, Plus, Search, Truck, Users, Warehouse, X, Bell, ArrowRight, LoaderCircle, Check, Settings2, ChevronDown } from 'lucide-react';
import { api, STATUSES, formatDate, statusLabel, initials } from './api.js';
import { Dashboard, Records, Tracking, Modal } from './Views.jsx';
import './styles.css';

const nav = [
  { group: 'Workspace', items: [['dashboard', 'Overview', LayoutDashboard], ['shipments', 'Shipments', Box], ['tracking', 'Tracking', MapPin]] },
  { group: 'Management', items: [['customers', 'Customers', Users], ['agents', 'Delivery agents', Truck], ['warehouses', 'Warehouses', Warehouse], ['payments', 'Payments', CreditCard]] },
];
const endpoints = { shipments: '/shipment/getAll', customers: '/customer/getAll', agents: '/deliveryAgent/getAll', warehouses: '/warehouse/getAll', payments: '/payment/getAll', tracking: '/trackingHistory/getAll' };

export default function App() {
  const [page, setPage] = useState('dashboard');
  const [data, setData] = useState({ shipments: [], customers: [], agents: [], warehouses: [], payments: [], tracking: [] });
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState('');
  const [error, setError] = useState('');
  const [notice, setNotice] = useState('');
  const [modal, setModal] = useState('');
  const [busy, setBusy] = useState(false);
  const [mobileNav, setMobileNav] = useState(false);

  const refresh = useCallback(async () => {
    setLoading(true);
    const entries = await Promise.all(Object.entries(endpoints).map(async ([key, url]) => {
      try { return [key, await api(url)]; }
      catch (e) { setError(`${e.message}. Is the backend running on localhost:8080?`); return [key, []]; }
    }));
    setData(Object.fromEntries(entries.map(([key, value]) => [key, Array.isArray(value) ? value : []])));
    setLoading(false);
  }, []);
  useEffect(() => { refresh(); }, [refresh]);
  useEffect(() => { if (!notice) return; const id = setTimeout(() => setNotice(''), 3500); return () => clearTimeout(id); }, [notice]);

  const counts = useMemo(() => ({
    total: data.shipments.length,
    moving: data.shipments.filter((s) => ['PICKED_UP', 'IN_TRANSIT', 'OUT_FOR_DELIVERY'].includes(s.status)).length,
    delivered: data.shipments.filter((s) => s.status === 'DELIVERED').length,
    pending: data.payments.filter((p) => p.paymentStatus === 'PENDING').length,
  }), [data]);

  async function changeStatus(shipment, status) {
    setBusy(true);
    try { await api(`/shipment/updateStatus/${shipment.id}?status=${status}`, { method: 'PUT' }); setNotice(`Shipment ${shipment.trackingNumber} updated.`); await refresh(); }
    catch (e) { setError(e.message); }
    finally { setBusy(false); }
  }
  async function save(kind, body) {
    setBusy(true);
    const route = { customer: ['/customer/save', 'POST'], agent: ['/deliveryAgent/saveAll', 'POST', true], warehouse: ['/warehouse/create', 'POST'], payment: ['/payment/create', 'POST'], shipment: ['/shipment/create', 'POST'] }[kind];
    try { await api(route[0], { method: route[1], body: JSON.stringify(route[2] ? [body] : body) }); setModal(''); setNotice(`${kind[0].toUpperCase()}${kind.slice(1)} saved.`); await refresh(); }
    catch (e) { setError(e.message); }
    finally { setBusy(false); }
  }
  const go = (value) => { setPage(value); setSearch(''); setMobileNav(false); setError(''); };
  const title = page === 'dashboard' ? 'Overview' : page === 'tracking' ? 'Tracking' : ({ shipments: 'Shipments', customers: 'Customers', agents: 'Delivery agents', warehouses: 'Warehouses', payments: 'Payments' })[page];

  return <div className="shell">
    <aside className={`sidebar ${mobileNav ? 'open' : ''}`}>
      <div className="brand"><span className="brand-icon"><PackageCheck size={20} /></span>parcel<span>pilot</span><button className="close-nav" onClick={() => setMobileNav(false)}><X size={18} /></button></div>
      <button className="workspace"><span className="workspace-icon">N</span><span><b>Northstar Logistics</b><small>Operations team</small></span><ChevronDown size={15} /></button>
      {nav.map((group) => <section className="nav-group" key={group.group}><p>{group.group}</p>{group.items.map(([id, label, Icon]) => <button className={`nav-link ${page === id ? 'selected' : ''}`} key={id} onClick={() => go(id)}><Icon size={18} /><span>{label}</span>{id === 'shipments' && <i>Live</i>}</button>)}</section>)}
      <div className="sidebar-spacer" /><div className="support"><b>Need a hand?</b><p>Our support team is here to help you move.</p><button onClick={() => setNotice('Contact your workspace administrator for support.')}>Visit help center <ArrowRight size={14} /></button></div>
      <div className="user-profile"><span>JD</span><div><b>Jordan Davis</b><small>Workspace admin</small></div><Settings2 size={17} /></div>
    </aside>
    {mobileNav && <button className="scrim" aria-label="Close navigation" onClick={() => setMobileNav(false)} />}
    <main className="main"><header className="topbar"><button className="mobile-menu" onClick={() => setMobileNav(true)}><Menu size={19} /></button><div className="crumb">Workspace <b>/</b> <strong>{title}</strong></div><div className="top-actions"><span className="healthy"><i /> All systems operational</span><button className="bell" onClick={() => setNotice('You’re all caught up.')}><Bell size={18} /></button><span className="top-user">JD</span></div></header>
      <div className="content">{error && <div className="alert">{error}<button onClick={() => setError('')}><X size={16} /></button></div>}{loading && <div className="sync"><LoaderCircle size={15} className="spin" /> Syncing data…</div>}
        {page === 'dashboard' && <Dashboard data={data} counts={counts} onPage={go} onNew={() => setModal('shipment')} onStatus={changeStatus} busy={busy} />}
        {page === 'tracking' && <Tracking shipments={data.shipments} tracking={data.tracking} />}
        {page !== 'dashboard' && page !== 'tracking' && <Records page={page} rows={data[page].filter((row) => !search || JSON.stringify(row).toLowerCase().includes(search.toLowerCase()))} search={search} setSearch={setSearch} onNew={() => setModal(({ agents: 'agent', customers: 'customer', warehouses: 'warehouse', payments: 'payment', shipments: 'shipment' })[page])} onStatus={changeStatus} busy={busy} />}
      </div>
    </main>
    {notice && <div className="toast"><Check size={16} />{notice}<button onClick={() => setNotice('')}><X size={15} /></button></div>}
    {modal && <Modal kind={modal} data={data} busy={busy} onClose={() => setModal('')} onSave={save} />}
  </div>;
}
