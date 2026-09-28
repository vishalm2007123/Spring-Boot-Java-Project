const api = '/api';
const state = { drives: [], volunteers: [], trees: [], dueTrees: [], leaderboard: [], driveRates: [], speciesRates: [], treeSearch: '' };

const $ = (selector) => document.querySelector(selector);
const formatDate = (value) => value ? new Date(`${value}T00:00:00`).toLocaleDateString(undefined, { day: '2-digit', month: 'short', year: 'numeric' }) : '-';

async function request(path, options = {}) {
    const response = await fetch(`${api}${path}`, {
        headers: { 'Content-Type': 'application/json', ...(options.headers || {}) },
        ...options
    });
    const text = await response.text();
    let body = null;
    try { body = text ? JSON.parse(text) : null; } catch (_) { body = text; }
    if (!response.ok) throw new Error(body?.message || `Request failed with status ${response.status}`);
    return body;
}

async function loadDashboard() {
    try {
        const [drives, volunteers, trees, dueTrees, leaderboard] = await Promise.all([
            request('/drives'), request('/volunteers'), request('/trees'), request('/trees/due-checkins'), request('/volunteers/leaderboard')
        ]);
        const driveRates = await Promise.all(drives.map(async drive => ({ drive, rate: await request(`/drives/${drive.id}/survival-rate`) })));
        const species = [...new Set(trees.map(tree => tree.species).filter(Boolean))];
        const speciesRates = await Promise.all(species.map(async name => ({ name, rate: await request(`/trees/survival-rate/species/${encodeURIComponent(name)}`) })));
        Object.assign(state, { drives, volunteers, trees, dueTrees, leaderboard, driveRates, speciesRates });
        renderDashboard();
    } catch (error) {
        showMessage(`Could not load dashboard: ${error.message}`, true);
    }
}

function renderDashboard() {
    const alive = state.trees.filter(tree => tree.status?.toUpperCase() !== 'DEAD').length;
    const survival = state.trees.length ? Math.round(alive * 10000 / state.trees.length) / 100 : 0;
    $('#metric-trees').textContent = state.trees.length;
    $('#metric-survival').textContent = `${survival}%`;
    $('#metric-drives').textContent = state.drives.length;
    $('#metric-volunteers').textContent = state.volunteers.length;
    $('#hero-alive').textContent = alive;
    $('#due-count').textContent = state.dueTrees.length;
    renderDueTrees(); renderLeaderboard(); renderTrees(); populateSelects(); renderInsights();
}

function renderInsights() {
    const renderRate = item => `<div class="insight-item"><div><strong>${escapeHtml(item.label)}</strong><small>${item.rate.aliveTrees} alive of ${item.rate.totalTrees} trees</small></div><span class="insight-rate">${item.rate.survivalRate}%</span></div>`;
    $('#drive-insights').innerHTML = state.driveRates.length ? state.driveRates.map(item => renderRate({ label: item.drive.name, rate: item.rate })).join('') : '<div class="empty-state">Add a tree to see progress here.</div>';
    $('#species-insights').innerHTML = state.speciesRates.length ? state.speciesRates.map(item => renderRate(item)).join('') : '<div class="empty-state">Add a tree to see progress here.</div>';
}

function renderDueTrees() {
    const list = $('#due-list');
    if (!state.dueTrees.length) { list.innerHTML = '<div class="empty-state">No trees are due today.</div>'; return; }
    list.innerHTML = state.dueTrees.map(tree => `<div class="record-item"><div><strong>${escapeHtml(tree.species)}</strong><small>${escapeHtml(tree.location)}</small></div><span class="record-date">${formatDate(tree.nextCheckInDate)}</span></div>`).join('');
}

function renderLeaderboard() {
    const list = $('#leaderboard');
    if (!state.leaderboard.length) { list.innerHTML = '<div class="empty-state">No volunteers recorded yet.</div>'; return; }
    list.innerHTML = state.leaderboard.slice(0, 5).map((person, index) => `<div class="leader-item"><span class="leader-rank">0${index + 1}</span><div class="leader-main"><strong>${escapeHtml(person.volunteerName)}</strong><small>Volunteer</small></div><span class="leader-count">${person.treesPlanted}</span></div>`).join('');
}

function renderTrees() {
    const body = $('#trees-table');
    if (!state.trees.length) { body.innerHTML = '<tr><td colspan="5" class="empty-state">No trees recorded yet.</td></tr>'; return; }
    const search = state.treeSearch.toLowerCase();
    const visibleTrees = state.trees.filter(tree => `${tree.species} ${tree.location}`.toLowerCase().includes(search));
    body.innerHTML = visibleTrees.slice().reverse().slice(0, 8).map(tree => {
        const drive = state.drives.find(item => item.id === tree.plantationDriveId);
        const status = (tree.status || 'ALIVE').toUpperCase();
        return `<tr><td>${escapeHtml(tree.species)}</td><td>${escapeHtml(tree.location)}</td><td>${escapeHtml(drive?.name || `Drive #${tree.plantationDriveId}`)}</td><td>${formatDate(tree.datePlanted)}</td><td><span class="status status-${status.toLowerCase()}">${status}</span></td></tr>`;
    }).join('');
}

function populateSelects() {
    $('#drive-select').innerHTML = '<option value="">Select a drive</option>' + state.drives.map(drive => `<option value="${drive.id}">${escapeHtml(drive.name)}</option>`).join('');
    $('#volunteer-select').innerHTML = '<option value="">Optional volunteer</option>' + state.volunteers.map(person => `<option value="${person.id}">${escapeHtml(person.name)}</option>`).join('');
    $('#tree-select').innerHTML = '<option value="">Select a tree</option>' + state.trees.filter(tree => tree.status?.toUpperCase() !== 'DEAD').map(tree => `<option value="${tree.id}">${escapeHtml(tree.species)} - ${escapeHtml(tree.location)}</option>`).join('');
}

function formPayload(form) {
    const data = Object.fromEntries(new FormData(form).entries());
    Object.keys(data).forEach(key => { if (data[key] === '') delete data[key]; });
    ['plantationDriveId', 'volunteerId', 'treeId'].forEach(key => { if (data[key]) data[key] = Number(data[key]); });
    return data;
}

function setupForms() {
    document.querySelectorAll('.form-tab').forEach(tab => tab.addEventListener('click', () => {
        document.querySelectorAll('.form-tab').forEach(item => item.classList.remove('active'));
        document.querySelectorAll('.record-form').forEach(form => form.classList.remove('active-form'));
        tab.classList.add('active'); $(`#${tab.dataset.form}`).classList.add('active-form');
    }));
    const routes = { 'drive-form': '/drives', 'volunteer-form': '/volunteers', 'tree-form': '/trees', 'checkin-form': '/checkins' };
    Object.entries(routes).forEach(([formId, path]) => $(`#${formId}`).addEventListener('submit', async event => {
        event.preventDefault();
        const form = event.currentTarget;
        const button = form.querySelector('button[type="submit"]');
        button.disabled = true;
        try { await request(path, { method: 'POST', body: JSON.stringify(formPayload(form)) }); form.reset(); showMessage('Saved successfully. Your database is up to date.'); await loadDashboard(); }
        catch (error) { showMessage(error.message, true); }
        finally { button.disabled = false; }
    }));
}

function showMessage(message, error = false) { const element = $('#form-message'); element.textContent = message; element.classList.toggle('error', error); }
function escapeHtml(value) { return String(value ?? '').replace(/[&<>'"]/g, character => ({ '&':'&amp;', '<':'&lt;', '>':'&gt;', "'":'&#39;', '"':'&quot;' }[character])); }

$('#today-label').textContent = new Date().toLocaleDateString(undefined, { weekday: 'short', day: '2-digit', month: 'short' });
$('#refresh-button').addEventListener('click', loadDashboard);
$('#tree-search').addEventListener('input', event => { state.treeSearch = event.target.value; renderTrees(); });
setupForms(); loadDashboard();
