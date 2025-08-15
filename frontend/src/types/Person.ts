export type Gender = 'MASCULINO' | 'FEMININO' | 'OUTRO'

export interface Person {
  id: number
  name: string
  gender?: Gender
  email?: string
  birthDate: string
  naturalness?: string
  nationality?: string
  cpf: string
  createdAt?: string
  updatedAt?: string
}

export interface PersonRequest {
  name: string
  gender?: Gender
  email?: string
  birthDate: string
  naturalness?: string
  nationality?: string
  cpf: string
}
