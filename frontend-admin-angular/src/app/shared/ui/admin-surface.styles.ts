export const ADMIN_SURFACE_STYLES = `
  .admin-grid {
    display: grid;
    gap: 1rem;
  }

  .admin-grid--split {
    grid-template-columns: minmax(320px, 0.95fr) minmax(420px, 1.05fr);
  }

  .surface-card {
    padding: 1.25rem;
    border-radius: 4px;
    background: linear-gradient(180deg, rgba(255, 255, 255, 0.94), rgba(255, 249, 244, 0.94));
    border: 1px solid #eaded4;
    box-shadow: 0 18px 45px rgba(92, 60, 42, 0.08);
  }

  .surface-card--tinted {
    background:
      linear-gradient(180deg, rgba(255, 247, 241, 0.98), rgba(255, 251, 248, 0.96)),
      radial-gradient(circle at top left, rgba(201, 110, 74, 0.08), transparent 30%);
  }

  .surface-header {
    display: grid;
    gap: 0.55rem;
    margin-bottom: 1rem;
  }

  .surface-kicker {
    display: inline-flex;
    align-items: center;
    gap: 0.55rem;
    margin: 0;
    color: #8a5c46;
    text-transform: uppercase;
    letter-spacing: 0.18em;
    font-size: 0.72rem;
  }

  .surface-title-row {
    display: flex;
    align-items: center;
    gap: 0.75rem;
  }

  .surface-title-row img {
    width: 28px;
    height: 28px;
    padding: 0.35rem;
    border-radius: 2px;
    background: rgba(255, 235, 225, 0.9);
  }

  .surface-title-row h3 {
    margin: 0;
    font: 700 1.25rem/1.1 var(--font-display, "Cormorant Garamond", Georgia, serif);
    color: #2d201a;
  }

  .surface-copy,
  .surface-meta,
  .table-note,
  .card-copy {
    margin: 0;
    color: #74645b;
    line-height: 1.6;
  }

  .surface-form {
    display: grid;
    gap: 0.9rem;
  }

  .surface-form label {
    display: grid;
    gap: 0.4rem;
    color: #5a473f;
    font-weight: 600;
  }

  .surface-row {
    display: grid;
    gap: 0.8rem;
  }

  .surface-row--2 {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .surface-row--3 {
    grid-template-columns: 2fr 1fr 1fr;
  }

  .surface-form input,
  .surface-form textarea,
  .surface-form select {
    width: 100%;
    border: 1px solid #dccdc2;
    border-radius: 2px;
    padding: 0.85rem 0.95rem;
    background: #fffdfb;
    color: #332821;
    font: inherit;
  }

  .surface-form textarea {
    resize: vertical;
  }

  .surface-button,
  .mini-button {
    border: 0;
    border-radius: 2px;
    cursor: pointer;
    font-weight: 700;
  }

  .surface-button {
    padding: 0.9rem 1rem;
    background: linear-gradient(135deg, #4f2519, #2f1912);
    color: white;
  }

  .surface-button:disabled {
    opacity: 0.6;
    cursor: not-allowed;
  }

  .mini-button {
    padding: 0.45rem 0.75rem;
    background: #efe1d7;
    color: #6e3420;
    font-size: 0.78rem;
  }

  .surface-table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 1rem;
  }

  .surface-table th,
  .surface-table td {
    text-align: left;
    padding: 0.9rem;
    border-top: 1px solid #f0e8e2;
    vertical-align: top;
  }

  .surface-table th {
    color: #6f5f57;
    font-size: 0.8rem;
    text-transform: uppercase;
    letter-spacing: 0.08em;
  }

  .status-pill {
    display: inline-flex;
    align-items: center;
    padding: 0.3rem 0.65rem;
    border-radius: 2px;
    background: #f3e0d6;
    color: #733b2a;
    font-size: 0.78rem;
    font-weight: 700;
  }

  .chip-row,
  .action-row {
    display: flex;
    flex-wrap: wrap;
    gap: 0.5rem;
  }

  .page-toolbar,
  .pager {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    justify-content: space-between;
    gap: 0.85rem;
  }

  .page-toolbar {
    margin-top: 1rem;
  }

  .pager {
    margin-top: 1.1rem;
    padding-top: 0.9rem;
    border-top: 1px solid #f0e4da;
  }

  .pager__meta {
    color: #7b6b62;
    font-size: 0.84rem;
  }

  .pager__controls {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    gap: 0.55rem;
  }

  .pager__size {
    display: inline-flex;
    align-items: center;
    gap: 0.45rem;
    color: #6a564c;
    font-size: 0.82rem;
  }

  .pager__size select {
    border: 1px solid #dccdc2;
    background: #fffdfb;
    padding: 0.35rem 0.55rem;
    color: #35271f;
    font: inherit;
  }

  .summary-chip {
    display: inline-flex;
    align-items: center;
    gap: 0.45rem;
    padding: 0.45rem 0.75rem;
    border-radius: 2px;
    background: #fff3ec;
    color: #7a4a37;
    font-size: 0.8rem;
    font-weight: 600;
  }

  .summary-chip img {
    width: 18px;
    height: 18px;
  }

  .empty-state {
    margin-top: 1rem;
    padding: 1rem 1.1rem;
    border-radius: 4px;
    background: #fff8f3;
    border: 1px dashed #e6d4c8;
    color: #7f746d;
  }

  .cards-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
    gap: 1rem;
    margin-top: 1rem;
  }

  .cards-grid article {
    padding: 1rem;
    border-radius: 4px;
    background: #fff9f4;
    border: 1px solid #f0dfd4;
  }

  .cards-grid article img {
    width: 30px;
    height: 30px;
    padding: 0.35rem;
    border-radius: 2px;
    background: rgba(255, 232, 220, 0.9);
  }

  .cards-grid article h4 {
    margin: 0.7rem 0 0.4rem;
    color: #34231c;
  }

  @media (max-width: 960px) {
    .admin-grid--split,
    .surface-row--2,
    .surface-row--3 {
      grid-template-columns: 1fr;
    }
  }
`;
