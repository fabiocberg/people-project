import React, { createContext, useContext, useEffect, useState } from 'react'
import api from '../api/client'

type AuthContextType = {
  token: string | null
  login: (username: string, password: string) => Promise<void>
  logout: () => void
}

const AuthContext = createContext<AuthContextType>({
  token: null,
  login: async () => {},
  logout: () => {}
})

export const AuthProvider: React.FC<{children: React.ReactNode}> = ({ children }) => {
  const initialToken = typeof window !== 'undefined' ? localStorage.getItem('token') : null
  if (initialToken) {
    ;(api.defaults.headers as any).common = { ...(api.defaults.headers as any).common, Authorization: `Bearer ${initialToken}` }
  }
  const [token, setToken] = useState<string | null>(initialToken)

  useEffect(() => {
    if (token) {
      localStorage.setItem('token', token)
      ;(api.defaults.headers as any).common = { ...(api.defaults.headers as any).common, Authorization: `Bearer ${token}` }
    } else {
      localStorage.removeItem('token')
      if ((api.defaults.headers as any).common) delete (api.defaults.headers as any).common['Authorization']
    }
  }, [token])

  const login = async (username: string, password: string) => {
    const res = await api.post('/auth/login', { username, password })
    const t = res.data.token as string
    ;(api.defaults.headers as any).common = { ...(api.defaults.headers as any).common, Authorization: `Bearer ${t}` }
    setToken(t)
  }

  const logout = () => setToken(null)

  return (
    <AuthContext.Provider value={{ token, login, logout }}>
      {children}
    </AuthContext.Provider>
  )
}

export const useAuth = () => useContext(AuthContext)
