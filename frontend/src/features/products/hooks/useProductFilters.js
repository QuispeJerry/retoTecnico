import { useMemo, useState } from 'react'

const ALL_CATEGORIES = 'Todas'

export function useProductFilters(products) {
  const [searchTerm, setSearchTerm] = useState('')
  const [selectedCategory, setSelectedCategory] = useState(ALL_CATEGORIES)

  const categories = useMemo(() => {
    return [ALL_CATEGORIES, ...new Set(products.map((product) => product.category))]
  }, [products])

  const filteredProducts = useMemo(() => {
    const normalizedSearch = searchTerm.trim().toLowerCase()

    return products.filter((product) => {
      const matchesName = product.title.toLowerCase().includes(normalizedSearch)
      const matchesCategory =
        selectedCategory === ALL_CATEGORIES || product.category === selectedCategory

      return matchesName && matchesCategory
    })
  }, [products, searchTerm, selectedCategory])

  return {
    categories,
    filteredProducts,
    searchTerm,
    selectedCategory,
    setSearchTerm,
    setSelectedCategory,
  }
}
