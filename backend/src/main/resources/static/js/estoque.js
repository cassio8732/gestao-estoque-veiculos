const API = "/api";

const el = {
  formFiltros: document.getElementById("form-filtros"),
  formVeiculo: document.getElementById("form-veiculo"),
  formMarca: document.getElementById("form-marca"),
  formEditar: document.getElementById("form-editar"),
  corpo: document.getElementById("corpo-tabela"),
  contagem: document.getElementById("contagem"),
  msgErro: document.getElementById("msg-erro"),
  msgOk: document.getElementById("msg-ok"),
  selectFiltro: document.getElementById("select-modelo-filtro"),
  selectCadastro: document.getElementById("select-modelo-cadastro"),
  listaMarcas: document.getElementById("lista-marcas"),
  dialog: document.getElementById("dialog-editar"),
  editarId: document.getElementById("editar-id"),
  btnLimpar: document.getElementById("btn-limpar-filtros"),
  btnFechar: document.getElementById("btn-fechar-dialog"),
};

let veiculoEmEdicao = null;

function formatarMoeda(valor) {
  return Number(valor).toLocaleString("pt-BR", { style: "currency", currency: "BRL" });
}

function rotuloStatus(s) {
  const map = {
    DISPONIVEL: "Disponível",
    VENDIDO: "Vendido",
    DESCONTINUADO: "Descontinuado",
  };
  return map[s] || s;
}

function mostrarMsg(tipo, texto) {
  el.msgErro.hidden = tipo !== "erro";
  el.msgOk.hidden = tipo !== "ok";
  if (tipo === "erro") el.msgErro.textContent = texto;
  if (tipo === "ok") el.msgOk.textContent = texto;
  if (texto) {
    setTimeout(() => {
      el.msgErro.hidden = true;
      el.msgOk.hidden = true;
    }, 5000);
  }
}

async function apiJson(url, options = {}) {
  const res = await fetch(url, {
    headers: { "Content-Type": "application/json", Accept: "application/json" },
    ...options,
  });
  if (res.status === 204) return null;
  const body = res.headers.get("content-type")?.includes("json") ? await res.json() : null;
  if (!res.ok) {
    const msg = body?.message || body?.error || `Erro HTTP ${res.status}`;
    const det = body?.details?.length ? ` (${body.details.join("; ")})` : "";
    throw new Error(msg + det);
  }
  return body;
}

function paramsFiltros(form) {
  const data = new FormData(form);
  const qs = new URLSearchParams();
  for (const [k, v] of data.entries()) {
    if (v !== "" && v != null) qs.set(k, v);
  }
  return qs;
}

async function carregarModelos() {
  const modelos = await apiJson(`${API}/modelos`);
  const opts = modelos.map((m) => `<option value="${m.nome}">${m.nome}</option>`).join("");
  el.selectCadastro.innerHTML = opts;
  el.selectFiltro.innerHTML = `<option value="">Todos</option>${opts}`;
}

async function carregarMarcasDatalist() {
  const marcas = await apiJson(`${API}/marcas`);
  el.listaMarcas.innerHTML = marcas.map((m) => `<option value="${m.nome}"></option>`).join("");
}

function renderTabela(veiculos) {
  el.contagem.textContent = String(veiculos.length);
  if (!veiculos.length) {
    el.corpo.innerHTML = `<tr><td colspan="9" class="vazio">Nenhum veículo encontrado.</td></tr>`;
    return;
  }
  el.corpo.innerHTML = veiculos
    .map(
      (v) => `
    <tr data-id="${v.id}">
      <td>${v.id}</td>
      <td>${v.marcaNome}</td>
      <td>${v.modeloNome}</td>
      <td>${v.ano}</td>
      <td>${v.cor}</td>
      <td>${formatarMoeda(v.preco)}</td>
      <td>${v.quilometragem.toLocaleString("pt-BR")} km</td>
      <td><span class="status status--${v.status}">${rotuloStatus(v.status)}</span></td>
      <td class="acoes-celula">
        <button type="button" class="btn btn--ghost btn--small" data-acao="editar">Editar</button>
        <button type="button" class="btn btn--danger btn--small" data-acao="excluir">Excluir</button>
      </td>
    </tr>`
    )
    .join("");
}

async function listarVeiculos(form = el.formFiltros) {
  const qs = paramsFiltros(form);
  const url = qs.toString() ? `${API}/veiculos?${qs}` : `${API}/veiculos`;
  el.corpo.innerHTML = `<tr><td colspan="9" class="vazio">Carregando…</td></tr>`;
  try {
    const dados = await apiJson(url);
    renderTabela(dados);
    mostrarMsg(null, "");
  } catch (e) {
    renderTabela([]);
    mostrarMsg("erro", e.message);
  }
}

el.formFiltros.addEventListener("submit", (ev) => {
  ev.preventDefault();
  listarVeiculos(el.formFiltros);
});

el.btnLimpar.addEventListener("click", () => {
  el.formFiltros.reset();
  listarVeiculos(el.formFiltros);
});

el.formVeiculo.addEventListener("submit", async (ev) => {
  ev.preventDefault();
  const data = new FormData(el.formVeiculo);
  const payload = {
    marca: data.get("marca").trim(),
    modelo: data.get("modelo"),
    ano: Number(data.get("ano")),
    cor: data.get("cor").trim(),
    preco: Number(data.get("preco")),
    quilometragem: Number(data.get("quilometragem")),
    status: data.get("status"),
  };
  try {
    await apiJson(`${API}/veiculos`, { method: "POST", body: JSON.stringify(payload) });
    mostrarMsg("ok", "Veículo cadastrado com sucesso.");
    el.formVeiculo.reset();
    el.formVeiculo.querySelector('[name="ano"]').value = "2023";
    el.formVeiculo.querySelector('[name="quilometragem"]').value = "0";
    await carregarMarcasDatalist();
    listarVeiculos(el.formFiltros);
  } catch (e) {
    mostrarMsg("erro", e.message);
  }
});

el.formMarca.addEventListener("submit", async (ev) => {
  ev.preventDefault();
  const nome = new FormData(el.formMarca).get("nome").trim();
  try {
    await apiJson(`${API}/marcas`, { method: "POST", body: JSON.stringify({ nome }) });
    mostrarMsg("ok", `Marca "${nome}" cadastrada.`);
    el.formMarca.reset();
    await carregarMarcasDatalist();
  } catch (e) {
    mostrarMsg("erro", e.message);
  }
});

el.corpo.addEventListener("click", async (ev) => {
  const btn = ev.target.closest("button[data-acao]");
  if (!btn) return;
  const row = btn.closest("tr");
  const id = row?.dataset.id;
  if (!id) return;

  if (btn.dataset.acao === "excluir") {
    if (!confirm(`Excluir veículo #${id}?`)) return;
    try {
      await apiJson(`${API}/veiculos/${id}`, { method: "DELETE" });
      mostrarMsg("ok", "Veículo removido.");
      listarVeiculos(el.formFiltros);
    } catch (e) {
      mostrarMsg("erro", e.message);
    }
    return;
  }

  if (btn.dataset.acao === "editar") {
    const veiculos = await apiJson(`${API}/veiculos/${id}`);
    veiculoEmEdicao = id;
    el.editarId.textContent = `#${id}`;
    el.formEditar.preco.value = veiculos.preco;
    el.formEditar.quilometragem.value = veiculos.quilometragem;
    el.formEditar.status.value = veiculos.status;
    el.dialog.showModal();
  }
});

el.formEditar.addEventListener("submit", async (ev) => {
  ev.preventDefault();
  if (!veiculoEmEdicao) return;
  const data = new FormData(el.formEditar);
  const payload = {
    preco: Number(data.get("preco")),
    quilometragem: Number(data.get("quilometragem")),
    status: data.get("status"),
  };
  try {
    await apiJson(`${API}/veiculos/${veiculoEmEdicao}`, {
      method: "PATCH",
      body: JSON.stringify(payload),
    });
    el.dialog.close();
    mostrarMsg("ok", "Veículo atualizado.");
    listarVeiculos(el.formFiltros);
  } catch (e) {
    mostrarMsg("erro", e.message);
  }
});

el.btnFechar.addEventListener("click", () => el.dialog.close());

async function init() {
  try {
    await Promise.all([carregarModelos(), carregarMarcasDatalist()]);
    await listarVeiculos(el.formFiltros);
  } catch (e) {
    mostrarMsg("erro", `Falha ao iniciar: ${e.message}. A API está rodando em ${window.location.origin}?`);
  }
}

init();
