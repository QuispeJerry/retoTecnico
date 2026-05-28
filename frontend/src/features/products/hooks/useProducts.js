import { useEffect, useState } from 'react'
import { getLocalProducts } from '../api/localProductRepository'

export function useProducts() {
  const [products, setProducts] = useState([])
  const [status, setStatus] = useState('loading')

  useEffect(() => {
    let isMounted = true

    getLocalProducts().then((data) => {
      if (isMounted) {
        setProducts(data)
        setStatus('success')
      }
    })

    return () => {
      isMounted = false
    }
  }, [])

  return { products, status }
}
