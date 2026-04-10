/**
 * employee.js — Portail employé / gestionnaire (e-Hotels).
 * Chaque page définit data-page sur <body> pour router vers l’init approprié.
 */

"use strict";

document.addEventListener("DOMContentLoaded", function () {
  const page = document.body.dataset.page;

  const pages = {
    "employee-login": initEmployeeLogin,
    "employee-dashboard": initEmployeeDashboard,
    "employee-reservations": initEmployeeReservations,
    "employee-reservation-details": initEmployeeReservationDetails,
    "employee-rentals": initEmployeeRentals,
    "employee-rental-details": initEmployeeRentalDetails,
    "employee-customers": initEmployeeCustomers,
    "employee-customer-details": initEmployeeCustomerDetails,
    "employee-rooms": initEmployeeRooms,
    "employee-admin-hotels": initAdminHotels,
    "employee-admin-hotel-form": initAdminHotelForm,
    "employee-admin-rooms": initAdminRooms,
    "employee-admin-room-form": initAdminRoomForm,
    "employee-admin-employees": initAdminEmployees,
    "employee-admin-employee-form": initAdminEmployeeForm,
    "employee-admin-reports": initAdminReports,
  };

  if (page === "employee-login") {
    pages[page]();
    return;
  }

  if (page && page.indexOf("employee-admin-") === 0) {
    if (!requireEmployeeAuth() || !requireManager()) return;
    revealAdminSidebar();
    if (pages[page]) pages[page]();
    return;
  }

  if (page && page.indexOf("employee-") === 0) {
    if (!requireEmployeeAuth()) return;
    revealAdminSidebar();
    if (pages[page]) pages[page]();
  }
});

function revealAdminSidebar() {
  if (!isManager()) return;
  document.querySelectorAll(".sidebar-admin-only").forEach(function (el) {
    el.removeAttribute("hidden");
  });
}

function empNum(v) {
  return Number(v).toFixed(2);
}

/* ---------- Login ---------- */

function initEmployeeLogin() {
  if (isLoggedIn()) {
    clearSession();
  }

  if (isEmployeeLoggedIn()) {
    window.location.href = getPrefix() + "employee/dashboard.html";
    return;
  }

  const form = document.getElementById("employee-login-form");
  if (!form) return;

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

    const res = await apiFetch("POST", "/employee/login", { email, password });

    if (res.success && res.data) {
      loginEmployeeExclusive(res.data);
      window.location.href = getPrefix() + "employee/dashboard.html";
    } else {
      let msg = res.message || "Identifiants invalides.";
      if (res.errors && res.errors.length > 0) {
        msg += " " + res.errors.map(function (e) { return e.error; }).join(", ");
      }
      showAlert("danger", msg);
      btn.disabled = false;
      btn.textContent = "Se connecter";
    }
  });
}

/* ---------- Dashboard ---------- */

function initEmployeeDashboard() {
  revealAdminSidebar();
}

/* ---------- Réservations ---------- */

async function initEmployeeReservations() {
  const el = document.getElementById("list-content");
  if (!el) return;
  showLoading("list-content");

  const res = await apiFetch("GET", "/employee/reservations");
  const list = res.success ? res.data : [];

  if (!list || list.length === 0) {
    el.innerHTML = "<p style=\"color:#999\">Aucune réservation.</p>";
    return;
  }

  const rows = list.map(function (r) {
    return (
      "<tr>" +
        "<td>#" + esc(r.reservationId) + "</td>" +
        "<td>" + esc(r.customerName || "") + "</td>" +
        "<td>#" + esc(r.roomNumber || r.roomId) + "</td>" +
        "<td>" + formatDate(r.startDate) + " — " + formatDate(r.endDate) + "</td>" +
        "<td>" + badgeHTML(r.status) + "</td>" +
        "<td><a href=\"reservation-details.html?id=" + r.reservationId + "\">Détail</a></td>" +
      "</tr>"
    );
  }).join("");

  el.innerHTML =
    "<div class=\"table-wrap\">" +
    "<table class=\"data-table\">" +
    "<thead><tr><th>ID</th><th>Client</th><th>Chambre</th><th>Dates</th><th>Statut</th><th></th></tr></thead>" +
    "<tbody>" + rows + "</tbody></table></div>";
}

async function initEmployeeReservationDetails() {
  const id = getParam("id");
  const el = document.getElementById("detail-content");
  if (!id || !el) return;

  showLoading("detail-content");

  const res = await apiFetch("GET", "/employee/reservations/" + id);
  if (!res.success || !res.data) {
    el.innerHTML = "<p style=\"color:#999\">Réservation introuvable.</p>";
    return;
  }

  const r = res.data;
  const canCheckIn = r.status === "RESERVED";
  const emp = getEmployee();

  el.innerHTML =
    "<div class=\"card\">" +
      "<h2>Réservation #" + esc(r.reservationId) + " " + badgeHTML(r.status) + "</h2>" +
      "<div class=\"detail-grid\" style=\"margin-top:16px\">" +
        detailRow("Client", esc(r.customerName || "")) +
        detailRow("Chambre", "#" + esc(r.roomNumber || r.roomId)) +
        detailRow("Arrivée", formatDate(r.startDate)) +
        detailRow("Départ", formatDate(r.endDate)) +
        detailRow("Prix total", r.totalPrice != null ? empNum(r.totalPrice) + " $" : "—") +
      "</div>" +
      "<div id=\"alert-box\" style=\"margin-top:12px\"></div>" +
      (canCheckIn
        ? "<div style=\"margin-top:18px\"><button type=\"button\" id=\"btn-checkin\" class=\"btn btn-primary\">Enregistrer l’arrivée (check-in)</button></div>"
        : "") +
    "</div>";

  if (canCheckIn) {
    document.getElementById("btn-checkin").addEventListener("click", async function () {
      clearAlert();
      const btn = document.getElementById("btn-checkin");
      btn.disabled = true;
      const body = {
        reservationId: Number(r.reservationId),
        customerId: r.customerId,
        roomId: r.roomId,
        employeeId: emp.employeeId,
        checkInDate: r.startDate,
      };
      const out = await apiFetch("POST", "/employee/rentals", body);
      if (out.success) {
        showAlert("success", "Check-in enregistré.");
        setTimeout(function () { location.reload(); }, 1200);
      } else {
        showAlert("danger", out.message || "Impossible d’enregistrer le check-in.");
        btn.disabled = false;
      }
    });
  }
}

/* ---------- Locations ---------- */

async function initEmployeeRentals() {
  const el = document.getElementById("list-content");
  if (!el) return;
  showLoading("list-content");

  const res = await apiFetch("GET", "/employee/rentals");
  const list = res.success ? res.data : [];

  if (!list || list.length === 0) {
    el.innerHTML = "<p style=\"color:#999\">Aucune location.</p>";
    return;
  }

  const rows = list.map(function (r) {
    return (
      "<tr>" +
        "<td>#" + esc(r.rentalId) + "</td>" +
        "<td>" + esc(r.customerName || "") + "</td>" +
        "<td>#" + esc(r.roomNumber || r.roomId) + "</td>" +
        "<td>" + formatDate(r.checkInDate) + "</td>" +
        "<td>" + (r.checkOutDate ? formatDate(r.checkOutDate) : "—") + "</td>" +
        "<td>" + badgeHTML(r.status) + "</td>" +
        "<td><a href=\"rental-details.html?id=" + r.rentalId + "\">Détail</a></td>" +
      "</tr>"
    );
  }).join("");

  el.innerHTML =
    "<div class=\"table-wrap\">" +
    "<table class=\"data-table\">" +
    "<thead><tr><th>ID</th><th>Client</th><th>Chambre</th><th>Arrivée</th><th>Départ</th><th>Statut</th><th></th></tr></thead>" +
    "<tbody>" + rows + "</tbody></table></div>";
}

async function initEmployeeRentalDetails() {
  const id = getParam("id");
  const el = document.getElementById("detail-content");
  if (!id || !el) return;

  showLoading("detail-content");

  const res = await apiFetch("GET", "/employee/rentals/" + id);
  if (!res.success || !res.data) {
    el.innerHTML = "<p style=\"color:#999\">Location introuvable.</p>";
    return;
  }

  const r = res.data;
  const canCheckout = r.status === "ACTIVE";

  el.innerHTML =
    "<div class=\"card\">" +
      "<h2>Location #" + esc(r.rentalId) + " " + badgeHTML(r.status) + "</h2>" +
      "<div class=\"detail-grid\" style=\"margin-top:16px\">" +
        detailRow("Client", esc(r.customerName || "")) +
        detailRow("Chambre", "#" + esc(r.roomNumber || r.roomId)) +
        detailRow("Employé", esc(r.employeeName || "")) +
        detailRow("Arrivée", formatDate(r.checkInDate)) +
        detailRow("Départ", r.checkOutDate ? formatDate(r.checkOutDate) : "—") +
      "</div>" +
      "<div id=\"alert-box\" style=\"margin-top:12px\"></div>" +
      (canCheckout
        ? "<div style=\"margin-top:18px\"><button type=\"button\" id=\"btn-checkout\" class=\"btn btn-primary\">Check-out</button></div>"
        : "") +
    "</div>";

  if (canCheckout) {
    document.getElementById("btn-checkout").addEventListener("click", async function () {
      clearAlert();
      const btn = document.getElementById("btn-checkout");
      btn.disabled = true;
      const out = await apiFetch("POST", "/employee/rentals/" + id + "/checkout");
      if (out.success) {
        showAlert("success", "Check-out effectué.");
        setTimeout(function () { location.reload(); }, 1200);
      } else {
        showAlert("danger", out.message || "Impossible d’effectuer le check-out.");
        btn.disabled = false;
      }
    });
  }
}

/* ---------- Clients ---------- */

async function initEmployeeCustomers() {
  const el = document.getElementById("list-content");
  if (!el) return;
  showLoading("list-content");

  const res = await apiFetch("GET", "/employee/customers");
  const list = res.success ? res.data : [];

  if (!list || list.length === 0) {
    el.innerHTML = "<p style=\"color:#999\">Aucun client.</p>";
    return;
  }

  const rows = list.map(function (c) {
    return (
      "<tr>" +
        "<td>#" + esc(c.customerId) + "</td>" +
        "<td>" + esc(c.firstName) + " " + esc(c.lastName) + "</td>" +
        "<td>" + esc(c.email || "") + "</td>" +
        "<td>" + esc(c.phone || "") + "</td>" +
        "<td><a href=\"customer-details.html?id=" + c.customerId + "\">Détail</a></td>" +
      "</tr>"
    );
  }).join("");

  el.innerHTML =
    "<div class=\"table-wrap\">" +
    "<table class=\"data-table\">" +
    "<thead><tr><th>ID</th><th>Nom</th><th>Email</th><th>Téléphone</th><th></th></tr></thead>" +
    "<tbody>" + rows + "</tbody></table></div>";
}

async function initEmployeeCustomerDetails() {
  const id = getParam("id");
  const el = document.getElementById("detail-content");
  if (!id || !el) return;

  showLoading("detail-content");

  const res = await apiFetch("GET", "/employee/customers/" + id);
  if (!res.success || !res.data) {
    el.innerHTML = "<p style=\"color:#999\">Client introuvable.</p>";
    return;
  }

  const c = res.data;
  el.innerHTML =
    "<div class=\"card\">" +
      "<h2>" + esc(c.firstName) + " " + esc(c.lastName) + "</h2>" +
      "<div class=\"detail-grid\" style=\"margin-top:16px\">" +
        detailRow("Email", esc(c.email || "")) +
        detailRow("Téléphone", esc(c.phone || "")) +
        detailRow("Adresse", esc(c.address || "")) +
        detailRow("Inscription", c.registrationDate ? formatDate(c.registrationDate) : "—") +
      "</div>" +
    "</div>";
}

/* ---------- Chambres (employé) ---------- */

async function initEmployeeRooms() {
  const el = document.getElementById("list-content");
  if (!el) return;
  showLoading("list-content");

  const res = await apiFetch("GET", "/employee/rooms");
  const list = res.success ? res.data : [];

  if (!list || list.length === 0) {
    el.innerHTML = "<p style=\"color:#999\">Aucune chambre.</p>";
    return;
  }

  const statuses = ["AVAILABLE", "RESERVED", "OCCUPIED", "MAINTENANCE"];

  const rows = list.map(function (r) {
    const opts = statuses.map(function (s) {
      return "<option value=\"" + s + "\"" + (r.status === s ? " selected" : "") + ">" + s + "</option>";
    }).join("");
    return (
      "<tr data-room-id=\"" + r.roomId + "\">" +
        "<td>#" + esc(r.roomNumber || r.roomId) + "</td>" +
        "<td>" + esc(r.hotelId != null ? "Hôtel " + r.hotelId : "") + "</td>" +
        "<td>" + badgeHTML(r.status) + "</td>" +
        "<td>" + capacityLabel(r.capacity) + "</td>" +
        "<td>" + empNum(r.pricePerNight) + " $</td>" +
        "<td><select class=\"room-status-select\" data-room-id=\"" + r.roomId + "\">" + opts + "</select> " +
        "<button type=\"button\" class=\"btn btn-sm btn-primary room-status-save\">Mettre à jour</button></td>" +
      "</tr>"
    );
  }).join("");

  el.innerHTML =
    "<div id=\"rooms-alert\"></div>" +
    "<div class=\"table-wrap\">" +
    "<table class=\"data-table\">" +
    "<thead><tr><th>Chambre</th><th>Hôtel</th><th>Statut</th><th>Capacité</th><th>Prix/nuit</th><th>Action</th></tr></thead>" +
    "<tbody>" + rows + "</tbody></table></div>";

  el.querySelectorAll(".room-status-save").forEach(function (btn) {
    btn.addEventListener("click", async function () {
      const tr = btn.closest("tr");
      const roomId = tr.getAttribute("data-room-id");
      const sel = tr.querySelector(".room-status-select");
      const status = sel.value;
      const box = document.getElementById("rooms-alert");
      const out = await apiFetch("POST", "/employee/rooms/" + roomId + "/status", { status });
      if (out.success) {
        if (box) box.innerHTML = "<div class=\"alert alert-success\">Statut mis à jour.</div>";
        setTimeout(function () { location.reload(); }, 800);
      } else {
        if (box) box.innerHTML = "<div class=\"alert alert-danger\">" + esc(out.message || "Erreur") + "</div>";
      }
    });
  });
}

/* ---------- Admin hôtels ---------- */

async function initAdminHotels() {
  const el = document.getElementById("list-content");
  if (!el) return;
  showLoading("list-content");

  const res = await apiFetch("GET", "/employee/admin/hotels");
  const list = res.success ? res.data : [];

  if (!list || list.length === 0) {
    el.innerHTML = "<p style=\"color:#999\">Aucun hôtel. <a href=\"hotel-form.html\">Ajouter</a></p>";
    return;
  }

  const rows = list.map(function (h) {
    return (
      "<tr>" +
        "<td>#" + esc(h.hotelId) + "</td>" +
        "<td>" + esc(h.name) + "</td>" +
        "<td>" + esc(h.city || "") + "</td>" +
        "<td>" + esc(h.category != null ? h.category + " ★" : "") + "</td>" +
        "<td class=\"row-actions\">" +
          "<a href=\"hotel-form.html?id=" + h.hotelId + "\" class=\"btn btn-sm btn-outline\">Modifier</a> " +
          "<button type=\"button\" class=\"btn btn-sm btn-danger btn-del-hotel\" data-id=\"" + h.hotelId + "\">Supprimer</button>" +
        "</td>" +
      "</tr>"
    );
  }).join("");

  el.innerHTML =
    "<p><a href=\"hotel-form.html\" class=\"btn btn-primary\">Nouvel hôtel</a></p>" +
    "<div class=\"table-wrap\">" +
    "<table class=\"data-table\">" +
    "<thead><tr><th>ID</th><th>Nom</th><th>Ville</th><th>Cat.</th><th></th></tr></thead>" +
    "<tbody>" + rows + "</tbody></table></div>";

  el.querySelectorAll(".btn-del-hotel").forEach(function (btn) {
    btn.addEventListener("click", async function () {
      if (!confirm("Supprimer cet hôtel ?")) return;
      const hid = btn.getAttribute("data-id");
      const out = await apiFetch("POST", "/employee/admin/hotels/" + hid + "/delete");
      if (out.success) location.reload();
      else alert(out.message || "Erreur");
    });
  });
}

async function initAdminHotelForm() {
  const form = document.getElementById("hotel-form");
  if (!form) return;

  const id = getParam("id");
  const sel = form.chainId;

  sel.innerHTML = "";

  const chainsRes = await apiFetch("GET", "/chains");
  console.log("CHAINS RESPONSE =", chainsRes);

  const chains = Array.isArray(chainsRes)
    ? chainsRes
    : (chainsRes && Array.isArray(chainsRes.data) ? chainsRes.data : []);

  console.log("CHAINS PARSED =", chains);

  if (chains.length > 0) {
    chains.forEach(function (c) {
      const o = document.createElement("option");
      o.value = String(c.chainId);
      o.textContent = (c.name || ("Chaîne #" + c.chainId)) + " (#" + c.chainId + ")";
      sel.appendChild(o);
    });
  } else {
    const fallback = document.createElement("option");
    fallback.value = "2";
    fallback.textContent = "Chaine Test (#2)";
    sel.appendChild(fallback);
  }

  if (id) {
    const listRes = await apiFetch("GET", "/employee/admin/hotels");
    const list = Array.isArray(listRes)
      ? listRes
      : (listRes && Array.isArray(listRes.data) ? listRes.data : []);

    const h = list.find(function (x) {
      return String(x.hotelId) === String(id);
    });

    if (h) {
      if (
        h.chainId &&
        !Array.from(form.chainId.options).some(function (o) {
          return o.value === String(h.chainId);
        })
      ) {
        const o = document.createElement("option");
        o.value = String(h.chainId);
        o.textContent = "Chaîne #" + h.chainId;
        form.chainId.appendChild(o);
      }

      form.chainId.value = h.chainId || "";
      form.name.value = h.name || "";
      form.category.value = h.category != null ? h.category : "";
      form.address.value = h.address || "";
      form.city.value = h.city || "";
      form.province.value = h.province || "";
      form.country.value = h.country || "";
      form.postalCode.value = h.postalCode || "";

      const ft = document.getElementById("form-title");
      if (ft) ft.textContent = "Modifier l’hôtel";
    }
  }

  form.addEventListener("submit", async function (e) {
    e.preventDefault();
    clearAlert();

    const data = {
      chainId: Number(form.chainId.value),
      name: form.name.value.trim(),
      category: Number(form.category.value),
      address: form.address.value.trim(),
      city: form.city.value.trim(),
      province: form.province.value.trim(),
      country: form.country.value.trim(),
      postalCode: form.postalCode.value.trim(),
    };

    const btn = form.querySelector("button[type=submit]");
    btn.disabled = true;

    let out;
    if (id) {
      out = await apiFetch("POST", "/employee/admin/hotels/" + id + "/update", data);
    } else {
      out = await apiFetch("POST", "/employee/admin/hotels", data);
    }

    if (out.success) {
      showAlert("success", "Hôtel enregistré.");
      setTimeout(function () {
        window.location.href = "hotels.html";
      }, 900);
    } else {
      let msg = out.message || "Erreur.";
      if (out.errors && out.errors.length > 0) {
        msg += " " + out.errors.map(function (e) { return e.error; }).join(", ");
      }
      showAlert("danger", msg);
      btn.disabled = false;
    }
  });
}

/* ---------- Admin chambres ---------- */

async function initAdminRooms() {
  const el = document.getElementById("list-content");
  if (!el) return;
  showLoading("list-content");

  const res = await apiFetch("GET", "/employee/admin/rooms");
  const list = res.success ? res.data : [];

  if (!list || list.length === 0) {
    el.innerHTML = "<p style=\"color:#999\">Aucune chambre. <a href=\"room-form.html\">Ajouter</a></p>";
    return;
  }

  const rows = list.map(function (r) {
    return (
      "<tr>" +
        "<td>#" + esc(r.roomId) + "</td>" +
        "<td>" + esc(r.hotelId != null ? r.hotelId : "") + "</td>" +
        "<td>" + esc(r.roomNumber || "") + "</td>" +
        "<td>" + esc(r.status) + "</td>" +
        "<td class=\"row-actions\">" +
          "<a href=\"room-form.html?id=" + r.roomId + "\" class=\"btn btn-sm btn-outline\">Modifier</a> " +
          "<button type=\"button\" class=\"btn btn-sm btn-danger btn-del-room\" data-id=\"" + r.roomId + "\">Supprimer</button>" +
        "</td>" +
      "</tr>"
    );
  }).join("");

  el.innerHTML =
    "<p><a href=\"room-form.html\" class=\"btn btn-primary\">Nouvelle chambre</a></p>" +
    "<div class=\"table-wrap\">" +
    "<table class=\"data-table\">" +
    "<thead><tr><th>ID</th><th>Hôtel</th><th>N°</th><th>Statut</th><th></th></tr></thead>" +
    "<tbody>" + rows + "</tbody></table></div>";

  el.querySelectorAll(".btn-del-room").forEach(function (btn) {
    btn.addEventListener("click", async function () {
      if (!confirm("Supprimer cette chambre ?")) return;
      const rid = btn.getAttribute("data-id");
      const out = await apiFetch("POST", "/employee/admin/rooms/" + rid + "/delete");
      if (out.success) location.reload();
      else alert(out.message || "Erreur");
    });
  });
}

async function initAdminRoomForm() {
  const form = document.getElementById("room-form");
  if (!form) return;

  const id = getParam("id");
  const hotelsRes = await apiFetch("GET", "/employee/admin/hotels");
  const hotels = hotelsRes.success ? hotelsRes.data : [];
  const sel = form.hotelId;

  hotels.forEach(function (h) {
    const o = document.createElement("option");
    o.value = h.hotelId;
    o.textContent = (h.name || "") + " (#" + h.hotelId + ")";
    sel.appendChild(o);
  });

  if (id) {
    const listRes = await apiFetch("GET", "/employee/admin/rooms");
    const list = listRes.success ? listRes.data : [];
    const r = list && list.find(function (x) { return String(x.roomId) === String(id); });

    if (r) {
      form.hotelId.value = r.hotelId || "";
      form.roomNumber.value = r.roomNumber || "";
      form.capacity.value = r.capacity || "";
      form.pricePerNight.value = r.pricePerNight != null ? r.pricePerNight : "";
      form.viewType.value = r.viewType || "NONE";
      form.extendable.checked = !!r.extendable;
      form.status.value = r.status || "AVAILABLE";

      const roomAmenities = r.amenities || [];
      form.querySelectorAll('input[name="amenities"]').forEach(function (cb) {
        cb.checked = roomAmenities.includes(cb.value);
      });

      const ft = document.getElementById("form-title");
      if (ft) ft.textContent = "Modifier la chambre";
    }
  }

  form.addEventListener("submit", async function (e) {
    e.preventDefault();
    clearAlert();

    const amenities = Array.from(
      form.querySelectorAll('input[name="amenities"]:checked')
    ).map(function (cb) {
      return cb.value;
    });

    const data = {
      hotelId: Number(form.hotelId.value),
      roomNumber: form.roomNumber.value.trim(),
      capacity: form.capacity.value,
      pricePerNight: Number(form.pricePerNight.value),
      viewType: form.viewType.value,
      extendable: form.extendable.checked,
      status: form.status.value,
      amenities: amenities
    };

    const btn = form.querySelector("button[type=submit]");
    btn.disabled = true;

    let out;
    if (id) {
      out = await apiFetch("POST", "/employee/admin/rooms/" + id + "/update", data);
    } else {
      out = await apiFetch("POST", "/employee/admin/rooms", data);
    }

    if (out.success || out.roomId) {
      showAlert("success", "Chambre enregistrée.");
      setTimeout(function () { window.location.href = "rooms.html"; }, 900);
    } else {
      let msg = out.message || "Erreur.";
      if (out.errors && out.errors.length > 0) {
        msg += " " + out.errors.map(function (e) { return e.error; }).join(", ");
      }
      showAlert("danger", msg);
      btn.disabled = false;
    }
  });
}

/* ---------- Admin employés ---------- */

async function initAdminEmployees() {
  const el = document.getElementById("list-content");
  if (!el) return;
  showLoading("list-content");

  const res = await apiFetch("GET", "/employee/admin/employees");
  const list = res.success ? res.data : [];

  if (!list || list.length === 0) {
    el.innerHTML = "<p style=\"color:#999\">Aucun employé. <a href=\"employee-form.html\">Ajouter</a></p>";
    return;
  }

  const rows = list.map(function (em) {
    return (
      "<tr>" +
        "<td>#" + esc(em.employeeId) + "</td>" +
        "<td>" + esc(em.firstName) + " " + esc(em.lastName) + "</td>" +
        "<td>" + esc(em.email) + "</td>" +
        "<td>" + esc(em.role || "") + "</td>" +
        "<td>" + (em.active ? "Oui" : "Non") + "</td>" +
        "<td class=\"row-actions\">" +
          "<a href=\"employee-form.html?id=" + em.employeeId + "\" class=\"btn btn-sm btn-outline\">Modifier</a> " +
          "<button type=\"button\" class=\"btn btn-sm btn-danger btn-del-emp\" data-id=\"" + em.employeeId + "\">Désactiver</button>" +
        "</td>" +
      "</tr>"
    );
  }).join("");

  el.innerHTML =
    "<p><a href=\"employee-form.html\" class=\"btn btn-primary\">Nouvel employé</a></p>" +
    "<div class=\"table-wrap\">" +
    "<table class=\"data-table\">" +
    "<thead><tr><th>ID</th><th>Nom</th><th>Email</th><th>Rôle</th><th>Actif</th><th></th></tr></thead>" +
    "<tbody>" + rows + "</tbody></table></div>";

  el.querySelectorAll(".btn-del-emp").forEach(function (btn) {
    btn.addEventListener("click", async function () {
      if (!confirm("Désactiver cet employé ?")) return;
      const eid = btn.getAttribute("data-id");
      const out = await apiFetch("POST", "/employee/admin/employees/" + eid + "/delete");
      if (out.success) location.reload();
      else alert(out.message || "Erreur");
    });
  });
}

async function initAdminEmployeeForm() {
  const form = document.getElementById("employee-form");
  if (!form) return;

  const id = getParam("id");
  const hotelsRes = await apiFetch("GET", "/employee/admin/hotels");
  const hotels = hotelsRes.success ? hotelsRes.data : [];
  const sel = form.hotelId;
  hotels.forEach(function (h) {
    const o = document.createElement("option");
    o.value = h.hotelId;
    o.textContent = (h.name || "") + " (#" + h.hotelId + ")";
    sel.appendChild(o);
  });

  if (id) {
    const listRes = await apiFetch("GET", "/employee/admin/employees");
    const list = listRes.success ? listRes.data : [];
    const em = list && list.find(function (x) { return String(x.employeeId) === String(id); });
    if (em) {
      form.hotelId.value = em.hotelId || "";
      form.firstName.value = em.firstName || "";
      form.lastName.value = em.lastName || "";
      form.email.value = em.email || "";
      form.phone.value = em.phone || "";
      form.role.value = em.role || "EMPLOYE";
      form.active.checked = em.active !== false;
      form.password.required = false;
      form.ssn.required = true;
      document.getElementById("password-hint").textContent = "Laisser vide pour ne pas changer.";
      const ft = document.getElementById("form-title");
      if (ft) ft.textContent = "Modifier l’employé";
    }
  }

  form.addEventListener("submit", async function (e) {
    e.preventDefault();
    clearAlert();

    const data = {
      hotelId: Number(form.hotelId.value),
      firstName: form.firstName.value.trim(),
      lastName: form.lastName.value.trim(),
      email: form.email.value.trim(),
      phone: form.phone.value.trim(),
      password: form.password.value,
      role: form.role.value,
      ssn: form.ssn.value.trim(),
      address: form.address.value.trim(),
      city: form.city.value.trim(),
      province: form.province.value.trim(),
      country: form.country.value.trim(),
      postalCode: form.postalCode.value.trim(),
      active: form.active.checked,
    };

    if (id) {
      if (!data.password || !data.password.trim()) {
        delete data.password;
      }
    }

    const btn = form.querySelector("button[type=submit]");
    btn.disabled = true;

    let out;
    if (id) {
      out = await apiFetch("POST", "/employee/admin/employees/" + id + "/update", data);
    } else {
      if (!data.password || !data.password.trim()) {
        showAlert("danger", "Le mot de passe est requis à la création.");
        btn.disabled = false;
        return;
      }
      out = await apiFetch("POST", "/employee/admin/employees", data);
    }

    if (out.success) {
      showAlert("success", "Employé enregistré.");
      setTimeout(function () { window.location.href = "employees.html"; }, 900);
    } else {
      showAlert("danger", out.message || "Erreur.");
      btn.disabled = false;
    }
  });
}

/* ---------- Rapports ---------- */

async function initAdminReports() {
  const el = document.getElementById("reports-content");
  if (!el) return;
  showLoading("reports-content");

  const res = await apiFetch("GET", "/employee/admin/reports");
  if (!res.success || !res.data) {
    el.innerHTML = "<p style=\"color:#999\">Impossible de charger les rapports.</p>";
    return;
  }

  const d = res.data;
  const cards = [
    ["totalHotels", "Hôtels"],
    ["totalRooms", "Chambres"],
    ["totalCustomers", "Clients"],
    ["totalEmployees", "Employés"],
    ["totalReservations", "Réservations"],
    ["activeRentals", "Locations actives"],
    ["completedRentals", "Locations terminées"],
  ].map(function (pair) {
    const key = pair[0];
    const lbl = pair[1];
    const val = d[key] != null ? d[key] : "—";
    return (
      "<div class=\"report-card\">" +
        "<div class=\"num\">" + esc(String(val)) + "</div>" +
        "<div class=\"lbl\">" + esc(lbl) + "</div>" +
      "</div>"
    );
  }).join("");

  el.innerHTML = "<div class=\"reports-grid\">" + cards + "</div>";
}

/* ---------- Helpers ---------- */

function detailRow(label, value) {
  return "<div class=\"item\"><label>" + esc(label) + "</label><span>" + value + "</span></div>";
}