import { FormEvent, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'

export default function LoginPage() {
  const { login } = useAuth()
  const [username, setUsername] = useState('admin')
  const [password, setPassword] = useState('admin')
  const [error, setError] = useState<string | null>(null)
  const [loading, setLoading] = useState(false)
  const navigate = useNavigate()

  const onSubmit = async (e: FormEvent) => {
    e.preventDefault()
    setError(null); setLoading(true)
    try {
      await login(username, password)
      navigate('/')
    } catch (err: any) {
      const msg = err?.response?.data?.message || 'Falha ao autenticar.'
      setError(msg)
    } finally { setLoading(false) }
  }

  return (
    <div style={{ display: 'grid', placeItems: 'center', height: '100vh' }}>
      <form onSubmit={onSubmit} style={{ width: 320, padding: 24, border: '1px solid #ddd', borderRadius: 8 }}>
        <h2 style={{ marginBottom: 16 }}>Login</h2>
        <label>Usuário</label>
        <input value={username} onChange={e=>setUsername(e.target.value)} required
               style={{ width: '100%', padding: 8, marginBottom: 12 }} />
        <label>Senha</label>
        <input type="password" value={password} onChange={e=>setPassword(e.target.value)} required
               style={{ width: '100%', padding: 8, marginBottom: 12 }} />
        {error && <div style={{ color: 'white', background:'#d9534f', padding: 8, borderRadius: 4, marginBottom: 12 }}>{error}</div>}
        <button disabled={loading} style={{ width: '100%', padding: 10, borderRadius: 6 }}>
          {loading ? 'Entrando...' : 'Entrar'}
        </button>
        <p style={{ fontSize: 12, color: '#666', marginTop: 12 }}>Dica: admin / admin</p>
      </form>
    </div>
  )
}
