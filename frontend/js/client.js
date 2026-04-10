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
  const hotels = Array.isArray(res) ? res : (res.success ? res.data : []);

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

  const hotelRaw = await apiFetch("GET", "/hotels/" + hotelId);
  const h = hotelRaw && hotelRaw.data ? hotelRaw.data : hotelRaw;

  if (!h || !h.hotelId) {
    el.innerHTML = emptyMsg("Hôtel introuvable.");
    return;
  }

  const today = new Date();
  const tomorrow = new Date();
  tomorrow.setDate(today.getDate() + 1);

  const fmt = d => d.toISOString().split("T")[0];
  const startDate = fmt(today);
  const endDate = fmt(tomorrow);

  el.innerHTML =
    `<div class="breadcrumb"><a href="hotels.html">Hôtels</a> / ${esc(h.name)}</div>` +
    `<h1>${esc(h.name)} ${starsHTML(h.category || 0)}</h1>` +
    `<p style="color:#777;margin-bottom:8px">${esc(h.address || "")}</p>` +
    `<p style="color:#777;margin-bottom:24px">${esc(h.city || "")}, ${esc(h.province || "")}, ${esc(h.country || "")}</p>` +
    `<div class="card" style="margin-bottom:20px">` +
      `<div class="detail-grid">` +
        detailItem("Nom", esc(h.name || "—")) +
        detailItem("Catégorie", starsHTML(h.category || 0)) +
        detailItem("Ville", esc(h.city || "—")) +
        detailItem("Province", esc(h.province || "—")) +
        detailItem("Pays", esc(h.country || "—")) +
        detailItem("Adresse", esc(h.address || "—")) +
      `</div>` +
    `</div>` +
    `<div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:8px">` +
      `<h2>Chambres disponibles</h2>` +
      `<a href="room-search.html?hotelId=${h.hotelId}" class="btn btn-sm btn-outline">Recherche avancée</a>` +
    `</div>` +
    `<p style="color:#777;margin-bottom:14px">Disponibilités affichées pour la période du <strong>${formatDate(startDate)}</strong> au <strong>${formatDate(endDate)}</strong>.</p>` +
    `<div id="hotel-rooms"><div class="loading"><div class="spinner"></div><p>Chargement…</p></div></div>`;

  const roomsRaw = await apiFetch(
    "GET",
    "/rooms/search?hotelId=" + encodeURIComponent(hotelId) +
      "&startDate=" + encodeURIComponent(startDate) +
      "&endDate=" + encodeURIComponent(endDate)
  );

  const rooms = Array.isArray(roomsRaw)
    ? roomsRaw
    : (roomsRaw && roomsRaw.data ? roomsRaw.data : []);

  const roomsEl = document.getElementById("hotel-rooms");

  if (!rooms || rooms.length === 0) {
    roomsEl.innerHTML = emptyMsg("Aucune chambre disponible pour cet hôtel sur la période sélectionnée.");
    return;
  }

  roomsEl.innerHTML = `<div class="card-grid">${rooms.map(roomCardHTML).join("")}</div>`;
}

/* ====================================================================
   PUBLIC — Room search
   ==================================================================== */

async function initRoomSearch() {
  const form = document.getElementById("search-form");
  const resultsEl = document.getElementById("search-results");

  const hotelId = getParam("hotelId");

  function fmt(d) {
    return d.toISOString().split("T")[0];
  }

  async function runSearch() {
    clearAlert();

    const startDate = form.startDate.value;
    const endDate = form.endDate.value;

    if (!startDate || !endDate) {
      showAlert("danger", "Veuillez choisir une date d'arrivée et une date de départ.");
      return;
    }

    if (startDate >= endDate) {
      showAlert("danger", "La date de départ doit être après la date d'arrivée.");
      return;
    }

    showLoading("search-results");

    const params = new URLSearchParams();
    const filterNames = [
      "startDate",
      "endDate",
      "capacity",
      "maxPrice",
      "category",
      "chainId",
      "minSurface",
      "minRoomCount"
    ];

    for (const name of filterNames) {
      const input = form.elements[name];
      if (input && input.value) {
        params.set(name, input.value);
      }
    }

    if (hotelId) {
      params.set("hotelId", hotelId);
    }

    const path = "/rooms/search?" + params.toString();
    const raw = await apiFetch("GET", path);
    const rooms = Array.isArray(raw) ? raw : (raw && raw.data ? raw.data : []);

    if (!rooms || rooms.length === 0) {
      resultsEl.innerHTML = emptyMsg("Aucune chambre disponible ne correspond à vos critères.");
      return;
    }

    const availableRooms = rooms.filter(function (r) {
      return r.status === "AVAILABLE";
    });

    if (availableRooms.length === 0) {
      resultsEl.innerHTML = emptyMsg("Aucune chambre avec le statut AVAILABLE.");
      return;
    }

    resultsEl.innerHTML =
      `<p style="color:#777;margin-bottom:12px">${availableRooms.length} chambre(s) disponible(s)</p>` +
      `<div class="card-grid">${availableRooms.map(roomCardHTML).join("")}</div>`;
  }

  form.addEventListener("submit", async function (e) {
    e.preventDefault();
    await runSearch();
  });

  const today = new Date();
  const tomorrow = new Date();
  tomorrow.setDate(today.getDate() + 1);

  if (!form.startDate.value) form.startDate.value = fmt(today);
  if (!form.endDate.value) form.endDate.value = fmt(tomorrow);

  await runSearch();
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
  const r = res && !Array.isArray(res) && res.data ? res.data : res;

  if (!r || !r.roomId) {
    el.innerHTML = emptyMsg("Chambre introuvable.");
    return;
  }

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
  if (isEmployeeLoggedIn()) {
  clearEmployeeSession();
  }

  if (isLoggedIn()) {
    window.location.href = "client/dashboard.html";
    return;
  }

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
      loginClientExclusive(res.data);
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
  if (isLoggedIn()) {
    window.location.href = "client/dashboard.html";
    return;
  }

  const form = document.getElementById("register-form");
  form.addEventListener("submit", async function (e) {
    e.preventDefault();
    clearAlert();

    const fields = {
      firstName: form.firstName.value.trim(),
      lastName: form.lastName.value.trim(),
      email: form.email.value.trim(),
      phone: form.phone.value.trim(),
      ssn: form.ssn.value.trim(),
      password: form.password.value,
      streetNumber: form.streetNumber.value.trim(),
      streetName: form.streetName.value.trim(),
      city: form.city.value.trim(),
      province: form.province.value.trim(),
      postalCode: form.postalCode.value.trim(),
      country: form.country.value.trim(),
    };

    if (
      !fields.firstName ||
      !fields.lastName ||
      !fields.email ||
      !fields.password ||
      !fields.ssn ||
      !fields.streetNumber ||
      !fields.streetName ||
      !fields.city ||
      !fields.province ||
      !fields.postalCode ||
      !fields.country
    ) {
      showAlert("danger", "Tous les champs requis doivent être remplis.");
      return;
    }

    const btn = form.querySelector("button[type=submit]");
    btn.disabled = true;
    btn.textContent = "Inscription…";

    const res = await apiFetch("POST", "/client/register", fields);

    if (res.success) {
      showAlert("success", "Inscription réussie ! Redirection…");
      setTimeout(function () {
        window.location.href = "client-login.html";
      }, 1500);
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

  const client = getClient();
  const clientId = client?.clientId;
  const el = document.getElementById("dashboard-content");

  if (!clientId) {
    el.innerHTML = '<div class="alert alert-danger">Session client invalide. Reconnectez-vous.</div>';
    return;
  }

  showLoading("dashboard-content");

  const raw = await apiFetch("GET", "/client/dashboard?clientId=" + encodeURIComponent(clientId));
  console.log("DASHBOARD RESPONSE =", raw);

  const d = raw && raw.data ? raw.data : raw;

  if (!d || typeof d !== "object") {
    el.innerHTML = '<div class="alert alert-danger">Impossible de charger le tableau de bord.</div>';
    return;
  }

  const reservations = d.recentReservations || d.reservations || [];

  el.innerHTML =
    `<h2>Bonjour, ${esc(d.client?.firstName || client.email || "Client")}</h2>` +
    `<div class="stats" style="margin-top:16px">` +
      statCard(d.totalReservations ?? reservations.length ?? 0, "Réservations") +
      statCard(d.activeReservations ?? d.upcomingReservations ?? 0, "À venir") +
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

  const client = getClient();
  const clientId = client?.clientId;
  const el = document.getElementById("profile-content");

  if (!clientId) {
    el.innerHTML = '<div class="alert alert-danger">Session client invalide. Reconnectez-vous.</div>';
    return;
  }

  showLoading("profile-content");

  const raw = await apiFetch("GET", "/client/profile?clientId=" + encodeURIComponent(clientId));
  console.log("PROFILE RESPONSE =", raw);

  const prof = raw && raw.data ? raw.data : raw;

  if (!prof || typeof prof !== "object") {
    el.innerHTML = '<div class="alert alert-danger">Impossible de charger le profil.</div>';
    return;
  }

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
        `<h3>Adresse</h3>` +
        `<input type="text" name="streetNumber" placeholder="Numéro de rue" value="${esc(prof.streetNumber || "")}">` +
        `<input type="text" name="streetName" placeholder="Nom de rue" value="${esc(prof.streetName || "")}">` +
        `<input type="text" name="city" placeholder="Ville" value="${esc(prof.city || "")}">` +
        `<input type="text" name="province" placeholder="Province" value="${esc(prof.province || "")}">` +
        `<input type="text" name="postalCode" placeholder="Code postal" value="${esc(prof.postalCode || "")}">` +
        `<input type="text" name="country" placeholder="Pays" value="${esc(prof.country || "")}">` +
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
      clientId,
      firstName: form.firstName.value.trim(),
      lastName: form.lastName.value.trim(),
      phone: form.phone.value.trim(),
      streetNumber: form.streetNumber.value.trim(),
      streetName: form.streetName.value.trim(),
      city: form.city.value.trim(),
      province: form.province.value.trim(),
      postalCode: form.postalCode.value.trim(),
      country: form.country.value.trim(),
    };

    const upd = await apiFetch("POST", "/client/profile/update", data);

    if (upd.success) {
      showAlert("success", "Profil mis à jour.");
    } else {
      let msg = upd.message || "Erreur lors de la mise à jour.";
      if (upd.errors && upd.errors.length > 0) {
        msg += " " + upd.errors.map(function (e) { return e.error; }).join(", ");
      }
      showAlert("danger", msg);
    }

    btn.disabled = false;
  });
}

/* ====================================================================
   CLIENT — Reservation list
   ==================================================================== */

async function initReservations() {
  if (!requireAuth()) return;

  const client = getClient();
  const clientId = client?.clientId;
  const el = document.getElementById("reservations-content");

  if (!clientId) {
    el.innerHTML = '<div class="alert alert-danger">Session client invalide. Reconnectez-vous.</div>';
    return;
  }

  showLoading("reservations-content");

  const raw = await apiFetch("GET", "/client/reservations?clientId=" + encodeURIComponent(clientId));
  console.log("RESERVATIONS RESPONSE =", raw);

  const list = Array.isArray(raw) ? raw : (raw && raw.data ? raw.data : []);

  if (!list || list.length === 0) {
    el.innerHTML = reservationListHTML([]);
    return;
  }

  el.innerHTML = reservationListHTML(list);
}

/* ====================================================================
   CLIENT — Reservation details
   ==================================================================== */

async function initReservationDetails() {
  if (!requireAuth()) return;

  const client = getClient();
  const clientId = client?.clientId;
  const id = getParam("id");
  const el = document.getElementById("reservation-content");

  if (!id || !el) return;

  if (!clientId) {
    el.innerHTML = '<div class="alert alert-danger">Session client invalide. Reconnectez-vous.</div>';
    return;
  }

  showLoading("reservation-content");

  const raw = await apiFetch(
    "GET",
    "/client/reservations/" + encodeURIComponent(id) + "?clientId=" + encodeURIComponent(clientId)
  );

  console.log("RESERVATION DETAILS RESPONSE =", raw);

  const r = raw && raw.data ? raw.data : raw;

  if (!r || !r.reservationId) {
    el.innerHTML = emptyMsg("Réservation introuvable.");
    return;
  }

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

    const client = getClient();
    const clientId = client?.clientId;

    if (!clientId) {
      showAlert("danger", "Session client invalide. Reconnectez-vous.");
      btn.disabled = false;
      btn.textContent = "Annuler cette réservation";
      return;
    }

    const cancelRes = await apiFetch(
      "POST",
      "/client/reservations/" + encodeURIComponent(id) + "/cancel?clientId=" + encodeURIComponent(clientId)
    );

    if (cancelRes.success || (cancelRes && cancelRes.reservationId)) {
      showAlert("success", "Réservation annulée.");
      btn.remove();
      setTimeout(function () { location.reload(); }, 1200);
    } else {
      let msg = cancelRes.message || "Impossible d'annuler.";
      if (cancelRes.errors && cancelRes.errors.length > 0) {
        msg += " " + cancelRes.errors.map(function (e) { return e.error; }).join(", ");
      }
      showAlert("danger", msg);
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

    const client = getClient();
    const clientId = client?.clientId;

    if (!clientId) {
      showAlert("danger", "Session client invalide. Reconnectez-vous.");
      btn.disabled = false;
      btn.textContent = "Confirmer la réservation";
      return;
    }

    const payload = {
      clientId: clientId,
      roomId: rid,
      startDate: startDate,
      endDate: endDate
    };

    console.log("RESERVATION PAYLOAD =", payload);

    const res = await apiFetch("POST", "/client/reservations", payload);

    if (res.success) {
      showAlert("success", "Réservation créée !");
      setTimeout(function () { window.location.href = "reservations.html"; }, 1500);
    } else {
      let msg = res.message || "Impossible de créer la réservation.";
      if (res.errors && res.errors.length > 0) {
        msg += " " + res.errors.map(function (e) { return e.error; }).join(", ");
      }
      showAlert("danger", msg);
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
