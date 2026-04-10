/**
 * main.js — Shared utilities for all e-Hotels frontend pages.
 *
 * Provides: API calls, session management, navbar rendering,
 * and formatting / display helpers.
 */

"use strict";

/* ---------- Configuration ---------- */

const API_BASE = "http://localhost:8080";

/* ---------- API ---------- */

/**
 * Sends an HTTP request to the backend and returns the parsed JSON response.
 * Every backend endpoint returns { success, message, data?, errors? }.
 */
async function apiFetch(method, path, body) {
  const options = {
    method: method,
    headers: { "Content-Type": "application/json" },
  };

  if (body !== undefined) {
    options.body = JSON.stringify(body);
  }

  try {
    const res = await fetch(API_BASE + path, options);
    return await res.json();
  } catch (err) {
    console.error("API call failed:", method, path, err);
    return { success: false, message: "Impossible de joindre le serveur." };
  }
}

/* ---------- Client session (localStorage) ---------- */

const SESSION_KEY = "ehotel_client";

function getClient() {
  try {
    return JSON.parse(localStorage.getItem(SESSION_KEY));
  } catch {
    return null;
  }
}

function saveClient(data) {
  localStorage.setItem(SESSION_KEY, JSON.stringify(data));
}

function clearSession() {
  localStorage.removeItem(SESSION_KEY);
}

function isLoggedIn() {
  return getClient() !== null;
}

/**
 * Redirects to the login page when the user is not authenticated.
 * Call at the top of every /client/ page init function.
 * Returns true if the user IS logged in so the caller can proceed.
 */
function requireAuth() {
  if (!isLoggedIn()) {
    window.location.href = getPrefix() + "client-login.html";
    return false;
  }
  return true;
}

/* ---------- Path prefix ---------- */

/**
 * Relative path from the current HTML file to the frontend root
 * (where index.html, css/, js/ live).
 */
function getPrefix() {
  const path = window.location.pathname;
  if (path.includes("/employee/admin/")) return "../../";
  if (path.includes("/employee/")) return "../";
  if (path.includes("/client/")) return "../";
  return "";
}

/** Alias for employee portal pages (same as getPrefix). */
function getEmployeeAssetPrefix() {
  return getPrefix();
}

/* ---------- Employee session (localStorage) ---------- */

const EMPLOYEE_SESSION_KEY = "ehotel_employee";

function getEmployee() {
  try {
    return JSON.parse(localStorage.getItem(EMPLOYEE_SESSION_KEY));
  } catch {
    return null;
  }
}

function saveEmployee(data) {
  localStorage.setItem(EMPLOYEE_SESSION_KEY, JSON.stringify(data));
}

function clearEmployeeSession() {
  localStorage.removeItem(EMPLOYEE_SESSION_KEY);
}

function isEmployeeLoggedIn() {
  return getEmployee() !== null;
}

function isManager() {
  const e = getEmployee();
  return e && e.role === "GESTIONNAIRE";
}

/**
 * Redirects to employee login if not authenticated as employee.
 * @returns {boolean}
 */
function requireEmployeeAuth() {
  if (!isEmployeeLoggedIn()) {
    window.location.href = getPrefix() + "employee-login.html";
    return false;
  }
  return true;
}

/**
 * Redirects non-managers away from admin pages.
 * @returns {boolean}
 */
function requireManager() {
  if (!isManager()) {
    window.location.href = getPrefix() + "employee/dashboard.html";
    return false;
  }
  return true;
}

/* ---------- Navbar ---------- */

function renderNavbar() {
  const el = document.getElementById("navbar");
  if (!el) return;

  const p = getPrefix();
  const client = getClient();

  let right = "";
  if (client) {
    right =
      `<a href="${p}client/dashboard.html">Mon espace</a>` +
      `<a href="${p}client/reservations.html">Réservations</a>` +
      `<span style="color:#999;font-size:0.85rem;padding:0 8px">${esc(client.firstName || "Client")}</span>` +
      `<a href="#" id="btn-logout" class="btn btn-sm btn-outline">Déconnexion</a>`;
  } else {
    right =
      `<a href="${p}client-login.html" class="btn btn-sm btn-outline">Connexion</a>` +
      `<a href="${p}client-register.html" class="btn btn-sm btn-primary">Inscription</a>`;
  }

  const emp = getEmployee();
  let empNav = "";
  if (emp) {
    empNav =
      `<a href="${p}employee/dashboard.html">Portail employé</a>` +
      `<span style="color:#999;font-size:0.85rem;padding:0 8px">${esc(emp.firstName || "Employé")}</span>` +
      `<a href="#" id="btn-employee-logout" class="btn btn-sm btn-outline">Déconnexion employé</a>`;
  } else {
    empNav = `<a href="${p}employee-login.html">Employés</a>`;
  }

  el.innerHTML =
    `<nav><div class="container" style="display:flex;justify-content:space-between;align-items:center;flex-wrap:wrap;gap:10px">` +
    `<a href="${p}index.html" class="logo">e-Hotels</a>` +
    `<div class="nav-links">` +
    `<a href="${p}index.html">Accueil</a>` +
    `<a href="${p}hotels.html">Hôtels</a>` +
    `<a href="${p}room-search.html">Recherche</a>` +
    empNav +
    right +
    `</div></div></nav>`;

  const logoutBtn = document.getElementById("btn-logout");
  if (logoutBtn) {
    logoutBtn.addEventListener("click", function (e) {
      e.preventDefault();
      apiFetch("POST", "/client/logout").catch(function () {});
      clearSession();
      window.location.href = p + "index.html";
    });
  }

  const empLogout = document.getElementById("btn-employee-logout");
  if (empLogout) {
    empLogout.addEventListener("click", function (e) {
      e.preventDefault();
      clearEmployeeSession();
      window.location.href = p + "index.html";
    });
  }
}

/* ---------- Alerts ---------- */

function showAlert(type, message) {
  const box = document.getElementById("alert-box");
  if (!box) return;
  box.innerHTML = `<div class="alert alert-${type}">${esc(message)}</div>`;
}

function clearAlert() {
  const box = document.getElementById("alert-box");
  if (box) box.innerHTML = "";
}

/* ---------- Formatting helpers ---------- */

function loginClientExclusive(data) {
  clearEmployeeSession();
  saveClient(data);
}

function loginEmployeeExclusive(data) {
  clearSession();
  saveEmployee(data);
}

function formatDate(str) {
  if (!str) return "—";
  const d = new Date(str + "T00:00:00");
  return d.toLocaleDateString("fr-CA", {
    year: "numeric",
    month: "long",
    day: "numeric",
  });
}

function nightCount(startDate, endDate) {
  const ms = new Date(endDate) - new Date(startDate);
  return Math.max(0, Math.round(ms / 86400000));
}

function starsHTML(n) {
  return '<span class="stars">' + "★".repeat(n) + "☆".repeat(5 - n) + "</span>";
}

function badgeHTML(status) {
  return `<span class="badge badge-${status.toLowerCase()}">${status}</span>`;
}

function viewLabel(type) {
  const labels = { SEA: "Mer", MOUNTAIN: "Montagne", CITY: "Ville", NONE: "Aucune" };
  return labels[type] || type || "—";
}

function capacityLabel(cap) {
  const labels = { single: "Simple", double: "Double", triple: "Triple", suite: "Suite", family: "Familiale" };
  return labels[cap] || cap || "—";
}

/** Escape HTML special characters to prevent XSS. */
function esc(str) {
  const d = document.createElement("div");
  d.textContent = str == null ? "" : str;
  return d.innerHTML;
}

/** Read a query-string parameter by name. */
function getParam(key) {
  return new URLSearchParams(window.location.search).get(key);
}

/** Replace the contents of an element with a centered spinner. */
function showLoading(elementId) {
  const el = document.getElementById(elementId);
  if (el) {
    el.innerHTML =
      '<div class="loading"><div class="spinner"></div><p>Chargement…</p></div>';
  }
}

/* ---------- Boot ---------- */

document.addEventListener("DOMContentLoaded", renderNavbar);
