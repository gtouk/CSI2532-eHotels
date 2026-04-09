/**
 * client.js — Page-specific logic for the e-Hotels client frontend.
 *
 * Each HTML page sets data-page="..." on <body>.
 * On DOMContentLoaded we look up the matching init function and call it.
 */

"use strict";

document.addEventListener("DOMContentLoaded", function () {
  const page = document.body.dataset.page;

  const pages = {
    "hotel-list":                   initHotelList,
    "hotel-details":                initHotelDetails,
    "room-search":                  initRoomSearch,
    "room-details":                 initRoomDetails,
    "client-login":                 initLogin,
    "client-register":              initRegister,
    "client-dashboard":             initDashboard,
    "client-profile":               initProfile,
    "client-reservations":          initReservations,
    "client-reservation-details":   initReservationDetails,
    "client-new-reservation":       initNewReservation,
  };

  if (pages[page]) pages[page]();
});

/* ====================================================================
   PUBLIC — Hotel list
   ==================================================================== */

async function initHotelList() {
  const container = document.getElementById("hotel-grid");
  showLoading("hotel-grid");

  const res = await apiFetch("GET", "/hotels");
  const hotels = res.success ? res.data : [];

  if (!hotels || hotels.length === 0) {
    container.innerHTML = emptyMsg("Aucun hôtel disponible pour le moment.");
    return;
  }

  let html = "";
  for (const h of hotels) {
    html +=
      `<a href="hotel-details.html?id=${h.hotelId}" class="card" style="text-decoration:none;color:inherit;display:block">` +
        `<h3>${esc(h.name)} ${starsHTML(h.category)}</h3>` +
        `<p style="color:#777">${esc(h.city || "")}${h.country ? ", " + esc(h.country) : ""}</p>` +
        `<p style="font-size:0.85rem;color:#999">${esc(h.address || "")}</p>` +
      `</a>`;
  }
  container.innerHTML = `<div class="card-grid">${html}</div>`;
}

/* ====================================================================
   PUBLIC — Hotel details
   ==================================================================== */

async function initHotelDetails() {
  const hotelId = getParam("id");
  if (!hotelId) return;

  const el = document.getElementById("hotel-content");
  showLoading("hotel-content");

  const res = await apiFetch("GET", "/hotels/" + hotelId);
  if (!res.success || !res.data) {
    el.innerHTML = emptyMsg("Hôtel introuvable.");
    return;
  }

  const h = res.data;
  el.innerHTML =
    `<div class="breadcrumb"><a href="hotels.html">Hôtels</a> / ${esc(h.name)}</div>` +
    `<h1>${esc(h.name)} ${starsHTML(h.category)}</h1>` +
    `<p style="color:#777;margin-bottom:24px">${esc(h.address || "")} — ${esc(h.city || "")}, ${esc(h.country || "")}</p>` +
    `<div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:14px">` +
      `<h2>Chambres</h2>` +
      `<a href="room-search.html?hotelId=${h.hotelId}" class="btn btn-sm btn-outline">Rechercher</a>` +
    `</div>` +
    `<div id="hotel-rooms"><div class="loading"><div class="spinner"></div></div></div>`;

  // Load rooms for this hotel
  const roomRes = await apiFetch("GET", "/rooms/search?hotelId=" + hotelId);
  const rooms = roomRes.success ? roomRes.data : [];
  const roomsEl = document.getElementById("hotel-rooms");

  if (!rooms || rooms.length === 0) {
    roomsEl.innerHTML = emptyMsg("Aucune chambre trouvée pour cet hôtel.");
  } else {
    roomsEl.innerHTML = `<div class="card-grid">${rooms.map(roomCardHTML).join("")}</div>`;
  }
}

/* ====================================================================
   PUBLIC — Room search
   ==================================================================== */

async function initRoomSearch() {
  const form = document.getElementById("search-form");
  const resultsEl = document.getElementById("search-results");

  // Populate the chain dropdown from the API
  const chainSelect = document.getElementById("filter-chain");
  if (chainSelect) {
    const chainsRes = await apiFetch("GET", "/chains");
    if (chainsRes.success && chainsRes.data) {
      for (const c of chainsRes.data) {
        const opt = document.createElement("option");
        opt.value = c.chainId;
        opt.textContent = c.name;
        chainSelect.appendChild(opt);
      }
    }
  }

  // Pre-fill hotelId from query string if present
  const hotelId = getParam("hotelId");

  form.addEventListener("submit", async function (e) {
    e.preventDefault();
    showLoading("search-results");

    const params = new URLSearchParams();
    const filterNames = ["city", "startDate", "endDate", "capacity", "maxPrice", "category", "chainId"];
    for (const name of filterNames) {
      const input = form.elements[name];
      if (input && input.value) params.set(name, input.value);
    }
    if (hotelId) params.set("hotelId", hotelId);

    const res = await apiFetch("GET", "/rooms/search?" + params.toString());
    const rooms = res.success ? res.data : [];

    if (!rooms || rooms.length === 0) {
      resultsEl.innerHTML = emptyMsg("Aucune chambre ne correspond à vos critères.");
      return;
    }

    resultsEl.innerHTML =
      `<p style="color:#777;margin-bottom:12px">${rooms.length} chambre(s) trouvée(s)</p>` +
      `<div class="card-grid">${rooms.map(roomCardHTML).join("")}</div>`;
  });

  if (hotelId) form.requestSubmit();
}

/* ====================================================================
   PUBLIC — Room details
   ==================================================================== */

async function initRoomDetails() {
  const roomId = getParam("id");
  if (!roomId) return;

  const el = document.getElementById("room-content");
  showLoading("room-content");

  const res = await apiFetch("GET", "/rooms/" + roomId);
  if (!res.success || !res.data) {
    el.innerHTML = emptyMsg("Chambre introuvable.");
    return;
  }

  const r = res.data;
  const p = getPrefix();
  const bookLink = isLoggedIn()
    ? `<a href="${p}client/new-reservation.html?roomId=${r.roomId}" class="btn btn-primary">Réserver cette chambre</a>`
    : `<a href="${p}client-login.html" class="btn btn-outline">Connectez-vous pour réserver</a>`;

  el.innerHTML =
    `<div class="breadcrumb"><a href="room-search.html">Recherche</a> / Chambre #${esc(r.roomNumber || r.roomId)}</div>` +
    `<div class="card">` +
      `<h1>Chambre #${esc(r.roomNumber || r.roomId)} ${badgeHTML(r.status)}</h1>` +
      `<div class="detail-grid">` +
        detailItem("Capacité", capacityLabel(r.capacity)) +
        detailItem("Vue", viewLabel(r.viewType)) +
        detailItem("Extensible", r.extendable ? "Oui" : "Non") +
        detailItem("Prix / nuit", `<span class="price">${num(r.pricePerNight)} $</span>`) +
      `</div>` +
      `<div style="margin-top:20px">${bookLink}</div>` +
    `</div>`;
}

/* ====================================================================
   AUTH — Login
   ==================================================================== */

function initLogin() {
  if (isLoggedIn()) { window.location.href = "client/dashboard.html"; return; }

  const form = document.getElementById("login-form");
  form.addEventListener("submit", async function (e) {
    e.preventDefault();
    clearAlert();

    const email = form.email.value.trim();
    const password = form.password.value;

    if (!email || !password) {
      showAlert("danger", "Veuillez remplir tous les champs.");
      return;
    }

    const btn = form.querySelector("button[type=submit]");
    btn.disabled = true;
    btn.textContent = "Connexion…";

    const res = await apiFetch("POST", "/client/login", { email, password });

    if (res.success) {
      saveClient(res.data);
      window.location.href = "client/dashboard.html";
    } else {
      showAlert("danger", res.message || "Identifiants invalides.");
      btn.disabled = false;
      btn.textContent = "Se connecter";
    }
  });
}

/* ====================================================================
   AUTH — Register
   ==================================================================== */

function initRegister() {
  if (isLoggedIn()) { window.location.href = "client/dashboard.html"; return; }

  const form = document.getElementById("register-form");
  form.addEventListener("submit", async function (e) {
    e.preventDefault();
    clearAlert();

    const fields = {
      firstName: form.firstName.value.trim(),
      lastName:  form.lastName.value.trim(),
      email:     form.email.value.trim(),
      phone:     form.phone.value.trim(),
      password:  form.password.value,
      address:   form.address.value.trim(),
    };

    if (!fields.firstName || !fields.lastName || !fields.email || !fields.password) {
      showAlert("danger", "Prénom, nom, email et mot de passe sont requis.");
      return;
    }

    const btn = form.querySelector("button[type=submit]");
    btn.disabled = true;
    btn.textContent = "Inscription…";

    const res = await apiFetch("POST", "/client/register", fields);

    if (res.success) {
      showAlert("success", "Inscription réussie ! Redirection…");
      setTimeout(function () { window.location.href = "client-login.html"; }, 1500);
    } else {
      let msg = res.message || "Erreur lors de l'inscription.";
      if (res.errors && res.errors.length > 0) {
        msg += " " + res.errors.map(function (e) { return e.error; }).join(", ");
      }
      showAlert("danger", msg);
      btn.disabled = false;
      btn.textContent = "S'inscrire";
    }
  });
}

/* ====================================================================
   CLIENT — Dashboard
   ==================================================================== */

async function initDashboard() {
  if (!requireAuth()) return;

  showLoading("dashboard-content");
  const res = await apiFetch("GET", "/client/dashboard");
  const el = document.getElementById("dashboard-content");

  if (!res.success) {
    el.innerHTML = '<div class="alert alert-danger">Impossible de charger le tableau de bord.</div>';
    return;
  }

  const d = res.data || {};
  const client = getClient();
  const reservations = d.recentReservations || d.reservations || [];

  el.innerHTML =
    `<h2>Bonjour, ${esc(client ? client.firstName : "Client")}</h2>` +

    `<div class="stats" style="margin-top:16px">` +
      statCard(d.totalReservations ?? reservations.length ?? 0, "Réservations") +
      statCard(d.upcomingReservations ?? 0, "À venir") +
      statCard(d.cancelledReservations ?? 0, "Annulées") +
    `</div>` +

    `<div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:14px">` +
      `<h3>Réservations récentes</h3>` +
      `<a href="reservations.html" class="btn btn-sm btn-outline">Voir tout</a>` +
    `</div>` +

    reservationListHTML(reservations.slice(0, 5));
}

/* ====================================================================
   CLIENT — Profile
   ==================================================================== */

async function initProfile() {
  if (!requireAuth()) return;

  showLoading("profile-content");
  const res = await apiFetch("GET", "/client/profile");
  const el = document.getElementById("profile-content");

  if (!res.success || !res.data) {
    el.innerHTML = '<div class="alert alert-danger">Impossible de charger le profil.</div>';
    return;
  }

  const prof = res.data;
  el.innerHTML =
    `<div class="card">` +
      `<h2>Mon profil</h2>` +
      `<div id="alert-box" style="margin-top:10px"></div>` +
      `<form id="profile-form" style="margin-top:14px">` +
        `<div class="form-row">` +
          `<div><label>Prénom</label><input type="text" name="firstName" value="${esc(prof.firstName || "")}"></div>` +
          `<div><label>Nom</label><input type="text" name="lastName" value="${esc(prof.lastName || "")}"></div>` +
        `</div>` +
        `<label>Email</label><input type="email" value="${esc(prof.email || "")}" disabled>` +
        `<label>Téléphone</label><input type="tel" name="phone" value="${esc(prof.phone || "")}">` +
        `<label>Adresse</label><input type="text" name="address" value="${esc(prof.address || "")}">` +
        `<button type="submit" class="btn btn-primary">Enregistrer</button>` +
      `</form>` +
    `</div>`;

  document.getElementById("profile-form").addEventListener("submit", async function (e) {
    e.preventDefault();
    clearAlert();
    const form = e.target;
    const btn = form.querySelector("button[type=submit]");
    btn.disabled = true;

    const data = {
      firstName: form.firstName.value.trim(),
      lastName:  form.lastName.value.trim(),
      phone:     form.phone.value.trim(),
      address:   form.address.value.trim(),
    };

    const upd = await apiFetch("POST", "/client/profile/update", data);

    if (upd.success) {
      showAlert("success", "Profil mis à jour.");
      const c = getClient();
      if (c) { c.firstName = data.firstName; c.lastName = data.lastName; saveClient(c); }
    } else {
      showAlert("danger", upd.message || "Erreur lors de la mise à jour.");
    }

    btn.disabled = false;
  });
}

/* ====================================================================
   CLIENT — Reservation list
   ==================================================================== */

async function initReservations() {
  if (!requireAuth()) return;

  showLoading("reservations-content");
  const res = await apiFetch("GET", "/client/reservations");
  const list = res.success ? res.data : [];
  document.getElementById("reservations-content").innerHTML = reservationListHTML(list);
}

/* ====================================================================
   CLIENT — Reservation details
   ==================================================================== */

async function initReservationDetails() {
  if (!requireAuth()) return;

  const id = getParam("id");
  if (!id) return;

  const el = document.getElementById("reservation-content");
  showLoading("reservation-content");

  const res = await apiFetch("GET", "/client/reservations/" + id);
  if (!res.success || !res.data) {
    el.innerHTML = emptyMsg("Réservation introuvable.");
    return;
  }

  const r = res.data;
  const nights = nightCount(r.startDate, r.endDate);
  const canCancel = r.status === "RESERVED";

  el.innerHTML =
    `<div class="card">` +
      `<div class="breadcrumb"><a href="reservations.html">Réservations</a> / #${r.reservationId}</div>` +
      `<h2>Réservation #${r.reservationId} ${badgeHTML(r.status)}</h2>` +
      `<div class="detail-grid" style="margin-top:16px">` +
        detailItem("Chambre", "#" + esc(r.roomNumber || r.roomId)) +
        detailItem("Arrivée", formatDate(r.startDate)) +
        detailItem("Départ", formatDate(r.endDate)) +
        detailItem("Nuits", nights) +
        detailItem("Prix total", r.totalPrice != null ? `<span class="price">${num(r.totalPrice)} $</span>` : "—") +
        detailItem("Créée le", r.createdAt ? formatDate(r.createdAt) : "—") +
      `</div>` +
      `<div id="alert-box" style="margin-top:12px"></div>` +
      (canCancel
        ? `<div style="margin-top:18px;padding-top:18px;border-top:1px solid #e5e5e5"><button id="cancel-btn" class="btn btn-danger">Annuler cette réservation</button></div>`
        : "") +
    `</div>`;

  if (canCancel) {
    document.getElementById("cancel-btn").addEventListener("click", async function () {
      if (!confirm("Voulez-vous vraiment annuler cette réservation ?")) return;

      const btn = document.getElementById("cancel-btn");
      btn.disabled = true;
      btn.textContent = "Annulation…";

      const cancelRes = await apiFetch("POST", "/client/reservations/" + id + "/cancel");

      if (cancelRes.success) {
        showAlert("success", "Réservation annulée.");
        btn.remove();
        setTimeout(function () { location.reload(); }, 1200);
      } else {
        showAlert("danger", cancelRes.message || "Impossible d'annuler.");
        btn.disabled = false;
        btn.textContent = "Annuler cette réservation";
      }
    });
  }
}

/* ====================================================================
   CLIENT — New reservation
   ==================================================================== */

async function initNewReservation() {
  if (!requireAuth()) return;

  const roomId = getParam("roomId");
  const form = document.getElementById("reservation-form");
  const summaryEl = document.getElementById("booking-summary");
  const priceEl = document.getElementById("price-display");
  const totalEl = document.getElementById("total-display");
  let room = null;

  // If a roomId was provided, load room info for the summary
  if (roomId) {
    const res = await apiFetch("GET", "/rooms/" + roomId);
    if (res.success && res.data) {
      room = res.data;
      summaryEl.innerHTML =
        `<h3>Chambre #${esc(room.roomNumber || room.roomId)}</h3>` +
        `<div class="detail-grid">` +
          detailItem("Capacité", capacityLabel(room.capacity)) +
          detailItem("Vue", viewLabel(room.viewType)) +
          detailItem("Prix / nuit", `<span class="price">${num(room.pricePerNight)} $</span>`) +
        `</div>`;
    }
  }

  // Recalculate total when dates change
  function recalculate() {
    if (!room) return;
    const start = form.startDate.value;
    const end = form.endDate.value;
    if (!start || !end) return;

    const n = nightCount(start, end);
    if (n > 0) {
      priceEl.textContent = n + " nuit(s)";
      totalEl.textContent = (n * Number(room.pricePerNight)).toFixed(2) + " $";
    } else {
      priceEl.textContent = "Dates invalides";
      totalEl.textContent = "—";
    }
  }

  form.startDate.addEventListener("change", recalculate);
  form.endDate.addEventListener("change", recalculate);

  // Submit reservation
  form.addEventListener("submit", async function (e) {
    e.preventDefault();
    clearAlert();

    const startDate = form.startDate.value;
    const endDate = form.endDate.value;
    const rid = parseInt(roomId || (form.roomId ? form.roomId.value : "0"), 10);

    if (!rid || !startDate || !endDate) {
      showAlert("danger", "Tous les champs sont requis.");
      return;
    }

    if (startDate >= endDate) {
      showAlert("danger", "La date de départ doit être après la date d'arrivée.");
      return;
    }

    const btn = form.querySelector("button[type=submit]");
    btn.disabled = true;
    btn.textContent = "Réservation en cours…";

    const res = await apiFetch("POST", "/client/reservations", { roomId: rid, startDate, endDate });

    if (res.success) {
      showAlert("success", "Réservation créée !");
      setTimeout(function () { window.location.href = "reservations.html"; }, 1500);
    } else {
      showAlert("danger", res.message || "Impossible de créer la réservation.");
      btn.disabled = false;
      btn.textContent = "Confirmer la réservation";
    }
  });
}

/* ====================================================================
   Shared HTML builders
   ==================================================================== */

/** Generates the HTML for a single room card (rich layout). */
function roomCardHTML(r) {
  const p = getPrefix();

  const chainName = r.chainName || "";
  const hotelName = r.hotelName || ("Hôtel #" + (r.hotelId || "?"));

  // Tags: capacity, view, surface if available
  let tags = `<span class="room-tag">${capacityLabel(r.capacity)}</span>`;
  if (r.viewType && r.viewType !== "NONE") {
    tags += `<span class="room-tag">👁 ${viewLabel(r.viewType)}</span>`;
  }
  if (r.surface) {
    tags += `<span class="room-tag">${r.surface} m²</span>`;
  }
  if (r.extendable) {
    tags += `<span class="room-tag room-tag-extra">+ Lit supplémentaire</span>`;
  }

  // Amenities
  let amenitiesHTML = "";
  const amenities = r.amenities || r.commodites || [];
  if (amenities.length > 0) {
    amenitiesHTML = `<div class="room-card-amenities">${amenities.map(function (a) {
      return `<span class="amenity-tag">${esc(a)}</span>`;
    }).join("")}</div>`;
  }

  return (
    `<div class="room-card">` +
      `<div class="room-card-header">` +
        `<div>` +
          (chainName ? `<div class="chain">${esc(chainName)}</div>` : "") +
          `<div class="hotel-name">${esc(hotelName)}</div>` +
        `</div>` +
        `<div class="room-price">${num(r.pricePerNight)} $ <small>/ nuit</small></div>` +
      `</div>` +
      `<div class="stars">${"★".repeat(r.category || 0)}${"☆".repeat(5 - (r.category || 0))}</div>` +
      `<div class="room-card-tags">${tags}</div>` +
      amenitiesHTML +
      `<a href="${p}room-details.html?id=${r.roomId}" class="btn btn-primary btn-block">Réserver</a>` +
    `</div>`
  );
}

/** Generates the HTML for a list of reservation items. */
function reservationListHTML(list) {
  if (!list || list.length === 0) {
    return emptyMsg("Aucune réservation.");
  }

  return list.map(function (r) {
    return (
      `<a href="reservation-details.html?id=${r.reservationId}" class="res-item">` +
        `<div>` +
          `<h4 style="margin:0 0 2px">Chambre #${esc(r.roomNumber || r.roomId)}</h4>` +
          `<p>${formatDate(r.startDate)} — ${formatDate(r.endDate)}</p>` +
        `</div>` +
        `<div style="text-align:right">` +
          badgeHTML(r.status) +
          (r.totalPrice != null
            ? `<div class="price" style="margin-top:4px">${num(r.totalPrice)} $</div>`
            : "") +
        `</div>` +
      `</a>`
    );
  }).join("");
}

function statCard(value, label) {
  return (
    `<div class="stat-card">` +
      `<div class="number">${value}</div>` +
      `<div class="label">${label}</div>` +
    `</div>`
  );
}

function detailItem(label, value) {
  return `<div class="item"><label>${label}</label><span>${value}</span></div>`;
}

function emptyMsg(text) {
  return `<p style="text-align:center;color:#999;padding:40px 0">${text}</p>`;
}

function num(value) {
  return Number(value).toFixed(2);
}
