document.addEventListener("DOMContentLoaded", function () {
  document.querySelectorAll(".btn-eliminar").forEach((btn) => {
    btn.addEventListener("click", function (e) {
      if (
        !confirm(
          "⚠️ ¿Estás seguro de eliminar este árbol? Esta acción no se puede deshacer.",
        )
      ) {
        e.preventDefault();
      }
    });
  });

  const filtroContainer = document.querySelector("header");
  if (filtroContainer) {
    const filtroEstado = document.createElement("select");
    filtroEstado.className = "form-select w-auto d-inline-block ms-3";
    filtroEstado.innerHTML = `
            <option value="">Todos los estados</option>
            <option value="Muy Bueno">🌳 Muy Bueno</option>
            <option value="Bueno">🌿 Bueno</option>
            <option value="Regular">🍂 Regular</option>
            <option value="Malo">🍁 Malo</option>
            <option value="Crítico">⚠️ Crítico</option>
        `;
    filtroContainer.appendChild(filtroEstado);

    filtroEstado.addEventListener("change", function () {
      const estadoSeleccionado = this.value;
      document.querySelectorAll("tbody tr").forEach((fila) => {
        const celdaEstado = fila.querySelector("td:nth-child(5) .badge");
        if (celdaEstado) {
          const matches =
            estadoSeleccionado === "" ||
            celdaEstado.textContent.trim() === estadoSeleccionado;
          fila.style.display = matches ? "" : "none";
        }
      });
    });
  }

  const form = document.getElementById("registroForm");
  if (form) {
    const inputs = ["especie", "ubicacion", "fecha", "estado"];

    // Fecha actual por defecto
    const fechaInput = document.getElementById("fecha");
    if (fechaInput && !fechaInput.value) {
      fechaInput.value = new Date().toISOString().split("T")[0];
    }

    form.addEventListener("submit", function (event) {
      let esValido = true;

      inputs.forEach((id) => {
        const el = document.getElementById(id);
        if (el && el.value.trim() === "") {
          el.classList.add("is-invalid");
          esValido = false;
        } else if (el) {
          el.classList.remove("is-invalid");
        }
      });

      if (!esValido) {
        event.preventDefault();
        alert("❌ Por favor completa todos los campos obligatorios");
      }
    });

    inputs.forEach((id) => {
      const el = document.getElementById(id);
      if (el) {
        const evento =
          el.tagName === "SELECT" || el.type === "date" ? "change" : "input";
        el.addEventListener(evento, () => el.classList.remove("is-invalid"));
      }
    });
  }
});
