const ICONIFY_SYSTEM_PROMPT = `You are Iconify, a premium app icon generator. Create high-end, cohesive, front-facing app icons in a unified inflated 3D style.

CORE OUTPUT RULE: Generate one separate standalone square image per requested app or uploaded icon. Do not create a collage, grid, or grouped image unless explicitly requested.

INPUT INTERPRETATION: Treat uploaded app icons, logos, or screenshots as target sources. Preserve each source logo shape, symbol, color palette, and identity. If multiple target icons are uploaded, generate one separate image for each uploaded target icon. Do not ask for app names when the icons are visible.

STYLE: Premium inflated 3D app icon; soft air-filled rounded form; slightly puffy cushioned surface; high-end glossy finish; subtle tactile texture; smooth realistic highlights; gentle shadows; realistic dimensional depth; sharp HD rendering; clean square composition; centered, front-facing, straight-on view; no tilt, labels, watermark, phone mockup, scenery, or background clutter.

QUALITY: Mature, refined, cohesive icon-pack lighting and polish. Avoid childish cartoon, toy claymation, melted shapes, wrong colors, extra symbols, angled perspective, cheap plastic balloon gloss, captions, grids, or flat 2D output.`;

const generationDirection = (name, index) => `Create a single standalone square app icon based on ${name || `uploaded target icon ${index + 1}`}. Preserve the target icon's original symbol, silhouette, color palette, and core identity. Transform it into a premium inflated 3D icon with a soft air-filled rounded body, subtle tactile texture, smooth glossy highlights, realistic shadows, and refined dimensional depth. The icon must be centered, front-facing, straight-on, cleanly framed, high-definition, and professional. Do not tilt. Do not rotate. Do not add background clutter. Do not add text. Do not create a grid. Do not combine with other icons. Produce only one icon in this image.`;

const state = { uploads: [], projectName: '' };
const app = document.querySelector('#root');

const icons = {
  sparkles: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 3l1.9 5.8L20 11l-6.1 2.2L12 21l-1.9-7.8L4 11l6.1-2.2L12 3z"/><path d="M19 3v4M21 5h-4M5 17v3M6.5 18.5h-3"/></svg>',
  image: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="3" width="18" height="18" rx="3"/><circle cx="8.5" cy="8.5" r="1.5"/><path d="M21 15l-5-5L5 21"/></svg>',
  layers: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 2l9 5-9 5-9-5 9-5z"/><path d="M3 12l9 5 9-5"/><path d="M3 17l9 5 9-5"/></svg>',
  wand: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M15 4V2M15 16v-2M8 9H6M20 9h-2M17.8 6.2l1.4-1.4M10.8 13.2l-1.4 1.4"/><path d="M14 8l8 8-6 6-8-8 6-6z"/></svg>',
  download: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 3v12M7 10l5 5 5-5"/><path d="M5 21h14"/></svg>',
  x: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M18 6L6 18M6 6l12 12"/></svg>'
};

function render() {
  const prompts = (state.uploads.length ? state.uploads : [{ name: state.projectName || 'Icon 1' }]).map((file, index) => ({
    id: `${file.name}-${index}`,
    name: (file.name || `Icon ${index + 1}`).replace(/\.[^.]+$/, ''),
    text: generationDirection((file.name || state.projectName).replace?.(/\.[^.]+$/, '') || state.projectName, index)
  }));

  app.innerHTML = `
    <main class="app-shell">
      <section class="hero-panel">
        <nav class="nav"><div class="brand-mark">I</div><span>Iconify</span><a href="#studio">Studio</a></nav>
        <div class="hero-grid">
          <div class="hero-copy">
            <p class="eyebrow">${icons.sparkles} Premium icon transformation</p>
            <h1>Turn app logos into a cohesive inflated 3D icon pack.</h1>
            <p class="lede">Iconify preserves each uploaded logo's identity while translating it into a mature, glossy, front-facing, cushioned 3D app icon style—one standalone square image per source.</p>
            <div class="hero-actions"><button id="uploadHero" class="primary">${icons.image} Upload target icons</button><a class="secondary" href="#rules">View generation rules</a></div>
          </div>
          <div class="preview-stack" aria-label="Inflated 3D app icon style preview"><div class="preview-icon blue">${icons.layers}</div><div class="preview-icon pink">${icons.wand}</div><div class="preview-icon green">${icons.sparkles}</div></div>
        </div>
      </section>
      <section id="studio" class="studio-card">
        <div class="section-heading"><p class="eyebrow">Generation studio</p><h2>Upload icons, then generate separately.</h2><p>Every upload is treated as a target source, not a style reference. Iconify prepares one dedicated instruction set for each standalone output.</p></div>
        <input id="fileInput" class="visually-hidden" type="file" accept="image/*" multiple />
        <div id="dropZone" class="drop-zone">${icons.image}<strong>Drop app icons, logos, or screenshots here</strong><span>PNG, JPG, WEBP, SVG previews supported by your browser</span></div>
        ${state.uploads.length ? '' : `<label class="manual-name">No upload yet? Name a target app to draft a prompt.<input id="projectName" value="${escapeHtml(state.projectName)}" placeholder="e.g. Calendar, Notes, Music" /></label>`}
        <div class="upload-grid">${state.uploads.map((upload, index) => `<article class="upload-card"><button class="remove" data-index="${index}" aria-label="Remove ${escapeHtml(upload.name)}">${icons.x}</button><img src="${upload.url}" alt="${escapeHtml(upload.name)} target icon" /><span>${escapeHtml(upload.name)}</span></article>`).join('')}</div>
        <div class="prompt-list">${prompts.map((prompt, index) => `<article class="prompt-card"><div><span class="badge">Standalone image ${index + 1}</span><h3>${escapeHtml(prompt.name)}</h3><p>${escapeHtml(prompt.text)}</p></div><button class="copy" data-prompt="${escapeHtml(`${ICONIFY_SYSTEM_PROMPT}\n\n${prompt.text}`)}">${icons.download} Copy prompt</button></article>`).join('')}</div>
      </section>
      <section id="rules" class="rules-grid">${[['Separate outputs','One square image per uploaded icon by default. No grids or collages unless the user explicitly asks for one.'],['Brand preservation','Keep the source symbol, silhouette, color pattern, and recognizable app identity intact.'],['Premium 3D style','Soft inflated surfaces, subtle texture, glossy highlights, realistic shadows, and straight-on framing.'],['Batch cohesion','Shared lighting, puffiness, polish, framing, and mature high-end finish across every generated icon.']].map(([title, body]) => `<article><h3>${title}</h3><p>${body}</p></article>`).join('')}</section>
    </main>`;

  bindEvents();
}

function bindEvents() {
  const input = document.querySelector('#fileInput');
  document.querySelector('#uploadHero').addEventListener('click', () => input.click());
  input.addEventListener('change', (event) => addFiles(event.target.files));
  const dropZone = document.querySelector('#dropZone');
  dropZone.addEventListener('click', () => input.click());
  dropZone.addEventListener('dragover', (event) => event.preventDefault());
  dropZone.addEventListener('drop', (event) => { event.preventDefault(); addFiles(event.dataTransfer.files); });
  document.querySelector('#projectName')?.addEventListener('input', (event) => { state.projectName = event.target.value; render(); });
  document.querySelectorAll('.remove').forEach((button) => button.addEventListener('click', () => { state.uploads.splice(Number(button.dataset.index), 1); render(); }));
  document.querySelectorAll('.copy').forEach((button) => button.addEventListener('click', async () => { await navigator.clipboard.writeText(button.dataset.prompt); button.textContent = 'Copied'; }));
}

function addFiles(files) {
  const next = Array.from(files || []).filter((file) => file.type.startsWith('image/')).map((file) => ({ file, name: file.name, url: URL.createObjectURL(file) }));
  state.uploads.push(...next);
  render();
}

function escapeHtml(value) {
  return String(value ?? '').replace(/[&<>'"]/g, (char) => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', "'": '&#39;', '"': '&quot;' })[char]);
}

render();
