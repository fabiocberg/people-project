import { useEffect, useState } from "react";
import api from "../api/client";
import { Person, PersonRequest, Gender } from "../types/Person";
import { formatCpf, onlyDigits } from "../utils/cpf";

type Editing = { mode: "create" } | { mode: "edit"; person: Person } | null;

const emptyReq: PersonRequest = {
    name: "",
    gender: undefined,
    email: "",
    birthDate: "",
    naturalness: "",
    nationality: "",
    cpf: "",
};

export default function PeoplePage() {
    const [people, setPeople] = useState<Person[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [editing, setEditing] = useState<Editing>(null);
    const [form, setForm] = useState<PersonRequest>(emptyReq);
    const [busy, setBusy] = useState(false);

    const load = async () => {
        setLoading(true);
        setError(null);
        try {
            const res = await api.get<Person[]>("/api/v1/people");
            setPeople(res.data);
        } catch (err: any) {
            setError(
                err?.response?.data?.message || "Falha ao carregar pessoas."
            );
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        load();
    }, []);

    const startCreate = () => {
        setForm(emptyReq);
        setEditing({ mode: "create" });
    };

    const startEdit = (p: Person) => {
        setForm({
            name: p.name,
            gender: p.gender,
            email: p.email,
            birthDate: p.birthDate,
            naturalness: p.naturalness,
            nationality: p.nationality,
            cpf: formatCpf(p.cpf),
        });
        setEditing({ mode: "edit", person: p });
    };

    const save = async () => {
        setBusy(true);
        setError(null);
        try {
            if (editing?.mode === "create") {
                await api.post("/api/v1/people", {
                    ...form,
                    cpf: onlyDigits(form.cpf),
                });
            } else if (editing?.mode === "edit") {
                await api.put(`/api/v1/people/${editing.person.id}`, {
                    ...form,
                    cpf: onlyDigits(form.cpf),
                });
            }
            setEditing(null);
            await load();
        } catch (err: any) {
            setError(
                err?.message ||
                    err?.response?.data?.message ||
                    "Erro ao salvar."
            );
        } finally {
            setBusy(false);
        }
    };

    const remove = async (p: Person) => {
        if (!confirm(`Excluir ${p.name}?`)) return;
        setBusy(true);
        setError(null);
        try {
            await api.delete(`/api/v1/people/${p.id}`);
            await load();
        } catch (err: any) {
            setError(err?.response?.data?.message || "Erro ao excluir.");
        } finally {
            setBusy(false);
        }
    };

    return (
        <div style={{ maxWidth: 1000, margin: "24px auto", padding: 16 }}>
            <header
                style={{
                    display: "flex",
                    justifyContent: "space-between",
                    alignItems: "center",
                }}
            >
                <h2>Pessoas</h2>
                <div>
                    <button onClick={startCreate} style={{ marginRight: 8 }}>
                        Adicionar
                    </button>
                    {/* logout no App/Context se quiser */}
                </div>
            </header>

            {error && (
                <div
                    style={{
                        color: "white",
                        background: "#d9534f",
                        padding: 8,
                        borderRadius: 4,
                        marginBottom: 12,
                    }}
                >
                    {error}
                </div>
            )}
            {loading ? (
                <p>Carregando...</p>
            ) : (
                <table
                    width="100%"
                    cellPadding={6}
                    style={{ borderCollapse: "collapse" }}
                >
                    <thead>
                        <tr>
                            <th align="left">Nome</th>
                            <th align="left">CPF</th>
                            <th align="left">Email</th>
                            <th align="left">Nascimento</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        {people.map((p) => (
                            <tr
                                key={p.id}
                                style={{ borderTop: "1px solid #eee" }}
                            >
                                <td>{p.name}</td>
                                <td>{formatCpf(p.cpf)}</td>
                                <td>{p.email || "-"}</td>
                                <td>{p.birthDate}</td>
                                <td align="right">
                                    <button
                                        onClick={() => startEdit(p)}
                                        style={{ marginRight: 8 }}
                                    >
                                        Editar
                                    </button>
                                    <button onClick={() => remove(p)}>
                                        Excluir
                                    </button>
                                </td>
                            </tr>
                        ))}
                        {people.length === 0 && (
                            <tr>
                                <td colSpan={5}>
                                    <i>Nenhum registro.</i>
                                </td>
                            </tr>
                        )}
                    </tbody>
                </table>
            )}

            {editing && (
                <div
                    style={{
                        marginTop: 24,
                        padding: 16,
                        border: "1px solid #ddd",
                        borderRadius: 8,
                    }}
                >
                    <h3>
                        {editing.mode === "create"
                            ? "Adicionar Pessoa"
                            : "Editar Pessoa"}
                    </h3>
                    <div
                        style={{
                            display: "grid",
                            gridTemplateColumns: "repeat(2, minmax(0, 1fr))",
                            gap: 12,
                        }}
                    >
                        <div>
                            <label>Nome *</label>
                            <input
                                value={form.name}
                                onChange={(e) =>
                                    setForm({ ...form, name: e.target.value })
                                }
                                required
                                style={{ width: "100%", padding: 8 }}
                            />
                        </div>
                        <div>
                            <label>CPF *</label>
                            <input
                                value={form.cpf}
                                onChange={(e) =>
                                    setForm({
                                        ...form,
                                        cpf: formatCpf(e.target.value),
                                    })
                                }
                                required
                                inputMode="numeric"
                                placeholder="000.000.000-00"
                                style={{ width: "100%", padding: 8 }}
                            />
                        </div>
                        <div>
                            <label>E-mail</label>
                            <input
                                type="email"
                                value={form.email || ""}
                                onChange={(e) =>
                                    setForm({ ...form, email: e.target.value })
                                }
                                style={{ width: "100%", padding: 8 }}
                            />
                        </div>
                        <div>
                            <label>Data de Nascimento *</label>
                            <input
                                type="date"
                                value={form.birthDate}
                                onChange={(e) =>
                                    setForm({
                                        ...form,
                                        birthDate: e.target.value,
                                    })
                                }
                                required
                                style={{ width: "100%", padding: 8 }}
                            />
                        </div>
                        <div>
                            <label>Sexo</label>
                            <select
                                value={form.gender || ""}
                                onChange={(e) =>
                                    setForm({
                                        ...form,
                                        gender: e.target.value as Gender,
                                    })
                                }
                                style={{ width: "100%", padding: 8 }}
                            >
                                <option value="">-</option>
                                <option value="MASCULINO">Masculino</option>
                                <option value="FEMININO">Feminino</option>
                                <option value="OUTRO">Outro</option>
                            </select>
                        </div>
                        <div>
                            <label>Naturalidade</label>
                            <input
                                value={form.naturalness || ""}
                                onChange={(e) =>
                                    setForm({
                                        ...form,
                                        naturalness: e.target.value,
                                    })
                                }
                                style={{ width: "100%", padding: 8 }}
                            />
                        </div>
                        <div>
                            <label>Nacionalidade</label>
                            <input
                                value={form.nationality || ""}
                                onChange={(e) =>
                                    setForm({
                                        ...form,
                                        nationality: e.target.value,
                                    })
                                }
                                style={{ width: "100%", padding: 8 }}
                            />
                        </div>
                    </div>
                    <div style={{ marginTop: 12 }}>
                        <button
                            onClick={save}
                            disabled={busy}
                            style={{ marginRight: 8 }}
                        >
                            {busy ? "Salvando..." : "Salvar"}
                        </button>
                        <button
                            onClick={() => setEditing(null)}
                            disabled={busy}
                        >
                            Cancelar
                        </button>
                    </div>
                </div>
            )}
        </div>
    );
}
