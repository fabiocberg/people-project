import axios from 'axios'

// If VITE_API_BASE_URL is not set, default to same-origin (works behind nginx proxy)
const baseURL = (import.meta as any).env.VITE_API_BASE_URL ?? ''

const api = axios.create({ baseURL })

api.interceptors.response.use(
  (res) => res,
  (error) => {
    const msg = error?.response?.data?.message
    if (msg) console.error('API error:', msg)
    return Promise.reject(error)
  }
)

export default api
