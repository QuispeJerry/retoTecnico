import { memo } from 'react'

export const ProductFilters = memo(function ProductFilters({
  categories,
  searchTerm,
  selectedCategory,
  onCategoryChange,
  onSearchChange,
}) {
  return (
    <form className="filters" role="search" aria-label="Buscar y filtrar productos">
      <label className="field">
        <span>Buscar por nombre</span>
        <input
          aria-label="Buscar por nombre"
          onChange={(event) => onSearchChange(event.target.value)}
          placeholder="Ej. Fondo, seguro, tarjeta"
          type="search"
          value={searchTerm}
        />
      </label>

      <label className="field">
        <span>Categoria</span>
        <select
          aria-label="Filtrar por categoria"
          onChange={(event) => onCategoryChange(event.target.value)}
          value={selectedCategory}
        >
          {categories.map((category) => (
            <option key={category} value={category}>
              {category}
            </option>
          ))}
        </select>
      </label>
    </form>
  )
})
