document.addEventListener("DOMContentLoaded", function () {
  document.querySelectorAll(".btn-eliminar").forEach((btn) => {
    btn.addEventListener("click", function (e) {
      if (
        !confirm(
          " ¿Estás seguro de eliminar este árbol? Esta acción no se puede deshacer.",
        )
      ) {
        e.preventDefault();
      }
    });
  });

  const form = document.getElementById("registroForm");
  if (form) {
    const inputs = ["especie", "ubicacion", "fecha", "estado"];

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
